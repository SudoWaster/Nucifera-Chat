package pl.cezaryregec.auth.service;

import pl.cezaryregec.auth.ClientAuthState;

import java.io.Serializable;

public class PostAuthQuery implements Serializable {
    private static final long serialVersionUID = 7286090064689087685L;

    private String username;
    private String password;
    private String challenge;
    private ClientAuthState authState;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ClientAuthState getAuthState() {
        return authState;
    }

    public void setAuthState(ClientAuthState authState) {
        this.authState = authState;
    }

    public String getChallenge() {
        return challenge;
    }

    public void setChallenge(String challenge) {
        this.challenge = challenge;
    }
}
