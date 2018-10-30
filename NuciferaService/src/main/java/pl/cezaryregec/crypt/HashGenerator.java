package pl.cezaryregec.crypt;

import pl.cezaryregec.exception.APIException;

import javax.validation.constraints.NotNull;

public interface HashGenerator {

    String encode(@NotNull String text) throws APIException;
    byte[] encode(@NotNull byte[] bytes) throws APIException;
}
