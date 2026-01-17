/**
 * This function contains a number of commonly used functions applied
 * to a vector of numbers.
 *
 * @author GassiusODude
 * @since Jan 2026
 */
package net.kcundercover.jdsp.math;
import jdk.incubator.vector.DoubleVector;
import jdk.incubator.vector.FloatVector;
import jdk.incubator.vector.IntVector;
import jdk.incubator.vector.LongVector;
import jdk.incubator.vector.ShortVector;
import jdk.incubator.vector.VectorSpecies;
import jdk.incubator.vector.VectorOperators;
import java.util.Arrays;


public class Vector {
    private static final VectorSpecies<Double> SPECIES_DOUBLE = DoubleVector.SPECIES_PREFERRED;
    private static final VectorSpecies<Float> SPECIES_FLOAT = FloatVector.SPECIES_PREFERRED;
    private static final VectorSpecies<Integer> SPECIES_INT = IntVector.SPECIES_PREFERRED;
    private static final VectorSpecies<Long> SPECIES_LONG = LongVector.SPECIES_PREFERRED;
    private static final VectorSpecies<Short> SPECIES_SHORT = ShortVector.SPECIES_PREFERRED;

    // ===========================  double support  =========================
    /**Add two vectors together
     * @param vec1 Vector 1
     * @param vec2 Vector 2
     * @return Elementwisee sum of the two arrays
     */
    public static double[] add(final double[] vec1, final double[] vec2) {
        // ---------------------  error checking  ---------------------------
        if (vec1.length != vec2.length){
            throw new IllegalArgumentException(
                "array lengths need to match");
        }

        // initialize variables
        double[] output = new double[vec1.length];
        int index = 0;

        // ---------------------  perform operation  ------------------------
        for (; index < SPECIES_DOUBLE.loopBound(vec1.length); index += SPECIES_DOUBLE.length()) {
            // Load vectors from arrays
            DoubleVector va = DoubleVector.fromArray(SPECIES_DOUBLE, vec1, index);
            DoubleVector vb = DoubleVector.fromArray(SPECIES_DOUBLE, vec2, index);

            // Vector addition
            DoubleVector vc = va.add(vb);

            // Store back into array
            vc.intoArray(output, index);
        }

        // complete
        for (; index < vec1.length; index++) {
            output[index] = vec1[index] + vec2[index];
        }

        return output;
    }

    /**Add a scalar to each element of the vector
     * @param vec1 Input vector
     * @param scalar Scalar value
     * @return The updated vector
     */
    public static double[] add(final double[] vec1, double scalar) {
        int ind0 = 0;
        double[] output = new double[vec1.length];
        
        // Process in chunks of vector length
        for (; ind0 + SPECIES_DOUBLE.length() <= vec1.length; ind0 += SPECIES_DOUBLE.length()) {
            DoubleVector v = DoubleVector.fromArray(SPECIES_DOUBLE, vec1, ind0);
            DoubleVector added = v.add(scalar); // add scalar to vector
            added.intoArray(output, ind0);
        }

        // Handle remainder
        for (; ind0 < vec1.length; ind0 ++){
            output[ind0] = vec1[ind0] + scalar;
        }
        return output;
    }

    /**Add two vectors together and store result in vector 1
     * @param vec1 Vector 1
     * @param vec2 Vector 2
     */
    public static void addMe(double[] vec1, final double[] vec2) {
        // ---------------------  error checking  ---------------------------
        if (vec1.length != vec2.length){
            throw new IllegalArgumentException(
                "array lengths need to match");
        }

        // ---------------------  perform operation  ------------------------
        int ind0 = 0;
        for (; ind0 + SPECIES_DOUBLE.length() <= vec1.length; ind0 += SPECIES_DOUBLE.length()) {
            DoubleVector v1 = DoubleVector.fromArray(SPECIES_DOUBLE, vec1, ind0);
            DoubleVector v2 = DoubleVector.fromArray(SPECIES_DOUBLE, vec2, ind0);
            
            // in-place: write result directly into vec1
            (v1.add(v2)).intoArray(vec1, ind0);
        }

        // handle tail
        for (; ind0 < vec1.length; ind0++) {
            vec1[ind0] += vec2[ind0];
        }
    }

    /**Add scalar to the vector (without creating a new vector)
     * @param vec1 Input vector and target of where the sum is returned
     * @param scalar Value to add
     */
    public static void addMe(double[] vec1, double scalar) {
        int ind0 = 0;

        // Process in chunks of vector length
        for (; ind0 + SPECIES_DOUBLE.length() <= vec1.length; ind0 += SPECIES_DOUBLE.length()) {
            DoubleVector v = DoubleVector.fromArray(SPECIES_DOUBLE, vec1, ind0);
            DoubleVector added = v.add(scalar); // add scalar to vector
            added.intoArray(vec1, ind0);
        }

        // Handle remainder
        for (; ind0 < vec1.length; ind0++) {
            vec1[ind0] = vec1[ind0] + scalar;
        }

    }

    /**Divide elements of vec1 by the vec2
     * @param vec1 Input vector 1
     * @param vec2 Input vector 2
     * @return The quotient of the division operation
     */
    public static double[] divide(final double[] vec1, final double[] vec2) {
        if (vec1.length != vec2.length) {
            throw new IllegalArgumentException("divide(): array lengths need to match");
        }

        double[] output = new double[vec1.length];
        int ind0 = 0;

        // Vectorized loop
        for (; ind0 + SPECIES_DOUBLE.length() <= vec1.length; ind0 += SPECIES_DOUBLE.length()) {
            DoubleVector v1 = DoubleVector.fromArray(SPECIES_DOUBLE, vec1, ind0);
            DoubleVector v2 = DoubleVector.fromArray(SPECIES_DOUBLE, vec2, ind0);
            (v1.div(v2)).intoArray(output, ind0); // elementwise division
        }

        // Handle remaining elements (tail)
        for (; ind0 < vec1.length; ind0++) {
            output[ind0] = vec1[ind0] / vec2[ind0];
        }

        return output;
    }

    /**Divide a scalar to each element of the vector
     * @param vec1 Input vector
     * @param scalar Scalar value
     * @return The quotient vector
     */
    public static double[] divide(final double[] vec1, double scalar) {
        double[] output = new double[vec1.length];
        int ind0 = 0;

        // Broadcast scalar into a vector
        DoubleVector scalarVector = DoubleVector.broadcast(SPECIES_DOUBLE, scalar);

        // Vectorized loop
        for (; ind0 + SPECIES_DOUBLE.length() <= vec1.length; ind0 += SPECIES_DOUBLE.length()) {
            DoubleVector v = DoubleVector.fromArray(SPECIES_DOUBLE, vec1, ind0);
            (v.div(scalarVector)).intoArray(output, ind0);
        }

        // Handle remaining elements (tail)
        for (; ind0 < vec1.length; ind0++) {
            output[ind0] = vec1[ind0] / scalar;
        }

        return output;
    }

    /**Element-wise divide and store result in vector 1
     * @param vec1 Vector 1
     * @param vec2 Vector 2
     */
    public static void divideMe(double[] vec1, final double[] vec2) {
        if (vec1.length != vec2.length) {
            throw new IllegalArgumentException("divideMe(): array lengths need to match");
        }

        int ind0 = 0;

        // Vectorized loop
        for (; ind0 + SPECIES_DOUBLE.length() <= vec1.length; ind0 += SPECIES_DOUBLE.length()) {
            DoubleVector v1 = DoubleVector.fromArray(SPECIES_DOUBLE, vec1, ind0);
            DoubleVector v2 = DoubleVector.fromArray(SPECIES_DOUBLE, vec2, ind0);
            // Perform division and write directly back into vec1
            (v1.div(v2)).intoArray(vec1, ind0);
        }

        // Handle tail elements
        for (; ind0 < vec1.length; ind0++) {
            vec1[ind0] /= vec2[ind0];
        }
    }

    /**Divide scalar from vector (without creating a new vector)
     * @param vec1 Input vector (dividend) and output (quotient)
     * @param scalar Divisor
     */
     public static void divideMe(double[] vec1, double scalar) {
        int ind0 = 0;
        // Broadcast scalar into a vector
        DoubleVector scalarVector = DoubleVector.broadcast(SPECIES_DOUBLE, scalar);

        // Vectorized loop
        for (; ind0 + SPECIES_DOUBLE.length() <= vec1.length; ind0 += SPECIES_DOUBLE.length()) {
            DoubleVector v = DoubleVector.fromArray(SPECIES_DOUBLE, vec1, ind0);
            (v.div(scalarVector)).intoArray(vec1, ind0); // write back to vec1
        }

        // Tail loop for remaining elements
        for (; ind0 < vec1.length; ind0++) {
            vec1[ind0] /= scalar;
        }
    }

