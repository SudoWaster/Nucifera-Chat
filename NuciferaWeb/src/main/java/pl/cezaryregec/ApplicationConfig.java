package pl.cezaryregec;

import org.glassfish.jersey.server.ResourceConfig;

public class ApplicationConfig extends ResourceConfig {

    ApplicationConfig() {
        register(new GuiceFeature());
    }
}
