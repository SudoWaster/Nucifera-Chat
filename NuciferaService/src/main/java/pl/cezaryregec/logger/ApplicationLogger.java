package pl.cezaryregec.logger;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class ApplicationLogger implements NuciferaLoger {
    private final Logger LOGGER = LogManager.getLogger("application");

    @Override
    public void log(Throwable exception) {
        logException(exception, LOGGER);
    }

    static void logException(Throwable exception, Logger logger) {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (PrintStream printStream = new PrintStream(baos, true, StandardCharsets.UTF_8.name())) {
            exception.printStackTrace(printStream);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String stackTrace = new String(baos.toByteArray(), StandardCharsets.UTF_8);
        String message = exception.getMessage() + "\n";
        message += "Stack trace: " + stackTrace;

        logger.log(Level.ERROR, message);
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
