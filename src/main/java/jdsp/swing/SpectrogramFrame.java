/**
 * Frame to display Spectrogram
 * 
 * 2020-02-16
 *      Currently supports loading from float/short file.
 * 
 * TODO:
 * - Add UI support to specify sampling rate
 * - Add UI support to specify center frequency
 *  
 * @author Keith Chow
 */
package jdsp.swing;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.event.WindowEvent;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.plaf.basic.BasicSliderUI.ChangeHandler;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import jdsp.swing.Spectrogram;
import jdsp.io.FileReader;
import jdsp.dataformat.DataObject;

import jdsp.io.FileInfo;
public class SpectrogramFrame extends JFrame implements ChangeListener {
    JMenuBar menuBar = new JMenuBar();
    JMenu menu = new JMenu("File");
    JMenuItem menuItemLoad = new JMenuItem("Load Signal");
    JMenuItem menuItemExit = new JMenuItem("Exit");
    JFileChooser jfc = new JFileChooser();
    Spectrogram specgram = new Spectrogram();
    JSlider slPosition = new JSlider();

    // ----------------------  spectrogram settings  ------------------------
    int window = 1024;
    int nfft = 256;
    int bufferSize = 100000;
    float sampleRate = 1.0f;
    float centerFrequency = 0.0f;
    float timeOffset = 0.0f;

    FileReader fr;
    FileInfo fi;
    DataObject data;
    int fileType = 0; // specify how file reader loads data

    /** Constructor for the SpectrogramFrame */
    public SpectrogramFrame(){
        super("Spectrogram Frame");
        // --------------------------  setup panel  -------------------------
        JPanel p = new JPanel();
        p.setLayout(new BorderLayout());

        // add spectrogram
        p.add(specgram, BorderLayout.CENTER);

        // add slider to configure position
        slPosition.setOrientation(SwingConstants.HORIZONTAL);
        p.add(slPosition, BorderLayout.SOUTH);

        add(p);

        // ---------------  configure spectrogram fft and window  -----------
        specgram.setNfft(nfft);
        specgram.setWindow(window);

        // configure file chooser filter
        jfc.addChoosableFileFilter(
            new FileNameExtensionFilter("Complex Float32", "32cf"));
        jfc.addChoosableFileFilter(
            new FileNameExtensionFilter("Real Short", "16t"));
        jfc.addChoosableFileFilter(
            new FileNameExtensionFilter("Complex Short", "16ct"));
        jfc.addChoosableFileFilter(
            new FileNameExtensionFilter("Real Float", "32f"));
        jfc.setAcceptAllFileFilterUsed(true);
        slPosition.addChangeListener(this);

        // ----------------------------  Setup menu  ------------------------
        menu.add(menuItemLoad);
        menuItemLoad.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent event){
                try{
                    int retVal = jfc.showOpenDialog(null);
                    if (retVal == 0){
                        File f = jfc.getSelectedFile();
                        String filepath = f.getAbsolutePath();
                        String ext4 = filepath.substring(filepath.length() - 4);
                        String ext3 = filepath.substring(filepath.length() - 3);

                        // do something with the file
                        try{
                            fi = new FileInfo(filepath);
                            int numSamples=1;
                            if (ext4.equals("32cf")){
                                numSamples = (int) (fi.getFileSize() / 8);
                                fileType = 3;
                                System.out.println("Complex Float");
                            }
                            else if (ext4 == "16ct"){
                                numSamples = (int)(fi.getFileSize() / 4);
                                fileType = 1;
                                System.out.println("Complex Short");
                            }
                            else if (ext3 == "16t"){
                                numSamples = (int)(fi.getFileSize() / 2);
                                fileType=0;
                                System.out.println("Real Short");
                            }
                            else if (ext4 == "32f"){
                                numSamples = (int)(fi.getFileSize() / 4);
                                fileType = 2;
                                System.out.println("Real Float");
                            }
                            else{
                                System.out.println("ext4 = " + ext4);
                                System.out.println("ext3 = " + ext3);
                            }
                            // update position on slider
                            slPosition.setValue(0);
                            slPosition.setMinimum(0);
                            slPosition.setMaximum((int)(numSamples/bufferSize));
                            slPosition.setPaintTicks(true);
                            slPosition.setMajorTickSpacing((int)(numSamples / bufferSize / 5));
                            
                            fr = new FileReader(filepath, fileType, false);
                            data = fr.loadSignal(slPosition.getValue() * bufferSize, bufferSize);
                            specgram.setData(data);
                        }
                        catch(FileNotFoundException fnfe){System.err.println(fnfe);}
                        catch(IOException ioe){System.err.println(ioe);}
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
        addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent event){
              System.exit(0);
            }
        });

        // -------------------  set size and show  --------------------------
        setSize(500,500);
        setVisible(true);
    }

    /**
     * Handle the property change of the slider
     */
    public void stateChanged(ChangeEvent e){
        JSlider source = (JSlider) e.getSource();
        if (fr != null){
            // try updating the data with the new position
            data = fr.loadSignal(slPosition.getValue() * bufferSize, bufferSize);
            specgram.setData(data);

            // update the time offset of the spectrogram
            timeOffset = slPosition.getValue() * bufferSize / sampleRate;
            specgram.setSignalInfo(sampleRate, centerFrequency, timeOffset);

            // repaint
            this.repaint();
        }
    }

    public static void main(String[] args){ 
        javax.swing.SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                SpectrogramFrame specG = new SpectrogramFrame();
            }
        });
    } 
}