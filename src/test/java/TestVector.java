import static org.junit.Assert.*;
import org.junit.Test;
import net.kcundercover.jdsp.math.Vector;

public class TestVector {

    private static final double D_THRESH = 1e-9f;
    private static final float  F_THRESH = 1e-6f;

    // =========================================================
    // ======================= DOUBLE ==========================
    // =========================================================

    @Test
    public void testDoubleAdd() {
        double[] a = {1, 2, 3};
        double[] b = {4, 5, 6};
        assertArrayEquals(new double[]{5, 7, 9}, Vector.add(a, b), D_THRESH);
    }

    @Test
    public void testDoubleSubtract() {
        double[] a = {5, 7, 9};
        double[] b = {1, 2, 3};
        assertArrayEquals(new double[]{4, 5, 6}, Vector.subtract(a, b), D_THRESH);
    }

    @Test
    public void testDoubleMultiply() {
        double[] a = {2, 3, 4};
        double[] b = {5, 6, 7};
        assertArrayEquals(new double[]{10, 18, 28}, Vector.multiply(a, b), D_THRESH);
    }

    @Test
    public void testDoubleDivide() {
        double[] a = {10, 20, 30};
        double[] b = {2, 4, 5};
        assertArrayEquals(new double[]{5, 5, 6}, Vector.divide(a, b), D_THRESH);
    }

    @Test
    public void testDoubleSumAndProduct() {
        double[] a = {1, 2, 3, 4};
        assertEquals(10, Vector.sum(a), D_THRESH);
        assertEquals(24, Vector.product(a), D_THRESH);
    }

    @Test
    public void testDoubleDotAndNorms() {
        double[] a = {1, 2, 3};
        double[] b = {4, 5, 6};
        assertEquals(32, Vector.dot(a, b), D_THRESH);   // 1*4+2*5+3*6
        assertEquals(Math.sqrt(14), Vector.norm2(a), D_THRESH);
    }

    // =========================================================
    // ======================== FLOAT ==========================
    // =========================================================

    @Test
    public void testFloatAddAndMultiply() {
        float[] a = {1f, 2f, 3f};
        float[] b = {4f, 5f, 6f};
        assertArrayEquals(new float[]{5f, 7f, 9f}, Vector.add(a, b), F_THRESH);
        assertArrayEquals(new float[]{4f, 10f, 18f}, Vector.multiply(a, b), F_THRESH);
    }

    @Test
    public void testFloatDotAndNorm() {
        float[] a = {3f, 4f};
        assertEquals(25f, Vector.dot(a, a), F_THRESH);
        assertEquals(5f, Vector.norm2(a), F_THRESH);
    }

    // =========================================================
    // ========================= LONG ==========================
    // =========================================================

    @Test
    public void testLongAddMultiplyDot() {
        long[] a = {1, 2, 3};
        long[] b = {4, 5, 6};

        assertArrayEquals(new long[]{5, 7, 9}, Vector.add(a, b));
        assertArrayEquals(new long[]{4, 10, 18}, Vector.multiply(a, b));
        assertEquals(32L, Vector.dot(a, b));
    }

    @Test
    public void testLongSumProduct() {
        long[] a = {1, 2, 3, 4};
        assertEquals(10L, Vector.sum(a));
        assertEquals(24L, Vector.product(a));
    }

    // =========================================================
    // ========================== INT ==========================
    // =========================================================

    @Test
    public void testIntAddSubtractDot() {
        int[] a = {1, 2, 3};
        int[] b = {4, 5, 6};

        assertArrayEquals(new int[]{5, 7, 9}, Vector.add(a, b));
        assertArrayEquals(new int[]{3, 3, 3}, Vector.subtract(b, a));
        assertEquals(32L, Vector.dot(a, b)); // widened
    }

    @Test
    public void testIntSumProduct() {
        int[] a = {1, 2, 3, 4};
        assertEquals(10, Vector.sum(a));
        assertEquals(24L, Vector.product(a));
    }

    // =========================================================
    // ========================= SHORT =========================
    // =========================================================

    @Test
    public void testShortAddMultiplyDot() {
        short[] a = {1, 2, 3};
        short[] b = {4, 5, 6};

        assertArrayEquals(new short[]{5, 7, 9}, Vector.add(a, b));
        assertArrayEquals(new short[]{4, 10, 18}, Vector.multiply(a, b));
        assertEquals(32L, Vector.dot(a, b)); // widened
    }

    @Test
    public void testShortSumProduct() {
        short[] a = {1, 2, 3, 4};
        assertEquals(10, Vector.sum(a));
        assertEquals(24L, Vector.product(a));
    }
}
