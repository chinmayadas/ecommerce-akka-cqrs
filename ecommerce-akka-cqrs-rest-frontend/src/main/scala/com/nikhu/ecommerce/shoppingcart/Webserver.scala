package com.nikhu.ecommerce.shoppingcart

import akka.actor.{ActorRef, ActorSystem}
import akka.cluster.sharding.{ClusterSharding, ShardRegion}
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import scaldi.akka.AkkaInjectable
import spray.json.DefaultJsonProtocol
import akka.pattern.ask

import scala.concurrent.duration._
import scala.util.Success


/**
  * Created by vsubbarravuri on 9/25/16.
  */

// collect your json format instances into a support trait:
trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val productFormat = jsonFormat5(Product)
  implicit val cartItemFormat = jsonFormat2(CartItem)
}

object Configs {
  private val root = ConfigFactory.load()
  val frontEndConf          = root.getConfig("frontEnd")
  val clusterProxyConf          = root.getConfig("clusterProxy")
}

object WebServer extends LazyLogging with JsonSupport with App {


  implicit val actorSystem = ActorSystem("frontend", Configs.frontEndConf)
  implicit val materializer = ActorMaterializer()
  // needed for the future flatMap/onComplete in the end
  implicit val executionContext = actorSystem.dispatcher

  //val cart = ClusterSharding(actorSystem).shardRegion("Cart")

  //val cart = actorSystem.actorSelection("akka.tcp://default@127.0.0.1:2552/system/sharding/Cart")
  //logger.debug("remote actor path: " + cart.pathString)
  // Start sharding in proxy mode on every frontend node


    val numberOfShards = 100

    val cart: ActorRef = ClusterSharding(ActorSystem("clusterProxy", Configs.clusterProxyConf)).startProxy(
      typeName = "Cart",
      role = None,
      extractEntityId = extractEntityId(),
      extractShardId = extractShardId(numberOfShards)
    )

    logger.debug("cartRegion: " + cart.path.name)


  implicit val timeout = Timeout(1 minutes)

  val routes = {
    logRequestResult("Frontend WebServer") {
      pathPrefix("cart") {
        path("item") {
          post {
            entity(as[CartItem]) {
              cartItem => {
                onSuccess((cart ? AddToCart(CartId("123"), cartItem)).mapTo[Success[String]]) { success =>
                  complete(HttpResponse(status = StatusCodes.OK, entity = success.value))
                }
              }
            }
          }
        } ~
          post {
            entity(as[List[CartItem]]) {
              cartItems => {
                onSuccess((cart ? CreateCart(CartId("123"), cartItems)).mapTo[Success[String]]) { success =>
                  complete(HttpResponse(status = StatusCodes.OK, entity = success.value))
                }
              }
            }
          }
      }
    }
  }

  def extractEntityId(): ShardRegion.ExtractEntityId = {
    case msg@CreateCart(id, _) => (id.value.toString, msg)
    case msg@AddToCart(id, _) => (id.value.toString, msg)
  }

  def extractShardId(numberOfShards: Int): ShardRegion.ExtractShardId = {
    case CreateCart(id, _) => Math.abs(id.hashCode() % numberOfShards).toString
    case AddToCart(id, _) =>  Math.abs(id.hashCode() % numberOfShards).toString
  }

  // val config = ConfigFactory.load()
  val (interface, port) = (Configs.frontEndConf.getString("http.interface"), Configs.frontEndConf.getInt("http.port"))
  val bindingFuture = Http().bindAndHandle(handler = routes, interface = interface, port = port)

  logger.info(s"Server online at, http://$interface:$port/")

  bindingFuture.onFailure {
    case ex: Exception ⇒
      logger.error(s"Failed to bind to $interface:$port!", ex)
  }
  sys.addShutdownHook(
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => actorSystem.terminate()) // and shutdown when done
  )
}
