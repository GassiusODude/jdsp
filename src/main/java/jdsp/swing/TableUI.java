/**
 * This TableUI will allow for the manipulation of data
 *      * Loading
 *      * Editing
 *      * Saving
 *
 * @author GassiusODude
 * @since June 20, 2019
 */
package net.kcundercover.jdsp.swing;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import net.kcundercover.jdsp.dataformat.DataObject;
import java.io.File;
import java.awt.Dimension;
import java.awt.BorderLayout;

public class TableUI extends JPanel{
    /** Commonly uses separator token */
    String[] TOKENS = {",", "$", "~", ".", "?", "#", "@", "+", " "};

    private JButton buttonReset = new JButton("Reset Data");
    private JButton buttonLoad = new JButton("Load CSV");
    private JButton buttonSave = new JButton("Save CSV");
    private JCheckBox checkboxFRH = new JCheckBox("First Row Header", true);
    private JComboBox comboToken = new JComboBox<String>(TOKENS);
    private JTable table;
    private JFileChooser jfc = new JFileChooser();
    private JPanel topPanel = new JPanel();
    private JScrollPane midPanel;
    private DataObject dObj = new DataObject("My Data");

    /** Constructor.  Setup and layout panel. */
    public TableUI(){
        super();

        // -------------------  setup top panel  ----------------------------
        topPanel.add(buttonReset);
        topPanel.add(buttonLoad);
        topPanel.add(buttonSave);
        topPanel.add(checkboxFRH);
        topPanel.add(comboToken);
        comboToken.setSelectedIndex(0);
        buttonReset.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){
                dObj.resetData();
            }
        });
        buttonLoad.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){
                int retVal = jfc.showOpenDialog(null);
                if (retVal == 0){
                    File f = jfc.getSelectedFile();
                    dObj.resetData();
                    dObj.loadCSV(f, checkboxFRH.isSelected(),
                        TOKENS[comboToken.getSelectedIndex()]);
                }
            }
        });
        buttonSave.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){
                int retVal = jfc.showSaveDialog(null);
                if (retVal == 0){
                    File f = jfc.getSelectedFile();
                    dObj.saveCSV(f, checkboxFRH.isSelected(),
                        TOKENS[comboToken.getSelectedIndex()]);
                }
            }
        });

        // --------------------  setup mid panel  ---------------------------
        table = new JTable(dObj);
        table.setFillsViewportHeight(true);
        midPanel = new JScrollPane(table);

        // --------------------  layout all panels  -------------------------
        this.setLayout(new BorderLayout());
        this.add(topPanel, BorderLayout.NORTH);
        this.add(midPanel, BorderLayout.CENTER);
    }

    /**
     * Set the data format with just the columns, assuming String format
     * @param columnNames The list of column names
     */
    public void setDataFormat(String[] columnNames){
        dObj.resetData();
        dObj.setDataFormat(columnNames);
    }

    /**
     * Set the Data format with the specified data types
     * @param columnNames List of column names
     * @param columnTypes List of data types (int, float, double, bool, str)
     */
    public void setDataFormat(String[] columnNames, String[] columnTypes){
        dObj.resetData();
        dObj.setDataFormat(columnNames, columnTypes);
    }

    /**
     * Add a row of data
     * @param msg String of comma separated values
     * @param token The separater, typically a comma.
     */
    public void addRow(String msg, String token){
        dObj.addObservation(msg, token);
    }

    /**
     * Display method for debug purposes.
     */
    public void display(){
        dObj.display();
    }
}