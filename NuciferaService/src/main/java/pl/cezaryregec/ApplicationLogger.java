package pl.cezaryregec;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ApplicationLogger {
    private static final Logger LOGGER = LogManager.getLogger("application");

    public static void log(Exception exception) {
        LOGGER.log(Level.ERROR, exception.getMessage());
    }
}
