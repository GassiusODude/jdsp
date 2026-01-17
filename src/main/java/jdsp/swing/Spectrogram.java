/**
 * Spectrogram
 *
 * @author Keith Chow
 */
package net.kcundercover.jdsp.swing;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import net.kcundercover.jdsp.math.DTFT;
import java.awt.Graphics2D;
import net.kcundercover.jdsp.math.Vector;
import net.kcundercover.jdsp.math.ComplexInterleaved;

/** Spectrogram Plot */
public class Spectrogram extends Plot{
    /** The buffered image */
    private BufferedImage bImage;
    /** Window */
    protected int window = 1024;
    /** Number of FFT points */
    protected int nfft = 256;
    /** Sample Rate */
    private float sampleRate = 1.0f;
    /** Center frequency */
    private float centerFrequency = 0;
    /** Time Offset */
    private float timeOffset = 0;
    /** minMax value to scale spectrogram by */
    private float[] minMax = {-30.0f, 25.0f};
    
    private static final int COLOR_MAX_VALUE = 255*255;
    private static final int COLOR_MIN_VALUE = 0;

    /** Constructor */
    public Spectrogram(){
        setLabels("Time", "Freq", "Spectrogram");
        this.displayGrid = false;
    }

    /**
     * Set the min and max values to scale the color of the spectrogram.
     * There is a limited range of color values.
     * @param minValue Minimum value to clip values
     * @param maxValue Maximum value to clip values
     */
    public void setMinMax(float minValue, float maxValue){
        assert maxValue > minValue : "maxValue should be > minValue";
        minMax[0] = minValue;
        minMax[1] = maxValue;
    }

    /** Set the window size to calculate the spectrum.
     * Reduce value to increase time resolution.
     * @param window Number of samples for single time slice
     */
    public void setWindow(int window){
        assert window > 1 : "Expecting a positive integer";
        this.window = window;
    }
    /** Set the number of FFT points to use.
     * @param newVal Number of FFT points to calculate spectrum
     */
    public void setNfft(int newVal){
        assert newVal > 2 : "nfft should be > 2";
        nfft = newVal;
    }

    /** Set signal information
     *
     * These contribute to the displayed axis for time and frequency
     *
     * @param sampleRate Sample rate of the signal
     * @param centerFrequency Tuned frequency of the signal
     * @param timeOffset The time offset from the start of file
     */
    public void setSignalInfo(float sampleRate, float centerFrequency,
        float timeOffset){
        // ---------------------  error checking  ---------------------------
        assert sampleRate > 0 : "Sample Rate should be > 0";
        assert centerFrequency >= 0 : "Center Frequency should be >= 0";

        // --------------------  update properties  -------------------------
        this.sampleRate = sampleRate;
        this.centerFrequency = centerFrequency;
        this.timeOffset = timeOffset;
    }

    @Override
    public void drawData(final Graphics g) {
        final Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        loadLineType(g2);
        g2.setColor(COLOR_MAP[0 % COLOR_MAP.length]);

        if (data.getNumFeatures() == 1)
            // ---------------  send real signal to draw  -------------------
            draw(g2, data.getFeature(0));

        else if (data.getNumFeatures() > 1) {
            // -----------  interleave real/imaginary together  -------------
            int n = data.getRowCount();
            ArrayList a = new ArrayList(2 * n);
            ArrayList d1 = data.getFeature(0);
            ArrayList d2 = data.getFeature(1);
            // interleave real/imag
            for (int ind0 = 0; ind0 < n; ind0++){
                a.add(d1.get(ind0));
                a.add(d2.get(ind0));
            }

            // ---------------------  send to draw  -------------------------
            draw(g2, a);
        }

        if (floatData != null) {
            drawFloatData(g2);
        }
    }

