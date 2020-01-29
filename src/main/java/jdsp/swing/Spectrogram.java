/**
 * Spectrogram
 * 
 */
package jdsp.swing;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import jdsp.math.DTFT;
import java.awt.Graphics2D;
import jdsp.math.Vector;
import jdsp.math.ComplexInterleaved;
public class Spectrogram extends Plot{
    private BufferedImage bImage;
    protected int window = 256;
    protected int nfft = 256;

    /** minMax value to scale spectrogram by */
    private float[] minMax = {-30.0f, 25.0f};
    private int COLOR_MAX_VALUE = 255*255;
    private int COLOR_MIN_VALUE = 0;

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

    /**
     * Set the window size to calculate the spectrum.
     * Reduce value to increase time resolution.
     * @param window Number of samples for single time slice
     */
    public void setWindow(int window){
        assert window > 1 : "Expecting a positive integer";
        this.window = window;
    }
    /**
     * Set the number of FFT points to use.
     * @param newVal Number of FFT points to calculate spectrum
     */
    public void setNfft(int newVal){
        assert newVal > 2 : "nfft should be > 2";
        nfft = newVal;
    }

    @Override
    public void draw(Graphics2D g2, ArrayList aList){
        // specify the width, height, and color scheme
        bImage = new BufferedImage(plotWidth, plotHeight,
            //BufferedImage.TYPE_3BYTE_BGR);
            BufferedImage.TYPE_BYTE_GRAY);

        int numWindows = aList.size() / window;
        if (numWindows == 0){
            // ----------- draw something since no windows  -----------------
            for (int ind0 = 0; ind0 < plotWidth; ind0++){
                for (int ind1 = 0; ind1 < plotHeight; ind1++){
                    bImage.setRGB(ind0, ind1, (ind0 + ind1)%COLOR_MAX_VALUE);
                }
            }
        }
        else{
            float[] currWindow = new float[window];
            float[] currMagn, fftOut;
            float valF;
            int val, colLoc;
            String tmpStr;

            for (int winIndex = 0; winIndex < numWindows; winIndex++){
                // -------------------  load current window  ----------------
                for (int sampleIndex = 0; sampleIndex < window; sampleIndex++){
                    tmpStr = aList.get(winIndex * window + sampleIndex).toString();
                    currWindow[sampleIndex] = Float.parseFloat(tmpStr);
                }

                // -----------------  calculate magn spectrum  --------------
                fftOut = DTFT.discreteFourierTransform(
                    currWindow, nfft);
                currMagn = ComplexInterleaved.magnitude(fftOut);

                // ---------------------  plot 
                for (int row = 0; row < plotHeight; row++)
                    for (int col = 0; col < plotWidth / numWindows; col++){
                        // calculate current column location
                        colLoc = plotWidth * winIndex / numWindows + col;

                        // get value for specified location in spectrogram
                        valF = (float)(10.0 * Math.log10(currMagn[
                            (int)(row * 1.0 / plotHeight * nfft)]));

                        // ---------  scale value to select color  ----------
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
            }
        }

        // ---------------------   draw image  ------------------------------
        g2.drawImage(bImage, marginX, marginY, null);
    }
}