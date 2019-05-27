package jdsp.filters;
/**
 * The Filter class will implement the following static methods:
 *      convolve()
 */
public class Filter{
    
    public static float[] convolve(float[] input1, float[] input2){
        int len1 = input1.length;
        int len2 = input2.length;
        int len3 = len1 + len2 - 1;
        int minLen = (len1<len2)?len1:len2;
        int start, end;
        float[] output = new float[len3];
        if (len2 >= len1){
            for (int ind0 = 0; ind0 < len1; ind0++){
                for (int ind1 = 0; ind1 < ind0+1; ind1++){
                    output[ind0] += input1[ind0 - ind1] * input2[ind1];
                }
            }
            for (int ind0 = len1; ind0 < len3; ind0++){
                start = (ind0-len2+1<0) ? 0 : ind0 - len2 + 1;
                for (int ind1=start; ind1 < len1; ind1++){
                    float tmp = input2[ind0-ind1];
                    output[ind0] += input1[ind1] * tmp;
                }
            }
        }
        else{
            for (int ind0 = 0; ind0 < len2; ind0++){
                for (int ind1 = 0; ind1 < ind0 + 1; ind1++){
                    output[ind0] += input2[ind0 - ind1] * input1[ind1];
                }
            }
            for (int ind0 = len2; ind0 < len3; ind0++){
                start = (ind0 - len1 + 1 < 0) ? 0 : ind0 - len1 + 1;
                for (int ind1=start; ind1 < len2; ind1++){
                    float tmp = input1[ind0-ind1];
                    output[ind0] += input2[ind1] * tmp;
                }
            }
        }

        return output;
    }
}
