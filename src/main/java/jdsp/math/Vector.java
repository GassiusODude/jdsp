package jdsp.math;
import java.util.Arrays;
public class Vector{
    // ===========================  double support  =========================
    /**
     * Add two vectors together
     * @param vec1 Vector 1
     * @param vec2 Vector 2
     * @return Elementwisee sum of the two arrays
     */
    public static double[] add(final double[] vec1, final double[] vec2){
        // ---------------------  error checking  ---------------------------
        if (vec1.length != vec2.length){
            throw new IllegalArgumentException(
                "array lengths need to match");
        }

        // ---------------------  perform operation  ------------------------
        double[] output = new double[vec1.length];
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            output[ind0] = vec1[ind0] + vec2[ind0];
        }
        return output;
    }

    /**
     * Add a scalar to each element of the vector
     * @param vec1 Input vector
     * @param scalar Scalar value
     * @return The updated vector
     */
    public static double[] add(final double[] vec1, double scalar){
        double[] output = new double[vec1.length];
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            output[ind0] = vec1[ind0] + scalar;
        }
        return output;
    }

    /**
     * Add two vectors together and store result in vector 1
     * @param vec1 Vector 1
     * @param vec2 Vector 2
     */
    public static void addMe(double[] vec1, final double[] vec2){
        // ---------------------  error checking  ---------------------------
        if (vec1.length != vec2.length){
            throw new IllegalArgumentException(
                "array lengths need to match");
        }

        // ---------------------  perform operation  ------------------------
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            vec1[ind0] += vec2[ind0];
        }
    }

    /**
     * Add scalar to the vector (without creating a new vector)
     * @param vec1 Input vector and target of where the sum is returned
     * @param scalar Value to add
     */
    public static void addMe(double[] vec1, double scalar){
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            vec1[ind0] += scalar;
        }
    }

    /**
     * Divide elements of vec1 by the vec2
     * @param vec1 Input vector 1
     * @param vec2 Input vector 2
     * @return The quotient of the division operation
     */
    public static double[] divide(final double[] vec1, final double[] vec2){
        // ---------------------  error checking  ---------------------------
        if (vec1.length != vec2.length){
            throw new IllegalArgumentException(
                "divide(): array lengths need to match");
        }

        // ---------------------  perform operation  ------------------------
        double[] output = new double[vec1.length];
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            output[ind0] = vec1[ind0] / vec2[ind0];
        }
        return output;
    }

    /**
     * Divide a scalar to each element of the vector
     * @param vec1 Input vector
     * @param scalar Scalar value
     * @return The quotient vector
     */
    public static double[] divide(final double[] vec1, double scalar){
        double[] output = new double[vec1.length];
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            output[ind0] = vec1[ind0] / scalar;
        }
        return output;
    }

    /**
     * Element-wise divide and store result in vector 1
     * @param vec1 Vector 1
     * @param vec2 Vector 2
     */
    public static void divideMe(double[] vec1, final double[] vec2){
        // ---------------------  error checking  ---------------------------
        if (vec1.length != vec2.length){
            throw new IllegalArgumentException(
                "array lengths need to match");
        }

        // ---------------------  perform operation  ------------------------
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            vec1[ind0] /= vec2[ind0];
        }
    }

    /**
     * Divide scalar from vector (without creating a new vector)
     * @param vec1 Input vector and target of where the sum is returned
     * @param scalar Value to divide
     */
    public static void divideMe(double[] vec1, double scalar){
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            vec1[ind0] /= scalar;
        }
    }
    
    /**
     * Multiply the two vectors together
     * @param vec1 Input vector 1
     * @param vec2 Input vector 2
     * @return The element-wise product of the 2 vectors
     */
    public static double[] multiply(final double[] vec1, final double[] vec2){
        // ---------------------  error checking  ---------------------------
        if (vec1.length != vec2.length){
            throw new IllegalArgumentException(
                "multiply(): array lengths need to match");
        }

        // ---------------------  perform operation  ------------------------
        double[] output = new double[vec1.length];
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            output[ind0] = vec1[ind0] * vec2[ind0];
        }
        return output;
    }

    /**
     * Multiply a scalar to each element of the vector
     * @param vec1 Input vector
     * @param scalar Scalar value
     * @return The product vector
     */
    public static double[] multiply(final double[] vec1, double scalar){
        double[] output = new double[vec1.length];
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            output[ind0] = vec1[ind0] * scalar;
        }
        return output;
    }

    /**
     * Element-wise multiply and store result in vector 1
     * @param vec1 Vector 1
     * @param vec2 Vector 2
     */
    public static void multiplyMe(double[] vec1, final double[] vec2){
        // ---------------------  error checking  ---------------------------
        if (vec1.length != vec2.length){
            throw new IllegalArgumentException(
                "array lengths need to match");
        }

        // ---------------------  perform operation  ------------------------
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            vec1[ind0] *= vec2[ind0];
        }
    }

    /**
     * Multiply scalar with vector (without creating a new vector)
     * @param vec1 Input vector and target of where the sum is returned
     * @param scalar Value to divide
     */
    public static void multiplyMe(double[] vec1, double scalar){
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            vec1[ind0] *= scalar;
        }
    }

    /**
     * Multiply all the elements of the vector
     * @param vec1 Input vector 1
     * @return Product of all the elements.
     */
    public static double product(final double[] vec1){
        double out = 1;
        for (int ind0 = 0; ind0 < vec1.length; ind0++){
            out *= vec1[ind0];
        }
        return out;
    }

    /**
     * Subtract vector 2 from vector 1
     * @param vec1 Input vector 1
     * @param vec2 Input vector 2
     * @return The output of the subtraction operation
     */
    public static double[] subtract(final double[] vec1, final double[] vec2){
        // ---------------------  error checking  ---------------------------
        if (vec1.length != vec2.length){
            throw new IllegalArgumentException(
                "subtract(): array lengths need to match");
        }

        // ---------------------  perform operation  ------------------------
        double[] output = new double[vec1.length];
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            output[ind0] = vec1[ind0] - vec2[ind0];
        }
        return output;
    }

    /**
     * Subtract a scalar from each element of the vector
     * @param vec1 Input vector
     * @param scalar Scalar value
     * @return The subtraaction vector
     */
    public static double[] subtract(final double[] vec1, double scalar){
        double[] output = new double[vec1.length];
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            output[ind0] = vec1[ind0] - scalar;
        }
        return output;
    }

    /**
     * Element-wise subtract and store result in vector 1
     * @param vec1 Vector 1
     * @param vec2 Vector 2
     */
    public static void subtractMe(double[] vec1, final double[] vec2){
        // ---------------------  error checking  ---------------------------
        if (vec1.length != vec2.length){
            throw new IllegalArgumentException(
                "array lengths need to match");
        }

        // ---------------------  perform operation  ------------------------
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            vec1[ind0] /= vec2[ind0];
        }
    }

    /**
     * Subtract scalar from vector (without creating a new vector)
     * @param vec1 Input vector and target of where the sum is returned
     * @param scalar Value to divide
     */
    public static void subtractMe(double[] vec1, double scalar){
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            vec1[ind0] -= scalar;
        }
    }
 
    /**
     * Sum up the elements of the vector
     * @param vec1 Input vector 1
     * @return Sum of all the elements.
     */
    public static double sum(final double[] vec1){
        return Arrays.stream(vec1).sum();        
    }

    // ==============================  Float  ===============================
    /**
     * Add two vectors together
     * @param vec1 Vector 1
     * @param vec2 Vector 2
     * @return Elementwisee sum of the two arrays
     */
    public static float[] add(final float[] vec1, final float[] vec2){
        // ---------------------  error checking  ---------------------------
        if (vec1.length != vec2.length){
            throw new IllegalArgumentException(
                "array lengths need to match");
        }

        // ---------------------  perform operation  ------------------------
        float[] output = new float[vec1.length];
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            output[ind0] = vec1[ind0] + vec2[ind0];
        }
        return output;
    }

    /**
     * Add a scalar to each element of the vector
     * @param vec1 Input vector
     * @param scalar Scalar value
     * @return The updated vector
     */
    public static float[] add(final float[] vec1, float scalar){
        float[] output = new float[vec1.length];
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            output[ind0] = vec1[ind0] + scalar;
        }
        return output;
    }

    /**
     * Add two vectors together and store result in vector 1
     * @param vec1 Vector 1
     * @param vec2 Vector 2
     */
    public static void addMe(float[] vec1, final float[] vec2){
        // ---------------------  error checking  ---------------------------
        if (vec1.length != vec2.length){
            throw new IllegalArgumentException(
                "array lengths need to match");
        }

        // ---------------------  perform operation  ------------------------
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            vec1[ind0] += vec2[ind0];
        }
    }

    /**
     * Add scalar to the vector (without creating a new vector)
     * @param vec1 Input vector and target of where the sum is returned
     * @param scalar Value to add
     */
    public static void addMe(float[] vec1, float scalar){
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            vec1[ind0] += scalar;
        }
    }

    /**
     * Divide elements of vec1 by the vec2
     * @param vec1 Input vector 1
     * @param vec2 Input vector 2
     * @return The quotient of the division operation
     */
    public static float[] divide(final float[] vec1, final float[] vec2){
        // ---------------------  error checking  ---------------------------
        if (vec1.length != vec2.length){
            throw new IllegalArgumentException(
                "divide(): array lengths need to match");
        }

        // ---------------------  perform operation  ------------------------
        float[] output = new float[vec1.length];
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            output[ind0] = vec1[ind0] / vec2[ind0];
        }
        return output;
    }

    /**
     * Divide a scalar to each element of the vector
     * @param vec1 Input vector
     * @param scalar Scalar value
     * @return The quotient vector
     */
    public static float[] divide(final float[] vec1, float scalar){
        float[] output = new float[vec1.length];
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            output[ind0] = vec1[ind0] / scalar;
        }
        return output;
    }

    /**
     * Element-wise divide and store result in vector 1
     * @param vec1 Vector 1
     * @param vec2 Vector 2
     */
    public static void divideMe(float[] vec1, final float[] vec2){
        // ---------------------  error checking  ---------------------------
        if (vec1.length != vec2.length){
            throw new IllegalArgumentException(
                "array lengths need to match");
        }

        // ---------------------  perform operation  ------------------------
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            vec1[ind0] /= vec2[ind0];
        }
    }

    /**
     * Divide scalar from vector (without creating a new vector)
     * @param vec1 Input vector and target of where the sum is returned
     * @param scalar Value to divide
     */
    public static void divideMe(float[] vec1, float scalar){
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            vec1[ind0] /= scalar;
        }
    }
    
    /**
     * Multiply the two vectors together
     * @param vec1 Input vector 1
     * @param vec2 Input vector 2
     * @return The element-wise product of the 2 vectors
     */
    public static float[] multiply(final float[] vec1, final float[] vec2){
        // ---------------------  error checking  ---------------------------
        if (vec1.length != vec2.length){
            throw new IllegalArgumentException(
                "multiply(): array lengths need to match");
        }

        // ---------------------  perform operation  ------------------------
        float[] output = new float[vec1.length];
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            output[ind0] = vec1[ind0] * vec2[ind0];
        }
        return output;
    }

    /**
     * Multiply a scalar to each element of the vector
     * @param vec1 Input vector
     * @param scalar Scalar value
     * @return The product vector
     */
    public static float[] multiply(final float[] vec1, float scalar){
        float[] output = new float[vec1.length];
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            output[ind0] = vec1[ind0] * scalar;
        }
        return output;
    }

    /**
     * Element-wise multiply and store result in vector 1
     * @param vec1 Vector 1
     * @param vec2 Vector 2
     */
    public static void multiplyMe(float[] vec1, final float[] vec2){
        // ---------------------  error checking  ---------------------------
        if (vec1.length != vec2.length){
            throw new IllegalArgumentException(
                "array lengths need to match");
        }

        // ---------------------  perform operation  ------------------------
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            vec1[ind0] *= vec2[ind0];
        }
    }

    /**
     * Multiply scalar with vector (without creating a new vector)
     * @param vec1 Input vector and target of where the sum is returned
     * @param scalar Value to divide
     */
    public static void multiplyMe(float[] vec1, float scalar){
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            vec1[ind0] *= scalar;
        }
    }

    /**
     * Multiply all the elements of the vector
     * @param vec1 Input vector 1
     * @return Product of all the elements.
     */
    public static float product(final float[] vec1){
        float out = 1;
        for (int ind0 = 0; ind0 < vec1.length; ind0++){
            out *= vec1[ind0];
        }
        return out;
    }

    /**
     * Subtract vector 2 from vector 1
     * @param vec1 Input vector 1
     * @param vec2 Input vector 2
     * @return The output of the subtraction operation
     */
    public static float[] subtract(final float[] vec1, final float[] vec2){
        // ---------------------  error checking  ---------------------------
        if (vec1.length != vec2.length){
            throw new IllegalArgumentException(
                "subtract(): array lengths need to match");
        }

        // ---------------------  perform operation  ------------------------
        float[] output = new float[vec1.length];
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            output[ind0] = vec1[ind0] - vec2[ind0];
        }
        return output;
    }

    /**
     * Subtract a scalar from each element of the vector
     * @param vec1 Input vector
     * @param scalar Scalar value
     * @return The subtraaction vector
     */
    public static float[] subtract(final float[] vec1, float scalar){
        float[] output = new float[vec1.length];
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            output[ind0] = vec1[ind0] - scalar;
        }
        return output;
    }

    /**
     * Element-wise subtract and store result in vector 1
     * @param vec1 Vector 1
     * @param vec2 Vector 2
     */
    public static void subtractMe(float[] vec1, final float[] vec2){
        // ---------------------  error checking  ---------------------------
        if (vec1.length != vec2.length){
            throw new IllegalArgumentException(
                "array lengths need to match");
        }

        // ---------------------  perform operation  ------------------------
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            vec1[ind0] /= vec2[ind0];
        }
    }

    /**
     * Subtract scalar from vector (without creating a new vector)
     * @param vec1 Input vector and target of where the sum is returned
     * @param scalar Value to divide
     */
    public static void subtractMe(float[] vec1, float scalar){
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            vec1[ind0] -= scalar;
        }
    }
 
    /**
     * Sum up the elements of the vector
     * @param vec1 Input vector 1
     * @return Sum of all the elements.
     */
    public static float sum(final float[] vec1){
        float out = 0;
        for (int ind0 = 0; ind0 < vec1.length; ind0++){
            out += vec1[ind0];
        }
        return out;       
    }
    //==============================  long support  =========================
    /**
     * Add two vectors together
     * @param vec1 Vector 1
     * @param vec2 Vector 2
     * @return Elementwisee sum of the two arrays
     */
    public static long[] add(final long[] vec1, final long[] vec2){
        // ---------------------  error checking  ---------------------------
        if (vec1.length != vec2.length){
            throw new IllegalArgumentException(
                "array lengths need to match");
        }

        // ---------------------  perform operation  ------------------------
        long[] output = new long[vec1.length];
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            output[ind0] = vec1[ind0] + vec2[ind0];
        }
        return output;
    }

    /**
     * Add a scalar to each element of the vector
     * @param vec1 Input vector
     * @param scalar Scalar value
     * @return The updated vector
     */
    public static long[] add(final long[] vec1, long scalar){
        long[] output = new long[vec1.length];
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            output[ind0] = vec1[ind0] + scalar;
        }
        return output;
    }

    /**
     * Add two vectors together and store result in vector 1
     * @param vec1 Vector 1
     * @param vec2 Vector 2
     */
    public static void addMe(long[] vec1, final long[] vec2){
        // ---------------------  error checking  ---------------------------
        if (vec1.length != vec2.length){
            throw new IllegalArgumentException(
                "array lengths need to match");
        }

        // ---------------------  perform operation  ------------------------
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            vec1[ind0] += vec2[ind0];
        }
    }

    /**
     * Add scalar to the vector (without creating a new vector)
     * @param vec1 Input vector and target of where the sum is returned
     * @param scalar Value to add
     */
    public static void addMe(long[] vec1, long scalar){
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            vec1[ind0] += scalar;
        }
    }

    /**
     * Divide elements of vec1 by the vec2
     * @param vec1 Input vector 1
     * @param vec2 Input vector 2
     * @return The quotient of the division operation
     */
    public static long[] divide(final long[] vec1, final long[] vec2){
        // ---------------------  error checking  ---------------------------
        if (vec1.length != vec2.length){
            throw new IllegalArgumentException(
                "divide(): array lengths need to match");
        }

        // ---------------------  perform operation  ------------------------
        long[] output = new long[vec1.length];
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            output[ind0] = vec1[ind0] / vec2[ind0];
        }
        return output;
    }

    /**
     * Divide a scalar to each element of the vector
     * @param vec1 Input vector
     * @param scalar Scalar value
     * @return The quotient vector
     */
    public static long[] divide(final long[] vec1, long scalar){
        long[] output = new long[vec1.length];
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            output[ind0] = vec1[ind0] / scalar;
        }
        return output;
    }

    /**
     * Element-wise divide and store result in vector 1
     * @param vec1 Vector 1
     * @param vec2 Vector 2
     */
    public static void divideMe(long[] vec1, final long[] vec2){
        // ---------------------  error checking  ---------------------------
        if (vec1.length != vec2.length){
            throw new IllegalArgumentException(
                "array lengths need to match");
        }

        // ---------------------  perform operation  ------------------------
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            vec1[ind0] /= vec2[ind0];
        }
    }

    /**
     * Divide scalar from vector (without creating a new vector)
     * @param vec1 Input vector and target of where the sum is returned
     * @param scalar Value to divide
     */
    public static void divideMe(long[] vec1, long scalar){
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            vec1[ind0] /= scalar;
        }
    }
    
    /**
     * Multiply the two vectors together
     * @param vec1 Input vector 1
     * @param vec2 Input vector 2
     * @return The element-wise product of the 2 vectors
     */
    public static long[] multiply(final long[] vec1, final long[] vec2){
        // ---------------------  error checking  ---------------------------
        if (vec1.length != vec2.length){
            throw new IllegalArgumentException(
                "multiply(): array lengths need to match");
        }

        // ---------------------  perform operation  ------------------------
        long[] output = new long[vec1.length];
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            output[ind0] = vec1[ind0] * vec2[ind0];
        }
        return output;
    }

    /**
     * Multiply a scalar to each element of the vector
     * @param vec1 Input vector
     * @param scalar Scalar value
     * @return The product vector
     */
    public static long[] multiply(final long[] vec1, long scalar){
        long[] output = new long[vec1.length];
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            output[ind0] = vec1[ind0] * scalar;
        }
        return output;
    }

    /**
     * Element-wise multiply and store result in vector 1
     * @param vec1 Vector 1
     * @param vec2 Vector 2
     */
    public static void multiplyMe(long[] vec1, final long[] vec2){
        // ---------------------  error checking  ---------------------------
        if (vec1.length != vec2.length){
            throw new IllegalArgumentException(
                "array lengths need to match");
        }

        // ---------------------  perform operation  ------------------------
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            vec1[ind0] *= vec2[ind0];
        }
    }

    /**
     * Multiply scalar with vector (without creating a new vector)
     * @param vec1 Input vector and target of where the sum is returned
     * @param scalar Value to divide
     */
    public static void multiplyMe(long[] vec1, long scalar){
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            vec1[ind0] *= scalar;
        }
    }

    /**
     * Multiply all the elements of the vector
     * @param vec1 Input vector 1
     * @return Product of all the elements.
     */
    public static long product(final long[] vec1){
        long out = 1;
        for (int ind0 = 0; ind0 < vec1.length; ind0++){
            out *= vec1[ind0];
        }
        return out;
    }

    /**
     * Subtract vector 2 from vector 1
     * @param vec1 Input vector 1
     * @param vec2 Input vector 2
     * @return The output of the subtraction operation
     */
    public static long[] subtract(final long[] vec1, final long[] vec2){
        // ---------------------  error checking  ---------------------------
        if (vec1.length != vec2.length){
            throw new IllegalArgumentException(
                "subtract(): array lengths need to match");
        }

        // ---------------------  perform operation  ------------------------
        long[] output = new long[vec1.length];
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            output[ind0] = vec1[ind0] - vec2[ind0];
        }
        return output;
    }

    /**
     * Subtract a scalar from each element of the vector
     * @param vec1 Input vector
     * @param scalar Scalar value
     * @return The subtraaction vector
     */
    public static long[] subtract(final long[] vec1, long scalar){
        long[] output = new long[vec1.length];
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            output[ind0] = vec1[ind0] - scalar;
        }
        return output;
    }

    /**
     * Element-wise subtract and store result in vector 1
     * @param vec1 Vector 1
     * @param vec2 Vector 2
     */
    public static void subtractMe(long[] vec1, final long[] vec2){
        // ---------------------  error checking  ---------------------------
        if (vec1.length != vec2.length){
            throw new IllegalArgumentException(
                "array lengths need to match");
        }

        // ---------------------  perform operation  ------------------------
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            vec1[ind0] /= vec2[ind0];
        }
    }

    /**
     * Subtract scalar from vector (without creating a new vector)
     * @param vec1 Input vector and target of where the sum is returned
     * @param scalar Value to divide
     */
    public static void subtractMe(long[] vec1, long scalar){
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            vec1[ind0] -= scalar;
        }
    }
 
    /**
     * Sum up the elements of the vector
     * @param vec1 Input vector 1
     * @return Sum of all the elements.
     */
    public static long sum(final long[] vec1){
        return Arrays.stream(vec1).sum();        
    }
    //============================ support int  =============================
    /**
     * Add two vectors together
     * @param vec1 Vector 1
     * @param vec2 Vector 2
     * @return Elementwisee sum of the two arrays
     */
    public static int[] add(final int[] vec1, final int[] vec2){
        // ---------------------  error checking  ---------------------------
        if (vec1.length != vec2.length){
            throw new IllegalArgumentException(
                "array lengths need to match");
        }

        // ---------------------  perform operation  ------------------------
        int[] output = new int[vec1.length];
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            output[ind0] = vec1[ind0] + vec2[ind0];
        }
        return output;
    }

    /**
     * Add a scalar to each element of the vector
     * @param vec1 Input vector
     * @param scalar Scalar value
     * @return The updated vector
     */
    public static int[] add(final int[] vec1, int scalar){
        int[] output = new int[vec1.length];
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            output[ind0] = vec1[ind0] + scalar;
        }
        return output;
    }

    /**
     * Add two vectors together and store result in vector 1
     * @param vec1 Vector 1
     * @param vec2 Vector 2
     */
    public static void addMe(int[] vec1, final int[] vec2){
        // ---------------------  error checking  ---------------------------
        if (vec1.length != vec2.length){
            throw new IllegalArgumentException(
                "array lengths need to match");
        }

        // ---------------------  perform operation  ------------------------
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            vec1[ind0] += vec2[ind0];
        }
    }

    /**
     * Add scalar to the vector (without creating a new vector)
     * @param vec1 Input vector and target of where the sum is returned
     * @param scalar Value to add
     */
    public static void addMe(int[] vec1, int scalar){
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            vec1[ind0] += scalar;
        }
    }

    /**
     * Divide elements of vec1 by the vec2
     * @param vec1 Input vector 1
     * @param vec2 Input vector 2
     * @return The quotient of the division operation
     */
    public static int[] divide(final int[] vec1, final int[] vec2){
        // ---------------------  error checking  ---------------------------
        if (vec1.length != vec2.length){
            throw new IllegalArgumentException(
                "divide(): array lengths need to match");
        }

        // ---------------------  perform operation  ------------------------
        int[] output = new int[vec1.length];
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            output[ind0] = vec1[ind0] / vec2[ind0];
        }
        return output;
    }

    /**
     * Divide a scalar to each element of the vector
     * @param vec1 Input vector
     * @param scalar Scalar value
     * @return The quotient vector
     */
    public static int[] divide(final int[] vec1, int scalar){
        int[] output = new int[vec1.length];
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            output[ind0] = vec1[ind0] / scalar;
        }
        return output;
    }

    /**
     * Element-wise divide and store result in vector 1
     * @param vec1 Vector 1
     * @param vec2 Vector 2
     */
    public static void divideMe(int[] vec1, final int[] vec2){
        // ---------------------  error checking  ---------------------------
        if (vec1.length != vec2.length){
            throw new IllegalArgumentException(
                "array lengths need to match");
        }

        // ---------------------  perform operation  ------------------------
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            vec1[ind0] /= vec2[ind0];
        }
    }

    /**
     * Divide scalar from vector (without creating a new vector)
     * @param vec1 Input vector and target of where the sum is returned
     * @param scalar Value to divide
     */
    public static void divideMe(int[] vec1, int scalar){
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            vec1[ind0] /= scalar;
        }
    }
    
    /**
     * Multiply the two vectors together
     * @param vec1 Input vector 1
     * @param vec2 Input vector 2
     * @return The element-wise product of the 2 vectors
     */
    public static int[] multiply(final int[] vec1, final int[] vec2){
        // ---------------------  error checking  ---------------------------
        if (vec1.length != vec2.length){
            throw new IllegalArgumentException(
                "multiply(): array lengths need to match");
        }

        // ---------------------  perform operation  ------------------------
        int[] output = new int[vec1.length];
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            output[ind0] = vec1[ind0] * vec2[ind0];
        }
        return output;
    }

    /**
     * Multiply a scalar to each element of the vector
     * @param vec1 Input vector
     * @param scalar Scalar value
     * @return The product vector
     */
    public static int[] multiply(final int[] vec1, int scalar){
        int[] output = new int[vec1.length];
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            output[ind0] = vec1[ind0] * scalar;
        }
        return output;
    }

    /**
     * Element-wise multiply and store result in vector 1
     * @param vec1 Vector 1
     * @param vec2 Vector 2
     */
    public static void multiplyMe(int[] vec1, final int[] vec2){
        // ---------------------  error checking  ---------------------------
        if (vec1.length != vec2.length){
            throw new IllegalArgumentException(
                "array lengths need to match");
        }

        // ---------------------  perform operation  ------------------------
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            vec1[ind0] *= vec2[ind0];
        }
    }

    /**
     * Multiply scalar with vector (without creating a new vector)
     * @param vec1 Input vector and target of where the sum is returned
     * @param scalar Value to divide
     */
    public static void multiplyMe(int[] vec1, int scalar){
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            vec1[ind0] *= scalar;
        }
    }

    /**
     * Multiply all the elements of the vector
     * @param vec1 Input vector 1
     * @return Product of all the elements.
     */
    public static int product(final int[] vec1){
        int out = 1;
        for (int ind0 = 0; ind0 < vec1.length; ind0++){
            out *= vec1[ind0];
        }
        return out;
    }

    /**
     * Subtract vector 2 from vector 1
     * @param vec1 Input vector 1
     * @param vec2 Input vector 2
     * @return The output of the subtraction operation
     */
    public static int[] subtract(final int[] vec1, final int[] vec2){
        // ---------------------  error checking  ---------------------------
        if (vec1.length != vec2.length){
            throw new IllegalArgumentException(
                "subtract(): array lengths need to match");
        }

        // ---------------------  perform operation  ------------------------
        int[] output = new int[vec1.length];
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            output[ind0] = vec1[ind0] - vec2[ind0];
        }
        return output;
    }

    /**
     * Subtract a scalar from each element of the vector
     * @param vec1 Input vector
     * @param scalar Scalar value
     * @return The subtraaction vector
     */
    public static int[] subtract(final int[] vec1, int scalar){
        int[] output = new int[vec1.length];
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            output[ind0] = vec1[ind0] - scalar;
        }
        return output;
    }

    /**
     * Element-wise subtract and store result in vector 1
     * @param vec1 Vector 1
     * @param vec2 Vector 2
     */
    public static void subtractMe(int[] vec1, final int[] vec2){
        // ---------------------  error checking  ---------------------------
        if (vec1.length != vec2.length){
            throw new IllegalArgumentException(
                "array lengths need to match");
        }

        // ---------------------  perform operation  ------------------------
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            vec1[ind0] /= vec2[ind0];
        }
    }

    /**
     * Subtract scalar from vector (without creating a new vector)
     * @param vec1 Input vector and target of where the sum is returned
     * @param scalar Value to divide
     */
    public static void subtractMe(int[] vec1, int scalar){
        for (int ind0 = 0; ind0 < vec1.length; ind0 ++){
            vec1[ind0] -= scalar;
        }
    }
 
    /**
     * Sum up the elements of the vector
     * @param vec1 Input vector 1
     * @return Sum of all the elements.
     */
    public static int sum(final int[] vec1){
        return Arrays.stream(vec1).sum();        
    }
}