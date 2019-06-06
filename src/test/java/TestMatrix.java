import static org.junit.Assert.assertEquals;
import org.junit.Test;
import jdsp.math.Matrix;

public class TestMatrix{
    @Test
    public void testDet(){
        double[][] matrix = {{-2, 2, -3}, {-1, 1, 3}, {2, 0, -1}};
        double d = Matrix.determinant(matrix);
        assertEquals(18, d, 0.000001);
    }
    @Test
    public void testMultiply(){
        double[][] matrix_a = {{-2, 2, -3}, {-1, 1, 3}, {2, 0, -1}};
        double[][] matrix_b = {{-2, 2, -3}, {-1, 1, 3}, {2, 0, -1}};
        double[][] matrix_c = new double[3][3];
        Matrix.matrixMultiply(matrix_a, matrix_b, matrix_c);

        assertEquals(-4, matrix_c[0][0], 0.0000001);
        assertEquals(-2, matrix_c[0][1], 0.0000001);
        assertEquals(15, matrix_c[0][2], 0.0000001);
        assertEquals(7, matrix_c[1][0], 0.0000001);
        assertEquals(-1, matrix_c[1][1], 0.0000001);
        assertEquals(3, matrix_c[1][2], 0.0000001);
        assertEquals(-6, matrix_c[2][0], 0.0000001);
        assertEquals(4, matrix_c[2][1], 0.0000001);
        assertEquals(-5, matrix_c[2][2], 0.0000001);
    }
}