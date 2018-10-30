package pl.cezaryregec.exception;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect(
        fieldVisibility = Visibility.NONE,
        setterVisibility = Visibility.NONE,
        getterVisibility = Visibility.NONE,
        isGetterVisibility = Visibility.NONE,
        creatorVisibility = Visibility.NONE
)
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
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("errorCode")
    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }
}
