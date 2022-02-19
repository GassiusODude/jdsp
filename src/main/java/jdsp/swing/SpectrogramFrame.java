/**
 * Frame to display Spectrogram
 *
 * 2020-02-16
 *      Currently supports loading from float/short file.
 *
 * @author Keith Chow
 */
package net.kcundercover.jdsp.swing;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.*;
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

import net.kcundercover.jdsp.audio.Audio;
import net.kcundercover.jdsp.swing.Spectrogram;
import net.kcundercover.jdsp.io.FileReader;
import net.kcundercover.jdsp.dataformat.DataObject;
import net.kcundercover.jdsp.io.FileInfo;


public class SpectrogramFrame extends JFrame{

    public static final long serialVersionUID = 1L;
    JMenuBar menuBar = new JMenuBar();
    JMenu menuFile = new JMenu("File");
    JMenuItem menuItemLoad = new JMenuItem("Load Signal");
    JMenuItem menuItemExit = new JMenuItem("Exit");
    JMenu menuEdit = new JMenu("Edit");
    JMenuItem menuItemFs = new JMenuItem("Sample Rate");
    JMenuItem menuItemFc = new JMenuItem("Radio Frequency");
    JFileChooser jfc = new JFileChooser();
    Spectrogram specgram = new Spectrogram();
    JSlider slPosition = new JSlider();
    JSlider slNfft = new JSlider(JSlider.HORIZONTAL, 256, 8192, 256);
    JSlider slWindow = new JSlider(JSlider.HORIZONTAL, 1024, 32 * 1024, 1024);
    JPanel slidePanel;

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
    float[] floatData = null;
    int fileType = 0; // specify how file reader loads data

