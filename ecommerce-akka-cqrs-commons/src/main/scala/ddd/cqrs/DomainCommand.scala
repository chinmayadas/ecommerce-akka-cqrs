package ddd.cqrs

import java.util.UUID

/**
  * Created by vsubbarravuri on 8/29/16.
  */
trait DomainCommand {

  val id: CommandId = CommandId()

}

case class CommandId(value: UUID = UUID.randomUUID())
