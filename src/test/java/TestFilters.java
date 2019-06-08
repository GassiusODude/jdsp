import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import java.util.Arrays;
import org.junit.Test;
import jdsp.math.Convolve;
import jdsp.filters.FilterD;

/**
 * Tests the convolution function
 * @author GassiusODude
 * @since May 27, 2019
 */
public class TestFilters{
    float threshF = 0.0000001f;
    double threshD = 0.000000000001;
    float[] expected1 = {1.0f, 4.0f, 4.0f};
    float[] expected2 = {9.0f, 24.0f, 46.0f, 40.0f, 25.0f};
    float[] expected3 = {3.0f, 10.0f, 13.0f, 10.0f};
    float[] expected4 = {3.0f, 16.0f, 33.0f, 36.0f, 20.0f};
    float[] s1 = {1.0f, 2.0f};
    float[] s2 = {3.0f, 4.0f, 5.0f};
    float[] outF;
    final double[] d1 = {3.0, 4.0, 5.0};
    final double[] d2 = {4, 5, 6, 7, 8, 9};
    final double[] d3 = {12, 31, 58, 70, 82, 94, 76, 45};
    double[] outD;
    
    @Test
    /** Test convolve of floats equal length */
    public void testConvolve1(){
        outF = Convolve.convolve(s1, s1);
        assertArrayEquals(expected1, outF, threshF);
    }

    @Test
    /** Test convolve of floats equal length 2 */
    public void testConvolve2(){
        outF = Convolve.convolve(s2, s2);
        assertArrayEquals(expected2, outF, threshF);
    }

    @Test
    /** Test convolve of floats of different length */
    public void testConvolve3(){
        outF = Convolve.convolve(s2, s1);
        assertArrayEquals(expected3, outF, threshF);
    }

    @Test
    /** Test convolve of floats of different length */
    public void testConvolve4(){
        outF = Convolve.convolve(expected3, s1);
        assertArrayEquals(expected4, outF, threshF);
    }

    @Test
    /** Test convolve of doubles */
    public void testConvolve5(){
        outD = Convolve.convolve(d1, d2);
        assertArrayEquals(d3, outD, threshD);
    }

    @Test
    /**
     * Test running filter in blocks.
     */
    public void testFilterD(){
        FilterD f = new FilterD(1);
        f.designFilter(11, 0, "HANN", 0.1);

        double[] tmp1 = {1,2,3,4,5,6,7};
        double[] tmp2 = {1,2,3,4,5,6,7};
        double[] out = f.applyFilter(tmp1);
        
        FilterD f2 = new FilterD(1);
        f2.designFilter(11, 0, "HANN", 0.1);
        double[] outPart1 = f2.applyFilter(Arrays.copyOfRange(tmp2, 0, 3));
        double[] outPart2 = f2.applyFilter(Arrays.copyOfRange(tmp2, 3, tmp2.length));
        double[] out2 = new double[out.length];
        System.arraycopy(outPart1, 0, out2, 0, outPart1.length);
        System.arraycopy(outPart2, 0, out2, outPart1.length, outPart2.length);
        
        assertEquals(out.length, outPart1.length + outPart2.length);
        assertArrayEquals(out, out2, 0.1);
    }
}