package pl.cezaryregec.exception;

import javax.ws.rs.ClientErrorException;
import com.fasterxml.jackson.annotation.JsonProperty;

public class APIException extends Exception {
    private static final long serialVersionUID = -5611226621818155268L;

    private Integer errorCode;
    private String message;

    public APIException(String message) {
        super(message);
        this.message = message;
        this.errorCode = 500;
    }

    public APIException(String message, Integer errorCode) {
        super(message);
        this.message = message;
        this.errorCode = errorCode;
    }

    public APIException(Throwable throwable) {
        this.message = throwable.getMessage();
        this.setStackTrace(throwable.getStackTrace());
        this.errorCode = 500;

        if (throwable instanceof ClientErrorException) {
            this.errorCode = ((ClientErrorException) throwable).getResponse().getStatus();
        }

        if (throwable instanceof APIException) {
            this.errorCode = ((APIException) throwable).errorCode;
        }
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("error")
    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }
}