    /**Return the minimum and maximum of the provided vector
     * @param vec1 Input vector
     * @return The [min, max]
     */
    public static double[] getMinMax(double[] vec1) {
        double[] out = new double[2];
        out[0] = Double.MAX_VALUE; // min
        out[1] = -Double.MAX_VALUE; // max

        int ind0 = 0;
        int length = vec1.length;

        // Initialize vectors for min and max
        DoubleVector vMin = DoubleVector.broadcast(SPECIES_DOUBLE, Double.MAX_VALUE);
        DoubleVector vMax = DoubleVector.broadcast(SPECIES_DOUBLE, -Double.MAX_VALUE);

        // Vectorized loop
        for (; ind0 + SPECIES_DOUBLE.length() <= length; ind0 += SPECIES_DOUBLE.length()) {
            DoubleVector v = DoubleVector.fromArray(SPECIES_DOUBLE, vec1, ind0);
            vMin = vMin.min(v);
            vMax = vMax.max(v);
        }

        // Reduce the vector lanes to scalar min/max
        for (int lane = 0; lane < SPECIES_DOUBLE.length(); lane++) {
            out[0] = Math.min(out[0], vMin.lane(lane));
            out[1] = Math.max(out[1], vMax.lane(lane));
        }

        // Tail loop for remaining elements
        for (; ind0 < length; ind0++) {
            out[0] = Math.min(out[0], vec1[ind0]);
            out[1] = Math.max(out[1], vec1[ind0]);
        }

        return out;
    }

    public static double[] multiply(final double[] vec1, final double[] vec2) {
        if (vec1.length != vec2.length) {
            throw new IllegalArgumentException("multiply(): array lengths need to match");
        }

        double[] output = new double[vec1.length];
        int ind0 = 0;

        // Vectorized loop
        for (; ind0 + SPECIES_DOUBLE.length() <= vec1.length; ind0 += SPECIES_DOUBLE.length()) {
            DoubleVector v1 = DoubleVector.fromArray(SPECIES_DOUBLE, vec1, ind0);
            DoubleVector v2 = DoubleVector.fromArray(SPECIES_DOUBLE, vec2, ind0);
            (v1.mul(v2)).intoArray(output, ind0);
        }

        // Tail loop
        for (; ind0 < vec1.length; ind0++) {
            output[ind0] = vec1[ind0] * vec2[ind0];
        }

        return output;
    }

    // --------------------- Multiply by scalar --------------------
    public static double[] multiply(final double[] vec1, double scalar) {
        double[] output = new double[vec1.length];
        int ind0 = 0;

        DoubleVector scalarVector = DoubleVector.broadcast(SPECIES_DOUBLE, scalar);

        // Vectorized loop
        for (; ind0 + SPECIES_DOUBLE.length() <= vec1.length; ind0 += SPECIES_DOUBLE.length()) {
            DoubleVector v = DoubleVector.fromArray(SPECIES_DOUBLE, vec1, ind0);
            (v.mul(scalarVector)).intoArray(output, ind0);
        }

        // Tail loop
        for (; ind0 < vec1.length; ind0++) {
            output[ind0] = vec1[ind0] * scalar;
        }

        return output;
    }

    /**Element-wise multiply and store result in vector 1
     * @param vec1 Vector 1
     * @param vec2 Vector 2
     */
    public static void multiplyMe(double[] vec1, final double[] vec2) {
        if (vec1.length != vec2.length) {
            throw new IllegalArgumentException("multiplyMe(): array lengths need to match");
        }

        int ind0 = 0;

        // Vectorized loop
        for (; ind0 + SPECIES_DOUBLE.length() <= vec1.length; ind0 += SPECIES_DOUBLE.length()) {
            DoubleVector v1 = DoubleVector.fromArray(SPECIES_DOUBLE, vec1, ind0);
            DoubleVector v2 = DoubleVector.fromArray(SPECIES_DOUBLE, vec2, ind0);
            (v1.mul(v2)).intoArray(vec1, ind0); // write result back to vec1
        }

        // Tail loop for remaining elements
        for (; ind0 < vec1.length; ind0++) {
            vec1[ind0] *= vec2[ind0];
        }
    }

    // --------------------- In-place multiply by scalar --------------------
    public static void multiplyMe(double[] vec1, double scalar) {
        int ind0 = 0;
        DoubleVector scalarVector = DoubleVector.broadcast(SPECIES_DOUBLE, scalar);

        // Vectorized loop
        for (; ind0 + SPECIES_DOUBLE.length() <= vec1.length; ind0 += SPECIES_DOUBLE.length()) {
            DoubleVector v = DoubleVector.fromArray(SPECIES_DOUBLE, vec1, ind0);
            (v.mul(scalarVector)).intoArray(vec1, ind0); // in-place
        }

        // Tail loop
        for (; ind0 < vec1.length; ind0++) {
            vec1[ind0] *= scalar;
        }
    }

    /**Multiply all the elements of the vector
     * @param vec1 Input vector 1
     * @return Product of all the elements.
     */
    public static double product(final double[] vec1) {
        int ind0 = 0;
        double result = 1.0;

        // Initialize vector accumulator with 1s
        DoubleVector acc = DoubleVector.broadcast(SPECIES_DOUBLE, 1.0);

        // Vectorized loop
        for (; ind0 + SPECIES_DOUBLE.length() <= vec1.length; ind0 += SPECIES_DOUBLE.length()) {
            DoubleVector v = DoubleVector.fromArray(SPECIES_DOUBLE, vec1, ind0);
            acc = acc.mul(v); // elementwise multiply
        }

        // Reduce vector lanes to scalar
        for (int lane = 0; lane < SPECIES_DOUBLE.length(); lane++) {
            result *= acc.lane(lane);
        }

        // Tail loop for remaining elements
        for (; ind0 < vec1.length; ind0++) {
            result *= vec1[ind0];
        }

        return result;
    }

    /**Subtract vector 2 from vector 1
     * @param vec1 Input vector 1
     * @param vec2 Input vector 2
     * @return The output of the subtraction operation
     */
    public static double[] subtract(final double[] vec1, final double[] vec2) {
        if (vec1.length != vec2.length) {
            throw new IllegalArgumentException("subtract(): array lengths need to match");
        }

        double[] output = new double[vec1.length];
        int ind0 = 0;

        // Vectorized loop
        for (; ind0 + SPECIES_DOUBLE.length() <= vec1.length; ind0 += SPECIES_DOUBLE.length()) {
            DoubleVector v1 = DoubleVector.fromArray(SPECIES_DOUBLE, vec1, ind0);
            DoubleVector v2 = DoubleVector.fromArray(SPECIES_DOUBLE, vec2, ind0);
            (v1.sub(v2)).intoArray(output, ind0);
        }

        // Tail loop
        for (; ind0 < vec1.length; ind0++) {
            output[ind0] = vec1[ind0] - vec2[ind0];
        }

        return output;
    }

    // --------------------- Subtract scalar from vector --------------------
    public static double[] subtract(final double[] vec1, double scalar) {
        double[] output = new double[vec1.length];
        int ind0 = 0;

        DoubleVector scalarVector = DoubleVector.broadcast(SPECIES_DOUBLE, scalar);

        // Vectorized loop
        for (; ind0 + SPECIES_DOUBLE.length() <= vec1.length; ind0 += SPECIES_DOUBLE.length()) {
            DoubleVector v = DoubleVector.fromArray(SPECIES_DOUBLE, vec1, ind0);
            (v.sub(scalarVector)).intoArray(output, ind0);
        }

        // Tail loop
        for (; ind0 < vec1.length; ind0++) {
            output[ind0] = vec1[ind0] - scalar;
        }

        return output;
    }

    /**Element-wise subtract and store result in vector 1
     * @param vec1 Vector 1
     * @param vec2 Vector 2
     */
    public static void subtractMe(double[] vec1, final double[] vec2) {
        if (vec1.length != vec2.length) {
            throw new IllegalArgumentException("subtractMe(): array lengths need to match");
        }

        int ind0 = 0;

        // Vectorized loop
        for (; ind0 + SPECIES_DOUBLE.length() <= vec1.length; ind0 += SPECIES_DOUBLE.length()) {
            DoubleVector v1 = DoubleVector.fromArray(SPECIES_DOUBLE, vec1, ind0);
            DoubleVector v2 = DoubleVector.fromArray(SPECIES_DOUBLE, vec2, ind0);
            (v1.sub(v2)).intoArray(vec1, ind0); // in-place subtraction
        }

        // Tail loop
        for (; ind0 < vec1.length; ind0++) {
            vec1[ind0] -= vec2[ind0];
        }
    }

    // --------------------- In-place subtract scalar --------------------
    public static void subtractMe(double[] vec1, double scalar) {
        int ind0 = 0;
        DoubleVector scalarVector = DoubleVector.broadcast(SPECIES_DOUBLE, scalar);

        // Vectorized loop
        for (; ind0 + SPECIES_DOUBLE.length() <= vec1.length; ind0 += SPECIES_DOUBLE.length()) {
            DoubleVector v = DoubleVector.fromArray(SPECIES_DOUBLE, vec1, ind0);
            (v.sub(scalarVector)).intoArray(vec1, ind0); // in-place subtraction
        }

        // Tail loop
        for (; ind0 < vec1.length; ind0++) {
            vec1[ind0] -= scalar;
        }
    }

