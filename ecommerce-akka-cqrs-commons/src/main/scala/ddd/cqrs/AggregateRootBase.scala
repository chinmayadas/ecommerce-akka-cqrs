package ddd.cqrs

import akka.actor.ActorLogging
import akka.persistence.PersistentActor

import scala.util.{Success, Try}

/**
  * Created by vsubbarravuri on 8/31/16.
  */

trait AggregateRootBase extends PersistentActor with ActorLogging {

  def id = self.path.name

  /**
    * Event handler, not invoked during recovery.
    */
  def handle(event: DomainEvent) {
    acknowledgeCommandProcessed(event)
  }

  def acknowledgeCommandProcessed(event: DomainEvent) {
    val result: Try[Any] = Success(s"Command successfully processed.")
    log.debug("Acknowledge response to sender({}).", sender)
    sender ! result
  }

}

