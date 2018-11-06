package pl.cezaryregec;

import com.google.inject.servlet.ServletModule;
import pl.cezaryregec.auth.AuthResponseFactory;
import pl.cezaryregec.crypt.*;

public class APIServletModule extends ServletModule {

    APIServletModule() {
    }

    @Override
    public void configureServlets() {
        bind(HashGenerator.class).to(Sha256Generator.class);
        bind(AsymmetricDecryptor.class).to(RsaDecryptor.class);
        bind(AsymmetricSigner.class).to(RsaSigner.class);
        bind(AuthResponseFactory.class);
    }
}
