package dev.teamhelios.pebbleconfigcreator

import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

object HeliosLogger {
    private var logger: Logger = LogManager.getLogger("HeliosLogger")

    fun info(message: String) {
        logger.info(message)
    }

    fun warn(message: String) {
        logger.warn(message)
    }

    fun error(message: String) {
        logger.error(message)
    }

    fun debug(message: String) {
        logger.debug(message)
    }

    fun success(message: String) {
        logger.log(Level.getLevel("SUCCESS"), message)
    }
}