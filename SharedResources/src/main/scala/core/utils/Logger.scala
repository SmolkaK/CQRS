package core.utils

import org.apache.logging.log4j.LogManager

trait Logger {
  val log: org.apache.logging.log4j.Logger = LogManager.getLogger("Default")
}
