package pl.cezaryregec;

import com.google.inject.servlet.ServletModule;
import pl.cezaryregec.auth.AuthResponseFactory;
import pl.cezaryregec.crypt.*;
import pl.cezaryregec.crypt.aes.AesDecryptor;
import pl.cezaryregec.crypt.aes.AesEncryptor;
import pl.cezaryregec.crypt.rsa.RsaDecryptor;
import pl.cezaryregec.crypt.rsa.RsaSigner;
import pl.cezaryregec.exception.APIExceptionMapper;
import pl.cezaryregec.filter.RequestEncryptedReaderInterceptor;
import pl.cezaryregec.filter.ResponseEncryptedWriterInterceptor;
import pl.cezaryregec.heartbeat.service.HeartbeatService;

import java.time.Clock;

public class APIServletModule extends ServletModule {

    APIServletModule() {
    }

    @Override
    public void configureServlets() {
        bind(Clock.class).toInstance(Clock.systemUTC());

        bind(HashGenerator.class).to(Sha256Generator.class);
        bind(AsymmetricDecryptor.class).to(RsaDecryptor.class);
        bind(AsymmetricSigner.class).to(RsaSigner.class);
        bind(SymmetricEncryptor.class).to(AesEncryptor.class);
        bind(SymmetricDecryptor.class).to(AesDecryptor.class);
        bind(AuthResponseFactory.class);
        bind(HeartbeatService.class);

        bind(APIExceptionMapper.class);
        bind(RequestEncryptedReaderInterceptor.class);
        bind(ResponseEncryptedWriterInterceptor.class);
    }
}
