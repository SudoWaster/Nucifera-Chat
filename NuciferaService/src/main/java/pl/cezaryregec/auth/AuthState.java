package pl.cezaryregec.auth;

public enum AuthState {
    /**
     * Hello initialized, challenge accepted
     */
    HELLO,
    /**
     * HELLO or LOGIN has failed
     */
    FAIL,
    /**
     * Used when HELLO or LOGIN is successful
     */
    AUTH_VALID
}
