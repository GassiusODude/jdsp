package net.kcundercover.jdsp.math;
import java.lang.Thread;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

/** Convolver object */
public class Convolver {
    /** Number of threads */
    int numThreads = 4;

    /** Executor Service */
    ExecutorService executorService;

    /**
     * Convolver constructor
     * @param numThreads create the convolver
     */
    public Convolver(int numThreads){
        this.numThreads = numThreads;
        executorService = Executors.newFixedThreadPool(numThreads);
    }

    /**
     * Convole method using Executor Service
     * @param in1 Input vector 1
     * @param in2 Input Vector 2
     * @return Convoled vected
     */
    public float[] convolve(final float[] in1, final float[] in2){

        float[] output = new float[in1.length + in2.length - 1];
        for (int ind0=0; ind0<output.length; ind0++){
            Runnable command = new ConvolverFloat(in1, in2, output, ind0);
            executorService.execute(command);
        }

        executorService.shutdown();
        while (!executorService.isTerminated()){ }
        return output;
    }
}

/** 
 * ConvolverFloat object
*/
class ConvolverFloat extends Thread {
    
    final float[] input1;
    final float[] input2;
    float[] output;
    int len1, len2, ind0;

    public ConvolverFloat(final float[] in1, final float[] in2, float[] out, int ind0) {
        input1 = in1;
        input2 = in2;
        output = out;
        len1 = input1.length;
        len2 = input2.length;
        this.ind0 = ind0;
    }

    @Override
    public void run(){
        int start;
        if (len2 >= len1){
            if (ind0 < len1){
                for (int ind1 = 0; ind1 < ind0 + 1; ind1++){
                    output[ind0] += input1[ind0 - ind1] * input2[ind1];
                }
            }
            else {
                start = (ind0 - len2 + 1 < 0) ? 0 : ind0 - len2 + 1;
                for (int ind1=start; ind1 < len1; ind1++){
                    float tmp = input2[ind0 - ind1];
                    output[ind0] += input1[ind1] * tmp;
                }
            }
        }
        else {
            if (ind0 < len2){
                for (int ind1 = 0; ind1 < ind0 + 1; ind1++){
                    output[ind0] += input2[ind0 - ind1] * input1[ind1];
                }
            }
            else{
                start = (ind0 - len1 + 1 < 0) ? 0 : ind0 - len1 + 1;
                for (int ind1=start; ind1 < len2; ind1++){
                    float tmp = input1[ind0 - ind1];
                    output[ind0] += input2[ind1] * tmp;
                }
            }
        }
    }
}