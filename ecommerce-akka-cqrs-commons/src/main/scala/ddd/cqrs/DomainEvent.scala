package ddd.cqrs

import java.util.UUID

/**
  * Created by vsubbarravuri on 8/29/16.
  */
trait DomainEvent {

  val id: EventId = EventId()

  def commandId: CommandId

}

case class EventId(value: UUID = UUID.randomUUID())