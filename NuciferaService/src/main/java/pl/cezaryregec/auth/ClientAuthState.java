package pl.cezaryregec.auth;

public enum ClientAuthState {
    /**
     * Init session with sent challenge
     */
    HELLO_INIT,
    /**
     * Client accepted hello (and challenge is valid)
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
