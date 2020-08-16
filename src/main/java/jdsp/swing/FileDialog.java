package jdsp.swing;
import java.awt.Window;
import java.awt.Container;
import javax.swing.*;
import java.awt.Dialog.ModalityType;
public class FileDialog extends JDialog{
    private String filepath = "";
    private String dataType = "SHORT";
    public static final String[] DATA_TYPES =
        {"SHORT", "COMPLEX SHORT", "FLOAT32", "COMPLEX FLOAT32", "WAVE"};
    private JTextField tfFilePath = new JTextField();
    private JButton bSelect = new JButton("Select File");
    private JButton bConfirm = new JButton("Confirm");
    private JButton bCancel = new JButton("Cancel");
    private JComboBox cbDataType = new JComboBox(DATA_TYPES);

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

    public void show(){

    }

}