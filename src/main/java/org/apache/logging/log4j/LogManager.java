package org.apache.logging.log4j;

import org.slf4j.LoggerFactory;

public class LogManager {

    public static Logger getLogger(final String name) {
        return new Logger(LoggerFactory.getLogger(name));
    }

    public static Logger getLogger(final Class<?> clazz) {
        return new Logger(LoggerFactory.getLogger(clazz.getName()));
    }
}
