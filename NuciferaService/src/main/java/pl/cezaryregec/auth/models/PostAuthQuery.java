package pl.cezaryregec.auth.models;

import lombok.Data;
import pl.cezaryregec.auth.ClientAuthState;

import java.io.Serializable;
import java.math.BigInteger;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public @Data class PostAuthQuery implements Serializable {
    private static final long serialVersionUID = 7286090064689087685L;

    private ClientAuthState state;
    private String challenge;
}
