import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import org.junit.Test;
import jdsp.math.ComplexInterleaved;
public class TestComplexInterleaved{
    double threshDouble = 1e-12;
    float threshFloat = (float) 1e-6;
    
    @Test
    public void testMagnDouble(){
        double[] in = {3, 4, 7, 7};
        double[] out = ComplexInterleaved.magnitude(in);
        assertEquals("Magn double check", 5, out[0], threshDouble);
        assertEquals("Magn double check", Math.sqrt(2*7*7), out[1], threshDouble);
    }

    @Test
    public void testMagnFloat(){
        float[] in = {3.0f, 4.0f, 7.0f, 7.0f};
        float[] out = ComplexInterleaved.magnitude(in);
        assertEquals("Magn float check", 5.0f, out[0], threshFloat);
        assertEquals("Magn float check", (float)Math.sqrt(2*7*7), out[1], threshFloat);
    }

    @Test
    public void testAngleDouble(){
        double[] in = {7, 7, 3, -3, -4, 4, -2, -2};
        double[] out = ComplexInterleaved.angle(in);
        double[] expected = {Math.PI*0.25, -Math.PI*0.25,
            Math.PI * 0.75, -Math.PI * 0.75};
        for (int ind0 = 0; ind0 < expected.length; ind0++){
            System.out.println("Expected = " + expected[ind0] 
                + ",\tOut = " + out[ind0]);
        }
        assertArrayEquals("Angle check", expected, out, threshDouble);
    }
    @Test
    public void testAngleFloat(){
        float[] in = {7f, 7f, 3f, -3f, -4f, 4f, -2f, -2f};
        float[] out = ComplexInterleaved.angle(in);
        float[] expected = {(float) Math.PI * 0.25f, (float) -Math.PI * 0.25f,
            (float) Math.PI * 0.75f, (float) -Math.PI * 0.75f};
        for (int ind0 = 0; ind0 < expected.length; ind0++){
            System.out.println("Expected = " + expected[ind0] 
                + ",\tOut = " + out[ind0]);
        }
        assertArrayEquals("Angle check", expected, out, threshFloat);
    }

}