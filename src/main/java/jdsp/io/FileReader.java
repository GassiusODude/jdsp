package jdsp.io;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.io.RandomAccessFile;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileReader{
    /** Name of the data types */
    public final static String[] DATA_TYPE = {
        "INT16", "COMPLEX INT16", "FLOAT32", "COMPLEX FLOAT32"};

    /** Byter per sample given data type */
    public final static int[] BYTES_PER_SAMPLE = {2, 2, 4, 4};
    
    /** Multiplier if complex */
    public final static int[] MULTIPLIER = {1, 2, 1, 2};
    public final RandomAccessFile myFile;
    public  boolean bigEndian = false;
    public int dType = 0;
    public ByteBuffer myBuffer;
    public FileReader(final String filepath, int dtype, final boolean bigEndian)
            throws FileNotFoundException {
        myFile = new RandomAccessFile(filepath, "r");
        this.bigEndian = bigEndian;
        assert (dtype < 0 || dtype > DATA_TYPE.length) : "dtype out of range";

        dType = dtype;
    }

    public ArrayList loadSignal(final int sampleOffset, final int numSamples) {
        ArrayList out = new ArrayList<Short>(numSamples);
        int numBytes = numSamples * BYTES_PER_SAMPLE[dType];
        try{
            myFile.seek(sampleOffset * BYTES_PER_SAMPLE[dType]);

            // get byte array
            byte[] tmp = new byte[numBytes];
            myFile.read(tmp);

            /*
            FileChannel fc = myFile.getChannel();
            myBuffer = ByteBuffer.allocate(numBytes);
            fc.read(myBuffer);
            myBuffer.flip();
            ShortBuffer sb = ShortBuffer.allocate(numSamples);
            sb = myBuffer.asShortBuffer();
            
            short[] sArray = sb.array();
            
            for (int ind0 = 0; ind0 < numSamples; ind0++)
                //out.add(myFile.readShort());
                
            /* */
            switch(DATA_TYPE[dType]){
                case "INT16":
                    short[] sArray = new short[numSamples];
                    bytes_to_short(tmp, sArray, bigEndian);
                    for (int ind0 = 0; ind0 < numSamples; ind0++)
                        out.add(sArray[ind0]);
                    break;
                case "FLOAT32":
                    float[] fArray = new float[numSamples];
                    bytes_to_float(tmp, fArray, bigEndian);
                    for (int ind0 = 0; ind0 < numSamples; ind0++)
                        out.add(fArray[ind0]);
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