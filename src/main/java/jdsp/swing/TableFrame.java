/**
 * The JFrame to show the TableUI.
 * @author GassiusODude
  */
package jdsp.swing;
import javax.swing.JFrame;
import jdsp.swing.TableUI;
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
     * Display information of the current state of the table.
     */
    public void display(){
        table.display();
    }
}