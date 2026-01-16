import static org.junit.Assert.assertEquals;
import org.junit.Test;
import net.kcundercover.jdsp.math.Matrix;

public class TestMatrix {
    final static double thresh = 0.000001;

    @Test
    public void testDet() {
        double[][] matrix = {{-2, 2, -3}, {-1, 1, 3}, {2, 0, -1}};
        double d = Matrix.determinant(matrix);
        assertEquals(18, d, thresh);
    }

    @Test
    public void testMultiply() {
        double[][] matrixA = {{-2, 2, -3}, {-1, 1, 3}, {2, 0, -1}};
        double[][] matrixB = {{-2, 2, -3}, {-1, 1, 3}, {2, 0, -1}};
        double[][] matrixC = new double[3][3];
        Matrix.matrixMultiply(matrixA, matrixB, matrixC);

        assertEquals(-4, matrixC[0][0], thresh);
        assertEquals(-2, matrixC[0][1], thresh);
        assertEquals(15, matrixC[0][2], thresh);
        assertEquals(7, matrixC[1][0], thresh);
        assertEquals(-1, matrixC[1][1], thresh);
        assertEquals(3, matrixC[1][2], thresh);
        assertEquals(-6, matrixC[2][0], thresh);
        assertEquals(4, matrixC[2][1], thresh);
        assertEquals(-5, matrixC[2][2], thresh);
    }

    @Test
    public void testInverse() {
        double[][] matrixA = {{4, 7}, {2, 6}};
        double[][] matrixB = new double[2][2];
        Matrix.inverse(matrixA, matrixB);
        assertEquals(0.6, matrixB[0][0], thresh);
        assertEquals(-0.7, matrixB[0][1], thresh);
        assertEquals(-0.2, matrixB[1][0], thresh);
        assertEquals(0.4, matrixB[1][1], thresh);
    }
}