package net.kcundercover.jdsp.swing;
import java.awt.Window;
import java.awt.Container;
import javax.swing.*;
import java.awt.Dialog.ModalityType;

/**
 * The FileDialog class
 */
public class FileDialog extends JDialog {
    /** The file path */
    private String filepath = "";

    /** The data type */
    private String dataType = "SHORT";

    /** Supported data types */
    public static final String[] DATA_TYPES =
        {"SHORT", "COMPLEX SHORT", "FLOAT32", "COMPLEX FLOAT32", "WAVE"};

    /** The text field to enter the file path */
    private JTextField tfFilePath = new JTextField();

    /** Button to select the file path*/
    private JButton bSelect = new JButton("Select File");

    /** Button to confirm the selection */
    private JButton bConfirm = new JButton("Confirm");

    /** Button to cancel file selection */
    private JButton bCancel = new JButton("Cancel");

    /** Combobox to select the data type */
    private JComboBox cbDataType = new JComboBox(DATA_TYPES);

    /** 
     * Constructor for FileDialog
     * @param win The parent window calling this dialog
     */
    public FileDialog(Window win){
        super(win, "File Select", ModalityType.APPLICATION_MODAL);
        tfFilePath.setEditable(false);

        JRootPane cp = getRootPane();
        cp.add(tfFilePath);
        cp.add(bSelect);
        cp.add(cbDataType);
        cp.add(bConfirm);
        cp.add(bCancel);
        this.dialogInit();
        setSize(300,300);
        setVisible(true);
    }


}