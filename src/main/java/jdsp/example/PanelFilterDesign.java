package jdsp.example;
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
import jdsp.swing.Plot;
import jdsp.swing.PowerSpectralDensity;
import jdsp.filters.FilterDesign;
import jdsp.dataformat.DataObject;
import jdsp.math.Vector;
public class PanelFilterDesign extends JPanel {
    Plot plot = new Plot();
    PowerSpectralDensity psd = new PowerSpectralDensity();
    public static final String[] SUPPORTED_FILTERS = 
        {"BARTLETT", "HAMMING", "HANN"};
    
    
    JPanel sidePanel;
    JComboBox comboFilter = new JComboBox(SUPPORTED_FILTERS);
    JSlider slideBandwidth = new JSlider(1, 100);
    JTextField tfNumTaps = new JTextField("11");
    JButton buttonDesign;
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
