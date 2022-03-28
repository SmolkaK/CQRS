package core.etl.initializer

import core.etl.JokeEtlActor

object JokesETLInitializer {
  def runInitialLoad(initialLoadParamsBuilder: InitialLoadParamsBuilder = InitialLoadParamsBuilder): Unit = {
    initialLoadParamsBuilder.initialLoadRestRequestsParams().foreach(JokeEtlActor.jokesRestETLActorRef ! _)
  }
}
