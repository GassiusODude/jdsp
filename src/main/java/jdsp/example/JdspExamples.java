package jdsp.example;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame; 
import javax.swing.JScrollPane; 
import javax.swing.JTable; 
import jdsp.dataformat.DataObject;
import javax.swing.JTabbedPane;
import jdsp.swing.Plot;
import jdsp.swing.TableUI;
import jdsp.swing.Spectrogram;
import jdsp.example.PanelFilterDesign;
public class JdspExamples extends JTabbedPane{
    // Table 
    JTable j; 
  
    // Constructor 
    public JdspExamples() 
    { 
        super();

        // --------------------  table example  -----------------------------
        DataObject dObj = new DataObject("Hello");
        float[] f1 = {1.0f, 2.0f};
        boolean[] b1 = {true, false};
        dObj.addFeature(f1, "Number");
        dObj.addFeature(b1, "Boolean");
        JTable table = new JTable(dObj);
        JScrollPane sp = new JScrollPane(table);
        this.add("Table", sp);

        // ----------------------  plot example  ----------------------------
        Plot p = new Plot();
        float[] newAxes = {-0.f, 20f, -1f, 1.f};
        p.setAxes(newAxes);
        p.setMargin(60, 40);
        p.setPlotSize(540, 340);
        p.setLabels("Frequency", "Feat. 1", "Freq. vs Feature 1");
        p.setMarker(" ", 5, 10);
        DataObject d = new DataObject("Cosine");
        float[] cos = new float[20];
        float[] sin = new float[20];
        for (int ind0 = 0; ind0 < cos.length; ind0++){
            cos[ind0] = (float) Math.cos(2 * Math.PI * 0.05 * ind0);
            sin[ind0] = (float) Math.sin(2 * Math.PI * 0.05 * ind0);
        }
        d.addFeature(cos, "Real");
        d.addFeature(sin, "Imag");
        p.setData(d);
        this.add("Plot", p);

        PanelFilterDesign fd = new PanelFilterDesign();
        this.add("Filter Design", fd);

        TableUI tui = new TableUI();
        this.add("TableUI", tui);

        DataObject d2 = new DataObject("Cosine");
        float[] cos2 = new float[2000];
        for (int ind0 = 0; ind0 < cos2.length; ind0++){
            cos2[ind0] = (float) Math.cos(2 * Math.PI * 0.15 * ind0);
        }
        d2.addFeature(cos2, "Cosine");
        Spectrogram specgram = new Spectrogram();
        this.add("Spectrogram", specgram);
        specgram.setData(d2);

    } 
  
    // Driver  method 
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