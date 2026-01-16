/**
 * The JFrame to show the TableUI.
 * @author GassiusODude
  */
package net.kcundercover.jdsp.swing;
import javax.swing.JFrame;
import net.kcundercover.jdsp.swing.TableUI;
public class TableFrame extends JFrame{
    TableUI table;
    /**
     * Table Frame
     */
    public TableFrame(){
        super("TableFrame");
        table = new TableUI();

        this.add(table);
        setSize(500,500);
        setVisible(true);
    }

    /**
     * Constructor
     * @param name Specify the name of the table
     */
    public TableFrame(String name){
        super(name);
        table = new TableUI();

        this.add(table);
        setSize(500,500);
        setVisible(true);
    }

    /**
     * Set the data format of the table, specifying column names and assuming string format.
     * @param columnNames List of column names.
     */
    public void setDataFormat(String[] columnNames){
        table.setDataFormat(columnNames);
    }

    /**
     * Set the data format of the table and data types
     * @param columnNames List of names for columns
     * @param columnTypes List of types (int, str, float, double, bool)
     */
    public void setDataFormat(String[] columnNames, String[] columnTypes){
        table.setDataFormat(columnNames, columnTypes);
    }

    /**
     * Add a row
     * @param msg Comma separated variable string
     * @param token Token separating elements.
     */
    public void addRow(String msg, String token){
        table.addRow(msg, token);
    }

    /**
     * Allow bulk proceess of adding rows.
     * @param msgs List of messages
     * @param tokens List of tokens, matching length of msgs
     * @throws Exception Errors on input
     */
    public void addRows(String[] msgs, String[] tokens) throws Exception {
        if (msgs.length == 0 || tokens.length == 0) {
            throw new Exception("Length of msgs and tokens should not be 0");
        }
        if (msgs.length != tokens.length){
            throw new Exception("Length of msgs and tokens do not match!");
        }
        for (int ind0=0; ind0<msgs.length; ind0++){
            table.addRow(msgs[ind0], tokens[ind0]);
        }
    }

    /**
     * Display information of the current state of the table.
     */
    public void display(){
        table.display();
    }

    public static void main(String[] args) {
        TableFrame tf = new TableFrame("Hello");
        tf.setVisible(true);

    }
}