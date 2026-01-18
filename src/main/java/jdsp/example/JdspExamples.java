package net.kcundercover.jdsp.example;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import net.kcundercover.jdsp.dataformat.DataObject;
import javax.swing.JTabbedPane;
import net.kcundercover.jdsp.swing.Plot;
import net.kcundercover.jdsp.swing.TableUI;
import net.kcundercover.jdsp.swing.Spectrogram;
import net.kcundercover.jdsp.example.PanelFilterDesign;

/**
 * Main class housing the swing examples in a tabbed pane
 */
public class JdspExamples extends JTabbedPane{

    /**
     * The Constructor
     */
    public JdspExamples()
    {
        super();

        // table example
        // ------------------------------------------------------
        
        DataObject dataObject0 = new DataObject("Hello");
        float[] f1 = {1.0f, 2.0f};
        boolean[] b1 = {true, false};
        dataObject0.addFeature(f1, "Number");
        dataObject0.addFeature(b1, "Boolean");
        JTable table = new JTable(dataObject0);
        JScrollPane sp = new JScrollPane(table);
        this.add("Table", sp);

        // plot example
        // ------------------------------------------------------
        Plot myPlot = new Plot();
        float[] newAxes = {-0.f, 20f, -1f, 1.f};
        myPlot.setAxes(newAxes);
        myPlot.setMargin(60, 40);
        myPlot.setPlotSize(540, 340);
        myPlot.setLabels("Frequency", "Feat. 1", "Freq. vs Feature 1");
        myPlot.setMarker(" ", 5, 10);
        DataObject dataObject1 = new DataObject("Cosine");
        float[] cos = new float[20];
        float[] sin = new float[20];
        for (int ind0 = 0; ind0 < cos.length; ind0++){
            cos[ind0] = (float) Math.cos(2 * Math.PI * 0.05 * ind0);
            sin[ind0] = (float) Math.sin(2 * Math.PI * 0.05 * ind0);
        }
        dataObject1.addFeature(cos, "Real");
        dataObject1.addFeature(sin, "Imag");
        myPlot.setData(dataObject1);
        this.add("Plot", myPlot);

        // filter design
        // ------------------------------------------------------
        PanelFilterDesign fd = new PanelFilterDesign();
        this.add("Filter Design", fd);

        // table example
        // ------------------------------------------------------
        TableUI tui = new TableUI();
        this.add("TableUI", tui);

        // spectrogram example
        // ------------------------------------------------------
        DataObject dataObject2 = new DataObject("Cosine");
        float[] cos2 = new float[2000];
        for (int ind0 = 0; ind0 < cos2.length; ind0++){
            cos2[ind0] = (float) Math.cos(2 * Math.PI * 0.15 * ind0);
        }
        dataObject2.addFeature(cos2, "Cosine");
        Spectrogram specgram = new Spectrogram();
        this.add("Spectrogram", specgram);
        specgram.setData(dataObject2);

    }

    /** 
     * Main function
     * @param args Input arguments
     */
    public static void main(String[] args)
    {
        javax.swing.SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                javax.swing.JFrame newFrame = new javax.swing.JFrame("JdspExamples");
                newFrame.addWindowListener(new WindowAdapter(){
                    public void windowClosing(WindowEvent event){
                      System.exit(0);
                    }
                });
                JdspExamples examples = new JdspExamples();

                newFrame.add(examples);

                newFrame.setSize(600,400);
                newFrame.setVisible(true);
            }
        });
    }
}