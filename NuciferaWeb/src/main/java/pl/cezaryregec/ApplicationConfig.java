package pl.cezaryregec;

import org.glassfish.jersey.server.ResourceConfig;
import pl.cezaryregec.exception.APIExceptionMapper;
import pl.cezaryregec.filter.RequestCommunicationDecryptorFilter;
import pl.cezaryregec.filter.RequestCommunicationLogFilter;
import pl.cezaryregec.filter.ResponseCommunicationLogFilter;

public class ApplicationConfig extends ResourceConfig {

    public ApplicationConfig() {
        register(new GuiceFeature());

        // register classes after Guice has been set up
        // so it can inject dependencies without any error
        register(APIExceptionMapper.class);
        registerFilters();

        // services
        packages("pl.cezaryregec.services");
    }

    private void registerFilters() {
        register(RequestCommunicationLogFilter.class);
        register(ResponseCommunicationLogFilter.class);
        register(RequestCommunicationDecryptorFilter.class);
    }
}
