package pl.cezaryregec.auth;

public enum ClientAuthState {
    HELLO_INIT(true),
    HELLO_CLIENT_DONE(true),
    HELLO_CLIENT_REFUSED(false),
    CHANGE_CIPHER_SPEC(true),
    HELLO_RESUME(true),
    HELLO_CLIENT_VERIFY(true),
    CIPHER_SPEC_SYNC(true),
    BYE(true),
    LOGIN_INIT(true);

    private Boolean isSuccessful;

    ClientAuthState(Boolean isSuccessful) {
        this.isSuccessful = isSuccessful;
    }

    public Boolean isSuccessful() {
        return isSuccessful;
    }
}
