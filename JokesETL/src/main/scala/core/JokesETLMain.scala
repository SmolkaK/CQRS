package core

import core.db.flyway.FlywaySupport
import core.db.connections.properties.PostgresDBConnProperties
import core.etl.initializer.JokesETLInitializer
import core.utils.ActorUtils

object JokesETLMain extends App with FlywaySupport with PostgresDBConnProperties{
  // INFO: przy każdym odpaleniu schemat jest czyszczony dla wygody testów aplikacji.
  cleanDB()
  migrateDB()

  JokesETLInitializer.runInitialLoad()

  Thread.sleep(10000)
  ActorUtils.actorSystem.terminate()
}