    // --------------------- Sum of elements --------------------
    public static double sum(final double[] vec1) {
        int ind0 = 0;
        DoubleVector acc = DoubleVector.broadcast(SPECIES_DOUBLE, 0.0);

        // Vectorized accumulation
        for (; ind0 + SPECIES_DOUBLE.length() <= vec1.length; ind0 += SPECIES_DOUBLE.length()) {
            DoubleVector v = DoubleVector.fromArray(SPECIES_DOUBLE, vec1, ind0);
            acc = acc.add(v);
        }

        // Reduce vector lanes to scalar
        double result = 0.0;
        for (int lane = 0; lane < SPECIES_DOUBLE.length(); lane++) {
            result += acc.lane(lane);
        }

        // Tail elements
        for (; ind0 < vec1.length; ind0++) {
            result += vec1[ind0];
        }

        return result;
    }

    public static double dot(final double[] vec1, final double[] vec2) {
        if (vec1.length != vec2.length) {
            throw new IllegalArgumentException("dot(): array lengths need to match");
        }

        int ind0 = 0;
        DoubleVector acc = DoubleVector.zero(SPECIES_DOUBLE);

        int upper = SPECIES_DOUBLE.loopBound(vec1.length);

        // ---------------- SIMD loop ----------------
        for (; ind0 < upper; ind0 += SPECIES_DOUBLE.length()) {
            DoubleVector v1 = DoubleVector.fromArray(SPECIES_DOUBLE, vec1, ind0);
            DoubleVector v2 = DoubleVector.fromArray(SPECIES_DOUBLE, vec2, ind0);
            acc = acc.add(v1.mul(v2));
        }

        // ---------------- reduction ----------------
        double sum = acc.reduceLanes(VectorOperators.ADD);

        // ---------------- tail ---------------------
        for (; ind0 < vec1.length; ind0++) {
            sum += vec1[ind0] * vec2[ind0];
        }

        return sum;
    }


    public static double norm1(final double[] vec1) {
        double acc = 0.0;
        for (int ind0 = 0; ind0 < vec1.length; ind0++) {
            acc += Math.abs(vec1[ind0]);
        }
        return acc;
    }

    public static double norm2(final double[] vec1) {
        double acc = 0.0;
        for (int ind0 = 0; ind0 < vec1.length; ind0++) {
            acc += vec1[ind0] * vec1[ind0];
        }
        return Math.sqrt(acc);
    }

    public static void clampMe(double[] vec1, double min, double max) {
        for (int ind0 = 0; ind0 < vec1.length; ind0++) {
            vec1[ind0] = Math.max(min, Math.min(max, vec1[ind0]));
        }
    }



    // ==============================  Float  ===============================

    public static float[] add(final float[] vec1, final float[] vec2) {
        if (vec1.length != vec2.length) {
            throw new IllegalArgumentException("add(): array lengths need to match");
        }
        float[] output = new float[vec1.length];
        int ind0 = 0;

        for (; ind0 + SPECIES_FLOAT.length() <= vec1.length; ind0 += SPECIES_FLOAT.length()) {
            FloatVector v1 = FloatVector.fromArray(SPECIES_FLOAT, vec1, ind0);
            FloatVector v2 = FloatVector.fromArray(SPECIES_FLOAT, vec2, ind0);
            (v1.add(v2)).intoArray(output, ind0);
        }

        for (; ind0 < vec1.length; ind0++) {
            output[ind0] = vec1[ind0] + vec2[ind0];
        }
        return output;
    }

    // --------------------- Add scalar --------------------
    public static float[] add(final float[] vec1, float scalar) {
        float[] output = new float[vec1.length];
        int ind0 = 0;
        FloatVector scalarVector = FloatVector.broadcast(SPECIES_FLOAT, scalar);

        for (; ind0 + SPECIES_FLOAT.length() <= vec1.length; ind0 += SPECIES_FLOAT.length()) {
            FloatVector v = FloatVector.fromArray(SPECIES_FLOAT, vec1, ind0);
            (v.add(scalarVector)).intoArray(output, ind0);
        }

        for (; ind0 < vec1.length; ind0++) {
            output[ind0] = vec1[ind0] + scalar;
        }
        return output;
    }

    // --------------------- In-place addition --------------------
    public static void addMe(float[] vec1, final float[] vec2) {
        if (vec1.length != vec2.length) {
            throw new IllegalArgumentException("addMe(): array lengths need to match");
        }
        int ind0 = 0;

        for (; ind0 + SPECIES_FLOAT.length() <= vec1.length; ind0 += SPECIES_FLOAT.length()) {
            FloatVector v1 = FloatVector.fromArray(SPECIES_FLOAT, vec1, ind0);
            FloatVector v2 = FloatVector.fromArray(SPECIES_FLOAT, vec2, ind0);
            (v1.add(v2)).intoArray(vec1, ind0);
        }

        for (; ind0 < vec1.length; ind0++) {
            vec1[ind0] += vec2[ind0];
        }
    }

    public static void addMe(float[] vec1, float scalar) {
        int ind0 = 0;
        FloatVector scalarVector = FloatVector.broadcast(SPECIES_FLOAT, scalar);

        for (; ind0 + SPECIES_FLOAT.length() <= vec1.length; ind0 += SPECIES_FLOAT.length()) {
            FloatVector v = FloatVector.fromArray(SPECIES_FLOAT, vec1, ind0);
            (v.add(scalarVector)).intoArray(vec1, ind0);
        }

        for (; ind0 < vec1.length; ind0++) {
            vec1[ind0] += scalar;
        }
    }

    // --------------------- Subtraction --------------------
    public static float[] subtract(final float[] vec1, final float[] vec2) {
        if (vec1.length != vec2.length) {
            throw new IllegalArgumentException("subtract(): array lengths need to match");
        }
        float[] output = new float[vec1.length];
        int ind0 = 0;

        for (; ind0 + SPECIES_FLOAT.length() <= vec1.length; ind0 += SPECIES_FLOAT.length()) {
            FloatVector v1 = FloatVector.fromArray(SPECIES_FLOAT, vec1, ind0);
            FloatVector v2 = FloatVector.fromArray(SPECIES_FLOAT, vec2, ind0);
            (v1.sub(v2)).intoArray(output, ind0);
        }

        for (; ind0 < vec1.length; ind0++) {
            output[ind0] = vec1[ind0] - vec2[ind0];
        }
        return output;
    }

    public static float[] subtract(final float[] vec1, float scalar) {
        float[] output = new float[vec1.length];
        int ind0 = 0;
        FloatVector scalarVector = FloatVector.broadcast(SPECIES_FLOAT, scalar);

        for (; ind0 + SPECIES_FLOAT.length() <= vec1.length; ind0 += SPECIES_FLOAT.length()) {
            FloatVector v = FloatVector.fromArray(SPECIES_FLOAT, vec1, ind0);
            (v.sub(scalarVector)).intoArray(output, ind0);
        }

        for (; ind0 < vec1.length; ind0++) {
            output[ind0] = vec1[ind0] - scalar;
        }
        return output;
    }

    public static void subtractMe(float[] vec1, final float[] vec2) {
        if (vec1.length != vec2.length) {
            throw new IllegalArgumentException("subtractMe(): array lengths need to match");
        }
        int ind0 = 0;

        for (; ind0 + SPECIES_FLOAT.length() <= vec1.length; ind0 += SPECIES_FLOAT.length()) {
            FloatVector v1 = FloatVector.fromArray(SPECIES_FLOAT, vec1, ind0);
            FloatVector v2 = FloatVector.fromArray(SPECIES_FLOAT, vec2, ind0);
            (v1.sub(v2)).intoArray(vec1, ind0);
        }

        for (; ind0 < vec1.length; ind0++) {
            vec1[ind0] -= vec2[ind0];
        }
    }

    public static void subtractMe(float[] vec1, float scalar) {
        int ind0 = 0;
        FloatVector scalarVector = FloatVector.broadcast(SPECIES_FLOAT, scalar);

        for (; ind0 + SPECIES_FLOAT.length() <= vec1.length; ind0 += SPECIES_FLOAT.length()) {
            FloatVector v = FloatVector.fromArray(SPECIES_FLOAT, vec1, ind0);
            (v.sub(scalarVector)).intoArray(vec1, ind0);
        }

        for (; ind0 < vec1.length; ind0++) {
            vec1[ind0] -= scalar;
        }
    }

    // --------------------- Multiply --------------------
    public static float[] multiply(final float[] vec1, final float[] vec2) {
        if (vec1.length != vec2.length) {
            throw new IllegalArgumentException("multiply(): array lengths need to match");
        }
        float[] output = new float[vec1.length];
        int ind0 = 0;

        for (; ind0 + SPECIES_FLOAT.length() <= vec1.length; ind0 += SPECIES_FLOAT.length()) {
            FloatVector v1 = FloatVector.fromArray(SPECIES_FLOAT, vec1, ind0);
            FloatVector v2 = FloatVector.fromArray(SPECIES_FLOAT, vec2, ind0);
            (v1.mul(v2)).intoArray(output, ind0);
        }

        for (; ind0 < vec1.length; ind0++) {
            output[ind0] = vec1[ind0] * vec2[ind0];
        }
        return output;
    }

