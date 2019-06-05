package jdsp.math;

public class Matrix{
    /** Calculate the inverse of the matrix.
     *
     *  @param matrix The matrix that you want to find the inverse of.
     *  @param out The output to store the inverse of matrix. 
     */
    public static void inverse(final double[][] matrix, double[][] out){
        // -----------------------  error checking  -------------------------
        if (matrix.length != matrix[0].length){
            System.err.println(
                "jdsp.math.Matrix::inverse: Must be a square matrix");
            System.exit(1);
        }
        if (out.length != out[0].length){
            System.err.println(
                "jdsp.math.Matrix::inverse: output should be a square matrix");
            System.exit(1);
        }
        if (matrix.length != out.length){
            System.err.println("jdsp.math.Matrix::inverse: " + 
                "the size of the input and output should be the same.");
            System.exit(1);
        }

        // ---------------------- perform inverse  --------------------------
        // FIXME: not implemented yet.
    }
    
    /**
     *
     * @param matrix
     * @param out
     */
    public static void gaussJordan(final double[][] matrix, double[][] out){
        // -----------------------  error checking  -------------------------
        if (matrix.length == 0 || matrix.length != matrix[0].length ){
            System.err.println("jdsp.math.Matrix::gaussJordan: "+
                " Input matrix should be a square matrix");
            System.exit(1);
        }
        if (out.length ==0 || out.length != out[0].length){
            System.err.println("jdsp.math.Matrix::gaussJordan: "+
                "output should be square matrix");
            System.exit(1);
        }
        if (matrix.length != out.length){
            System.err.println("jdsp.math.Matrix::gaussJordan: "+
                " the size of the input and output should be the same.");
            System.exit(1);
        }
        
        // Perform function to get matrix inverse.
        double[][] temp = new double[matrix.length][matrix[0].length];
        for (int ind0 = 0; ind0 < matrix.length; ind0++){
            System.arraycopy(matrix[ind0], 0, temp[ind0], 0, temp[ind0].length);
            for (int ind1 = 0; ind1 < out.length; ind1++){
                if (ind0==ind1)
                    out[ind0][ind1] = 1;
                else
                    out[ind0][ind1] = 0;
            }
        }
        for (int ind0 = 0; ind0<temp.length; ind0++){
            if (temp[ind0][ind0] == 0){
                //Swap row with a lower row
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
                row_scale(out, ind0, 1 / temp[ind0][ind0]);
                row_scale(temp, ind0, 1 / temp[ind0][ind0]);
                for (int ind1 = 0; ind1 < temp.length; ind1++)
                    if (ind1!= ind0){
                        rowSubtract(out, ind1, ind0, temp[ind1][ind0]);
                        rowSubtract(temp, ind1, ind0, temp[ind1][ind0]);
                    }
            }
        }
    }
    
    public static void rowSwap(double[][] matrix, int i, int j){
        if (matrix.length > i && matrix.length > j && i >= 0 && j >= 0){
            if (i != j){
                double[] temp = new double[matrix[0].length];
                System.arraycopy(matrix[i], 0, temp,0, temp.length);
                System.arraycopy(matrix[j],0, matrix[i], 0, temp.length);
                System.arraycopy(temp,0, matrix[j],0, temp.length);
            }
        }
        else
            System.err.println("jdsp.math.Matrix::rowSwap: " + 
                    "Indices must be in range [0, numRows] in matrix");
    }
    /**
     * Subtract row j from row i.
     * @param matrix
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
            System.err.println("jdsp.math.Matrix::rowSubtract: " + 
                    "Indices must be within the number of rows of the matrix.");
            System.exit(1);
        }
    }
    /**
     * Scale a row by double value.
     * @param matrix
     * @param i The row of the matrix to scale
     * @param scale The value to scale the matrix by.
     */
    public static void row_scale(final double[][] matrix, int i, double scale){
       if (matrix.length >0 && i >= 0 && i < matrix.length){
           for (int ind0 = 0; ind0 < matrix[i].length; ind0++)
               matrix[i][ind0] *= scale;
       } 
       else{
           System.err.println("jdsp.math.Matrix::scaleRow: " + 
                   "index i must be within the constraints of the length of matrix.");
           System.exit(1);
       }
    }
    
    /**
     * Perform multiplication of two matrices.  matrix 1 should have number of
     * columns matching the number of rows in matrix 2.
     * @param mat1
     * @param mat2
     * @param out 
     */
    public static void matrixMultiply(final double[][] mat1, 
            final double[][] mat2, double[][] out){
        // --------------------------  error checking  ----------------------
        // check to make sure correct dimensions
        if (mat1.length==0 || mat1[0].length == 0 || mat2.length ==0 || 
                mat2[0].length == 0 || out.length == 0 || out[0].length == 0){
            System.err.println("jdsp.math.Matrix::matrixMultiply: " + 
                "One of the dimensions is 0.");
            System.exit(1);
        }
        if (mat1.length != out.length || mat1[0].length != mat2.length ||
                mat2[0].length != out[0].length){
            System.err.println("jdsp.math.Matrix::matrix_multipy: "+
                    "Incorrect dimensions cannot perform matrix multiply.");
            System.exit(1);
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
     * @param matrix
     * @return 
     */
    public static double determinant(final double[][] matrix){
        // perform error checking
        if (matrix.length == 0 || matrix[0].length != matrix.length){
            System.err.println("jdsp.math.Matrix::determinant: " +
                " error can only operate on square matrix");
            System.exit(1);
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
                            System.arraycopy(matrix[ind1], 1, temp[i++], 0, temp.length);
                    
                    sum+= mult * matrix[ind0][0] * determinant(temp);
                    mult = mult * -1;
                }   return sum;
        }
    }
    public static void display(final double[][] matrix){
        for (double[] matrix1 : matrix) {
            for (int ind1 = 0; ind1 < matrix1.length; ind1++) {
                System.out.print("\t" + matrix1[ind1]);
            }
            System.out.println();
        }
    }
    public static void display(final double[] matrix){
        for (int ind0 = 0; ind0 < matrix.length; ind0++)
            System.out.print("\t" + matrix[ind0]);
        System.out.println();
                    
    }
    /**
     * Calculate the cumulative product of every element in the matrix.
     * @param matrix
     * @return 
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
     * @param matrix
     * @return 
     */
    public static double elementwiseProduct(final double[] matrix){
        if (matrix.length == 0)
            return 0;
        else{
            double out = 1;
            for (int ind0 = 0; ind0 < matrix.length; ind0++)
                out*= matrix[ind0];
            return out;
        }
            
    }
}