package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Guo Weize
 * @date 2021/5/6
 */
public final class Log {
    private static final Logger LOGGER = LoggerFactory.getLogger(Log.class);

    public static void debug(String message) {
        LOGGER.debug(message);
    }

    public static void info(String message) {
        LOGGER.info(message);
    }

    public static void warn(String message) {
        LOGGER.warn(message);
    }

    public static void error(String message) {
        LOGGER.error(message);
    }

    public static void main(String[] args) {
        debug("debug level log");
        info("info level log");
        warn("warn level log");
        error("error level log");
    }

}