    public static float[] multiply(final float[] vec1, float scalar) {
        float[] output = new float[vec1.length];
        int ind0 = 0;
        FloatVector scalarVector = FloatVector.broadcast(SPECIES_FLOAT, scalar);

        for (; ind0 + SPECIES_FLOAT.length() <= vec1.length; ind0 += SPECIES_FLOAT.length()) {
            FloatVector v = FloatVector.fromArray(SPECIES_FLOAT, vec1, ind0);
            (v.mul(scalarVector)).intoArray(output, ind0);
        }

        for (; ind0 < vec1.length; ind0++) {
            output[ind0] = vec1[ind0] * scalar;
        }
        return output;
    }

    public static void multiplyMe(float[] vec1, final float[] vec2) {
        if (vec1.length != vec2.length) {
            throw new IllegalArgumentException("multiplyMe(): array lengths need to match");
        }
        int ind0 = 0;

        for (; ind0 + SPECIES_FLOAT.length() <= vec1.length; ind0 += SPECIES_FLOAT.length()) {
            FloatVector v1 = FloatVector.fromArray(SPECIES_FLOAT, vec1, ind0);
            FloatVector v2 = FloatVector.fromArray(SPECIES_FLOAT, vec2, ind0);
            (v1.mul(v2)).intoArray(vec1, ind0);
        }

        for (; ind0 < vec1.length; ind0++) {
            vec1[ind0] *= vec2[ind0];
        }
    }

    public static void multiplyMe(float[] vec1, float scalar) {
        int ind0 = 0;
        FloatVector scalarVector = FloatVector.broadcast(SPECIES_FLOAT, scalar);

        for (; ind0 + SPECIES_FLOAT.length() <= vec1.length; ind0 += SPECIES_FLOAT.length()) {
            FloatVector v = FloatVector.fromArray(SPECIES_FLOAT, vec1, ind0);
            (v.mul(scalarVector)).intoArray(vec1, ind0);
        }

        for (; ind0 < vec1.length; ind0++) {
            vec1[ind0] *= scalar;
        }
    }

    // --------------------- Divide --------------------
    public static float[] divide(final float[] vec1, final float[] vec2) {
        if (vec1.length != vec2.length) {
            throw new IllegalArgumentException("divide(): array lengths need to match");
        }
        float[] output = new float[vec1.length];
        int ind0 = 0;

        for (; ind0 + SPECIES_FLOAT.length() <= vec1.length; ind0 += SPECIES_FLOAT.length()) {
            FloatVector v1 = FloatVector.fromArray(SPECIES_FLOAT, vec1, ind0);
            FloatVector v2 = FloatVector.fromArray(SPECIES_FLOAT, vec2, ind0);
            (v1.div(v2)).intoArray(output, ind0);
        }

        for (; ind0 < vec1.length; ind0++) {
            output[ind0] = vec1[ind0] / vec2[ind0];
        }
        return output;
    }

    public static float[] divide(final float[] vec1, float scalar) {
        float[] output = new float[vec1.length];
        int ind0 = 0;
        FloatVector scalarVector = FloatVector.broadcast(SPECIES_FLOAT, scalar);

        for (; ind0 + SPECIES_FLOAT.length() <= vec1.length; ind0 += SPECIES_FLOAT.length()) {
            FloatVector v = FloatVector.fromArray(SPECIES_FLOAT, vec1, ind0);
            (v.div(scalarVector)).intoArray(output, ind0);
        }

        for (; ind0 < vec1.length; ind0++) {
            output[ind0] = vec1[ind0] / scalar;
        }
        return output;
    }

    public static void divideMe(float[] vec1, final float[] vec2) {
        if (vec1.length != vec2.length) {
            throw new IllegalArgumentException("divideMe(): array lengths need to match");
        }
        int ind0 = 0;

        for (; ind0 + SPECIES_FLOAT.length() <= vec1.length; ind0 += SPECIES_FLOAT.length()) {
            FloatVector v1 = FloatVector.fromArray(SPECIES_FLOAT, vec1, ind0);
            FloatVector v2 = FloatVector.fromArray(SPECIES_FLOAT, vec2, ind0);
            (v1.div(v2)).intoArray(vec1, ind0);
        }

        for (; ind0 < vec1.length; ind0++) {
            vec1[ind0] /= vec2[ind0];
        }
    }

    public static void divideMe(float[] vec1, float scalar) {
        int ind0 = 0;
        FloatVector scalarVector = FloatVector.broadcast(SPECIES_FLOAT, scalar);

        for (; ind0 + SPECIES_FLOAT.length() <= vec1.length; ind0 += SPECIES_FLOAT.length()) {
            FloatVector v = FloatVector.fromArray(SPECIES_FLOAT, vec1, ind0);
            (v.div(scalarVector)).intoArray(vec1, ind0);
        }

        for (; ind0 < vec1.length; ind0++) {
            vec1[ind0] /= scalar;
        }
    }

    // --------------------- Sum --------------------
    public static float sum(final float[] vec1) {
        int ind0 = 0;
        FloatVector acc = FloatVector.broadcast(SPECIES_FLOAT, 0.0f);

        for (; ind0 + SPECIES_FLOAT.length() <= vec1.length; ind0 += SPECIES_FLOAT.length()) {
            FloatVector v = FloatVector.fromArray(SPECIES_FLOAT, vec1, ind0);
            acc = acc.add(v);
        }

        float result = 0.0f;
        for (int lane = 0; lane < SPECIES_FLOAT.length(); lane++) {
            result += acc.lane(lane);
        }

        for (; ind0 < vec1.length; ind0++) {
            result += vec1[ind0];
        }

        return result;
    }

    // --------------------- Product --------------------
    public static float product(final float[] vec1) {
        int ind0 = 0;
        FloatVector acc = FloatVector.broadcast(SPECIES_FLOAT, 1.0f);

        for (; ind0 + SPECIES_FLOAT.length() <= vec1.length; ind0 += SPECIES_FLOAT.length()) {
            FloatVector v = FloatVector.fromArray(SPECIES_FLOAT, vec1, ind0);
            acc = acc.mul(v);
        }

        float result = 1.0f;
        for (int lane = 0; lane < SPECIES_FLOAT.length(); lane++) {
            result *= acc.lane(lane);
        }

        for (; ind0 < vec1.length; ind0++) {
            result *= vec1[ind0];
        }

        return result;
    }

    // --------------------- Min/Max --------------------
    public static float[] getMinMax(float[] vec1) {
        float[] out = new float[2];
        out[0] = Float.MAX_VALUE;
        out[1] = -Float.MAX_VALUE;

        int ind0 = 0;
        FloatVector vMin = FloatVector.broadcast(SPECIES_FLOAT, Float.MAX_VALUE);
        FloatVector vMax = FloatVector.broadcast(SPECIES_FLOAT, -Float.MAX_VALUE);

        for (; ind0 + SPECIES_FLOAT.length() <= vec1.length; ind0 += SPECIES_FLOAT.length()) {
            FloatVector v = FloatVector.fromArray(SPECIES_FLOAT, vec1, ind0);
            vMin = vMin.min(v);
            vMax = vMax.max(v);
        }

        for (int lane = 0; lane < SPECIES_FLOAT.length(); lane++) {
            out[0] = Math.min(out[0], vMin.lane(lane));
            out[1] = Math.max(out[1], vMax.lane(lane));
        }

        for (; ind0 < vec1.length; ind0++) {
            out[0] = Math.min(out[0], vec1[ind0]);
            out[1] = Math.max(out[1], vec1[ind0]);
        }

        return out;
    }

    public static float dot(final float[] vec1, final float[] vec2) {
        if (vec1.length != vec2.length) {
            throw new IllegalArgumentException("dot(): array lengths need to match");
        }

        FloatVector acc = FloatVector.zero(SPECIES_FLOAT);

        int ind0 = 0;
        int upper = SPECIES_FLOAT.loopBound(vec1.length);

        // ---------------- SIMD loop ----------------
        for (; ind0 < upper; ind0 += SPECIES_FLOAT.length()) {
            FloatVector v1 = FloatVector.fromArray(SPECIES_FLOAT, vec1, ind0);
            FloatVector v2 = FloatVector.fromArray(SPECIES_FLOAT, vec2, ind0);
            acc = acc.add(v1.mul(v2));
        }

        // ---------------- reduction ----------------
        float sum = acc.reduceLanes(VectorOperators.ADD);

        // ---------------- tail ---------------------
        for (; ind0 < vec1.length; ind0++) {
            sum += vec1[ind0] * vec2[ind0];
        }

        return sum;
    }


    public static float norm1(final float[] vec1) {
        float acc = 0.0f;
        for (int ind0 = 0; ind0 < vec1.length; ind0++) {
            acc += Math.abs(vec1[ind0]);
        }
        return acc;
    }

    public static float norm2(final float[] vec1) {
        float acc = 0.0f;
        for (int ind0 = 0; ind0 < vec1.length; ind0++) {
            acc += vec1[ind0] * vec1[ind0];
        }
        return (float) Math.sqrt(acc);
    }

    public static void clampMe(float[] vec1, float min, float max) {
        for (int ind0 = 0; ind0 < vec1.length; ind0++) {
            vec1[ind0] = Math.max(min, Math.min(max, vec1[ind0]));
        }
    }


