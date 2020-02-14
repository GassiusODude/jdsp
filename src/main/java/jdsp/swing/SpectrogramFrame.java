package jdsp.swing;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import jdsp.swing.Spectrogram;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import javax.swing.filechooser.FileNameExtensionFilter;
import jdsp.swing.Spectrogram;
import jdsp.io.FileReader;
import jdsp.dataformat.DataObject;
public class SpectrogramFrame extends JFrame{
    JMenuBar menuBar = new JMenuBar();
    JMenu menu = new JMenu("File");
    JMenuItem menuItemLoad = new JMenuItem("Load Signal");
    JMenuItem menuItemExit = new JMenuItem("Exit");

    JFileChooser jfc = new JFileChooser();
    Spectrogram specgram = new Spectrogram();
    public SpectrogramFrame(){
        super("Spectrogram Frame");
        add(specgram);
        jfc.addChoosableFileFilter(new FileNameExtensionFilter("Complex Float32", "32cf"));
        jfc.setAcceptAllFileFilterUsed(true);

        // ----------------------------  Setup menu  ------------------------
        menu.add(menuItemLoad);
        menuItemLoad.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent event){
                try{
                    int retVal = jfc.showOpenDialog(null);
                    if (retVal == 0){
                        File f = jfc.getSelectedFile();
                        // do something with the file
                        try{
                            FileReader fr = new FileReader(f.getAbsolutePath(), 3, false);
                            DataObject data = fr.loadSignal(0, 100000);
                            specgram.setData(data);
                        }catch(FileNotFoundException fnfe){System.err.println(fnfe);}
                    }
                }
                catch (RuntimeException e){
                    JOptionPane.showMessageDialog(null, e.toString(), "Warning",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        menu.add(menuItemExit);
        menuItemExit.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){
                System.exit(0);
            }
        });
        menuBar.add(menu);
        setJMenuBar(menuBar);        
        // -----------------  window listener for closing  ------------------
        this.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent event){
              System.exit(0);
            }
        });

        // -------------------  set size and show  --------------------------
        setSize(500,500);
        setVisible(true);
    }
    public static void main(String[] args) 
    { 

        javax.swing.SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                SpectrogramFrame specG = new SpectrogramFrame();
                
            }
        });
    } 
}