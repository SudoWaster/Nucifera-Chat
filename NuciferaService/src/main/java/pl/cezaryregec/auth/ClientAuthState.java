package pl.cezaryregec.auth;

/**
 * Handshake states
 *
 * Used for indicating handshake state, used with additional layer of encryption
 */
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
    HELLO_CLIENT_REFUSED
}
