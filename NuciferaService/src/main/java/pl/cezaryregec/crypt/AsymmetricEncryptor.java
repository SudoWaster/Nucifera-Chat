package pl.cezaryregec.crypt;

public interface AsymmetricEncryptor {
    String encrypt(String input);
    byte[] encrypt(byte[] input);
}
