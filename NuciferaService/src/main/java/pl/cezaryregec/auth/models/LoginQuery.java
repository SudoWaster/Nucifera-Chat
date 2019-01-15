package pl.cezaryregec.auth.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public @Data class LoginQuery implements Serializable {
    private static final long serialVersionUID = -6336297086981421166L;

    private String username;
    private String password;
}
