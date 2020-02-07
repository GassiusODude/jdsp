package jdsp.io;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
public class FileReader{
    public static int bytes_to_short(byte[] byteArray, short[] shortArray){
        int numOut = 0;
        int nBytes = byteArray.length;
        int nShorts = shortArray.length;
        ByteBuffer byteBuffer = ByteBuffer.allocate(nBytes);
        numOut = Math.min(nBytes/2, nShorts);

        ShortBuffer sb = byteBuffer.get(byteArray, 0, 2*numOut).asShortBuffer();
        short[] tmp =  sb.array();
        //System.arraycopy(tmp, 0, shortArray, 0, numOut);
        return numOut;
    }
}