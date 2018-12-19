package pl.cezaryregec.logger;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SecurityLogger implements NuciferaLoger {
    private final Logger LOGGER = LogManager.getLogger("security");

    @Override
    public void log(Throwable exception) {
        ApplicationLogger.logException(exception, LOGGER);
    }

    @Override
    public void log(String message) {
        LOGGER.log(Level.INFO, message);
    }

    @Override
    public void log(String message, Level level) {
        LOGGER.log(level, message);
    }
}
