package pl.cezaryregec;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class ApplicationLogger {
    private final Logger LOGGER = LogManager.getLogger("application");

    public void log(Throwable exception) {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (PrintStream printStream = new PrintStream(baos, true, StandardCharsets.UTF_8.name())) {
            exception.printStackTrace(printStream);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String stackTrace = new String(baos.toByteArray(), StandardCharsets.UTF_8);
        String message = exception.getMessage() + "\n";
        message += "Stack trace: " + stackTrace;

        LOGGER.log(Level.ERROR, message);
    }
}
