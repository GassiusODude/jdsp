/**
 * The DataObject is intended to be used to help track information and
 * load in data.  It is used with Plots/Tables.
 * @author GassiusODude
 * @since June 2019  
 */
package jdsp.dataformat;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
public class DataObject extends DefaultTableModel{
    private static final long serialVersionUID = 1L;
    private String name = "";
    private ArrayList<ArrayList> features;
    private ArrayList<String> featureNames;
    private int numObs = 0;
    private boolean tableEditable = false;

    /**
     * Constructor for the DataObject
     * @param name The name of this DataObject
     */
    public DataObject(String name){
        this.name = name;
        features = new ArrayList<ArrayList>();
        featureNames = new ArrayList<String>();
    }
    // ========================== DefaultTableModel  ========================
    @Override
    public int getColumnCount(){
        return this.getNumFeatures();
    }
    @Override
    public int getRowCount(){
        return numObs;
    }

    @Override
    public String getColumnName(int col){
        return this.getFeatureName(col);
    }

    @Override
    public Object getValueAt(int row, int col){
        return this.features.get(col).get(row);
    }

    @Override
    public Class getColumnClass(int col){
        return features.get(col).get(0).getClass();
    }
    @Override
    public boolean isCellEditable(int row, int col){
        return tableEditable;
    }

    // =============================  set/get  ==============================

    /**
     * Specify that this model is editable.  This is intended for use
     * with JTables.
     * @param newVal The new value.
     */
    public void setEditable(boolean newVal){
        this.tableEditable = newVal;
    }
    /**
     * Return the name of the dataset
     * @return Name of the dataset
     */
    public String getName(){
        return name;
    }
    /**
     * Get the number of features;
     * @return The number of features
     */
    public int getNumFeatures(){
        return features.size();
    }

    /**
     * Get a particula feature based on index
     * @param index Index of the features of interest
     * @return The ArrayList of the feature of interest.
     */
    public ArrayList getFeature(int index){
        if (index >= 0 && index < features.size()){
            return features.get(index);
        }
        return null;
    }

    /**
     * Get the name of the feature given the index
     * @param index Index of feature
     * @return The name of the feature
     */
    public String getFeatureName(int index){
        if (index >= 0 && index < features.size()){
            return featureNames.get(index);
        }
        return "";
    }

    // ======================  manipulation of data  ========================
    /**
     * Add a feature
     * @param feature The ArrayList of the feature.
     * @param featureName The name of this feature
     */
    public void addFeature(ArrayList feature, String featureName){
        // ------------------------  error checkingg  -----------------------
        if (numObs > 0 && numObs != feature.size()){
            throw new IllegalArgumentException(
                "Number of observations don't match");
        }

        features.add(feature);
        featureNames.add(featureName);
        numObs = feature.size();
        this.fireTableStructureChanged();
    }

    /**
     * Add a feature provided an array
     * @param floatData Feature in the form of a float[] vector
     * @param featureName Name of the feature.
     */
    public void addFeature(float[] floatData, String featureName){
        // ------------------------  error checkingg  -----------------------
        if (numObs > 0 && numObs != floatData.length){
            throw new IllegalArgumentException(
                "Number of observations don't match");
        }

        // convert to ArrayList
        ArrayList myArray = new ArrayList<Float>(floatData.length);
        for (int ind0 = 0; ind0 < floatData.length; ind0++)
            myArray.add(floatData[ind0]);
        features.add(myArray);
        featureNames.add(featureName);
        numObs = floatData.length;
        this.fireTableStructureChanged();
    }
    /**
     * Add a feature provided an array
     * @param intData Feature in the form of a int[] vector
     * @param featureName Name of the feature.
     */
    public void addFeature(int[] intData, String featureName){
        // ------------------------  error checkingg  -----------------------
        if (numObs > 0 && numObs != intData.length){
            throw new IllegalArgumentException(
                "Number of observations don't match");
        }

        // convert to ArrayList
        ArrayList myArray = new ArrayList<Integer>(intData.length);
        for (int ind0 = 0; ind0 < intData.length; ind0++)
            myArray.add(intData[ind0]);
        features.add(myArray);
        featureNames.add(featureName);
        numObs = intData.length;
        this.fireTableStructureChanged();
    }

