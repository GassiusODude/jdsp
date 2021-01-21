/**
 * @author Keith Chow
 */
package net.kcundercover.jdsp.swing;
import java.util.ArrayList;
import java.awt.Graphics2D;
import net.kcundercover.jdsp.math.DTFT;
import net.kcundercover.jdsp.math.ComplexInterleaved;

public class PowerSpectralDensity extends Plot{
    public final static long serialVersionUID = 0;
    private int nfft = 128;

    /**
     * Set the number of frequency points to create.
     * @param nFreq The number of frequency points.
     */
    public void setNfft(int nFreq){
        assert nFreq <= 0 : "Expecting a positive integer";
        nfft = nFreq;
    }

    @Override
    /**
     * Calculate and draw the power spectral density
     * @param g2 The graphics object to draw with
     * @param aList Array List of te input signal (assuming real)
     */
    public void draw(Graphics2D g2, ArrayList aList){
        float[] fArray = new float[aList.size()];
        for (int ind0=0; ind0<fArray.length; ind0++){
            fArray[ind0] = (float) aList.get(ind0);
        }
        float[] dtft = DTFT.discreteFourierTransform(fArray, nfft);
        float[] magnArray = ComplexInterleaved.magnitude(dtft);
        super.draw(g2, magnArray);
    }
}