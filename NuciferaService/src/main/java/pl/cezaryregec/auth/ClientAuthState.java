package pl.cezaryregec.auth;

public enum ClientAuthState {
    /**
     * Init session with sent challenge
     */
    HELLO_INIT,
    /**
     * Client accepted signed challenge
     */
    HELLO_CLIENT_DONE,
    /**
     * Client refused hello
     */
    HELLO_CLIENT_REFUSED,
    /**
     * Login as user
     */
    LOGIN,
    /**
     * Invalidate session
     */
    BYE
}
