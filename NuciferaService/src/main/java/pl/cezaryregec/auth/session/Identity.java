package pl.cezaryregec.auth.session;

import lombok.Data;
import pl.cezaryregec.auth.models.AuthToken;

import java.util.Optional;

public @Data
class Identity {
    private Optional<AuthToken> token = Optional.empty();
    private String fingerprint;
}
