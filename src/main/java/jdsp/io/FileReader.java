/**
 * The FileReader will support providing I/O to reading in
 * raw data format as short/float and complex interleaved
 * formats.
 *
 * @author Keith Chow
 */
package net.kcundercover.jdsp.io;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.io.RandomAccessFile;
import java.io.FileNotFoundException;
import java.io.IOException;
import net.kcundercover.jdsp.dataformat.DataObject;
import net.kcundercover.jdsp.math.ComplexInterleaved;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.ConsoleHandler;

/**
 * The FileReader will access a RandomAccessFile to load data
 *
 */
public class FileReader {
    /** Logger */
    protected Logger logger;

    /** Name of the data types */
    public final static String[] DATA_TYPE = {
        "INT16", "COMPLEX INT16", "FLOAT32", "COMPLEX FLOAT32"};

    /** Byter per sample given data type */
    public final static int[] BYTES_PER_SAMPLE = {2, 2, 4, 4};

    /** Multiplier if complex */
    public final static int[] MULTIPLIER = {1, 2, 1, 2};

    private String filepath;
    private final RandomAccessFile myFile;
    private boolean bigEndian = false;
    private int dType = 0;


    /** Construct a FileReader.
     * This creates a RandomAccessFile to the specified filepath.
     * @param filepath Path to the file
     * @param dtype Data type index to the file.  @see DATA_TYPE
     * @param bigEndian Specify endianess
     * @throws FileNotFoundException If file not found.
     */
    public FileReader(final String filepath, int dtype, final boolean bigEndian)
            throws FileNotFoundException {
        assert (dtype < 0 || dtype > DATA_TYPE.length) : "dtype out of range";

        // store the parameters to the attributes
        this.filepath = filepath;
        myFile = new RandomAccessFile(filepath, "r");
        this.bigEndian = bigEndian;
        dType = dtype;

        // setup logger
        logger = Logger.getLogger("FileReader");
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.INFO);
        logger.addHandler(handler);
        logger.setLevel(Level.INFO);
    }

    /**
     * Set the log level of the logger
     * @param newLevel The log level to use.
     */
    public void setLogLevel(Level newLevel) {
        logger.setLevel(newLevel);
    }

    /** Load Signal
     *
     * Load the signal at the specified sample offset with the specified
     * number of samples.  Storing as DataObject is not efficient in terms
     * of storing things in ArrayList.  Consider loadSignalRawAsFloat
     * instead
     * @param sampleOffset Offset in samples from start of file
     * @param numSamples Number of samples to extract
     * @return DataObject to store the recovered samples
     * @see loadSignalRawAsFloat
     */
    public DataObject loadSignal(final int sampleOffset, final int numSamples) {
        // ------------------------  prepare variables  ---------------------
        // initialize output
        DataObject out = new DataObject(filepath);

        // number of bytes to load
        int numBytes = numSamples *
            BYTES_PER_SAMPLE[dType] * MULTIPLIER[dType];

        // number of values to use
        int numVals;

        try {
            // ---------------  skip to desired sample offset  --------------
            myFile.seek(sampleOffset *
                BYTES_PER_SAMPLE[dType] * MULTIPLIER[dType]);

            // get byte array
            byte[] tmp = new byte[numBytes];
            myFile.read(tmp);
            short[] sArray;
            float[] fArray;
            switch(DATA_TYPE[dType]){
                case "INT16":
                    sArray = new short[numSamples];
                    numVals = bytesToShort(tmp, sArray, bigEndian);
                    logger.fine("loadSignal() Real Int16");
                    out.addFeature(sArray, "Real");
                    break;

                case "COMPLEX INT16":
                    sArray = new short[numSamples * 2];
                    numVals = bytesToShort(tmp, sArray, bigEndian);
                    logger.fine("loadSignal() Complex Int16");
                    short[][] sRealImag = ComplexInterleaved.getRealImag(sArray);
                    out.addFeature(sRealImag[0], "Real");
                    out.addFeature(sRealImag[1], "Imaginary");
                    break;

                case "FLOAT32":
                    fArray = new float[numSamples];
                    numVals = byteToFloat(tmp, fArray, bigEndian);
                    logger.fine("loadSignal() Float32");
                    out.addFeature(fArray, "Real");
                    break;

                case "COMPLEX FLOAT32":
                    fArray = new float[numSamples * 2];
                    numVals = byteToFloat(tmp, fArray, bigEndian);
                    logger.fine("loadSignal() Complex Float32");
                    float[][] fRealImag = ComplexInterleaved.getRealImag(fArray);
                    out.addFeature(fRealImag[0], "Real");
                    out.addFeature(fRealImag[1], "Imaginary");
                    break;
            }
        }
        catch(IOException ioe) {
            logger.warning(ioe.toString());
        }

        return out;
    }

    /**
     * Load signal as raw floats
     * @param sampleOffset An offset of samples from start of file
     * @param numSamples Number of samples to load
     * @param isComplex If true, output will be doubled in sized, with real and imaginary values interleaved.
     * @return The float or complex interleaved float array
     */
    public float[] loadSignalRawFloat(final int sampleOffset, final int numSamples, boolean isComplex) {
        // number of bytes to load
        int numBytes = numSamples * BYTES_PER_SAMPLE[dType] * MULTIPLIER[dType];

        // get byte array
        byte[] tmp = new byte[numBytes];
        logger.fine("Reading in " + numSamples + " samples or " + numSamples * MULTIPLIER[dType] + " elements");

        try {
            // skip to the appropriate file location (sample offset from front of file)
            myFile.seek(sampleOffset *
                BYTES_PER_SAMPLE[dType] * MULTIPLIER[dType]);

            // read from file
            myFile.read(tmp);
        }
        catch(IOException ioe) {
            logger.warning(ioe.toString());
        }

        // convert the bytes to float
        float[] fArray = new float[numSamples * MULTIPLIER[dType]];
        int numVals = byteToFloat(tmp, fArray, bigEndian);
        if (numVals < numSamples) {
            float[] out = new float[numVals];
            System.arraycopy(fArray, 0, out, 0, numVals);
            return out;
        }
        // return the float array
        return fArray;
    }

