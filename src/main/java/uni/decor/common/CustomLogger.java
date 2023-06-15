package uni.decor.common;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class CustomLogger {
    private static final Logger logger = LoggerFactory.getLogger(CustomLogger.class);
    private static final String PREFIX = "[UNIDECOR] ";

    public static void error(String message) {
        logger.error(PREFIX + message);
    }

    public static void warn(String message) {
        logger.warn(PREFIX + message);
    }

    public static void info(String message) {
        logger.info(PREFIX + message);
    }

    public static void debug(String message) {
        logger.debug(PREFIX + message);
    }

    public static void trace(String message) {
        logger.trace(PREFIX + message);
    }
}
