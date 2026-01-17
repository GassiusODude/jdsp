package net.kcundercover.jdsp.example;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Dimension;
import net.kcundercover.jdsp.swing.Plot;
import net.kcundercover.jdsp.swing.PowerSpectralDensity;
import net.kcundercover.jdsp.filters.FilterDesign;
import net.kcundercover.jdsp.dataformat.DataObject;
import net.kcundercover.jdsp.math.Vector;

/** Panel for Filter design */
public class PanelFilterDesign extends JPanel {

    /** A plot object to show frequency response of the filter */
    private Plot plot = new Plot();

    /** Power spectral density Plot */
    private PowerSpectralDensity psd = new PowerSpectralDensity();

    /** The support filter design options */
    public static final String[] SUPPORTED_FILTERS =
        {"BARTLETT", "HAMMING", "HANN"};


    /** The side panel */
    private JPanel sidePanel;
    /** Combobox to select the supported filter design option */
    private JComboBox comboFilter = new JComboBox(SUPPORTED_FILTERS);

    /** Slider to specify the bandwidth of the filter */
    private JSlider slideBandwidth = new JSlider(1, 100);
    
    /** TextField to specify the number of taps for the designed FIR filter */
    private JTextField tfNumTaps = new JTextField("11");
    
    /** Button to execute the filter design on the current settings */
    private JButton buttonDesign;

    /** Constructor for the PanelFilterDesign */
    public PanelFilterDesign(){
        this.setLayout(new BorderLayout());
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(2,1));
        centerPanel.add(plot);
        centerPanel.add(psd);
        this.add(centerPanel, BorderLayout.CENTER);

        // ------------------  setup side panel  ----------------------------
        sidePanel = new JPanel();
        sidePanel.setPreferredSize(new Dimension(200, 100));
        sidePanel.setLayout(new GridLayout(5,2));
        sidePanel.add(new JLabel("Method"));
        sidePanel.add(comboFilter);

        sidePanel.add(new JLabel("Number Taps"));
        sidePanel.add(tfNumTaps);

        sidePanel.add(new JLabel("Bandwidth"));
        sidePanel.add(slideBandwidth);

        buttonDesign = new JButton("Design\nFilter");
        buttonDesign.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                String designMethod = comboFilter.getSelectedItem().toString();
                float normBandwidth =
                    (float) slideBandwidth.getValue() / 100.0f;
                int numTaps = Integer.parseInt(tfNumTaps.getText());
                float[] filter = FilterDesign.firWindowDesignF(
                    numTaps, designMethod, normBandwidth);
                DataObject dObj = new DataObject(designMethod);
                float[] axes = {0f, 1f, -0.5f, 1f};
                axes[1] = (float) numTaps;
                float[] minMax = Vector.getMinMax(filter);
                axes[2] = minMax[0];
                axes[3] = minMax[1];
                plot.setAxes(axes);
                dObj.addFeature(filter, designMethod);
                plot.setLabels("Time Domain", "", designMethod);
                plot.setData(dObj);

                float[] axes2 = {0f, 128f, 0f, 1f};
                psd.setAxes(axes2);
                psd.setLabels("Freq", "PSD", "Magnitude Response for " + designMethod);
                psd.setData(dObj);
            }
        });
        sidePanel.add(buttonDesign);
        this.add(sidePanel, BorderLayout.EAST);
    }
}
