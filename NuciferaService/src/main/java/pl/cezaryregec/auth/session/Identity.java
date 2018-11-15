package pl.cezaryregec.auth.session;

import java.util.Optional;
import lombok.Data;
import pl.cezaryregec.auth.models.AuthToken;

public @Data
class Identity {
    private Optional<AuthToken> token = Optional.empty();
    private String fingerprint;
}
