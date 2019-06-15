package jdsp.math;
import jdsp.math.Vector;
public class Matrix{
    /** Calculate the inverse of the matrix.
     *
     *  @param matrix The matrix that you want to find the inverse of.
     *  @param out The output to store the inverse of matrix. 
     */
    public static void inverse(final double[][] matrix, double[][] out){
        // -----------------------  error checking  -------------------------
        if (matrix.length != matrix[0].length){
            throw new IllegalArgumentException(
                "Input matrix should be a square matrix");
        }
        if (out.length != out[0].length){
            throw new IllegalArgumentException(
                "Output should be square matrix");
        }
        if (matrix.length != out.length){
            throw new IllegalArgumentException(
                "Size of the input and output matrix must match");
        }

        // ---------------------- perform inverse  --------------------------
        // use the Gauss-Jordan method to find inverse
        gaussJordan(matrix, out);
    }
    
    /**
     * Gauss-Jordan is a Gaussian elimination method for solving a set of
     * linear equations.
     * @param matrix The Matrix
     * @param out The output matrix
     */
    public static void gaussJordan(final double[][] matrix, double[][] out){
        // -----------------------  error checking  -------------------------
        if (matrix.length == 0 || matrix.length != matrix[0].length ){
            throw new IllegalArgumentException(
                "Input matrix needs to be square");
        }
        if (out.length ==0 || out.length != out[0].length){
            throw new IllegalArgumentException(
                "Output should be square matrix");
        }
        if (matrix.length != out.length){
            throw new IllegalArgumentException(
                "Size of input and output should be the same");
        }
        
        // Perform function to get matrix inverse.
        double[][] temp = new double[matrix.length][matrix[0].length];
        for (int ind0 = 0; ind0 < matrix.length; ind0++){
            System.arraycopy(matrix[ind0], 0, temp[ind0], 0, temp[ind0].length);
            for (int ind1 = 0; ind1 < out.length; ind1++){
                if (ind0 == ind1)
                    out[ind0][ind1] = 1;
                else
                    out[ind0][ind1] = 0;
            }
        }
        for (int ind0 = 0; ind0<temp.length; ind0++){
            if (temp[ind0][ind0] == 0){
                // swap row with a lower row
                for (int ind1 = ind0 + 1; ind1 < temp.length; ind1++){
                    if (temp[ind1][ind0] != 0){
                        rowSwap(temp, ind0, ind1);
                        rowSwap(out, ind0, ind1);
                        break;
                    }
                }
            }
            if (temp[ind0][ind0] == 0)
                //Still zero, break out now.
                break;
            else{
                rowScale(out, ind0, 1 / temp[ind0][ind0]);
                rowScale(temp, ind0, 1 / temp[ind0][ind0]);
                for (int ind1 = 0; ind1 < temp.length; ind1++)
                    if (ind1!= ind0){
                        rowSubtract(out, ind1, ind0, temp[ind1][ind0]);
                        rowSubtract(temp, ind1, ind0, temp[ind1][ind0]);
                    }
            }
        }
    }
    /**
     * Perform row swap
     * 
     * @param matrix Input matrix
     * @param i First row
     * @param j Second row
     */
    public static void rowSwap(double[][] matrix, int i, int j){
        if (matrix.length > i && matrix.length > j && i >= 0 && j >= 0){
            if (i != j){
                double[] temp = new double[matrix[0].length];
                System.arraycopy(matrix[i], 0, temp,0, temp.length);
                System.arraycopy(matrix[j], 0, matrix[i], 0, temp.length);
                System.arraycopy(temp, 0, matrix[j], 0, temp.length);
            }
        }
        else
            throw new IllegalArgumentException("Indices must be in range"+
                    " [0, numRows)");
    }
    /**
     * Subtract row j from row i.
     * @param matrix Input matrix
     * @param i Target row
     * @param j The row holding the values to subtract.
     * @param scale The scalar factor to apply to row j before subtracting
     */
    public static void rowSubtract(final double[][] matrix, int i, int j, double scale){
        if (i >= 0 && j >= 0 && matrix.length > i && matrix.length > j){
            for (int ind0 = 0; ind0 < matrix[i].length; ind0++){
                matrix[i][ind0] -= scale * matrix[j][ind0];
            }
        }            
        else{
            throw new IllegalArgumentException(
                "Indices must be within number rows of matrix");
        }
    }
    /**
     * Scale a row by double value.
     * @param matrix Input matrix
     * @param i The row of the matrix to scale
     * @param scale The value to scale the matrix by.
     */
    public static void rowScale(final double[][] matrix, int i, double scale){
       if (matrix.length > 0 && i >= 0 && i < matrix.length){
           Vector.multiplyMe(matrix[i], scale);
           //for (int ind0 = 0; ind0 < matrix[i].length; ind0++)
           //    matrix[i][ind0] *= scale;
       } 
       else{
           throw new IllegalArgumentException("index must be within length of matrix");
       }
    }
    
