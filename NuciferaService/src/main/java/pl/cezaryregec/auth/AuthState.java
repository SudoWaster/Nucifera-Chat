package pl.cezaryregec.auth;

/**
 * Handshake states
 *
 * Used for indicating handshake state, used with additional layer of encryption
 */
public enum AuthState {
    /**
     * Hello initialized, challenge accepted
     */
    HELLO,
    /**
     * HELLO has failed
     */
    FAIL,
    /**
     * Used when HELLO is successful
     */
    HELLO_VALID
}
