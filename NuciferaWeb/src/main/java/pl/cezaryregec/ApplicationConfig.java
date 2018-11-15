package pl.cezaryregec;

import com.google.inject.Inject;
import com.google.inject.Injector;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import pl.cezaryregec.exception.APIExceptionMapper;
import pl.cezaryregec.filter.*;

public class ApplicationConfig extends ResourceConfig {

    @Inject
    private Injector injector;

    public ApplicationConfig() {
        property(ServerProperties.MOXY_JSON_FEATURE_DISABLE, true);
        register(JacksonFeature.class);
        register(new GuiceFeature());

        // register classes after Guice has been set up
        // so it can inject dependencies without any error
        // NOTE: anything using Guice you register here should be bound in APIServletModule
        register(getInjected(APIExceptionMapper.class));
        registerFilters();

        // services
        packages("pl.cezaryregec.services");
    }

    private void registerFilters() {
        register(RequestCommunicationLogFilter.class);
        register(ResponseCommunicationLogFilter.class);
        register(getInjected(RequestEncryptedReaderInterceptor.class));
        register(getInjected(ResponseEncryptedWriterInterceptor.class));
    }

    /**
     * Used for providers with injected constructors via Guice
     *
     * @param componentClass
     * @return instance managed by Guice
     */
    public Object getInjected(Class<?> componentClass) {
        return APIServletContextListener.injector.getInstance(componentClass);
    }
}
