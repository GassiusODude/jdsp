package jdsp.io;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.io.RandomAccessFile;
import java.io.FileNotFoundException;
import java.io.IOException;
import jdsp.dataformat.DataObject;
import jdsp.math.ComplexInterleaved;


public class FileReader{
    /** Name of the data types */
    public final static String[] DATA_TYPE = {
        "INT16", "COMPLEX INT16", "FLOAT32", "COMPLEX FLOAT32"};

    /** Byter per sample given data type */
    public final static int[] BYTES_PER_SAMPLE = {2, 2, 4, 4};
    
    /** Multiplier if complex */
    public final static int[] MULTIPLIER = {1, 2, 1, 2};

    public String filepath;
    public final RandomAccessFile myFile;
    public boolean bigEndian = false;
    public int dType = 0;
    public ByteBuffer myBuffer;

    /** Construct a FileReader.  
     * This creates a RandomAccessFile to the specified filepath.
     * @param filepath Path to the file
     * @param dtype Data type index to the file.  @see DATA_TYPE
     * @param bigEndian Specify endianess
     * @throws FileNotFoundException If file not found.
     */
    public FileReader(final String filepath, int dtype, final boolean bigEndian)
            throws FileNotFoundException {
        this.filepath = filepath;
        myFile = new RandomAccessFile(filepath, "r");
        this.bigEndian = bigEndian;
        assert (dtype < 0 || dtype > DATA_TYPE.length) : "dtype out of range";

        dType = dtype;
    }

    /** Load Signal
     * Load the signal
     * @param sampleOffset Offset in samples from start of file
     * @param numSamples Number of samples to extract
     * @return DataObject to store the recovered samples
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

        try{
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
                    numVals = bytes_to_short(tmp, sArray, bigEndian);

                    out.addFeature(sArray, "Real");
                    break;
                case "COMPLEX INT16":
                    sArray = new short[numSamples];
                    numVals = bytes_to_short(tmp, sArray, bigEndian);
                        
                    short[][] sRealImag = ComplexInterleaved.getRealImag(sArray);
                    out.addFeature(sRealImag[0], "Real");
                    out.addFeature(sRealImag[1], "Imaginary");
                    break;

                case "FLOAT32":
                    fArray = new float[numSamples];
                    numVals = bytes_to_float(tmp, fArray, bigEndian);
                    
                    out.addFeature(fArray, "Real");
                    break;
                case "COMPLEX FLOAT32":
                    fArray = new float[numSamples];
                    numVals = bytes_to_float(tmp, fArray, bigEndian);
                        
                    float[][] fRealImag = ComplexInterleaved.getRealImag(fArray);
                    out.addFeature(fRealImag[0], "Real");
                    out.addFeature(fRealImag[1], "Imaginary");
                    break;

            }

        }catch(IOException ioe){System.err.println(ioe.toString());}
        return out;
    }

    /**
     * Convert a byte array to short array
     * @param byteArray The input byte array
     * @param shortArray The short array to store the output
     * @param bigEndian Whether bytes are stored in big Endian format.
     * @return The number of shorts updated in shortArray.
     */
    public static int bytes_to_short(final byte[] byteArray, 
            final short[] shortArray, final boolean bigEndian) {
        int numOut = 0;
        final int nBytes = byteArray.length;
        final int nShorts = shortArray.length;
        final ByteBuffer byteBuffer = ByteBuffer.allocate(nBytes);
        numOut = Math.min(nBytes / 2, nShorts);

        if (bigEndian){
            for (int ind0 = 0; ind0 < numOut; ind0++)
                shortArray[ind0] = (short) ((byteArray[ind0*2]<<8) + byteArray[ind0*2+1]);
        }
        else{
            for (int ind0 = 0; ind0 < numOut; ind0++)
                shortArray[ind0] = (short) ((byteArray[ind0*2+1]<<8) + byteArray[ind0*2]);
        }
        return numOut;
    }

    /** Bytes to float
     * 
     * Handles conversion of bytes to float
     * @param byteArray The array of bytes
     * @param floatArray The output float array
     * @param bigEndian Endianess of the bytes
     * @return Number of floats to expect in float array
     */
    public static int bytes_to_float(final byte[] byteArray,
            final float[] floatArray, final boolean bigEndian){
        int tmpInt;
        int numOut = 0;
        final int nBytes = byteArray.length;
        final int nFloats = floatArray.length;
        final ByteBuffer byteBuffer = ByteBuffer.allocate(nBytes);
        numOut = Math.min(nBytes / 4, nFloats);

        if (bigEndian){
            for (int ind0 = 0; ind0 < numOut; ind0++){
                tmpInt = ((byteArray[ind0*4] & 0xFF) << 24) + 
                    ((byteArray[ind0*4 + 1] & 0xFF) << 16) + 
                    ((byteArray[ind0*4 + 2] & 0xFF) << 8) +
                    (byteArray[ind0*4+3] & 0xFF);
                floatArray[ind0] = Float.intBitsToFloat(tmpInt);
            }
        }
        else{
            for (int ind0 = 0; ind0 < numOut; ind0++){
                tmpInt = ((byteArray[ind0*4 + 3] & 0xFF) << 24) + 
                    ((byteArray[ind0*4 + 2] & 0xFF) << 16) + 
                    ((byteArray[ind0*4 + 1] & 0xFF) << 8) +
                    (byteArray[ind0*4] & 0xFF);
                floatArray[ind0] = Float.intBitsToFloat(tmpInt);
            }
        }
        return numOut;

    }
}