/**
     * Load signal as raw shorts
     * @param sampleOffset An offset of samples from start of file
     * @param numSamples Number of samples to load
     * @param isComplex If true, output will be doubled in sized, with real and imaginary values interleaved.
     * @return The short or complex interleaved short array
     */
    public short[] loadSignalRawShort(final int sampleOffset, final int numSamples, boolean isComplex) {
        // number of bytes to load
        int numBytes = numSamples *
            BYTES_PER_SAMPLE[dType] * MULTIPLIER[dType];

        // get byte array
        byte[] tmp = new byte[numBytes];
        logger.fine("Reading in " + numSamples + " samples or " + numSamples * MULTIPLIER[dType] + " elements");

        try {
            // skip to the appropriate file location (sample offset from front of file)
            myFile.seek(sampleOffset *
                BYTES_PER_SAMPLE[dType] * MULTIPLIER[dType]);

            // read from file
            myFile.read(tmp);
        }
        catch(IOException ioe) {
            logger.warning(ioe.toString());
        }

        // convert the bytes to float
        short[] fArray = new short[numSamples * MULTIPLIER[dType]];
        bytesToShort(tmp, fArray, bigEndian);

        // return the short array
        return fArray;
    }


    /**
     * Load signal as raw floats.
     * @param sampleOffset The number of samples from the start of file
     * @param numSamples Number of samples to load.
     * @return Float samples
     */
    public float[] loadSignalRawAsFloat(final int sampleOffset, final int numSamples) {
        // number of bytes to load
        int bps = BYTES_PER_SAMPLE[dType];
        int numBytes = numSamples * bps * MULTIPLIER[dType];

        // number of values to use
        int numVals;

        try {
            // ---------------  skip to desired sample offset  --------------
            myFile.seek(sampleOffset *
                BYTES_PER_SAMPLE[dType] * MULTIPLIER[dType]);

            // ---------------------  get byte array  -----------------------
            byte[] tmp = new byte[numBytes];
            int nBytes = myFile.read(tmp);

            // ----------------  convert to bytes to float  -----------------
            short[] sArray;
            float[] fArray;
            float[] out;

            switch(DATA_TYPE[dType]){
                case "INT16":
                    logger.fine("loadSignal() Real Int16");

                    // convert bytes to short
                    sArray = new short[numSamples];
                    numVals = bytesToShort(tmp, sArray, bigEndian);

                    // convert short to floats
                    out = new float[numVals];
                    for (int ind = 0; ind < numVals; ind++)
                        out[ind] = (float) (sArray[ind] / 32767.);
                    return out;

                case "COMPLEX INT16":
                    logger.fine("loadSignal() Complex Int16");

                    // convert bytes to short
                    sArray = new short[numSamples * 2];
                    numVals = bytesToShort(tmp, sArray, bigEndian);
                    fArray = new float[numVals];
                    for (int ind=0; ind < numVals; ind ++)
                        fArray[ind] = (float) (sArray[ind] / 32767.0);
                    return fArray;

                case "FLOAT32":
                    logger.fine("loadSignal() Float32");
                    fArray = new float[numSamples];

                    numVals = byteToFloat(tmp, fArray, bigEndian);

                    return fArray;

                case "COMPLEX FLOAT32":
                    logger.fine("loadSignal() Complex Float32");

                    fArray = new float[numSamples * 2];
                    numVals = byteToFloat(tmp, fArray, bigEndian);
                    return fArray;

            }
        }
        catch (IOException ioe) {
            logger.warning(ioe.toString());
        }
        return null;

    }


    /**
     * Convert a byte array to short array.
     * Assumes default of 2 bytes per short
     *
     * @param bytes The input byte array
     * @param shortArray The short array to store the output
     * @param bigEndian Whether bytes are stored in big Endian format.
     * @return The number of samples
     */
    public static int bytesToShort(final byte[] bytes,
            final short[] shortArray, final boolean bigEndian){
        return bytesToShort(bytes, shortArray, bigEndian, 2);
    }

    /**
     * Convert a byte array to short array
     * @param bytes The input byte array
     * @param shortArray The short array to store the output
     * @param bigEndian Whether bytes are stored in big Endian format.
     * @param numBytes Number of bytes per value
     * @return The number of shorts updated in shortArray.
     */
    public static int bytesToShort(final byte[] bytes,
            final short[] shortArray, final boolean bigEndian, int numBytes) {
        int numOut = 0;
        final int nBytes = bytes.length;
        final int nShorts = shortArray.length;
        //final ByteBuffer byteBuffer = ByteBuffer.allocate(nBytes);
        numOut = nBytes / numBytes;
        numOut = Math.min(numOut, nShorts);

        switch(numBytes) {
            case 1:
                // single byte per short
                for (int ind0 = 0; ind0 < numOut; ind0++) {
                    shortArray[ind0] = (short) (bytes[ind0]);
                }
                break;
            case 2:
                if (bigEndian){
                    for (int ind0 = 0; ind0 < numOut; ind0++) {
                        shortArray[ind0] = (short) ((bytes[ind0*2]<<8) + bytes[ind0*2+1]);
                    }
                }
                else{
                    for (int ind0 = 0; ind0 < numOut; ind0++) {
                        shortArray[ind0] = (short) ((bytes[ind0*2+1]<<8) + bytes[ind0*2]);
                    }
                }
                break;
            default:
                throw new IllegalArgumentException("bytesToShort: numBytes should be 1 or 2");
        }
        return numOut;
    }

    /**
     * Convert a byte array to int array
     * @param bytes The input byte array
     * @param ints The int array to store the output
     * @param bigEndian Whether bytes are stored in big Endian format.
     * @param numBytes Number of bytes per value
     * @param signed Describes wheter bytes represent signed or unsigned ints
     * @return Number of samples read
     */
    public static int bytesToInt(final byte[] bytes, final int[] ints,
            final boolean bigEndian, int numBytes, boolean signed) {
        // -----------------------  error checking  -------------------------
        assert numBytes > 0 && numBytes <= 4 : "numBytes: Expecting {1,2,4}";


        int numOut = 0;
        final int nBytes = bytes.length;
        final int nInts = ints.length;
        //final ByteBuffer byteBuffer = ByteBuffer.allocate(nBytes);
        numOut = nBytes / numBytes;
        numOut = Math.min(numOut, nInts);
        int mask1st = 0xFF;
        int thresh = (int) Math.pow(2, numBytes * 8 - 1);
        int maxValue = (int) Math.pow(2, numBytes * 8);
        switch (numBytes) {
            case 1:
                // single byte per int
                for (int ind0 = 0; ind0 < numOut; ind0++){
                    ints[ind0] = (int) (bytes[ind0] & mask1st);
                    if (signed && ints[ind0] > thresh)
                        ints[ind0] -= maxValue;
                }
                break;

            case 2:
                if (bigEndian) {
                    for (int ind0 = 0; ind0 < numOut; ind0++) {
                        ints[ind0] = (int) (
                            ((bytes[ind0 * 2] & mask1st) << 8) +
                            (bytes[ind0 * 2 + 1] & 0xFF));
                        if (signed && ints[ind0] > thresh)
                            ints[ind0] -= maxValue;
                    }
                }
                else{
                    for (int ind0 = 0; ind0 < numOut; ind0++) {
                        ints[ind0] = (int) (
                            ((bytes[ind0 * 2 + 1] & mask1st) << 8) +
                            (bytes[ind0 * 2] & 0xFF));
                        if (signed && ints[ind0] > thresh)
                            ints[ind0] -= maxValue;
                    }
                }
                break;
            case 4:
                if (bigEndian) {
                    for (int ind0 = 0; ind0 < numOut; ind0++) {
                        ints[ind0] = (int) (
                            ((bytes[ind0 * 4] & mask1st) << 24) +
                            ((bytes[ind0 * 4 + 1] & 0xFF) << 16) +
                            ((bytes[ind0 * 4 + 2] & 0xFF) << 8) +
                            ((bytes[ind0 * 4 + 3] & 0xFF)));
                        if (signed && ints[ind0] > thresh)
                            ints[ind0] -= maxValue;
                    }
                }
                else {
                    for (int ind0 = 0; ind0 < numOut; ind0++) {
                        ints[ind0] = (int) (
                            ((bytes[ind0 * 4 + 3] & mask1st) << 24) +
                            ((bytes[ind0 * 4 + 2] & 0xFF) << 16) +
                            ((bytes[ind0 * 4 + 1] & 0xFF) << 8) +
                            (bytes[ind0 * 4] & 0xFF));
                        if (signed && ints[ind0] > thresh)
                            ints[ind0] -= maxValue;
                    }
                }
                break;

            default:
                throw new IllegalArgumentException("bytesToInt: numBytes should be 1, 2 or 4");
        }
        return numOut;
    }


    /** Bytes to float
     *
     * Handles conversion of bytes to float
     * @param bytes The array of bytes
     * @param floatArray The output float array
     * @param bigEndian Endianess of the bytes
     * @return Number of floats to expect in float array
     */
    public static int byteToFloat(final byte[] bytes,
            final float[] floatArray, final boolean bigEndian) {
        int tmpInt;
        int numOut = 0;
        final int nBytes = bytes.length;
        final int nFloats = floatArray.length;
        //final ByteBuffer byteBuffer = ByteBuffer.allocate(nBytes);
        numOut = Math.min(nBytes / 4, nFloats);

        if (bigEndian) {
            for (int ind0 = 0; ind0 < numOut; ind0++) {
                tmpInt = ((bytes[ind0 * 4] & 0xFF) << 24) +
                    ((bytes[ind0 * 4 + 1] & 0xFF) << 16) +
                    ((bytes[ind0 * 4 + 2] & 0xFF) << 8) +
                    (bytes[ind0 * 4 + 3] & 0xFF);
                floatArray[ind0] = Float.intBitsToFloat(tmpInt);
            }
        }
        else {
            for (int ind0 = 0; ind0 < numOut; ind0++) {
                tmpInt = ((bytes[ind0*4 + 3] & 0xFF) << 24) +
                    ((bytes[ind0*4 + 2] & 0xFF) << 16) +
                    ((bytes[ind0*4 + 1] & 0xFF) << 8) +
                    (bytes[ind0*4] & 0xFF);
                floatArray[ind0] = Float.intBitsToFloat(tmpInt);
            }
        }
        return numOut;

    }

    /** Get the specified file path
     *
     * @return File path to load from.
     */
    public String getFilePath() {
        return filepath;
    }

    /** Get whether the file is big endian.
     * @return Big Endianess check.
     */
    public boolean getBigEndian() {
        return bigEndian;
    }

    /** Get the index of the data type
     * @return Get the index to DATA_TYPE that represents the current type.
     */
    public int getDataTypeIndex() {
        return dType;
    }

    /** Get string description of data type
     * @return Get the string name of the selected data type
     */
    public String getDataType() {
        return DATA_TYPE[dType];
    }
}