    //==============================  long support  =========================



    // --------------------- Elementwise addition --------------------
    public static long[] add(final long[] vec1, final long[] vec2) {
        if (vec1.length != vec2.length) throw new IllegalArgumentException("add(): lengths must match");

        long[] output = new long[vec1.length];
        int ind0 = 0;

        for (; ind0 + SPECIES_LONG.length() <= vec1.length; ind0 += SPECIES_LONG.length()) {
            LongVector v1 = LongVector.fromArray(SPECIES_LONG, vec1, ind0);
            LongVector v2 = LongVector.fromArray(SPECIES_LONG, vec2, ind0);
            (v1.add(v2)).intoArray(output, ind0);
        }

        for (; ind0 < vec1.length; ind0++) {
            output[ind0] = vec1[ind0] + vec2[ind0];
        }
        return output;
    }

    public static long[] add(final long[] vec1, long scalar) {
        long[] output = new long[vec1.length];
        int ind0 = 0;
        LongVector scalarVector = LongVector.broadcast(SPECIES_LONG, scalar);

        for (; ind0 + SPECIES_LONG.length() <= vec1.length; ind0 += SPECIES_LONG.length()) {
            LongVector v = LongVector.fromArray(SPECIES_LONG, vec1, ind0);
            (v.add(scalarVector)).intoArray(output, ind0);
        }

        for (; ind0 < vec1.length; ind0++) {
            output[ind0] = vec1[ind0] + scalar;
        }
        return output;
    }

    // --------------------- In-place addition --------------------
    public static void addMe(long[] vec1, final long[] vec2) {
        if (vec1.length != vec2.length) throw new IllegalArgumentException("addMe(): lengths must match");
        int ind0 = 0;

        for (; ind0 + SPECIES_LONG.length() <= vec1.length; ind0 += SPECIES_LONG.length()) {
            LongVector v1 = LongVector.fromArray(SPECIES_LONG, vec1, ind0);
            LongVector v2 = LongVector.fromArray(SPECIES_LONG, vec2, ind0);
            (v1.add(v2)).intoArray(vec1, ind0);
        }

        for (; ind0 < vec1.length; ind0++) {
            vec1[ind0] += vec2[ind0];
        }
    }

    public static void addMe(long[] vec1, long scalar) {
        int ind0 = 0;
        LongVector scalarVector = LongVector.broadcast(SPECIES_LONG, scalar);

        for (; ind0 + SPECIES_LONG.length() <= vec1.length; ind0 += SPECIES_LONG.length()) {
            LongVector v = LongVector.fromArray(SPECIES_LONG, vec1, ind0);
            (v.add(scalarVector)).intoArray(vec1, ind0);
        }

        for (; ind0 < vec1.length; ind0++) {
            vec1[ind0] += scalar;
        }
    }

    /**Product of all elements in a long vector
     * @param vec1 Input vector
     * @return Product of all elements
     */
    public static long product(final long[] vec1){
        long out = 1L;
        for (int ind0 = 0; ind0 < vec1.length; ind0++){
            out *= vec1[ind0];
        }
        return out;
    }

    // --------------------- Subtraction --------------------
    public static long[] subtract(final long[] vec1, final long[] vec2) {
        if (vec1.length != vec2.length) throw new IllegalArgumentException("subtract(): lengths must match");
        long[] output = new long[vec1.length];
        int ind0 = 0;

        for (; ind0 + SPECIES_LONG.length() <= vec1.length; ind0 += SPECIES_LONG.length()) {
            LongVector v1 = LongVector.fromArray(SPECIES_LONG, vec1, ind0);
            LongVector v2 = LongVector.fromArray(SPECIES_LONG, vec2, ind0);
            (v1.sub(v2)).intoArray(output, ind0);
        }

        for (; ind0 < vec1.length; ind0++) {
            output[ind0] = vec1[ind0] - vec2[ind0];
        }
        return output;
    }

    public static long[] subtract(final long[] vec1, long scalar) {
        long[] output = new long[vec1.length];
        int ind0 = 0;
        LongVector scalarVector = LongVector.broadcast(SPECIES_LONG, scalar);

        for (; ind0 + SPECIES_LONG.length() <= vec1.length; ind0 += SPECIES_LONG.length()) {
            LongVector v = LongVector.fromArray(SPECIES_LONG, vec1, ind0);
            (v.sub(scalarVector)).intoArray(output, ind0);
        }

        for (; ind0 < vec1.length; ind0++) {
            output[ind0] = vec1[ind0] - scalar;
        }
        return output;
    }

    public static void subtractMe(long[] vec1, final long[] vec2) {
        if (vec1.length != vec2.length) throw new IllegalArgumentException("subtractMe(): lengths must match");
        int ind0 = 0;

        for (; ind0 + SPECIES_LONG.length() <= vec1.length; ind0 += SPECIES_LONG.length()) {
            LongVector v1 = LongVector.fromArray(SPECIES_LONG, vec1, ind0);
            LongVector v2 = LongVector.fromArray(SPECIES_LONG, vec2, ind0);
            (v1.sub(v2)).intoArray(vec1, ind0);
        }

        for (; ind0 < vec1.length; ind0++) {
            vec1[ind0] -= vec2[ind0];
        }
    }

    public static void subtractMe(long[] vec1, long scalar) {
        int ind0 = 0;
        LongVector scalarVector = LongVector.broadcast(SPECIES_LONG, scalar);

        for (; ind0 + SPECIES_LONG.length() <= vec1.length; ind0 += SPECIES_LONG.length()) {
            LongVector v = LongVector.fromArray(SPECIES_LONG, vec1, ind0);
            (v.sub(scalarVector)).intoArray(vec1, ind0);
        }

        for (; ind0 < vec1.length; ind0++) {
            vec1[ind0] -= scalar;
        }
    }

    // --------------------- Multiply --------------------
    public static long[] multiply(final long[] vec1, final long[] vec2) {
        if (vec1.length != vec2.length) throw new IllegalArgumentException("multiply(): lengths must match");
        long[] output = new long[vec1.length];
        int ind0 = 0;

        for (; ind0 + SPECIES_LONG.length() <= vec1.length; ind0 += SPECIES_LONG.length()) {
            LongVector v1 = LongVector.fromArray(SPECIES_LONG, vec1, ind0);
            LongVector v2 = LongVector.fromArray(SPECIES_LONG, vec2, ind0);
            (v1.mul(v2)).intoArray(output, ind0);
        }

        for (; ind0 < vec1.length; ind0++) {
            output[ind0] = vec1[ind0] * vec2[ind0];
        }
        return output;
    }

    public static long[] multiply(final long[] vec1, long scalar) {
        long[] output = new long[vec1.length];
        int ind0 = 0;
        LongVector scalarVector = LongVector.broadcast(SPECIES_LONG, scalar);

        for (; ind0 + SPECIES_LONG.length() <= vec1.length; ind0 += SPECIES_LONG.length()) {
            LongVector v = LongVector.fromArray(SPECIES_LONG, vec1, ind0);
            (v.mul(scalarVector)).intoArray(output, ind0);
        }

        for (; ind0 < vec1.length; ind0++) {
            output[ind0] = vec1[ind0] * scalar;
        }
        return output;
    }

    public static void multiplyMe(long[] vec1, final long[] vec2) {
        if (vec1.length != vec2.length) throw new IllegalArgumentException("multiplyMe(): lengths must match");
        int ind0 = 0;

        for (; ind0 + SPECIES_LONG.length() <= vec1.length; ind0 += SPECIES_LONG.length()) {
            LongVector v1 = LongVector.fromArray(SPECIES_LONG, vec1, ind0);
            LongVector v2 = LongVector.fromArray(SPECIES_LONG, vec2, ind0);
            (v1.mul(v2)).intoArray(vec1, ind0);
        }

        for (; ind0 < vec1.length; ind0++) {
            vec1[ind0] *= vec2[ind0];
        }
    }

    public static void multiplyMe(long[] vec1, long scalar) {
        int ind0 = 0;
        LongVector scalarVector = LongVector.broadcast(SPECIES_LONG, scalar);

        for (; ind0 + SPECIES_LONG.length() <= vec1.length; ind0 += SPECIES_LONG.length()) {
            LongVector v = LongVector.fromArray(SPECIES_LONG, vec1, ind0);
            (v.mul(scalarVector)).intoArray(vec1, ind0);
        }

        for (; ind0 < vec1.length; ind0++) {
            vec1[ind0] *= scalar;
        }
    }

    // --------------------- Sum --------------------
    public static long sum(final long[] vec1) {
        int ind0 = 0;
        LongVector acc = LongVector.broadcast(SPECIES_LONG, 0L);

        for (; ind0 + SPECIES_LONG.length() <= vec1.length; ind0 += SPECIES_LONG.length()) {
            LongVector v = LongVector.fromArray(SPECIES_LONG, vec1, ind0);
            acc = acc.add(v);
        }

        long result = 0L;
        for (int lane = 0; lane < SPECIES_LONG.length(); lane++) {
            result += acc.lane(lane);
        }

        for (; ind0 < vec1.length; ind0++) {
            result += vec1[ind0];
        }

        return result;
    }

