package net.kcundercover.jdsp.signal;

import java.util.Arrays;
import java.util.logging.Logger;
import org.apache.commons.math3.util.FastMath;
import net.kcundercover.jdsp.signal.FrequencyShift;
import net.kcundercover.jdsp.filters.FilterD;


/**
 * Resampler object
 */
public final class Resampler {
    private static final Logger RESAMPLER_LOGGER = Logger.getLogger(Resampler.class.getName());

    /** The factor to upsample */
    private int upFactor;

    /** The factor to decimate */
    private int downFactor;

    /** The filter used between the upsampled signal and the decimation */
    private FilterD filterDouble;

    /**
     * Constructor
     * @param up Up factor
     * @param down Down factor
     */
    public Resampler(int up, int down) {

        // ------------------------------  error checking  ----------------------------------------
        if (up < 1) {
            throw new IllegalArgumentException("Up factor should be 1+");
        }
        if (down < 1) {
            throw new IllegalArgumentException("Down factor should be 1+");
        }

        // ----------------------------  store in properties  -------------------------------------
        upFactor = up;
        downFactor = down;
        filterDouble = new FilterD(11);
    }

    /**
     * Downconvert the signal
     *
     * This is a simple downconvert.  First shift the frequency of the signal to
     * baseband, then perform the reampling.  This is inefficient compared to
     * a polyphase resampler as it calculates the frequency shift at the current
     * rate, but a polyphase resampler only needs to calculate at the samples
     * kept at the lower rate.
     *
     * @param inReal Input (real part)
     * @param inImag Input (imaginary part)
     * @param freqOffset The frequency of the signal
     * @param sampleRate The Sample rate of the signal
     */
    public double[][] downConvert(double[] inReal, double[] inImag, double freqOffset, double sampleRate) {
        // shift in frequency
        double[] basebandReal = new double[inReal.length];
        double[] basebandImag = new double[inReal.length];

        FrequencyShift.applyFrequencyShift(
            inReal, inImag, -freqOffset, sampleRate,
            basebandReal, basebandImag);

        // resample
        double[][] out = resample(basebandReal, basebandImag);

        RESAMPLER_LOGGER.info(String.format(
            "Downconverted %d samples (%f samples per second) to %d samples (%f samples per second)",
            inReal.length, sampleRate,
            out[0].length, sampleRate * upFactor / downFactor
        ));

        return out;
    }

    /**
     * Resample the input signal
     *
     * @param bbReal The real part of the baseband signal
     * @param bbImag The imaginary part of the baseband signal
     * @return A [2xN] matrix vector the first list is the real part and 2nd is imaginary
     * */
    public double[][] resample(double[] bbReal, double[] bbImag) {
        // --------------------------------  error checking  --------------------------------------
        if (bbReal.length != bbImag.length) {
            throw new IllegalArgumentException(
                "Length of input (real and imaginary part) should match");
        }
        // ------------------------------  Upsample (zerostuff)  ----------------------------------
        double[] upR, upI;
        if (this.upFactor == 1) {
            upR = Arrays.copyOfRange(bbReal, 0, bbReal.length);
            upI = Arrays.copyOfRange(bbImag, 0, bbReal.length);

        } else {
            upR = new double[bbReal.length * upFactor];
            upI = new double[bbImag.length * upFactor];
            for (int ind = 0; ind < bbReal.length; ind++) {
                upR[ind * upFactor] = bbReal[ind];
                upI[ind * upFactor] = bbImag[ind];
            }
        }

        // ------------------------------------  LPF Filter  --------------------------------------
        filterDouble.designFilter(1 + 10 * upFactor,  0, "HANN", 1.0 / downFactor);
        double[][] upIQ = filterDouble.applyFilter(upR, upI);

        // Decimate
        double[][] out = new double[2][upIQ[0].length / downFactor];

        for (int ind0 = 0; ind0 < out[0].length; ind0++) {
            out[0][ind0] = upIQ[0][ind0 * downFactor];
            out[1][ind0] = upIQ[1][ind0 * downFactor];
        }

        return out;
    }

    /**
     * Shifts to baseband and decimates
     *
     * @param inR Input Real (I) array
     * @param inI Input Imaginary (Q) array
     * @param centerFreq Freq to move to 0Hz
     * @param fs Source sample rate
     * @param decim Decimation factor (integer)
     * @return A 2D array [2][N/decim] containing [outReal, outImag]
     */
    public static double[][] downConvertPolyphase(double[] inR, double[] inI, double centerFreq, double fs, int decim) {
        if (inR.length != inI.length) {
            throw new IllegalArgumentException("I/Q length mismatch");
        }

        int outLen = inR.length / decim;
        double[] outR = new double[outLen];
        double[] outI = new double[outLen];

        double omega = -2.0 * FastMath.PI * centerFreq / fs;

        // Simple Moving Average kernel size for anti-aliasing
        // For pro-grade results, use a FIR filter kernel instead of a simple average
        for (int i = 0; i < outLen; i++) {
            double sumR = 0;
            double sumI = 0;

            // Inner loop: Filter + Mix
            // We only calculate the samples we actually keep (Polyphase approach)
            for (int k = 0; k < decim; k++) {
                int idx = i * decim + k;
                double phase = omega * idx;
                double cos = FastMath.cos(phase);
                double sin = FastMath.sin(phase);

                // Complex Mixing
                double r = inR[idx] * cos - inI[idx] * sin;
                double j = inR[idx] * sin + inI[idx] * cos;

                sumR += r;
                sumI += j;
            }

            // Decimate & Scale
            outR[i] = sumR / decim;
            outI[i] = sumI / decim;
        }
        return new double[][]{outR, outI};
    }
}
