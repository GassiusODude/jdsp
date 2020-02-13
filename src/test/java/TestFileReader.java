import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import org.junit.Test;
import jdsp.io.FileReader;

public class TestFileReader{
    @Test
    public void testByteToShort(){
        byte[] bytes = {1,2,3,4,5,6,7,8};
        short[] sList = new short[4];
        short[] expectedBig = {258, 772, 1286, 1800 };
        short[] expectedLittle = {513, 1027, 1541, 2055};

        // --------------------  test big endian  ---------------------------
        FileReader.bytes_to_short(bytes, sList, true);
        assertArrayEquals("Byte to Short (big endian) failed",
            expectedBig, sList);

        // -----------------  test little endian  ---------------------------
        FileReader.bytes_to_short(bytes, sList, false);
        assertArrayEquals("Byte to Short (big endian) failed",
            expectedLittle, sList);
    }

    
}