    /** Constructor for the SpectrogramFrame */
    public SpectrogramFrame() {
        super("Spectrogram Frame");
        // --------------------------  setup panel  -------------------------
        JPanel p = new JPanel();
        p.setLayout(new BorderLayout());

        // add spectrogram
        p.add(specgram, BorderLayout.CENTER);

        // add slider panel
        slidePanel = new JPanel();
        slidePanel.setLayout(new GridLayout(3, 2));
        setupSliders();
        p.add(slidePanel, BorderLayout.SOUTH);
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
        jfc.addChoosableFileFilter(
            new FileNameExtensionFilter("Wave", "wav"));
        jfc.setAcceptAllFileFilterUsed(true);

        setupMenu();
        setJMenuBar(menuBar);
        // -----------------  window listener for closing  ------------------
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event){
              System.exit(0);
            }
        });

        // -------------------  set size and show  --------------------------
        setSize(500, 500);
        setVisible(true);
    }

    /**
     * Check the file reader to determine if data is complex
     */
    public void setFloatDataComplex() {
        switch (fr.getDataType()) {
            case "INT16":
            case "FLOAT32":
                specgram.setFloatDataComplex(false);
                break;
            case "COMPLEX INT16":
            case "COMPLEX FLOAT32":
                specgram.setFloatDataComplex(true);
                break;
        }
    }
    /**Setup Sliders
     *
     * Setup the sliders and state change listeners
     */
    private void setupSliders() {
        // add slider to configure position
        slPosition.setOrientation(SwingConstants.HORIZONTAL);
        slPosition.setSnapToTicks(true);

        // configure NFFT slider
        slNfft.setPaintTicks(true);
        slNfft.setMajorTickSpacing(256*8);
        slNfft.setMinorTickSpacing(256);
        slNfft.setPaintLabels(true);
        slNfft.setSnapToTicks(true);

        // configure window slider
        slWindow.setPaintTicks(true);
        slWindow.setMajorTickSpacing(256*32);
        slWindow.setMinorTickSpacing(1024);
        slWindow.setPaintLabels(true);
        slWindow.setSnapToTicks(true);

        // set up labels and sliders
        Dimension dim = new Dimension(30, 10);
        JLabel labNfft = new JLabel("NFFT");
        labNfft.setPreferredSize(dim);
        JLabel labWin = new JLabel("Window");
        labWin.setPreferredSize(dim);
        JLabel labPos = new JLabel("Position");
        labPos.setPreferredSize(dim);
        slidePanel.add(labNfft);
        slidePanel.add(slNfft);
        slidePanel.add(labWin);
        slidePanel.add(slWindow);
        slidePanel.add(labPos);
        slidePanel.add(slPosition);

        // ----------------  setup change listeners  ------------------------
        slPosition.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e){

                JSlider source = (JSlider) e.getSource();
                if (fr != null){
                    // try updating the data with the new position
                    floatData = fr.loadSignalRawAsFloat(
                        slPosition.getValue() * bufferSize, bufferSize);

                    // update the time offset of the spectrogram
                    timeOffset = slPosition.getValue() * bufferSize / sampleRate;
                    specgram.setSignalInfo(sampleRate, centerFrequency, timeOffset);

                    // update data and UI
                    specgram.setFloatData(floatData);

                }
            }
        });

        slNfft.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e){
                JSlider source = (JSlider) e.getSource();
                nfft = slNfft.getValue();
                specgram.setNfft(nfft);

                // update the time offset of the spectrogram
                timeOffset = slPosition.getValue() * bufferSize / sampleRate;
                specgram.setSignalInfo(sampleRate, centerFrequency, timeOffset);

                // update data and UI
                specgram.repaint();
            }
        });

        slWindow.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e){
                JSlider source = (JSlider) e.getSource();
                window = source.getValue();
                specgram.setWindow(window);

                // update the time offset of the spectrogram
                timeOffset = slPosition.getValue() * bufferSize / sampleRate;
                specgram.setSignalInfo(sampleRate, centerFrequency, timeOffset);

                // update data and UI
                specgram.repaint();
            }
        });

    }

    /**Setup Menu
     *
     * Setup the menus and action listeners
     */
    private void setupMenu() {
        // ----------------------------  Setup menu  ------------------------
        menuFile.add(menuItemLoad);
        menuItemLoad.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                try{
                    int retVal = jfc.showOpenDialog(null);
                    if (retVal == 0) {
                        File f = jfc.getSelectedFile();
                        String filepath = f.getAbsolutePath();
                        String ext4 = filepath.substring(filepath.length() - 4);
                        String ext3 = filepath.substring(filepath.length() - 3);

                        // do something with the file
                        try {
                            fi = new FileInfo(filepath);
                            int numSamples=1;
                            if (ext4.equals("32cf")) {
                                numSamples = (int) (fi.getFileSize() / 8);
                                fileType = 3;
                            }
                            else if (ext4.equals("16ct")) {
                                numSamples = (int)(fi.getFileSize() / 4);
                                fileType = 1;
                            }
                            else if (ext3.equals("16t")) {
                                numSamples = (int)(fi.getFileSize() / 2);
                                fileType=0;
                            }
                            else if (ext3.equals("32f")) {
                                numSamples = (int)(fi.getFileSize() / 4);
                                fileType = 2;
                            }
                            else if (ext3.equals("wav")) {
                                int[][] intMat = Audio.extractSignal(filepath, null);
                                int nChan = intMat.length;
                                int sampPerChan = intMat[0].length;
                                int totalSamples = nChan * sampPerChan;
                                floatData = new float[totalSamples];
                                int fdIndex = 0;
                                for (int chanInd = 0; chanInd < nChan; chanInd++){
                                    for (int sInd = 0; sInd < sampPerChan; sInd++) {
                                        floatData[fdIndex++] = (float) intMat[chanInd][sInd];
                                    }
                                }
                                numSamples = data.getRowCount();
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

                            if (ext3 != "wav") {

                                fr = new FileReader(filepath, fileType, false);

                                floatData = fr.loadSignalRawAsFloat(slPosition.getValue() * bufferSize, bufferSize);
                                setFloatDataComplex();

                                specgram.setFloatData(floatData);
                            }

                            timeOffset = slPosition.getValue() * bufferSize / sampleRate;
                            specgram.setSignalInfo(sampleRate, centerFrequency, timeOffset);

                            specgram.repaint();
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
        menuFile.add(menuItemExit);
        menuItemExit.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){
                System.exit(0);
            }
        });
        menuBar.add(menuFile);

        // -------------------  Setup Edit menu  ----------------------------
        menuEdit.add(menuItemFs);
        menuItemFs.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){
                // launch an input dialog and query user
                String s = (String) JOptionPane.showInputDialog(
                    "Enter the sampling rate of the signal",
                    Float.toString(sampleRate));

                if (s == null)
                    // if cancelled
                    return;
                else {
                    // parse float from textfield
                    float f = Float.parseFloat(s);
                    if (f > 0){
                        sampleRate = f;
                        specgram.setSignalInfo(sampleRate, centerFrequency, timeOffset);
                    }
                }
            }
        });
        menuEdit.add(menuItemFc);
        menuItemFc.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){
                // launch dialog and query user
                String s = (String) JOptionPane.showInputDialog(
                    "Enter the radio frequency of the signal",
                    Float.toString(centerFrequency));
                if (s == null)
                    // cancelled
                    return;
                else{
                    // parse input
                    float f = Float.parseFloat(s);
                    if (f > 0){
                        centerFrequency= f;
                        specgram.setSignalInfo(sampleRate, centerFrequency, timeOffset);
                    }
                }
            }
        });
        menuBar.add(menuEdit);
    }

    public static void main(String[] args){
        javax.swing.SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                SpectrogramFrame specG = new SpectrogramFrame();
            }
        });
    }
}