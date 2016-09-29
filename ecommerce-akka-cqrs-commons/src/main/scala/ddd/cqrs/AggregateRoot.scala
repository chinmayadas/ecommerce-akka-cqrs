package ddd.cqrs

import akka.persistence.{RecoveryCompleted, SnapshotOffer}

/**
  * Created by vsubbarravuri on 8/29/16.
  */
trait AggregateState[T <: AggregateState[T]] {
  type StateMachine = PartialFunction[DomainEvent, T]

  def apply: StateMachine
}


abstract class AggregateRoot[S <: AggregateState[S]] extends AggregateRootBase {

  type AggregateRootFactory = PartialFunction[DomainEvent, S]

  val factory: AggregateRootFactory

  private lazy val sm = StateManager(factory)

  def initialized = sm.initialized

  def state = sm.state

  override def receiveRecover = {
    case event: DomainEvent => {
      log.debug("Recovering event, {}", event.id)
    }
    case SnapshotOffer(metadata, snapshot) =>
      // Restore your full state from the data in the snapshot
      log.info("Restore your full state from the data in the snapshot.")
    case RecoveryCompleted => {
      log.info("Recovery completed") // use logger here
      log.debug("state:" + sm.initialized)
    }
    case _ => log.info("Received unknown message.")
  }

  override def receiveCommand: Receive = {
    case de: DomainCommand => {
      log.debug("Received command: " + de)
      log.debug("Sender of command: " + sender)
      handleCommand.applyOrElse(de, unhandled)
    }
    case _ => log.debug("Unknown command.")
  }

  def handleCommand: Receive

  def raise(event: DomainEvent) {
    persist(event) {
      persisted => {
        log.debug("Persisted event {}", persisted.toString)
        sm.apply(persisted)
        log.debug("state: " + sm.state)
        handle(persisted)
      }
    }
  }

  case class StateManager(factory: AggregateRootFactory) {
    private var s: Option[S] = None

    def apply(de: DomainEvent): Unit = {
      s = s match {
        case Some(as) => Some(as.apply(de))
        case None => Some(factory.apply(de))
      }
    }

    def initialized = s.isDefined

    def state = if (initialized) s.get else throw new RuntimeException
  }


}