    // --------------------- Min/Max --------------------
    public static long[] getMinMax(final long[] vec1) {
        long[] out = new long[2];
        out[0] = Long.MAX_VALUE;
        out[1] = Long.MIN_VALUE;

        int ind0 = 0;
        LongVector vMin = LongVector.broadcast(SPECIES_LONG, Long.MAX_VALUE);
        LongVector vMax = LongVector.broadcast(SPECIES_LONG, Long.MIN_VALUE);

        for (; ind0 + SPECIES_LONG.length() <= vec1.length; ind0 += SPECIES_LONG.length()) {
            LongVector v = LongVector.fromArray(SPECIES_LONG, vec1, ind0);
            vMin = vMin.min(v);
            vMax = vMax.max(v);
        }

        for (int lane = 0; lane < SPECIES_LONG.length(); lane++) {
            out[0] = Math.min(out[0], vMin.lane(lane));
            out[1] = Math.max(out[1], vMax.lane(lane));
        }

        for (; ind0 < vec1.length; ind0++) {
            out[0] = Math.min(out[0], vec1[ind0]);
            out[1] = Math.max(out[1], vec1[ind0]);
        }

        return out;
    }

    public static long dot(final long[] vec1, final long[] vec2) {
        if (vec1.length != vec2.length) {
            throw new IllegalArgumentException("dot(): array lengths need to match");
        }

        LongVector acc = LongVector.zero(SPECIES_LONG);

        int ind0 = 0;
        int upper = SPECIES_LONG.loopBound(vec1.length);

        // ---------------- SIMD loop ----------------
        for (; ind0 < upper; ind0 += SPECIES_LONG.length()) {
            LongVector v1 = LongVector.fromArray(SPECIES_LONG, vec1, ind0);
            LongVector v2 = LongVector.fromArray(SPECIES_LONG, vec2, ind0);
            acc = acc.add(v1.mul(v2));
        }

        // ---------------- reduction ----------------
        long sum = acc.reduceLanes(VectorOperators.ADD);

        // ---------------- tail ---------------------
        for (; ind0 < vec1.length; ind0++) {
            sum += vec1[ind0] * vec2[ind0];
        }

        return sum;
    }


    public static long norm1(final long[] vec1) {
        long acc = 0L;
        for (int ind0 = 0; ind0 < vec1.length; ind0++) {
            acc += Math.abs(vec1[ind0]);
        }
        return acc;
    }

    public static double norm2(final long[] vec1) {
        double acc = 0.0;
        for (int ind0 = 0; ind0 < vec1.length; ind0++) {
            acc += (double) vec1[ind0] * vec1[ind0];
        }
        return Math.sqrt(acc);
    }

    public static void clampMe(long[] vec1, long min, long max) {
        for (int ind0 = 0; ind0 < vec1.length; ind0++) {
            vec1[ind0] = Math.max(min, Math.min(max, vec1[ind0]));
        }
    }


    //============================ support int  =============================


    // --------------------- Elementwise addition --------------------
    public static int[] add(final int[] vec1, final int[] vec2) {
        if (vec1.length != vec2.length) throw new IllegalArgumentException("add(): lengths must match");

        int[] output = new int[vec1.length];
        int ind0 = 0;

        for (; ind0 + SPECIES_INT.length() <= vec1.length; ind0 += SPECIES_INT.length()) {
            IntVector v1 = IntVector.fromArray(SPECIES_INT, vec1, ind0);
            IntVector v2 = IntVector.fromArray(SPECIES_INT, vec2, ind0);
            (v1.add(v2)).intoArray(output, ind0);
        }

        for (; ind0 < vec1.length; ind0++) {
            output[ind0] = vec1[ind0] + vec2[ind0];
        }
        return output;
    }

    public static int[] add(final int[] vec1, int scalar) {
        int[] output = new int[vec1.length];
        int ind0 = 0;
        IntVector scalarVector = IntVector.broadcast(SPECIES_INT, scalar);

        for (; ind0 + SPECIES_INT.length() <= vec1.length; ind0 += SPECIES_INT.length()) {
            IntVector v = IntVector.fromArray(SPECIES_INT, vec1, ind0);
            (v.add(scalarVector)).intoArray(output, ind0);
        }

        for (; ind0 < vec1.length; ind0++) {
            output[ind0] = vec1[ind0] + scalar;
        }
        return output;
    }

    // --------------------- In-place addition --------------------
    public static void addMe(int[] vec1, final int[] vec2) {
        if (vec1.length != vec2.length) throw new IllegalArgumentException("addMe(): lengths must match");
        int ind0 = 0;

        for (; ind0 + SPECIES_INT.length() <= vec1.length; ind0 += SPECIES_INT.length()) {
            IntVector v1 = IntVector.fromArray(SPECIES_INT, vec1, ind0);
            IntVector v2 = IntVector.fromArray(SPECIES_INT, vec2, ind0);
            (v1.add(v2)).intoArray(vec1, ind0);
        }

        for (; ind0 < vec1.length; ind0++) {
            vec1[ind0] += vec2[ind0];
        }
    }

    public static void addMe(int[] vec1, int scalar) {
        int ind0 = 0;
        IntVector scalarVector = IntVector.broadcast(SPECIES_INT, scalar);

        for (; ind0 + SPECIES_INT.length() <= vec1.length; ind0 += SPECIES_INT.length()) {
            IntVector v = IntVector.fromArray(SPECIES_INT, vec1, ind0);
            (v.add(scalarVector)).intoArray(vec1, ind0);
        }

        for (; ind0 < vec1.length; ind0++) {
            vec1[ind0] += scalar;
        }
    }

    // --------------------- Subtraction --------------------
    public static int[] subtract(final int[] vec1, final int[] vec2) {
        if (vec1.length != vec2.length) throw new IllegalArgumentException("subtract(): lengths must match");
        int[] output = new int[vec1.length];
        int ind0 = 0;

        for (; ind0 + SPECIES_INT.length() <= vec1.length; ind0 += SPECIES_INT.length()) {
            IntVector v1 = IntVector.fromArray(SPECIES_INT, vec1, ind0);
            IntVector v2 = IntVector.fromArray(SPECIES_INT, vec2, ind0);
            (v1.sub(v2)).intoArray(output, ind0);
        }

        for (; ind0 < vec1.length; ind0++) {
            output[ind0] = vec1[ind0] - vec2[ind0];
        }
        return output;
    }

    public static int[] subtract(final int[] vec1, int scalar) {
        int[] output = new int[vec1.length];
        int ind0 = 0;
        IntVector scalarVector = IntVector.broadcast(SPECIES_INT, scalar);

        for (; ind0 + SPECIES_INT.length() <= vec1.length; ind0 += SPECIES_INT.length()) {
            IntVector v = IntVector.fromArray(SPECIES_INT, vec1, ind0);
            (v.sub(scalarVector)).intoArray(output, ind0);
        }

        for (; ind0 < vec1.length; ind0++) {
            output[ind0] = vec1[ind0] - scalar;
        }
        return output;
    }

    public static void subtractMe(int[] vec1, final int[] vec2) {
        if (vec1.length != vec2.length) throw new IllegalArgumentException("subtractMe(): lengths must match");
        int ind0 = 0;

        for (; ind0 + SPECIES_INT.length() <= vec1.length; ind0 += SPECIES_INT.length()) {
            IntVector v1 = IntVector.fromArray(SPECIES_INT, vec1, ind0);
            IntVector v2 = IntVector.fromArray(SPECIES_INT, vec2, ind0);
            (v1.sub(v2)).intoArray(vec1, ind0);
        }

        for (; ind0 < vec1.length; ind0++) {
            vec1[ind0] -= vec2[ind0];
        }
    }

    public static void subtractMe(int[] vec1, int scalar) {
        int ind0 = 0;
        IntVector scalarVector = IntVector.broadcast(SPECIES_INT, scalar);

        for (; ind0 + SPECIES_INT.length() <= vec1.length; ind0 += SPECIES_INT.length()) {
            IntVector v = IntVector.fromArray(SPECIES_INT, vec1, ind0);
            (v.sub(scalarVector)).intoArray(vec1, ind0);
        }

        for (; ind0 < vec1.length; ind0++) {
            vec1[ind0] -= scalar;
        }
    }

    // --------------------- Multiply --------------------
    public static int[] multiply(final int[] vec1, final int[] vec2) {
        if (vec1.length != vec2.length) throw new IllegalArgumentException("multiply(): lengths must match");
        int[] output = new int[vec1.length];
        int ind0 = 0;

        for (; ind0 + SPECIES_INT.length() <= vec1.length; ind0 += SPECIES_INT.length()) {
            IntVector v1 = IntVector.fromArray(SPECIES_INT, vec1, ind0);
            IntVector v2 = IntVector.fromArray(SPECIES_INT, vec2, ind0);
            (v1.mul(v2)).intoArray(output, ind0);
        }

        for (; ind0 < vec1.length; ind0++) {
            output[ind0] = vec1[ind0] * vec2[ind0];
        }
        return output;
    }

