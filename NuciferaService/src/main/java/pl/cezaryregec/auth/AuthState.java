package pl.cezaryregec.auth;

public enum AuthState {
    /**
     * Hello initialized, challenge accepted
     */
    HELLO,
    /**
     * Hello failed
     */
    HELLO_FAIL,
    /**
     * Login failed
     */
    LOGIN_FAIL,
    /**
     * Used when HELLO or LOGIN is successful
     */
    AUTH_VALID
}
