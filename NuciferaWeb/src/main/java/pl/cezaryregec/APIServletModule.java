package pl.cezaryregec;

import com.google.inject.servlet.ServletModule;
import pl.cezaryregec.auth.AuthResponseFactory;
import pl.cezaryregec.crypt.HashGenerator;
import pl.cezaryregec.crypt.Sha256Generator;

public class APIServletModule extends ServletModule {

    APIServletModule() {
    }

    @Override
    public void configureServlets() {
        bind(HashGenerator.class).to(Sha256Generator.class);
        bind(AuthResponseFactory.class);
    }
}
