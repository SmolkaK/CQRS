package core.utils

import akka.actor.ActorSystem

import scala.concurrent.ExecutionContextExecutor

trait ActorUtils {
  implicit val actorSystem: ActorSystem = ActorUtils.actorSystem
  implicit val dispatcher: ExecutionContextExecutor = ActorUtils.dispatcher
}

object ActorUtils {
  val actorSystem: ActorSystem = ActorSystem("GlobalActorSystem")
  val dispatcher: ExecutionContextExecutor = actorSystem.dispatcher
}