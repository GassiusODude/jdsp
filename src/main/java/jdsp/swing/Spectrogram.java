package jdsp.swing;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

import jdsp.math.DTFT;
import java.awt.Graphics2D;
import jdsp.math.Vector;
import jdsp.math.ComplexInterleaved;
public class Spectrogram extends Plot{
    BufferedImage bImage;
    int window = 256;
    public Spectrogram(){
        setLabels("Time", "Freq", "Spectrogram");
    }

    @Override
    public void draw(Graphics2D g2, ArrayList aList){
        bImage = new BufferedImage(this.plotWidth, this.plotHeight,
            BufferedImage.TYPE_BYTE_GRAY);
        int numWindows = aList.size() / window;
        if (numWindows == 0)
            for (int ind0 = 0; ind0 < plotWidth; ind0++){
                for (int ind1 = 0; ind1 < plotHeight; ind1++){
                    bImage.setRGB(ind0, ind1, (ind0 + ind1)%255);
                }
            }
        else{
            float[] currWindow = new float[window];
            float[] currMagn, fftOut;
            float[] minMax;
            float maxVal = 255;
            String s;
            for (int winIndex = 0; winIndex < numWindows; winIndex++){
                for (int sampleIndex = 0; sampleIndex < window; sampleIndex++){
                    s = aList.get(winIndex * window + sampleIndex).toString();
                    currWindow[sampleIndex] = Float.parseFloat(s);
                }
                fftOut = DTFT.discreteFourierTransform(
                    currWindow, plotHeight);
                currMagn = ComplexInterleaved.magnitude(fftOut);
                minMax = Vector.getMinMax(currMagn);
                for (int row = 0; row < plotHeight; row++)
                    for (int col = 0; col < plotWidth / numWindows; col++)
                        bImage.setRGB(plotWidth * winIndex / numWindows + col,
                            row, 
                            (int)((currMagn[row] - minMax[0]) / (minMax[1] - minMax[0]) * maxVal));
            }
        }
        g2.drawImage(bImage, marginX, marginY, null);
    }
}