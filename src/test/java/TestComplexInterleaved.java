import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import org.junit.jupiter.api.Test;
import net.kcundercover.jdsp.math.ComplexInterleaved;
public class TestComplexInterleaved{
    double threshDouble = 1e-12;
    float threshFloat = (float) 1e-6;

    @Test
    public void testMagnDouble(){
        double[] in = {3, 4, 7, 7};
        double[] out = ComplexInterleaved.magnitude(in);
        assertEquals(5, out[0], threshDouble, "Magn double check");
        assertEquals(Math.sqrt(2*7*7), out[1], threshDouble, "Magn double check");
    }

    @Test
    public void testMagnFloat(){
        float[] in = {3.0f, 4.0f, 7.0f, 7.0f};
        float[] out = ComplexInterleaved.magnitude(in);
        assertEquals(5.0f, out[0], threshFloat, "Magn float check");
        assertEquals((float)Math.sqrt(2*7*7), out[1], threshFloat, "Magn float check");
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
        assertArrayEquals(expected, out, threshDouble, "Angle check");
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
        assertArrayEquals(expected, out, threshFloat, "Angle check");
    }

}