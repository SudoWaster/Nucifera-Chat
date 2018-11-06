package pl.cezaryregec.logger;

import org.apache.logging.log4j.Level;

public interface NuciferaLoger {
    void log(Throwable exception);
    void log(String message);
    void log(String message, Level level);
}