    @Override
    public void draw(Graphics2D g2, ArrayList aList){
        // -------------------  initialize image  ---------------------------
        bImage = new BufferedImage(plotWidth, plotHeight,
            BufferedImage.TYPE_BYTE_GRAY);

        // ------------------ get number of windows  ------------------------
        int numWindows = aList.size() / window;
        if (numWindows == 0){
            // ----------- draw something since no windows  -----------------
            for (int ind0 = 0; ind0 < plotWidth; ind0++){
                for (int ind1 = 0; ind1 < plotHeight; ind1++){
                    bImage.setRGB(ind0, ind1, (ind0 + ind1)%COLOR_MAX_VALUE);
                }
            }
            g2.drawImage(bImage, marginX, marginY, null);
        }

        // ---------------------- available windows  ------------------------
        float[] currWindow = new float[window];
        float[] currMagn, fftOut;
        float valF;
        int val, colLoc;
        String tmpStr;
        boolean isComplex = data.getNumFeatures() > 1;
        for (int winIndex = 0; winIndex < numWindows; winIndex++){
            // -------------------  load current window  ----------------
            for (int sampleIndex = 0; sampleIndex < window; sampleIndex++){
                // FIXME: why convert to string and back
                tmpStr = aList.get(winIndex * window + sampleIndex).toString();
                currWindow[sampleIndex] = Float.parseFloat(tmpStr);
            }

            // -----------------  calculate magn spectrum  --------------
            if (!isComplex)
                fftOut = DTFT.discreteFourierTransform(currWindow, nfft);
            else
                fftOut = DTFT.discreteFourierTransformComplex(currWindow, nfft);
            currMagn = ComplexInterleaved.magnitude(fftOut);
            currMagn = DTFT.fftShift(currMagn);

            // ---------------------  update image  ---------------------
            for (int row = 0; row < plotHeight; row++)
                for (int col = 0; col < plotWidth / numWindows; col++){
                    // calculate current column location
                    colLoc = plotWidth * winIndex / numWindows + col;

                    // get value for specified location in spectrogram
                    valF = (float)(10.0 * Math.log10(currMagn[
                        (int)(row * 1.0 / plotHeight * nfft)]));

                    // ----------  scale value to select color  -------------
                    if (valF < minMax[0])
                        val = COLOR_MIN_VALUE;
                    else if (valF > minMax[1])
                        val = (int) COLOR_MAX_VALUE;
                    else
                        // normalize value in range and scale color
                        val = (int)((valF - minMax[0]) /
                            (minMax[1] - minMax[0]) * COLOR_MAX_VALUE);

                    // set the color of the current pixel
                    bImage.setRGB(colLoc, row, (int) val);
                }

            // ---------------------  update axis  --------------------------
            float[] newAxes = new float[4];
            if (isComplex){
                newAxes[0] = timeOffset;
                newAxes[1] = timeOffset + 0.5f * aList.size() / sampleRate;
                newAxes[2] = centerFrequency - sampleRate / 2;
                newAxes[3] = centerFrequency + sampleRate / 2;
            }
            else{
                newAxes[0] = timeOffset;
                newAxes[1] = timeOffset + aList.size() / sampleRate;
                newAxes[2] = 0;
                newAxes[3] = centerFrequency + sampleRate / 2;
            }

            setAxes(newAxes);
        }

        // ---------------------   draw image  ------------------------------
        g2.drawImage(bImage, marginX, marginY, null);
    }

    /**
     * Draw float data
     * @param g2 Graphics object
     */
    public void drawFloatData(Graphics2D g2){
        if (floatData == null)
            return;

        // -------------------  initialize image  ---------------------------
        bImage = new BufferedImage(plotWidth, plotHeight,
            BufferedImage.TYPE_BYTE_GRAY);

        // ------------------ get number of windows  ------------------------
        int numWindows = floatData.length / window;
        if (numWindows == 0){
            // ----------- draw something since no windows  -----------------
            for (int ind0 = 0; ind0 < plotWidth; ind0++){
                for (int ind1 = 0; ind1 < plotHeight; ind1++){
                    bImage.setRGB(ind0, ind1, (ind0 + ind1)%COLOR_MAX_VALUE);
                }
            }
            g2.drawImage(bImage, marginX, marginY, null);
        }

        // ---------------------- available windows  ------------------------
        float[] currWindow = new float[window];
        float[] currMagn, fftOut;
        float valF;
        int val, colLoc;
        String tmpStr;
        boolean isComplex = floatDataComplex;
        for (int winIndex = 0; winIndex < numWindows; winIndex++){
            // -------------------  load current window  ----------------
            System.arraycopy(floatData, winIndex * window, currWindow, 0, window);

            // -----------------  calculate magn spectrum  --------------
            if (!isComplex)
                fftOut = DTFT.discreteFourierTransform(currWindow, nfft);
            else
                fftOut = DTFT.discreteFourierTransformComplex(currWindow, nfft);
            currMagn = ComplexInterleaved.magnitude(fftOut);
            currMagn = DTFT.fftShift(currMagn);

            // ---------------------  update image  ---------------------
            for (int row = 0; row < plotHeight; row++)
                for (int col = 0; col < plotWidth / numWindows; col++){
                    // calculate current column location
                    colLoc = plotWidth * winIndex / numWindows + col;

                    // get value for specified location in spectrogram
                    valF = (float)(10.0 * Math.log10(currMagn[
                        (int)(row * 1.0 / plotHeight * nfft)]));

                    // ----------  scale value to select color  -------------
                    if (valF < minMax[0])
                        val = COLOR_MIN_VALUE;
                    else if (valF > minMax[1])
                        val = (int) COLOR_MAX_VALUE;
                    else
                        // normalize value in range and scale color
                        val = (int)((valF - minMax[0]) /
                            (minMax[1] - minMax[0]) * COLOR_MAX_VALUE);

                    // set the color of the current pixel
                    bImage.setRGB(colLoc, row, (int) val);
                }

            // ---------------------  update axis  --------------------------
            float[] newAxes = new float[4];
            if (isComplex){
                newAxes[0] = timeOffset;
                newAxes[1] = timeOffset + 0.5f * floatData.length / sampleRate;
                newAxes[2] = centerFrequency - sampleRate / 2;
                newAxes[3] = centerFrequency + sampleRate / 2;
            }
            else{
                newAxes[0] = timeOffset;
                newAxes[1] = timeOffset + floatData.length / sampleRate;
                newAxes[2] = 0;
                newAxes[3] = centerFrequency + sampleRate / 2;
            }

            setAxes(newAxes);
        }

        // ---------------------   draw image  ------------------------------
        g2.drawImage(bImage, marginX, marginY, null);
    }
}