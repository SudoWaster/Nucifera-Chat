package pl.cezaryregec.crypt;

import pl.cezaryregec.exception.APIException;

public interface AsymmetricSigner {
    String sign(String input) throws APIException;
    byte[] sign(byte[] input) throws APIException;
}
