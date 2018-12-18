package pl.cezaryregec.crypt;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CredentialsCombinerTest {

    private final CredentialsCombiner credentialsCombiner;

    public CredentialsCombinerTest() {
        credentialsCombiner = new CredentialsCombiner();
    }

    @Test
    public void shouldCombineLoginAndPassword() {
        String actual = credentialsCombiner.combine("test", "test", "do_not_use_default");
        String expected = "ØÔtÒesât";

        assertEquals(expected, actual);
    }
}
