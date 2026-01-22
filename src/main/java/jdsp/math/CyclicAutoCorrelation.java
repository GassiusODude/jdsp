package net.kcundercover.jdsp.math;
import org.apache.commons.math3.complex.Complex;
import java.util.stream.IntStream;

public class CyclicAutoCorrelation {
    /**
     * Calculate the CAF for the given alph and tau
     * @param real Real part of the input signal
     * @param imag Imaginary part of the input signal
     * @param fs Sampling rate of the input signal
     * @param tauRange Delays to apply
     * @param alphaRange Cyclic frequencies to apply
     * @return CAF grid
     */
    public static double[][] calculateCafGrid(double[] real, double[] imag, double fs, int[] tauRange, double[] alphaRange) {
        int N = real.length;
        int numAlphas = alphaRange.length;
        int numTaus = tauRange.length;
        double[][] cafGrid = new double[numAlphas][numTaus];

        // 1. Parallelize across the cyclic frequencies (Alpha)
        IntStream.range(0, numAlphas).parallel().forEach(aIdx -> {
            double alpha = alphaRange[aIdx];

            // 2. Precompute the frequency shift (exp term) for this alpha
            // This saves us N * numTaus complex multiplications
            double[] cosAlpha = new double[N];
            double[] sinAlpha = new double[N];
            for (int i = 0; i < N; i++) {
                double angle = -2.0 * Math.PI * alpha * (i / fs);
                cosAlpha[i] = Math.cos(angle);
                sinAlpha[i] = Math.sin(angle);
            }

            for (int tIdx = 0; tIdx < numTaus; tIdx++) {
                int tau = tauRange[tIdx];
                double sumR = 0;
                double sumI = 0;

                int start = tau >= 0 ? 0 : -tau;
                int end   = tau >= 0 ? N - tau : N;

                for (int i = start; i < end; i++) {
                    int sIdx = i + tau;
                    if (sIdx < 0) sIdx += N;

                    // 3. Inlined Complex Math: signal[i] * conj(signal[sIdx])
                    double r1 = real[i], i1 = imag[i];
                    double r2 = real[sIdx], i2 = -imag[sIdx]; // Conjugate

                    double prodR = r1 * r2 - i1 * i2;
                    double prodI = r1 * i2 + i1 * r2;

                    // 4. Multiply by precomputed exp(-j * 2pi * alpha * t)
                    double expR = cosAlpha[i];
                    double expI = sinAlpha[i];

                    sumR += (prodR * expR - prodI * expI);
                    sumI += (prodR * expI + prodI * expR);
                }

                // 5. Finalize mean and phase correction (abs removes need for complex division)
                double meanR = sumR / N;
                double meanI = sumI / N;

                // Magnitude is invariant to the phase correction rotation,
                // so we can often skip the phase_corr multiplication if only magnitude is needed.
                // If you need the complex result, keep it; for abs, skip to:
                cafGrid[aIdx][tIdx] = Math.sqrt(meanR * meanR + meanI * meanI);
            }
        });

        return cafGrid;
    }


}