    /**
     * Perform multiplication of two matrices.  matrix 1 should have number of
     * columns matching the number of rows in matrix 2.
     * @param mat1 Matrix 1 (a x b)
     * @param mat2 Matrix 2 (b x c)
     * @param out Output Matrix (a x c)
     */
    public static void matrixMultiply(final double[][] mat1, 
            final double[][] mat2, double[][] out){
        // --------------------------  error checking  ----------------------
        // check to make sure correct dimensions
        if (mat1.length == 0 || mat1[0].length == 0 || mat2.length == 0 || 
                mat2[0].length == 0 || out.length == 0 || out[0].length == 0){
            throw new IllegalArgumentException("Matrix dimesnions should not be 0");
        }
        if (mat1.length != out.length || mat1[0].length != mat2.length ||
                mat2[0].length != out[0].length){
            throw new IllegalArgumentException("Matrix multiply requires "+
                    "shapes (A x B) and (B x C)");
        }
        for (int ind0 = 0; ind0 < out.length; ind0++)
            for(int ind1 = 0; ind1 < out[0].length; ind1++){
                out[ind0][ind1] = 0;
                for (int ind2 = 0; ind2 < mat2.length; ind2++)
                    out[ind0][ind1] += mat1[ind0][ind2] * mat2[ind2][ind1];
            }
    }
    
    /**
     * Calculate the determinant of the given matrix
     * @param matrix The input matrix
     * @return Returns the determinant of the matrix
     */
    public static double determinant(final double[][] matrix){
        // perform error checking
        if (matrix.length == 0 || matrix[0].length != matrix.length){
            throw new IllegalArgumentException("Requires a square matrix");
        }
        switch (matrix.length) {
            case 2:
                return (matrix[0][0] * matrix[1][1] - 
                    matrix[1][0] * matrix[0][1]);
            case 1:
                return matrix[0][0];
            default:
                double sum = 0, mult = 1;
                int i;
                double[][] temp = new double[matrix.length - 1][matrix.length - 1];
                for (int ind0 = 0; ind0 < matrix.length; ind0++){
                    i = 0;
                    for (int ind1 = 0; ind1 < matrix.length; ind1++)
                        if (ind1 != ind0)
                            System.arraycopy(matrix[ind1], 1, 
                                temp[i++], 0, temp.length);
                    
                    sum += mult * matrix[ind0][0] * determinant(temp);
                    mult = mult * -1;
                }   return sum;
        }
    }

    /**
     * Print out the matrix
     * 
     * @param matrix The matrix to print
     */
    public static void display(final double[][] matrix){
        for (double[] matrix1 : matrix) {
            for (int ind1 = 0; ind1 < matrix1.length; ind1++) {
                System.out.print("\t" + matrix1[ind1]);
            }
            System.out.println();
        }
    }

    /**
     * Print out a vector
     * 
     * @param vector The vector to print.
     */
    public static void display(final double[] vector){
        for (int ind0 = 0; ind0 < vector.length; ind0++)
            System.out.print("\t" + vector[ind0]);
        System.out.println();
                    
    }
    /**
     * Calculate the cumulative product of every element in the matrix.
     * @param matrix The input matrix
     * @return The cumulative product of all elements of the matrix
     */
    public static double elementwiseProduct(final double[][] matrix){
        
        if (matrix.length ==0 || matrix[0].length == 0)
            return 0; //Empty matrix.
        double out = 1;
        for (double[] matrix1 : matrix) {
            for (int ind1 = 0; ind1 < matrix1.length; ind1++) {
                out *= matrix1[ind1];
            }
        }
        return out;
    }
    
    /**
     * Calculate the cumulative product of every element in a row vector.
     * @param vector Input vector
     * @return Return the cumulative product of all elements of vector
     */
    public static double elementwiseProduct(final double[] vector){
        if (vector.length == 0)
            return 0;
        else{
            double out = 1;
            for (int ind0 = 0; ind0 < vector.length; ind0++)
                out*= vector[ind0];
            return out;
        }
            
    }
}
