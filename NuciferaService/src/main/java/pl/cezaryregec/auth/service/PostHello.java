package pl.cezaryregec.auth.service;

import com.google.inject.Inject;
import pl.cezaryregec.auth.AuthState;
import pl.cezaryregec.auth.models.PostAuthQuery;
import pl.cezaryregec.auth.models.PostAuthResponse;
import pl.cezaryregec.auth.session.IdentityService;
import pl.cezaryregec.config.ConfigSupplier;
import pl.cezaryregec.crypt.AsymmetricDecryptor;
import pl.cezaryregec.crypt.AsymmetricSigner;
import pl.cezaryregec.crypt.aes.AesUtilities;
import pl.cezaryregec.exception.APIException;

import java.util.Random;

public class PostHello implements PostAuth {
    private final IdentityService identityService;
    private final AsymmetricDecryptor decryptor;
    private final AsymmetricSigner signer;
    private final ConfigSupplier configSupplier;
    private final Random random;

    @Inject
    PostHello(
            IdentityService identityService,
            AsymmetricDecryptor decryptor,
            AsymmetricSigner signer,
            ConfigSupplier configSupplier,
            Random random
    ) {
        this.identityService = identityService;
        this.decryptor = decryptor;
        this.signer = signer;
        this.configSupplier = configSupplier;
        this.random = random;
    }

    /**
     * Client initiated a handshake. Save new session data and verify server identity.
     *
     * @param postAuthQuery
     * @return {@link PostAuthResponse} with current auth state
     * @throws APIException when decryption or challenge signing has a problem
     */
    @Override
    public PostAuthResponse execute(PostAuthQuery postAuthQuery) throws APIException {
        String plainChallenge = String.valueOf(random.nextLong());
        if (configSupplier.get().getSecurity().getAdditionalEncryption()) {
            String challenge = postAuthQuery.getChallenge();
            plainChallenge = decryptor.decrypt(challenge);

            if (!AesUtilities.SUPPORTED_KEY_SIZES.contains(plainChallenge.length())) {
                throw new APIException("Wrong key size", 400);
            }
        }

        identityService.createToken(plainChallenge);
        return createResponseFromToken();
    }

    private PostAuthResponse createResponseFromToken() throws APIException {
        PostAuthResponse postAuthResponse = new PostAuthResponse();
        postAuthResponse.setState(AuthState.HELLO);
        if (configSupplier.get().getSecurity().getAdditionalEncryption()) {
            String signedChallenge = signer.sign(identityService.getChallenge());
            postAuthResponse.setChallenge(signedChallenge);
        }
        postAuthResponse.setToken(identityService.getTokenId());

        return postAuthResponse;
    }
}
