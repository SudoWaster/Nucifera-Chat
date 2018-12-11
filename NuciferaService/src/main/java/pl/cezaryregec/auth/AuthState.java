package pl.cezaryregec.auth;

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
    HELLO_VALID,
    /**
     * Used when LOGIN fails (usually login or password is incorrect)
     */
    LOGIN_FAIL,
    /**
     * Used when LOGIN is successful
     */
    AUTH_VALID
}
