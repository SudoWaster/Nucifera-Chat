package pl.cezaryregec.crypt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.cezaryregec.exception.APIException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class Sha256GeneratorTest {

    private Sha256Generator sha256Generator;

    public Sha256GeneratorTest() {
        sha256Generator = new Sha256Generator();
    }

    @Test
    @DisplayName("Test SHA-256 to byte[] encoding")
    public void checkEncoding() throws APIException {
        // given
        byte[] plain = { 86, 105, 118, 97, 109, 117, 115, 32, 114, 117, 116, 114, 117, 109, 32, 97, 32, 111, 114, 99, 105, 32, 115, 117, 115, 99, 105, 112, 105, 116, 32, 103, 114, 97, 118, 105, 100, 97, 46, 32, 86, 101, 115, 116, 105, 98, 117, 108, 117, 109, 32, 101, 117, 32, 118, 97, 114, 105, 117, 115, 32, 111, 100, 105, 111, 46 };
        byte[] encoded = { -86, 22, -39, 13, -24, 75, 33, 27, -11, -57, -27, -16, 16, 10, -128, -72, 5, -17, 89, 99, -50, 22, 35, -33, -87, 119, -97, -105, -47, 82, 96, -110 };
        // when
        byte[] result = sha256Generator.encode(plain);
        // then
        assertArrayEquals(encoded, result);
    }

    @Test
    @DisplayName("Test SHA-256 to Base64 encoding")
    public void checkBase64Encoding() throws APIException {
        // given
        String plain = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin sollicitudin tristique massa id semper. Aenean commodo aliquam tempus. Maecenas in ipsum tristique, facilisis tellus in, ultricies leo. Integer urna enim, semper non ullamcorper eget, tincidunt interdum ligula. Vivamus eu velit vestibulum, rhoncus dolor in, dapibus quam. Praesent maximus, purus ac rhoncus mattis, odio justo aliquam justo, sit amet sagittis tellus magna imperdiet tortor. Phasellus sem est, mattis nec lacus blandit, congue eleifend magna. Nulla facilisi. Quisque facilisis, leo quis dictum cursus, quam justo laoreet elit, a blandit dolor justo vitae sapien. Vestibulum egestas hendrerit odio, sed dictum metus gravida at. Nulla sagittis interdum augue eget placerat. Integer sed mi non dui congue lacinia at sit amet orci. Aliquam erat volutpat. Fusce tempus, sem ut imperdiet cursus, risus ante feugiat ligula, id rhoncus arcu augue ac dui. Interdum et malesuada fames ac ante ipsum primis in faucibus. Vivamus porttitor, nunc nec placerat laoreet, sapien magna feugiat dui, sed tempus mauris dolor vitae sem. ";
        String encoded = "YcnklTMX1HIiij3NvoS6WJp6agUlthWKRai4ExF+3HU=";
        // when
        String result = sha256Generator.encode(plain);
        // then
        assertEquals(encoded, result);
    }
}
