
import jdsp.filters.Filter;
/**
 * Tests the convolution function
 * @author GassiusODude
 * @since May 27, 2019
 */
public class TestFilters{
    public static void compareVectors(float[] vector1, float[] vector2){
        if (vector1.length != vector2.length){
            throw new AssertionError("Lengths do not match");
        }
        boolean passed = true;
        for (int ind0=0; ind0<vector1.length; ind0++){
            if (vector1[ind0] != vector2[ind0]){
                passed = false;
            }
        }
        if (!passed){
            // ------------------  print vectors  ---------------------------
            for (int ind0=0; ind0<vector1.length; ind0++){
                System.out.print(vector1[ind0] + ",");
            }
            System.out.println();
            for (int ind0=0; ind0<vector1.length; ind0++){
                System.out.print(vector2[ind0] + ",");
            }
            System.out.println();

            // raise exception
            throw new AssertionError("Output does not match expected");
        }
    }

    public static void testConv(){
        float[] expected1 = {1.0f, 4.0f, 4.0f};
        float[] expected2 = {9.0f, 24.0f, 46.0f, 40.0f, 25.0f};
        float[] expected3 = {3.0f, 10.0f, 13.0f, 10.0f};
        float[] expected4 = {3.0f, 16.0f, 33.0f, 36.0f, 20.0f};
        float[] s1 = {1.0f, 2.0f};
        float[] s2 = {3.0f, 4.0f, 5.0f};
        float[] s3;

        // expecting {1, 4, 4}
        s3 = Filter.convolve(s1, s1);
        compareVectors(s3, expected1);
        System.out.println("Conv test1 passed");

        // expecting {9, 24, 46, 40, 25}
        s3 = Filter.convolve(s2, s2);
        compareVectors(s3, expected2);
        System.out.println("Conv test2 passed");

        // expecting {3, 10, 13, 10}
        s3 = Filter.convolve(s2, s1);
        compareVectors(s3, expected3);
        System.out.println("Conv test3 passed");

        s3 = Filter.convolve(expected3, s1);
        compareVectors(s3, expected4);
        System.out.println("Conv test4 passed");
    }
    public static void main(String[] args){
        testConv();
    }
}
