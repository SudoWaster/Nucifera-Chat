package pl.cezaryregec;

import org.glassfish.jersey.server.ResourceConfig;

public class ApplicationConfig extends ResourceConfig {

    public ApplicationConfig() {
        packages("pl.cezaryregec.exception");
        packages("pl.cezaryregec.filter");
        packages("pl.cezaryregec.services");

        register(new GuiceFeature());
    }
}
