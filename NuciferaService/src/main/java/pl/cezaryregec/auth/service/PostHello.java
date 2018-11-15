package pl.cezaryregec.auth.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import pl.cezaryregec.auth.AuthState;
import pl.cezaryregec.auth.models.AuthToken;
import pl.cezaryregec.auth.models.PostAuthQuery;
import pl.cezaryregec.auth.models.PostAuthResponse;
import pl.cezaryregec.auth.session.Identity;
import pl.cezaryregec.auth.session.IdentityService;
import pl.cezaryregec.crypt.AsymmetricDecryptor;
import pl.cezaryregec.crypt.AsymmetricSigner;
import pl.cezaryregec.crypt.HashGenerator;
import pl.cezaryregec.crypt.aes.AesUtilities;
import pl.cezaryregec.exception.APIException;

public class PostHello implements PostAuth {
    private final IdentityService identityService;
    private final AsymmetricDecryptor decryptor;
    private final AsymmetricSigner signer;

    @Inject
    PostHello(IdentityService identityService, AsymmetricDecryptor decryptor, AsymmetricSigner signer) {
        this.identityService = identityService;
        this.decryptor = decryptor;
        this.signer = signer;
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
        String challenge = postAuthQuery.getChallenge();
        String plainChallenge = decryptor.decrypt(challenge);

        if (!AesUtilities.SUPPORTED_KEY_SIZES.contains(plainChallenge.length())) {
            throw new APIException("Wrong key size", 400);
        }

        identityService.createToken(plainChallenge);
        return createResponseFromToken();
    }

    private PostAuthResponse createResponseFromToken() throws APIException {
        String signedChallenge = signer.sign(identityService.getChallenge());

        PostAuthResponse postAuthResponse = new PostAuthResponse();
        postAuthResponse.setState(AuthState.HELLO);
        postAuthResponse.setChallenge(signedChallenge);
        postAuthResponse.setToken(identityService.getTokenId());

        return postAuthResponse;
    }
}
