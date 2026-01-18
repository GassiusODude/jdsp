import static org.junit.Assert.assertEquals;
import org.junit.Test;

import org.ejml.simple.SimpleMatrix;
import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.row.CommonOps_DDRM;

import net.kcundercover.jdsp.math.Matrix;

public class TestMatrix {
    final static double thresh = 0.000001;

    @Test
    public void testDet() {
        double[][] data = {{-2, 2, -3}, {-1, 1, 3}, {2, 0, -1}};
        int iterations = 1_000_000;

        // 1. Your Custom Matrix Object
        long startCustom = System.nanoTime();
        double dCustom = 0;
        for (int i = 0; i < iterations; i++) {
            dCustom = Matrix.determinant(data);
        }
        long endCustom = System.nanoTime();

        // 2. EJML SimpleMatrix
        SimpleMatrix ejmlMatrix = new SimpleMatrix(data);
        long startEjml = System.nanoTime();
        double dEjml = 0;
        for (int i = 0; i < iterations; i++) {
            dEjml = ejmlMatrix.determinant();
        }
        long endEjml = System.nanoTime();

        // Validation
        assertEquals(18, dCustom, thresh);
        assertEquals(18, dEjml, thresh);

        // Performance Output
        System.out.printf("Custom Matrix: %.2f ms%n", (endCustom - startCustom) / 1e6);
        System.out.printf("EJML Matrix:   %.2f ms%n", (endEjml - startEjml) / 1e6);

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

     @Test
    public void benchmarkMultiply() {
        double[][] dataA = {{-2, 2, -3}, {-1, 1, 3}, {2, 0, -1}};
        double[][] dataB = {{-2, 2, -3}, {-1, 1, 3}, {2, 0, -1}};
        int iterations = 100_000;

        // Warm-up: Essential for JIT optimization in modern JVMs
        for (int i = 0; i < 10_000; i++) {
            double[][] temp = new double[3][3];
            Matrix.matrixMultiply(dataA, dataB, temp);
        }

        // --- 1. Custom Implementation ---
        double[][] customC = new double[3][3];
        long startCustom = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            Matrix.matrixMultiply(dataA, dataB, customC);
        }
        long timeCustom = System.nanoTime() - startCustom;

        // --- 2. EJML SimpleMatrix (Object-Oriented) ---
        SimpleMatrix ejmlA = new SimpleMatrix(dataA);
        SimpleMatrix ejmlB = new SimpleMatrix(dataB);
        long startEjmlSimple = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            SimpleMatrix ejmlC = ejmlA.mult(ejmlB); // Creates new object per iteration
        }
        long timeEjmlSimple = System.nanoTime() - startEjmlSimple;

        // --- 3. EJML Procedural API (High Performance) ---
        DMatrixRMaj matA = new DMatrixRMaj(dataA);
        DMatrixRMaj matB = new DMatrixRMaj(dataB);
        DMatrixRMaj matC = new DMatrixRMaj(3, 3); // Pre-allocate result memory
        long startEjmlProc = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            CommonOps_DDRM.mult(matA, matB, matC); // Reuses memory like your custom code
        }
        long timeEjmlProc = System.nanoTime() - startEjmlProc;

        // --- Side-by-Side Printout ---
        System.out.println("\n--- Multiplication Benchmark (3x3, " + iterations + " iterations) ---");
        System.out.printf("%-20s | %-15s%n", "Implementation", "Time (ms)");
        System.out.println("---------------------|----------------");
        System.out.printf("%-20s | %-15.2f%n", "Custom (Manual)", timeCustom / 1e6);
        System.out.printf("%-20s | %-15.2f%n", "EJML SimpleMatrix", timeEjmlSimple / 1e6);
        System.out.printf("%-20s | %-15.2f%n", "EJML Procedural", timeEjmlProc / 1e6);
        
        // Final Validation
        assertEquals(-4, customC[0][0], thresh);
        assertEquals(-4, matC.get(0, 0), thresh);
    }

    @Test
    public void benchmarkInverse() {
        double[][] dataA = {{4, 7}, {2, 6}};
        int iterations = 100_000;

        // Warm-up (Ensures JIT compiler optimizes code before timing)
        for (int i = 0; i < 10_000; i++) {
            double[][] temp = new double[2][2];
            Matrix.inverse(dataA, temp);
        }

        // 1. Custom Implementation
        double[][] customB = new double[2][2];
        long startCustom = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            Matrix.inverse(dataA, customB);
        }
        long timeCustom = System.nanoTime() - startCustom;

        // 2. EJML SimpleMatrix (Object-oriented, creates new objects)
        SimpleMatrix ejmlA = new SimpleMatrix(dataA);
        long startEjmlSimple = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            SimpleMatrix ejmlB = ejmlA.invert();
        }
        long timeEjmlSimple = System.nanoTime() - startEjmlSimple;

        // 3. EJML Procedural API (High performance, reuses memory)
        DMatrixRMaj matA = new DMatrixRMaj(dataA);
        DMatrixRMaj matB = new DMatrixRMaj(2, 2);
        long startEjmlProc = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            CommonOps_DDRM.invert(matA, matB);
        }
        long timeEjmlProc = System.nanoTime() - startEjmlProc;

        // Print Side-by-Side Results
        System.out.println("\n--- Inverse Benchmark (2x2, " + iterations + " iterations) ---");
        System.out.printf("%-20s | %-15s%n", "Implementation", "Time (ms)");
        System.out.println("---------------------|----------------");
        System.out.printf("%-20s | %-15.2f%n", "Custom (Manual)", timeCustom / 1e6);
        System.out.printf("%-20s | %-15.2f%n", "EJML SimpleMatrix", timeEjmlSimple / 1e6);
        System.out.printf("%-20s | %-15.2f%n", "EJML Procedural", timeEjmlProc / 1e6);

        // Validation
        assertEquals(0.6, customB[0][0], thresh);
        assertEquals(0.6, matB.get(0, 0), thresh);
    }
}