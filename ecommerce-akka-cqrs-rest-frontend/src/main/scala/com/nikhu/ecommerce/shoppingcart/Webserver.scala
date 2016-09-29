package com.nikhu.ecommerce.shoppingcart

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import spray.json.DefaultJsonProtocol

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

object WebServer extends LazyLogging with JsonSupport with App {


  implicit val actorSystem = ActorSystem("shopping-cart-akka-http-microservice")
  implicit val materializer = ActorMaterializer()
  // needed for the future flatMap/onComplete in the end
  implicit val executionContext = actorSystem.dispatcher

  val selection = actorSystem.actorSelection("akka.tcp://cartBackendApp@127.0.0.1:2552/user/cartActor")
  logger.debug("remote actor path: " + selection.pathString)
  implicit val timeout = Timeout(1 minutes)

  val routes = {
    logRequestResult("Frontend WebServer") {
      pathPrefix("cart") {
        path("item") {
          post {
            entity(as[CartItem]) {
              cartItem => {
                onSuccess((selection ? AddToCart(CartId("123"), cartItem)).mapTo[Success[String]]) { success =>
                  complete(HttpResponse(status = StatusCodes.OK, entity = success.value))
                }
              }
            }
          }
        } ~
          post {
            entity(as[List[CartItem]]) {
              cartItems => {
                selection ? CreateCart(CartId("123"), cartItems)
                onSuccess((selection ? CreateCart(CartId("123"), cartItems)).mapTo[Success[String]]) { success =>
                  complete(HttpResponse(status = StatusCodes.OK, entity = success.value))
                }
              }
            }
          }
      }
    }
  }


  val config = ConfigFactory.load()
  val (interface, port) = (config.getString("http.interface"), config.getInt("http.port"))
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