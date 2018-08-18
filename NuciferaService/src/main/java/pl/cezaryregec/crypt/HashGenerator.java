package pl.cezaryregec.crypt;

public interface HashGenerator {
    public String encode(String text);

    public byte[] encode(byte[] bytes);
}
