package com.nikhu.ecommerce.shoppingcart

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props, Terminated}
import akka.pattern.ask
import akka.util.Timeout
import com.typesafe.config.{Config, ConfigFactory}

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.Success
import org.slf4j.{Logger, LoggerFactory}

/**
  * Created by vsubbarravuri on 9/29/16.
  */
object CartBackendApp extends App {

  lazy val log: Logger = LoggerFactory.getLogger(getClass.getName)
  lazy val config: Config = ConfigFactory.load()
  implicit lazy val system = ActorSystem("cartBackendApp", config)

  val cartActor = system.actorOf(Props[Cart], "cartActor")
  system.actorOf(Props(classOf[Terminator], cartActor), "terminator")

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
