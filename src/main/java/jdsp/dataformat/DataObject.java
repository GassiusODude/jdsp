package jdsp.dataformat;

import java.util.ArrayList;
import java.security.InvalidParameterException;


public class DataObject {
    private String name = "";
    private ArrayList<ArrayList> features;
    private ArrayList<String> featureNames;
    private int numObs = 0;
    public DataObject(String name){
        this.name = name;
        features = new ArrayList<ArrayList>();
        featureNames = new ArrayList<String>();
    }
    public int getNumFeatures(){
        return features.size();
    }
    public ArrayList getFeature(int index){
        if (index >= 0 && index < features.size()){
            return features.get(index);
        }
        return null;
    }
    public String getFeatureName(int index){
        if (index >= 0 && index < features.size()){
            return featureNames.get(index);
        }
        return "";
    }
    public void addFeature(ArrayList feature, String featureName){
        // ------------------------  error checkingg  -----------------------
        if (numObs > 0 && numObs != feature.size()){
            throw new InvalidParameterException(
                "Number of observations don't match");
        }

        features.add(feature);
        featureNames.add(featureName);
    }

    public void addFeature(float[] floatData, String featureName){
        // ------------------------  error checkingg  -----------------------
        if (numObs > 0 && numObs != floatData.length){
            throw new InvalidParameterException(
                "Number of observations don't match");
        }


        // convert to ArrayList
        ArrayList myArray = new ArrayList<Float>(floatData.length);
        for (int ind0 = 0; ind0 < floatData.length; ind0++)
            myArray.add(floatData[ind0]);
        features.add(myArray);
        featureNames.add(featureName);
        numObs = floatData.length;

    }
    
}