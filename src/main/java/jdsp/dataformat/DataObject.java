package jdsp.dataformat;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
public class DataObject extends AbstractTableModel{
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

    public int getColumnCount(){
        return this.getNumFeatures();
    }

    public int getRowCount(){
        return numObs;
    }

    public String getColumnName(int col){
        return this.getFeatureName(col);
    }
    public Object getValueAt(int row, int col){
        return this.features.get(col).get(row);
    }
    public Class getColumnClass(int col){
        return features.get(col).get(0).getClass();
    }
    public boolean isCellEditable(int row, int col){
        return tableEditable;
    }
    public void setEditable(boolean newVal){
        this.tableEditable = newVal;
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
        this.fireTableDataChanged();
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
        this.fireTableDataChanged();
    }
    /**
     * Add a feature provided an array
     * @param floatData Feature in the form of a float[] vector
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
        this.fireTableDataChanged();
    }

    /**
     * Add a feature provided an array
     * @param floatData Feature in the form of a float[] vector
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
        this.fireTableDataChanged();
    }

    /**
     * Add a feature provided an array
     * @param floatData Feature in the form of a float[] vector
     * @param featureName Name of the feature.
     */
    public void addFeature(boolean[] intData, String featureName){
        // ------------------------  error checkingg  -----------------------
        if (numObs > 0 && numObs != intData.length){
            throw new IllegalArgumentException(
                "Number of observations don't match");
        }

        // convert to ArrayList
        ArrayList myArray = new ArrayList<Boolean>(intData.length);
        for (int ind0 = 0; ind0 < intData.length; ind0++)
            myArray.add(intData[ind0]);
        features.add(myArray);
        featureNames.add(featureName);
        numObs = intData.length;
        this.fireTableDataChanged();
    }
}