package pl.cezaryregec.auth.models;

import lombok.Data;
import pl.cezaryregec.auth.AuthState;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public @Data class PostAuthResponse implements Serializable {
    private static final long serialVersionUID = -4073519791448071448L;

    private AuthState state;
    private String challenge;
    private String token;
}
