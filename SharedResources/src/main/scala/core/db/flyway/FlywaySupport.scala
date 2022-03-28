package core.db.flyway

import core.db.flyway.FlywaySupport._
import core.db.connections.properties.DBConnProperties
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.configuration.FluentConfiguration
import org.flywaydb.core.api.output.{CleanResult, MigrateResult}

trait FlywaySupport { this: DBConnProperties =>
  def migrateDB(additionalMigrationDirs: Seq[String] = Nil, useDefaultMigrationDir: Boolean = true): MigrateResult = {
    val migrationDirs = if(useDefaultMigrationDir) addClasspath(additionalMigrationDirs) :+ defaultMigrationDir else addClasspath(additionalMigrationDirs)
    setupFlyway(migrationDirs).migrate()
  }

  def cleanDB(): CleanResult = {
    setupFlyway(Nil).clean()
  }

  private def setupFlyway(migrationDirs: Seq[String] = Nil): Flyway ={
    flywayConfig(migrationDirs).load
  }

  private def flywayConfig(migrationDirs: Seq[String] = Nil): FluentConfiguration ={
    Flyway.configure
      .dataSource(jdbcUrl, user, password.orNull)
      .baselineOnMigrate(true)
      .locations(migrationDirs:_*)
  }
}

object FlywaySupport {
  val defaultMigrationDir = "classpath:db/migration"
  def addClasspath(migrationDirs: Seq[String]): Seq[String] = migrationDirs.map("classpath:" + _)
}
