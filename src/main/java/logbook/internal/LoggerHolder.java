package logbook.internal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.ReflectionUtil;

/**
 * ロギング
 *
 */
public class LoggerHolder {

    public static LoggerProxy get() {
        return new LoggerProxy();
    }

    public static class LoggerProxy {

        private Class<?> callerClass;

        private Logger logger;

        public LoggerProxy() {
            this.callerClass = ReflectionUtil.getCallerClass(3);
            this.logger = LogManager.getLogger(this.callerClass);
        }

        public boolean isDebugEnabled() {
            return this.logger.isDebugEnabled();
        }

        public void debug(Object message) {
            this.logger.debug(message);
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
            this.logger.info(message);
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
            this.logger.warn(message);
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
            this.logger.error(message);
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
            this.logger.fatal(message);
        }

        public void fatal(String message) {
            this.logger.fatal(message);
        }

        public void fatal(String message, Object... params) {
            this.logger.fatal(message, params);
        }

        public void fatal(String message, Throwable t) {
            this.logger.fatal(message, t);
        }
    }
}
