package net.kcundercover.jdsp.signal;

import java.util.logging.Logger;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.apache.commons.math3.complex.Complex;

/**
 * Power spectral density
 */
public class PowerSpectralDensity {
    private static final Logger PSD_LOGGER = Logger.getLogger(PowerSpectralDensity.class.getName());
    public static boolean isPowerOfTwo(int n) {
        return n > 0 && (n & (n - 1)) == 0;
    }
    public static int nextPowerOfTwo(int n) {
        int highestOneBit = Integer.highestOneBit(n);
        if (n == highestOneBit) {
            return n; // Already a power of 2
        }
        return highestOneBit << 1;       // Go to the next one
    }

    /**
     * Calculate the PSD
     *
     * @param data Input data.  data[0] is the real part, data[1] is the imaginary part.
     * @param sampleRate The sample rate of the signal
     *
     */
    public static double[][] calculatePsdWelch(double[][] data, double sampleRate, int windowSize) {
        int nfft;
        if (isPowerOfTwo(windowSize)) {
            PSD_LOGGER.info(String.format(
                "Using Apache Common Math FFT requires windowSize(%d) to be a power of 2, setting nfft to match",
                windowSize));
            nfft = windowSize;
        } else {
            nfft = nextPowerOfTwo(windowSize);

        }

        int totalSamples = data[0].length;

        int overlap = windowSize / 2; // 50% overlap is standard
        int step = windowSize - overlap;

        FastFourierTransformer transformer = new FastFourierTransformer(DftNormalization.STANDARD);
        double[] averagedPSD = new double[nfft];
        int segmentCount = 0;

        // Normalization factor for the window (Hanning window power)
        double windowPowerSum = 0;
        double[] window = new double[windowSize];
        for (int i = 0; i < windowSize; i++) {
            window[i] = 0.5 * (1 - Math.cos(2 * Math.PI * i / (windowSize - 1)));
            windowPowerSum += window[i] * window[i];
        }

        // Process overlapping segments
        Complex[] segmentComplex = new Complex[nfft];

        for (int start = 0; start + windowSize <= totalSamples; start += step) {
            // Apply window to segment
            for (int i = 0; i < nfft; i++) {
                if (i < windowSize) {
                    segmentComplex[i] = new Complex(
                        data[0][start + i] * window[i],
                        data[1][start + i] * window[i]);
                } else {
                    segmentComplex[i] = Complex.ZERO;
                }
            }

            // Perform FFT
            Complex[] fftResult = transformer.transform(segmentComplex, TransformType.FORWARD);

            // Accumulate Magnitude Squared (Periodogram)
            for (int k = 0; k < averagedPSD.length; k++) {
                double magSq = fftResult[k].abs() * fftResult[k].abs();
                averagedPSD[k] += magSq;
            }
            segmentCount++;
        }
        double[][] out = new double[2][nfft];
        double scalingFactor = 1.0 / (segmentCount * sampleRate * windowPowerSum);
        for (int k = 0; k < averagedPSD.length; k++) {
            double freq = k * sampleRate / nfft - sampleRate / 2;
            double psdValue = averagedPSD[(k + nfft / 2) % nfft] * scalingFactor;

            // Convert to dB/Hz for standard PSD visualization
            double dbPsd = 10 * Math.log10(psdValue);

            out[0][k] = freq;
            out[1][k] = dbPsd;

        }
        return out;
    }
}
