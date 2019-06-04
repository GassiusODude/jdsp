package jdsp.swing;
import java.util.ArrayList;
import java.awt.Graphics2D;
import jdsp.math.DTFT;

public class PowerSpectralDensity extends Plot{
    public final static long serialVersionUID = 0;
    private int numFreq = 128;
    public void setNumFreq(int nFreq){
        if (nFreq <= 0)
            throw new IllegalArgumentException(
                "Expecting a positive integer");
        numFreq = nFreq;
    }
    @Override
    public void draw(Graphics2D g2, ArrayList aList){
        float[] fArray = new float[aList.size()];
        for (int ind0=0; ind0<fArray.length; ind0++){
            fArray[ind0] = (float) aList.get(ind0);
        }
        float[] dtft = DTFT.discreteFourierTransform(fArray, numFreq);
        float[] magnArray = DTFT.magnitude(dtft);
        super.draw(g2, magnArray);
    }
}