package com.nikhu.ecommerce.shoppingcart

import java.util.UUID

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props, Terminated}
import akka.cluster.sharding.{ClusterSharding, ClusterShardingSettings}
import akka.pattern.ask
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.typesafe.config.{Config, ConfigFactory}

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.util.Success
import org.slf4j.{Logger, LoggerFactory}
import scaldi.Module
import scaldi.akka.AkkaInjectable

/**
  * Created by vsubbarravuri on 9/29/16.
  */
object CartBackendApp extends App with AkkaInjectable  {


  lazy val log: Logger = LoggerFactory.getLogger(getClass.getName)
  lazy val config: Config = ConfigFactory.load()
  implicit val system = ActorSystem()
  //implicit lazy val system = ActorSystem("cartBackendApp", config)
  implicit val executor = system.dispatcher
  implicit val materializer = ActorMaterializer()
  implicit val timeout = Timeout(1 minutes)


  implicit val appModule = new CartModule
  val cartActor = inject[ActorRef]('cart)


  val cartItems : List[CartItem] = List.empty[CartItem] :+ CartItem(Product("p1", 0L, "SAMSUNG PRINTER", "PRINTER", 100.0), 1)
  val f: Future[Any] = cartActor ? CreateCart(CartId(UUID.randomUUID.toString), cartItems)

  val cartItems2 : List[CartItem] = List.empty[CartItem] :+ CartItem(Product("p1", 0L, "SAMSUNG PRINTER", "PRINTER", 100.0), 1)
  val f2: Future[Any] = cartActor ? CreateCart(CartId(UUID.randomUUID.toString), cartItems2)

  //val cartActor = system.actorOf(Props[Cart], "cartActor")
  //system.actorOf(Props(classOf[Terminator], cartActor), "terminator")

  f.onSuccess {
    case Success(msg) => println(s"Actor message: $msg")
  }

  f2.onSuccess {
    case Success(msg) => println(s"Actor message: $msg")
  }

  log.debug(cartActor.path.name)

  //system.terminate()

}

class Terminator(ref: ActorRef) extends Actor with ActorLogging {
  context watch ref

  def receive = {
    case Terminated(_) =>
      log.info("{} has terminated, shutting down system", ref.path)
      context.system.terminate()
  }

}


class CartModule(implicit system: ActorSystem) extends Module {

  val numberOfShards = 100

  val cartRegion: ActorRef = ClusterSharding(system).start(
    typeName = "Cart",
    entityProps = Props[Cart],
    settings = ClusterShardingSettings(system),
    extractEntityId = Cart.extractEntityId(),
    extractShardId = Cart.extractShardId(numberOfShards)
  )

  bind[ActorRef] as 'cart to cartRegion

}
