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
import java.io.FileWriter;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import java.lang.*;
public class DataObject extends DefaultTableModel{
    private static final long serialVersionUID = 1L;
    private String name = "";
    private ArrayList<ArrayList> features;
    private ArrayList<String> featureNames;
    private ArrayList<String> featureTypes;
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
        featureTypes = new ArrayList<String>();
    }

    /**
     * Constructor with specification of the feature names.  T
     * his assumes a string data format.
     * @param name Name of this DataObject
     * @param featureNames List of feature names.
     */
    public DataObject(String name, String[] featureNames){
        this.name = name;
        features = new ArrayList<ArrayList>();
        this.featureNames = new ArrayList<String>();
        featureTypes = new ArrayList<String>();
        setDataFormat(featureNames);
    }

    /**
     * Set the data format of feature names
     * @param featureNames List of names for the features.
     */
    public void setDataFormat(String[] featureNames){
        for (String tmpName : featureNames){
            addFeature(new String[0], tmpName);
        }
    }

    /**
     * Set the data format with feature names and types
     * @param featureNames List of names of features
     * @param featureTypes List of feature type.  Expecting (int, str, float, double, bool)
     */
    public void setDataFormat(String[] featureNames, String[] featureTypes){
        String tmpName;
        ArrayList myArray;
        for (int ind0 = 0; ind0 < featureNames.length; ind0++){
            // get feature name            
            tmpName = featureNames[ind0];

            // get type
            switch(featureTypes[ind0]){
                case "int":
                case "integer":
                    myArray = new ArrayList<Integer>();
                    break;
                case "float":
                    myArray = new ArrayList<Float>();
                    break;
                case "double":
                    myArray = new ArrayList<Double>();
                    break;
                case "bool":
                case "boolean":
                    myArray = new ArrayList<Boolean>();
                    break;
                default:
                    myArray = new ArrayList<String>();
            }

            // update the feature with name and type.
            addFeature(myArray, tmpName, featureTypes[ind0]);
        }
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
     * Add a feature with default assumption of string type
     * @param feature The ArrayList of the feature.
     * @param featureName The name of this feature
     */
    public void addFeature(ArrayList feature, String featureName){
        addFeature(feature, featureName, "str");
    }

    /**
     * Add a feature
     * @param feature The ArrayList of the feature.
     * @param featureName The name of this feature
     * @param type Feature type from (int, float, double, str, bool)
     */
    public void addFeature(ArrayList feature, String featureName, String type){
        // ------------------------  error checkingg  -----------------------
        if (numObs > 0 && numObs != feature.size()){
            throw new IllegalArgumentException(
                "Number of observations don't match");
        }

        features.add(feature);
        featureNames.add(featureName);
        featureTypes.add(type);
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
        featureTypes.add("float");
        numObs = floatData.length;
        this.fireTableStructureChanged();
    }

    /**
     * Add a feature provided an array
     * @param strData Feature in the form of a String[] vector
     * @param featureName Name of the feature.
     */
    public void addFeature(String[] strData, String featureName){
        // ------------------------  error checkingg  -----------------------
        if (numObs > 0 && numObs != strData.length){
            throw new IllegalArgumentException(
                "Number of observations don't match");
        }

        // convert to ArrayList
        ArrayList myArray = new ArrayList<String>(strData.length);
        for (int ind0 = 0; ind0 < strData.length; ind0++)
            myArray.add(strData[ind0]);
        features.add(myArray);
        featureNames.add(featureName);
        featureTypes.add("str");
        numObs = strData.length;
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
        featureTypes.add("int");
        numObs = intData.length;
        this.fireTableStructureChanged();
    }

    /**
     * Add a feature provided an array
     * @param shortData Feature in the form of a int[] vector
     * @param featureName Name of the feature.
     */
    public void addFeature(short[] shortData, String featureName){
        // ------------------------  error checkingg  -----------------------
        if (numObs > 0 && numObs != shortData.length){
            throw new IllegalArgumentException(
                "Number of observations don't match");
        }

        // convert to ArrayList
        ArrayList myArray = new ArrayList<Short>(shortData.length);
        for (int ind0 = 0; ind0 < shortData.length; ind0++)
            myArray.add(shortData[ind0]);
        features.add(myArray);
        featureNames.add(featureName);
        featureTypes.add("short");
        numObs = shortData.length;
        this.fireTableStructureChanged();
    }

    /** Add a feature provided an array
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
        featureTypes.add("double");
        numObs = doubleData.length;
        this.fireTableStructureChanged();
    }

    /** Add a feature provided an array
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
        featureTypes.add("bool");
        numObs = boolData.length;
        this.fireTableStructureChanged();
    }

    
    /** Add an observation
     * @param input CSV string
     * @param token Token representation separation.
     */
    public void addObservation(String input, String token){
        String[] elements;
        String featType, tmp, tmpNoSpace;
        ArrayList aList = new ArrayList();;

        // --------------------------  parse the elements  ------------------
        elements = input.split(token);
        if (elements.length != this.features.size())
            throw new RuntimeException("Number of features do not match");

        // verify
        try{
            for (int ind = 0; ind < elements.length; ind++){
                featType = featureTypes.get(ind);

                // get current string
                tmp = elements[ind];
                tmpNoSpace = tmp.replaceAll("\\s+", ""); // remove spaces

                switch (featType){
                    case "int":
                        aList.add(Integer.valueOf(tmpNoSpace));
                        break;
                    case "float":
                        aList.add(Float.valueOf(tmpNoSpace));
                        break;
                    case "double":
                        aList.add(Double.valueOf(tmpNoSpace));
                        break;
                    case "bool":
                        aList.add(Boolean.valueOf(tmpNoSpace));
                        break;
                    default:
                        // default as string
                        aList.add(tmp);
                }
            }
        }catch(Exception e){
            return;
        }
        // ------------------------  update  --------------------------------
        // passed check
        for (int ind = 0; ind < elements.length; ind++){
            features.get(ind).add(aList.get(ind));
        }
        this.numObs ++;
        this.fireTableStructureChanged();
    }

    /** Reset the data object */
    public void resetData(){
        numObs = 0;
        features.clear();
        featureNames.clear();
        fireTableStructureChanged();
    }

    /** Display the current state of the data object.*/
    public void display(){
        for (String feature : this.featureNames)
            System.out.println("Features = " + feature);
        System.out.println("Number observations = " + this.numObs);
    }

    /** Load a CSV file.  Assumes strings for each field.
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
                    // first line
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

    /** Save data to a CSV file
     * @param f File to save to
     * @param firstRowHeader Whether first row is the name of features
     * @param token Token to use to separate values.
     */
    public final void saveCSV(File f, boolean firstRowHeader, String token){
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(f));

            // ---------------------  write column names  -------------------
            if (firstRowHeader){
                for (String tmpS : featureNames){
                    writer.write(tmpS + token);
                }
                writer.write("\n");
            }

            // ---------------------  write features  -----------------------
            for (int obsInd=0; obsInd < this.numObs; obsInd++){
                for (int featInd=0; featInd<features.size(); featInd++){
                    writer.write(features.get(featInd).get(obsInd) + token);
                }
                writer.write("\n");
            }
            writer.close();
        }
        catch(java.io.FileNotFoundException fnfe){System.out.println("File not found.");}
        catch(java.io.IOException ioe){System.out.println("IO Exception");}
    }
}