package pl.cezaryregec.crypt;

import pl.cezaryregec.exception.APIException;

public interface AsymmetricDecryptor {
    String decrypt(String input) throws APIException;
    byte[] decrypt(byte[] input) throws APIException;
}
