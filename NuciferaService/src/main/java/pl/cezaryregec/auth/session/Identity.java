package pl.cezaryregec.auth.session;

import com.google.inject.servlet.SessionScoped;
import lombok.Data;
import pl.cezaryregec.auth.models.AuthToken;

@SessionScoped
public @Data
class Identity {
    private AuthToken token;
    private Boolean cipherSpec = false;
}