    public static int[] multiply(final int[] vec1, int scalar) {
        int[] output = new int[vec1.length];
        int ind0 = 0;
        IntVector scalarVector = IntVector.broadcast(SPECIES_INT, scalar);

        for (; ind0 + SPECIES_INT.length() <= vec1.length; ind0 += SPECIES_INT.length()) {
            IntVector v = IntVector.fromArray(SPECIES_INT, vec1, ind0);
            (v.mul(scalarVector)).intoArray(output, ind0);
        }

        for (; ind0 < vec1.length; ind0++) {
            output[ind0] = vec1[ind0] * scalar;
        }
        return output;
    }
    
    public static void multiplyMe(int[] vec1, final int[] vec2) {
        if (vec1.length != vec2.length) throw new IllegalArgumentException("multiplyMe(): lengths must match");
        int ind0 = 0;

        for (; ind0 + SPECIES_INT.length() <= vec1.length; ind0 += SPECIES_INT.length()) {
            IntVector v1 = IntVector.fromArray(SPECIES_INT, vec1, ind0);
            IntVector v2 = IntVector.fromArray(SPECIES_INT, vec2, ind0);
            (v1.mul(v2)).intoArray(vec1, ind0);
        }

        for (; ind0 < vec1.length; ind0++) {
            vec1[ind0] *= vec2[ind0];
        }
    }

    public static void multiplyMe(int[] vec1, int scalar) {
        int ind0 = 0;
        IntVector scalarVector = IntVector.broadcast(SPECIES_INT, scalar);

        for (; ind0 + SPECIES_INT.length() <= vec1.length; ind0 += SPECIES_INT.length()) {
            IntVector v = IntVector.fromArray(SPECIES_INT, vec1, ind0);
            (v.mul(scalarVector)).intoArray(vec1, ind0);
        }

        for (; ind0 < vec1.length; ind0++) {
            vec1[ind0] *= scalar;
        }
    }

    /**Product of all elements in an int vector (widened to long)
     * @param vec1 Input vector
     * @return Product of all elements as long
     */
    public static long product(final int[] vec1){
        long out = 1L;
        for (int ind0 = 0; ind0 < vec1.length; ind0++){
            out *= vec1[ind0];
        }
        return out;
    }


    // --------------------- Sum --------------------
    public static int sum(final int[] vec1) {
        int ind0 = 0;
        IntVector acc = IntVector.broadcast(SPECIES_INT, 0);

        for (; ind0 + SPECIES_INT.length() <= vec1.length; ind0 += SPECIES_INT.length()) {
            IntVector v = IntVector.fromArray(SPECIES_INT, vec1, ind0);
            acc = acc.add(v);
        }

        int result = 0;
        for (int lane = 0; lane < SPECIES_INT.length(); lane++) {
            result += acc.lane(lane);
        }

        for (; ind0 < vec1.length; ind0++) {
            result += vec1[ind0];
        }

        return result;
    }

    // --------------------- Min/Max --------------------
    public static int[] getMinMax(final int[] vec1) {
        int[] out = new int[2];
        out[0] = Integer.MAX_VALUE;
        out[1] = Integer.MIN_VALUE;

        int ind0 = 0;
        IntVector vMin = IntVector.broadcast(SPECIES_INT, Integer.MAX_VALUE);
        IntVector vMax = IntVector.broadcast(SPECIES_INT, Integer.MIN_VALUE);

        for (; ind0 + SPECIES_INT.length() <= vec1.length; ind0 += SPECIES_INT.length()) {
            IntVector v = IntVector.fromArray(SPECIES_INT, vec1, ind0);
            vMin = vMin.min(v);
            vMax = vMax.max(v);
        }

        for (int lane = 0; lane < SPECIES_INT.length(); lane++) {
            out[0] = Math.min(out[0], vMin.lane(lane));
            out[1] = Math.max(out[1], vMax.lane(lane));
        }

        for (; ind0 < vec1.length; ind0++) {
            out[0] = Math.min(out[0], vec1[ind0]);
            out[1] = Math.max(out[1], vec1[ind0]);
        }

        return out;
    }

    public static long dot(final int[] vec1, final int[] vec2) {
        if (vec1.length != vec2.length) {
            throw new IllegalArgumentException("dot(): array lengths need to match");
        }

        LongVector acc = LongVector.zero(SPECIES_LONG);

        int ind0 = 0;
        int upper = SPECIES_INT.loopBound(vec1.length);

        // ---------------- SIMD loop ----------------
        for (; ind0 < upper; ind0 += SPECIES_INT.length()) {
            IntVector v1 = IntVector.fromArray(SPECIES_INT, vec1, ind0);
            IntVector v2 = IntVector.fromArray(SPECIES_INT, vec2, ind0);

            // widen to long and accumulate
            acc = acc.add(v1.mul(v2).convert(VectorOperators.I2L, 0));
        }

        // ---------------- reduction ----------------
        long sum = acc.reduceLanes(VectorOperators.ADD);

        // ---------------- tail ---------------------
        for (; ind0 < vec1.length; ind0++) {
            sum += (long) vec1[ind0] * vec2[ind0];
        }

        return sum;
    }

    public static long norm1(final int[] vec1) {
        long acc = 0L;
        for (int ind0 = 0; ind0 < vec1.length; ind0++) {
            acc += Math.abs(vec1[ind0]);
        }
        return acc;
    }

    public static double norm2(final int[] vec1) {
        double acc = 0.0;
        for (int ind0 = 0; ind0 < vec1.length; ind0++) {
            acc += (double) vec1[ind0] * vec1[ind0];
        }
        return Math.sqrt(acc);
    }

    public static void clampMe(int[] vec1, int min, int max) {
        for (int ind0 = 0; ind0 < vec1.length; ind0++) {
            vec1[ind0] = Math.max(min, Math.min(max, vec1[ind0]));
        }
    }


    //============================ support short  =============================

    // --------------------- Elementwise addition --------------------
    public static short[] add(final short[] vec1, final short[] vec2) {
        if (vec1.length != vec2.length) throw new IllegalArgumentException("add(): lengths must match");
        short[] output = new short[vec1.length];
        int ind0 = 0;

        for (; ind0 + SPECIES_SHORT.length() <= vec1.length; ind0 += SPECIES_SHORT.length()) {
            ShortVector v1 = ShortVector.fromArray(SPECIES_SHORT, vec1, ind0);
            ShortVector v2 = ShortVector.fromArray(SPECIES_SHORT, vec2, ind0);
            (v1.add(v2)).intoArray(output, ind0);
        }

        for (; ind0 < vec1.length; ind0++) {
            output[ind0] = (short)(vec1[ind0] + vec2[ind0]);
        }

        return output;
    }

    public static short[] add(final short[] vec1, short scalar) {
        short[] output = new short[vec1.length];
        int ind0 = 0;
        ShortVector scalarVector = ShortVector.broadcast(SPECIES_SHORT, scalar);

        for (; ind0 + SPECIES_SHORT.length() <= vec1.length; ind0 += SPECIES_SHORT.length()) {
            ShortVector v = ShortVector.fromArray(SPECIES_SHORT, vec1, ind0);
            (v.add(scalarVector)).intoArray(output, ind0);
        }

        for (; ind0 < vec1.length; ind0++) {
            output[ind0] = (short)(vec1[ind0] + scalar);
        }

        return output;
    }

    // --------------------- In-place addition --------------------
    public static void addMe(short[] vec1, final short[] vec2) {
        if (vec1.length != vec2.length) throw new IllegalArgumentException("addMe(): lengths must match");
        int ind0 = 0;

        for (; ind0 + SPECIES_SHORT.length() <= vec1.length; ind0 += SPECIES_SHORT.length()) {
            ShortVector v1 = ShortVector.fromArray(SPECIES_SHORT, vec1, ind0);
            ShortVector v2 = ShortVector.fromArray(SPECIES_SHORT, vec2, ind0);
            (v1.add(v2)).intoArray(vec1, ind0);
        }

        for (; ind0 < vec1.length; ind0++) {
            vec1[ind0] += vec2[ind0];
        }
    }

    public static void addMe(short[] vec1, short scalar) {
        int ind0 = 0;
        ShortVector scalarVector = ShortVector.broadcast(SPECIES_SHORT, scalar);

        for (; ind0 + SPECIES_SHORT.length() <= vec1.length; ind0 += SPECIES_SHORT.length()) {
            ShortVector v = ShortVector.fromArray(SPECIES_SHORT, vec1, ind0);
            (v.add(scalarVector)).intoArray(vec1, ind0);
        }

        for (; ind0 < vec1.length; ind0++) {
            vec1[ind0] += scalar;
        }
    }

    // --------------------- Subtraction --------------------
    public static short[] subtract(final short[] vec1, final short[] vec2) {
        if (vec1.length != vec2.length) throw new IllegalArgumentException("subtract(): lengths must match");
        short[] output = new short[vec1.length];
        int ind0 = 0;

        for (; ind0 + SPECIES_SHORT.length() <= vec1.length; ind0 += SPECIES_SHORT.length()) {
            ShortVector v1 = ShortVector.fromArray(SPECIES_SHORT, vec1, ind0);
            ShortVector v2 = ShortVector.fromArray(SPECIES_SHORT, vec2, ind0);
            (v1.sub(v2)).intoArray(output, ind0);
        }

        for (; ind0 < vec1.length; ind0++) {
            output[ind0] = (short)(vec1[ind0] - vec2[ind0]);
        }

        return output;
    }

