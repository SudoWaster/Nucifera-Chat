package pl.cezaryregec;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import pl.cezaryregec.exception.APIExceptionMapper;
import pl.cezaryregec.filter.*;
import pl.cezaryregec.writer.ByteStreamBodyWriter;

public class ApplicationConfig extends ResourceConfig {

    public ApplicationConfig() {
        property(ServerProperties.MOXY_JSON_FEATURE_DISABLE, true);
        register(JacksonFeature.class);
        register(JacksonJaxbJsonProvider.class);
        register(new GuiceFeature());
        register(ByteStreamBodyWriter.class);
        register(APIServletContextListener.injector.getInstance(APIExceptionMapper.class));
        registerFilters();

        // resources
        packages("pl.cezaryregec.resources");
    }

    private void registerFilters() {
        register(RequestCommunicationLogFilter.class);
        register(ResponseCommunicationLogFilter.class);
        register(RequestEncryptedReaderInterceptor.class);
        register(ResponseEncryptedWriterInterceptor.class);
        register(PlainRequestTokenReceiverFilter.class);
    }
}
