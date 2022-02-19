/**
 * Audio support
 *
 * This class will provide support for reading and writing audio.
 * @author Keith Chow
 */
package net.kcundercover.jdsp.audio;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;
import javax.sound.sampled.AudioFormat.Encoding;
import net.kcundercover.jdsp.dataformat.DataObject;
import net.kcundercover.jdsp.io.FileReader;
public class Audio {
    public final static int MAX_BUFFER_SIZE = 200000;
    /**
     * Extract signal from the specified audio file
     * @param audioFile
     * @param dObj If provided, update with the channel information
     * @return The samples from the signal
     */
    public static int[][] extractSignal(String audioFile, DataObject dObj){
        int[][] tmp = {};
        try {
            // ---------------------  setup audio loading  ------------------
            File f = new File(audioFile);
            AudioFileFormat aff = AudioSystem.getAudioFileFormat(f);
            AudioFormat af;
            AudioInputStream ais = AudioSystem.getAudioInputStream(f);

            int numSamples = aff.getFrameLength();
            af = aff.getFormat();
            boolean bigEndian = af.isBigEndian(); //TODO: utilize
            int numChannels = af.getChannels();
            Encoding enc = af.getEncoding();
            int nBitsPerSample = af.getSampleSizeInBits();
            int nBytesPerSample = nBitsPerSample / 8;
            int frameSizeInBytes = numChannels * nBytesPerSample;
            int maxNumFrames = (MAX_BUFFER_SIZE / frameSizeInBytes);
            int bufferSize = maxNumFrames * frameSizeInBytes;

            // ----------------  print info on the wave file  ---------------
            if (true){
                System.out.println("Number samples = " + numSamples);
                System.out.println(
                    "Num bytes per sample = " + nBytesPerSample);
                System.out.println("Encoding = " + enc.toString());
                System.out.println("Big Endian = " + bigEndian);
            }

            // -----------------  allocate temporary buffers  ---------------
            byte[] byteBuffer = new byte[bufferSize];
            int[] intBuffer = new int[bufferSize / 2];
            int indexSample = 0, indexChan = 0;

            tmp = new int[numChannels][maxNumFrames];
            int sMod = 0;
            boolean signed = false;
            switch(enc.toString()){
                case "ALAW":
                    break;
                case "PCM_FLOAT":
                    break;
                case "PCM_SIGNED":
                    signed = true;
                    break;
                case "PCM_UNSIGNED":
                    if (nBytesPerSample == 2)
                        sMod = -32767;
                    else
                        sMod = -127;
                    break;
                case "ULAW":
                    break;
            }

            // -----------------------  read from file  ---------------------
            while(ais.available() > 0) {
                // read in next block of data
                ais.read(byteBuffer, 0, byteBuffer.length);

                // convert bytes to short value
                int numOut = FileReader.bytesToInt(
                    byteBuffer, intBuffer, bigEndian, nBytesPerSample, signed);

                // copy from buffer to the output array
                for (int ind0 = 0; ind0 < numOut; ind0++){
                    tmp[indexChan][indexSample] = (intBuffer[ind0] + sMod);

                    indexChan  = (indexChan + 1) % numChannels;
                    if (indexChan == 0)
                        indexSample += 1;
                    if (indexSample >= maxNumFrames)
                        break;
                }
                if (indexSample >= maxNumFrames)
                    break;
            }

            if (dObj != null) {
                // add to data object
                for (int chanInd = 0; chanInd < numChannels; chanInd++) {
                    dObj.addFeature(tmp[chanInd], "Channel "+ chanInd);
                }
            }
        }
        catch(UnsupportedAudioFileException uafe) {
            System.err.println(uafe.toString());
        }
        catch(IOException ioe) {
            System.err.print("Failed to load file." + ioe.toString());
        }

        return tmp;
    }

    /**
     * Encode using mu-law
     * @param in Input short
     * @return Encoded value
     */
    public static short encodeMuLaw(short in) {
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
    public static short decodeMuLaw(short s) {
        short mu = 255;
        short mult = (short)((s <= 0) ? -1 : 1);

        short output = (short) (mult * (Math.pow(1 + mu, (float) s) - 1) / mu);
        return output;
    }
}