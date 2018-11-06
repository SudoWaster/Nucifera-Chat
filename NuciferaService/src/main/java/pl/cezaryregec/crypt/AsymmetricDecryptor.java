package pl.cezaryregec.crypt;

public interface AsymmetricDecryptor {
    String decrypt(String input);
    byte[] decrypt(byte[] input);
}
