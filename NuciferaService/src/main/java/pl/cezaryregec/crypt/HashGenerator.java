package pl.cezaryregec.crypt;

public interface HashGenerator {

    String encode(String text);
    byte[] encode(byte[] bytes);
}
