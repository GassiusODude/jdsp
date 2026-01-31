package net.kcundercover.jdsp.signal;

import java.util.Arrays;
import java.util.stream.IntStream;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.util.FastMath;

public class FrequencyShift {
    // Optimization threshold: Parallelism usually pays off above 10k samples
    private static final int PARALLEL_THRESHOLD = 10_000;

    /**
     * @param inReal Input vector (real part)
     * @param inImag Input vector (imaginary part)
     * @param freqShift Frequency to shift by
     * @param sampleRate Sampling rate of the signal
     * @param outReal Output vector (real part)
     * @param outImag Output vector (imaginary part)
     * @throws IllegalArgumentException
    */
    public static void applyFrequencyShift(double[] inReal, double[] inImag, double freqShift, double sampleRate, double[] outReal, double[] outImag) {
        if (inReal.length != inImag.length) {
            throw new IllegalArgumentException ("Input real and imaginary needs to match in length");
        }
        if (outReal.length != outImag.length) {
            throw new IllegalArgumentException ("Input real and imaginary needs to match in length");
        }
        if (inReal.length != outImag.length) {
            throw new IllegalArgumentException ("Input real and output lengths needs to match in length");
        }
        System.arraycopy(inReal, 0, outReal, 0, inReal.length);
        System.arraycopy(inImag, 0, outImag, 0, inImag.length);

        applyFrequencyShiftInPlace(outReal, outImag, freqShift, sampleRate);
    }

    /**
     * Shifts the signal to baseband.
     * @param inRealeal      Real component (I)
     * @param inImag      Imaginary component (Q)
     * @param freqShift  The frequency shift by
     * @param sampleRate  The sampling rate of the input (Hz)
     */
    public static void applyFrequencyShiftInPlace(double[] inReal, double[] inImag, double freqShift, double sampleRate) {
        int n = inReal.length;

        double omega = 2.0 * FastMath.PI * freqShift / sampleRate;

        if (inReal.length < PARALLEL_THRESHOLD) {
            // Low overhead for small snippets
            computeShift(inReal, inImag, omega, 0, inReal.length);

        } else {
            // Use ForkJoinPool for large signals
            IntStream.range(0, inReal.length).parallel().forEach(ind -> {
                double phase = omega * ind;
                double cos = FastMath.cos(phase);
                double sin = FastMath.sin(phase);
                double real = inReal[ind];
                double imag = inImag[ind];
                inReal[ind] = real * cos - imag * sin;
                inImag[ind] = real * sin + imag * cos;
            });
        }
    }


    // Helper for serial execution
    private static void computeShift(double[] inReal, double[] inImag, double omega, int start, int end) {
        for (int ind = start; ind < end; ind++) {
            double phase = omega * ind;
            double cos = FastMath.cos(phase);
            double sin = FastMath.sin(phase);
            double r = inReal[ind];
            double j = inImag[ind];
            inReal[ind] = r * cos - j * sin;
            inImag[ind] = r * sin + j * cos;
        }
    }


}
