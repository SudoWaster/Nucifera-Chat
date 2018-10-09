package pl.cezaryregec.auth;

public enum AuthState {
    HELLO,
    HELLO_DONE,
    HELLO_REFUSED,
    HELLO_FINISH,
    HELLO_FAIL,
    HELLO_VERIFY,
    RESUME_DONE,
    RESUME_FAIL
}