    /**
     * Add a feature provided an array
     * @param doubleData Feature in the form of a double[] vector
     * @param featureName Name of the feature.
     */
    public void addFeature(double[] doubleData, String featureName){
        // ------------------------  error checkingg  -----------------------
        if (numObs > 0 && numObs != doubleData.length){
            throw new IllegalArgumentException(
                "Number of observations don't match");
        }

        // convert to ArrayList
        ArrayList myArray = new ArrayList<Double>(doubleData.length);
        for (int ind0 = 0; ind0 < doubleData.length; ind0++)
            myArray.add(doubleData[ind0]);
        features.add(myArray);
        featureNames.add(featureName);
        numObs = doubleData.length;
        this.fireTableStructureChanged();
    }

    /**
     * Add a feature provided an array
     * @param boolData Feature in the form of a boolean[] vector
     * @param featureName Name of the feature.
     */
    public void addFeature(boolean[] boolData, String featureName){
        // ------------------------  error checkingg  -----------------------
        if (numObs > 0 && numObs != boolData.length){
            throw new IllegalArgumentException(
                "Number of observations don't match");
        }

        // convert to ArrayList
        ArrayList myArray = new ArrayList<Boolean>(boolData.length);
        for (int ind0 = 0; ind0 < boolData.length; ind0++)
            myArray.add(boolData[ind0]);
        features.add(myArray);
        featureNames.add(featureName);
        numObs = boolData.length;
        this.fireTableStructureChanged();
    }

    /** Reset the data object */
    public void resetData(){
        numObs = 0;
        features.clear();
        featureNames.clear();
        fireTableStructureChanged();
    }

    /**
     * Load a CSV file.  Assumes strings for each field.
     * @param f The file object to the CSV file.
     * @param firstRowHeader If true, will use first row as header.
     *      Otherwise label as "feature 1"
     * @param token The token to separate. Typically ","
     */
    public final void loadCSV(File f, boolean firstRowHeader, String token){
        BufferedReader br = null;
        try{
            // open file and get buffered stream
            FileInputStream fis = new FileInputStream(f);
            br = new BufferedReader(new InputStreamReader(fis));
            
            // allocate string to read one line of the file
            // allocate array of string for parsed output
            String strLine;
            String[] featureNames = new String[1];
            String[] elements;
            int numFeatures = 0;
            ArrayList[] aList = new ArrayList[1];
            while ((strLine = br.readLine()) != null)
            {
                // parse the file looking for comma separation
                elements = strLine.split(token);

                if (numFeatures == 0){
                    // if not initialized, set the number of features based on 
                    //first line
                    numFeatures = elements.length;
                    featureNames = new String[numFeatures];

                    aList = new ArrayList[numFeatures];
                    for (int ind0 = 0; ind0 < numFeatures; ind0++){
                        aList[ind0] = new ArrayList<String>();
                        if (firstRowHeader)
                            featureNames[ind0] = elements[ind0];
                        else
                            featureNames[ind0] = "feature " + ind0;
                    }
                    
                    if (firstRowHeader)
                        // recorded as header, do not add as a data point.
                        continue;
                }
                else
                    if (elements.length != numFeatures){
                        // TODO: handle missing data
                        // currently, ignore line if it does not match
                        System.out.println("Number of features do not match");
                        continue;
                    }

                // save current observation
                for (int indFeat = 0; indFeat < numFeatures; indFeat++){
                    aList[indFeat].add(elements[indFeat]);
                }
            }

            // save to object
            for (int indFeat = 0; indFeat < numFeatures; indFeat++){
                this.addFeature(aList[indFeat], featureNames[indFeat]);
            }
            this.fireTableStructureChanged();
        }
        catch(java.io.FileNotFoundException fnfe){System.out.println("File not found.");}
        catch(java.io.IOException ioe){System.out.println("IO Exception");}
        finally{
            try{
                if (br != null)
                    br.close();
            }catch(IOException ioe){

            }
        }
    }
}