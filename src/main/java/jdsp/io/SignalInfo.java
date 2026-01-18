/**
 * Signal Information
 * Track information about how to intrepret data
 *
 * @author Keith Chow
 */
package net.kcundercover.jdsp.io;
import java.io.IOException;
import java.sql.Timestamp;

/** SignalInfo object */
public class SignalInfo extends FileInfo{
    /** Sampling rate of the signal */
    protected double samplingRate = 1.0;

    /** Center frequency of the signal */
    protected double centerFrequency = 0.0;

    /** Specify if signal is complex or real */
    protected boolean isComplex = false;

    /** Longitude attribute where recording is made */
    protected double longitude = Double.NaN;

    /** Latitude attribute where recording is made */
    protected double latitude = Double.NaN;

    /** Time of the recording */
    protected long timeStart = 0L;

    /**
     * Constructor for signal information
     *
     * @param filepath Path to the signal
     * @param fs Sampling rate associated to the signal
     * @param fc Center frequency of the recording
     * @param iC Whether the signal is complex IQ recording.
     * @throws IOException IO Exception for file not found
     */
    public SignalInfo(String filepath, double fs, double fc, boolean iC) throws IOException{
        super(filepath);
        // --------------------  error checking  ----------------------------
        assert fs > 0 : "Sampling rate should be positive";
        assert fc >= 0 : "Center frequency should be >= 0";

        // ----------------------  update properties  -----------------------
        samplingRate = fs;
        centerFrequency = fc;
        isComplex = iC;
    }

    /**
     * Constructor assuming real recording.
     * Center frequency is default to 0 and isComplex is false.
     * @param filepath Path to the signal.
     * @param fs Sampling rate of the signal.
     * @throws IOException IO Exception for file not found
     */
    public SignalInfo(String filepath, double fs) throws IOException{
        super(filepath);
        // --------------------  error checking  ----------------------------
        assert fs > 0 : "Sampling rate should be positive";

        // ----------------------  update properties  -----------------------
        samplingRate = fs;
        centerFrequency = 0;
        isComplex = false;
    }

    // ======================================================================
    //                            Get Methods
    // ======================================================================
    /**
     * Get the sampling rate information
     * @return Sampling rate.
     */
    public double getSamplingRate(){ return samplingRate;}

    /**
     * Get the center frequency.
     * @return Center frequency
     */
    public double getCenterFrequency(){ return centerFrequency;}

    /**
     * Check if the signal is complex
     * @return Whether the signal is complex
     */
    public boolean getComplex(){ return isComplex; }

    /**
     * Convert the specified time (long) to JDBC timestamp escape format.
     * @return Start time specified in JDBC timestamp escape format.
     */
    public String getTimeStamp(){
        Timestamp ts = new Timestamp(timeStart);
        return ts.toString();
    }

    // ======================================================================
    //                            Set Methods
    // ======================================================================
    /**
     * Set the sampling rate of the signal
     * @param fs Sampling rate.
     */
    public void setSamplingRate(double fs){
        assert fs > 0 : "Sampling should be positive value";
        samplingRate = fs;
    }

    /**
     * Set the center frequency of the signal
     * @param fc Center frequency
     */
    public void setCenterFrequency(double fc){
        assert fc >= 0 : "Center frequency should be >= 0";
        centerFrequency = fc;
    }

    /**
     * Set the start time
     * @param t New specification of start time.
     */
    public void setTimeStart(long t){
        timeStart = t;
    }
}