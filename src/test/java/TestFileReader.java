import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import org.junit.jupiter.api.Test;
import net.kcundercover.jdsp.io.FileReader;

public class TestFileReader{
    @Test
    public void testByteToShort(){
        byte[] bytes = {1,2,3,4,5,6,7,8};
        short[] sList = new short[4];
        short[] expectedBig = {258, 772, 1286, 1800 };
        short[] expectedLittle = {513, 1027, 1541, 2055};

        // --------------------  test big endian  ---------------------------
        FileReader.bytesToShort(bytes, sList, true);
        assertArrayEquals(
            expectedBig, sList, "Byte to Short (big endian) failed");

        // -----------------  test little endian  ---------------------------
        FileReader.bytesToShort(bytes, sList, false);
        assertArrayEquals(
            expectedLittle, sList, "Byte to Short (big endian) failed");
    }


}
