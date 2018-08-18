package pl.cezaryregec.auth.service;

import pl.cezaryregec.auth.ClientAuthState;

import java.io.Serializable;

public class PostAuthQuery implements Serializable {
    private static final long serialVersionUID = 7286090064689087685L;

    public String username;
    public String password;
    public String challenge;
    public ClientAuthState authState;
}
