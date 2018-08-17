package pl.cezaryregec;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.GuiceServletContextListener;

import javax.servlet.ServletContextEvent;

public class APIServletContextListener extends GuiceServletContextListener {

    static Injector injector;
    private static PersistService persistService;

    /**
     * Creates an injector for a whole app with JPA (and starts it)
     *
     * @return created injector
     */
    @Override
    protected Injector getInjector() {
        injector = Guice.createInjector(new APIServletModule(), new JpaPersistModule("NuciferaPersistence"));
        persistService = injector.getInstance(PersistService.class);
        persistService.start();
        return injector;
    }

    /**
     * Stops JPA service to prevent db data loss and errors
     *
     * @param servletContextEvent event that destroyed context
     */
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        persistService.stop();
    }
}
