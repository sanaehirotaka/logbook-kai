package org.apache.logging.log4j;

public class Logger {

    private org.slf4j.Logger logger;

    Logger(org.slf4j.Logger logger) {
        this.logger = logger;
    }

    public boolean isDebugEnabled() {
        return this.logger.isDebugEnabled();
    }

    public void debug(Object message) {
        this.logger.debug(String.valueOf(message));
    }

    public void debug(String message) {
        this.logger.debug(message);
    }

    public void debug(String message, Object... params) {
        this.logger.debug(message, params);
    }

    public void debug(String message, Throwable t) {
        this.logger.debug(message, t);
    }

    public void info(Object message) {
        this.logger.info(String.valueOf(message));
    }

    public void info(String message) {
        this.logger.info(message);
    }

    public void info(String message, Object... params) {
        this.logger.info(message, params);
    }

    public void info(String message, Throwable t) {
        this.logger.info(message, t);
    }

    public void warn(Object message) {
        this.logger.warn(String.valueOf(message));
    }

    public void warn(String message) {
        this.logger.warn(message);
    }

    public void warn(String message, Object... params) {
        this.logger.warn(message, params);
    }

    public void warn(String message, Throwable t) {
        this.logger.warn(message, t);
    }

    public void error(Object message) {
        this.logger.error(String.valueOf(message));
    }

    public void error(String message) {
        this.logger.error(message);
    }

    public void error(String message, Object... params) {
        this.logger.error(message, params);
    }

    public void error(String message, Throwable t) {
        this.logger.error(message, t);
    }

    public void fatal(Object message) {
        this.logger.error(String.valueOf(message));
    }

    public void fatal(String message) {
        this.logger.error(message);
    }

    public void fatal(String message, Object... params) {
        this.logger.error(message, params);
    }

    public void fatal(String message, Throwable t) {
        this.logger.error(message, t);
    }
}
