/**
 * This TableUI will allow for the manipulation of data
 *      * Loading
 *      * Editing
 *      * Saving.
 * @author GassiusODude
 * @since June 20, 2019
 */
package jdsp.swing;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import jdsp.dataformat.DataObject;
import java.io.File;
import java.awt.Dimension;
import java.awt.BorderLayout;

public class TableUI extends JPanel{
    String[] TOKENS = {",", "$", "~", ".", "?", "#", "@", "+", " "};
    JButton buttonReset = new JButton("Reset Data");
    JButton buttonLoad = new JButton("Load CSV");
    JCheckBox checkboxFRH = new JCheckBox("First Row Header", true);
    JComboBox comboToken = new JComboBox<String>(TOKENS);
    JTable table;
    JFileChooser jfc = new JFileChooser();
    JPanel topPanel = new JPanel();
    JScrollPane midPanel;
    DataObject dObj = new DataObject("My Data");

    /**
     * Constructor.  Setup and layout panel.
     */
    public TableUI(){
        super();

        // -------------------  setup top panel  ----------------------------
        topPanel.add(buttonReset);
        topPanel.add(buttonLoad);
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
                    dObj.loadCSV(f, checkboxFRH.isSelected(),
                        TOKENS[comboToken.getSelectedIndex()]);
                }
            }
        });

        // --------------------  setup mid panel  ---------------------------
        table = new JTable(dObj);

        table.setFillsViewportHeight(true);

        //midPanel.add(table);
        midPanel = new JScrollPane(table);

        // --------------------  layout all panels  -------------------------
        this.setLayout(new BorderLayout());
        this.add(topPanel, BorderLayout.NORTH);
        this.add(midPanel, BorderLayout.CENTER);
    }
}