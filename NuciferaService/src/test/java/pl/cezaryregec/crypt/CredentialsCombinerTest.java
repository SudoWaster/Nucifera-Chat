package pl.cezaryregec.crypt;

import org.junit.jupiter.api.Test;

public class CredentialsCombinerTest {

    private final CredentialsCombiner credentialsCombiner;

    public CredentialsCombinerTest() {
        credentialsCombiner = new CredentialsCombiner();
    }

    @Test
    public void shouldCombineLoginAndPassword() {
        String test = credentialsCombiner.combine("test", "test", "do_not_use_default");
        System.out.println(test);
    }
}
