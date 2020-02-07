/**
 * Audio support
 * 
 * This class will provide support for reading and writing audio.
 */
package jdsp.audio;


import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;
import javax.sound.sampled.AudioFormat.Encoding;
import jdsp.dataformat.DataObject;

public class Audio{
    public final static int MAX_BUFFER_SIZE = 10000;
    public static short[] extractSignal(String audioFile, DataObject dObj){
        short[] output={};
        try{
            File f = new File(audioFile);
            AudioFileFormat aff = AudioSystem.getAudioFileFormat(f);
            AudioFormat af;
            int numSamples = aff.getFrameLength();
            output = new short[numSamples];

            af = aff.getFormat();
            boolean bigEndian = af.isBigEndian(); //TODO: utilize
            int numChannels = af.getChannels();
            Encoding enc = af.getEncoding();
            int nBitsPerSample = af.getSampleSizeInBits();
            int nBytesPerSample = nBitsPerSample / 8;
            int frameSizeInBytes = numChannels * nBytesPerSample;
            
            int maxNumFrames = (MAX_BUFFER_SIZE / frameSizeInBytes);
            int bufferSize = maxNumFrames * frameSizeInBytes;

            byte[] byteBuffer = new byte[bufferSize];

            switch(enc.toString()){
                case "ALAW":
                    break;
                case "PCM_FLOAT":
                    break;
                case "PCM_SIGNED":
                    break;
                case "PCM_UNSIGNED":
                    break;
                case "ULAW":
                    break;
            }

            // -----------------------  read from file  ---------------------
            short[][] tmp = new short[numChannels][numSamples];
            AudioInputStream ais = AudioSystem.getAudioInputStream(f);

            while(ais.available() > 0){
                // read in next block of data
                ais.read(byteBuffer, 0, byteBuffer.length);

                // TODO: convert bytes to short value

            }

            // add to data object
            for (int chanInd = 0; chanInd < numChannels; chanInd++)
                dObj.addFeature(tmp[chanInd], "Channel "+ chanInd);

        }
        catch(UnsupportedAudioFileException uafe){
            System.err.println(uafe.toString());
        }
        catch(IOException ioe){
            System.err.print("Failed to load file." + ioe.toString());
        }

        return output;
    }

    /**
     * Encode using mu-law
     * @param in Input short
     * @return Encoded value
     */
    public static short encodeMuLaw(short in){
        short mu = 255;
        
        short mult = (short)((in <= 0) ? -1 : 1);
        short output = (short) Math.abs(in);
        output = (short) (mult * Math.log(1 + mu * output) / Math.log(1+mu));
        return output;
    }

    /**
     * Decode mu-law encoding
     * @param s Short encoded value
     * @return Decoded value
     */
    public static short decodeMuLaw(short s){
        short mu = 255;
        short mult = (short)((s <= 0) ? -1 : 1);
        
        short output = (short) (mult * (Math.pow(1 + mu, (float) s) - 1) / mu);
        return output;
    }
}