    public static short[] subtract(final short[] vec1, short scalar) {
        short[] output = new short[vec1.length];
        int ind0 = 0;
        ShortVector scalarVector = ShortVector.broadcast(SPECIES_SHORT, scalar);

        for (; ind0 + SPECIES_SHORT.length() <= vec1.length; ind0 += SPECIES_SHORT.length()) {
            ShortVector v = ShortVector.fromArray(SPECIES_SHORT, vec1, ind0);
            (v.sub(scalarVector)).intoArray(output, ind0);
        }

        for (; ind0 < vec1.length; ind0++) {
            output[ind0] = (short)(vec1[ind0] - scalar);
        }

        return output;
    }

    public static void subtractMe(short[] vec1, final short[] vec2) {
        if (vec1.length != vec2.length) throw new IllegalArgumentException("subtractMe(): lengths must match");
        int ind0 = 0;

        for (; ind0 + SPECIES_SHORT.length() <= vec1.length; ind0 += SPECIES_SHORT.length()) {
            ShortVector v1 = ShortVector.fromArray(SPECIES_SHORT, vec1, ind0);
            ShortVector v2 = ShortVector.fromArray(SPECIES_SHORT, vec2, ind0);
            (v1.sub(v2)).intoArray(vec1, ind0);
        }

        for (; ind0 < vec1.length; ind0++) {
            vec1[ind0] -= vec2[ind0];
        }
    }

    public static void subtractMe(short[] vec1, short scalar) {
        int ind0 = 0;
        ShortVector scalarVector = ShortVector.broadcast(SPECIES_SHORT, scalar);

        for (; ind0 + SPECIES_SHORT.length() <= vec1.length; ind0 += SPECIES_SHORT.length()) {
            ShortVector v = ShortVector.fromArray(SPECIES_SHORT, vec1, ind0);
            (v.sub(scalarVector)).intoArray(vec1, ind0);
        }

        for (; ind0 < vec1.length; ind0++) {
            vec1[ind0] -= scalar;
        }
    }

    // --------------------- Multiply --------------------
    public static short[] multiply(final short[] vec1, final short[] vec2) {
        if (vec1.length != vec2.length) throw new IllegalArgumentException("multiply(): lengths must match");
        short[] output = new short[vec1.length];
        int ind0 = 0;

        for (; ind0 + SPECIES_SHORT.length() <= vec1.length; ind0 += SPECIES_SHORT.length()) {
            ShortVector v1 = ShortVector.fromArray(SPECIES_SHORT, vec1, ind0);
            ShortVector v2 = ShortVector.fromArray(SPECIES_SHORT, vec2, ind0);
            (v1.mul(v2)).intoArray(output, ind0);
        }

        for (; ind0 < vec1.length; ind0++) {
            output[ind0] = (short)(vec1[ind0] * vec2[ind0]);
        }

        return output;
    }

    public static short[] multiply(final short[] vec1, short scalar) {
        short[] output = new short[vec1.length];
        int ind0 = 0;
        ShortVector scalarVector = ShortVector.broadcast(SPECIES_SHORT, scalar);

        for (; ind0 + SPECIES_SHORT.length() <= vec1.length; ind0 += SPECIES_SHORT.length()) {
            ShortVector v = ShortVector.fromArray(SPECIES_SHORT, vec1, ind0);
            (v.mul(scalarVector)).intoArray(output, ind0);
        }

        for (; ind0 < vec1.length; ind0++) {
            output[ind0] = (short)(vec1[ind0] * scalar);
        }

        return output;
    }

    public static void multiplyMe(short[] vec1, final short[] vec2) {
        if (vec1.length != vec2.length) throw new IllegalArgumentException("multiplyMe(): lengths must match");
        int ind0 = 0;

        for (; ind0 + SPECIES_SHORT.length() <= vec1.length; ind0 += SPECIES_SHORT.length()) {
            ShortVector v1 = ShortVector.fromArray(SPECIES_SHORT, vec1, ind0);
            ShortVector v2 = ShortVector.fromArray(SPECIES_SHORT, vec2, ind0);
            (v1.mul(v2)).intoArray(vec1, ind0);
        }

        for (; ind0 < vec1.length; ind0++) {
            vec1[ind0] *= vec2[ind0];
        }
    }

    public static void multiplyMe(short[] vec1, short scalar) {
        int ind0 = 0;
        ShortVector scalarVector = ShortVector.broadcast(SPECIES_SHORT, scalar);

        for (; ind0 + SPECIES_SHORT.length() <= vec1.length; ind0 += SPECIES_SHORT.length()) {
            ShortVector v = ShortVector.fromArray(SPECIES_SHORT, vec1, ind0);
            (v.mul(scalarVector)).intoArray(vec1, ind0);
        }

        for (; ind0 < vec1.length; ind0++) {
            vec1[ind0] *= scalar;
        }
    }

    /**Product of all elements in a short vector (widened to long)
     * @param vec1 Input vector
     * @return Product of all elements as long
     */
    public static long product(final short[] vec1){
        long out = 1L;
        for (int ind0 = 0; ind0 < vec1.length; ind0++){
            out *= vec1[ind0];
        }
        return out;
    }


    // --------------------- Sum --------------------
    public static int sum(final short[] vec1) {
        int ind0 = 0;
        ShortVector acc = ShortVector.broadcast(SPECIES_SHORT, (short)0);

        for (; ind0 + SPECIES_SHORT.length() <= vec1.length; ind0 += SPECIES_SHORT.length()) {
            ShortVector v = ShortVector.fromArray(SPECIES_SHORT, vec1, ind0);
            acc = acc.add(v);
        }

        int result = 0;
        for (int lane = 0; lane < SPECIES_SHORT.length(); lane++) {
            result += acc.lane(lane);
        }

        for (; ind0 < vec1.length; ind0++) {
            result += vec1[ind0];
        }

        return result;
    }

    // --------------------- Min/Max --------------------
    public static short[] getMinMax(final short[] vec1) {
        short[] out = new short[2];
        out[0] = Short.MAX_VALUE;
        out[1] = Short.MIN_VALUE;

        int ind0 = 0;
        ShortVector vMin = ShortVector.broadcast(SPECIES_SHORT, Short.MAX_VALUE);
        ShortVector vMax = ShortVector.broadcast(SPECIES_SHORT, Short.MIN_VALUE);

        for (; ind0 + SPECIES_SHORT.length() <= vec1.length; ind0 += SPECIES_SHORT.length()) {
            ShortVector v = ShortVector.fromArray(SPECIES_SHORT, vec1, ind0);
            vMin = vMin.min(v);
            vMax = vMax.max(v);
        }

        for (int lane = 0; lane < SPECIES_SHORT.length(); lane++) {
            out[0] = (short) Math.min(out[0], vMin.lane(lane));
            out[1] = (short) Math.max(out[1], vMax.lane(lane));
        }

        for (; ind0 < vec1.length; ind0++) {
            out[0] = (short) Math.min(out[0], vec1[ind0]);
            out[1] = (short) Math.max(out[1], vec1[ind0]);
        }

        return out;
    }

    public static long dot(final short[] vec1, final short[] vec2) {
        if (vec1.length != vec2.length) {
            throw new IllegalArgumentException("dot(): array lengths need to match");
        }

        LongVector acc = LongVector.zero(SPECIES_LONG);

        int ind0 = 0;
        int upper = SPECIES_SHORT.loopBound(vec1.length);

        // ---------------- SIMD loop ----------------
        for (; ind0 < upper; ind0 += SPECIES_SHORT.length()) {
            ShortVector v1 = ShortVector.fromArray(SPECIES_SHORT, vec1, ind0);
            ShortVector v2 = ShortVector.fromArray(SPECIES_SHORT, vec2, ind0);

            // short -> int -> multiply -> long
            acc = acc.add(
                v1.convert(VectorOperators.S2I, 0)
                .mul(v2.convert(VectorOperators.S2I, 0))
                .convert(VectorOperators.I2L, 0)
            );
        }

        // ---------------- reduction ----------------
        long sum = acc.reduceLanes(VectorOperators.ADD);

        // ---------------- tail ---------------------
        for (; ind0 < vec1.length; ind0++) {
            sum += (long) vec1[ind0] * vec2[ind0];
        }

        return sum;
    }


    public static int norm1(final short[] vec1) {
        int acc = 0;
        for (int ind0 = 0; ind0 < vec1.length; ind0++) {
            acc += Math.abs(vec1[ind0]);
        }
        return acc;
    }

    public static double norm2(final short[] vec1) {
        double acc = 0.0;
        for (int ind0 = 0; ind0 < vec1.length; ind0++) {
            acc += (double) vec1[ind0] * vec1[ind0];
        }
        return Math.sqrt(acc);
    }

    public static void clampMe(short[] vec1, short min, short max) {
        for (int ind0 = 0; ind0 < vec1.length; ind0++) {
            int v = vec1[ind0];
            if (v < min) v = min;
            if (v > max) v = max;
            vec1[ind0] = (short) v;
        }
    }

}