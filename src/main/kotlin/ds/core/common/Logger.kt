package ds.core.common

import java.util.logging.Level
import java.util.logging.Logger
import kotlin.math.log

/**
 * Logger
 */
class Logger {
    var logger = Logger.getLogger("mainLogger");

    fun logInfo(message : String) {
        logger.log(Level.INFO, message);
    }

    fun logWarning(message: String) {
        logger.log(Level.WARNING, message);
    }

    fun logSevere(message: String) {
        logger.log(Level.SEVERE, message);
    }

}