/** File information
 * This is intended to extract useful information from files.
 * 
 * @author Keith Chow
 */
package jdsp.io;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.LinkOption;
import java.io.IOException;
public class FileInfo{
    /** The filepath */
    protected String filepath = "";

    /** The size of the file in bytes */
    protected long fileSize = 0L;

    /** Constructor
    * @param filepath The path to the desired file.
    * @throws IOException IO Exception for file not found
    */
    public FileInfo(String filepath) throws IOException {
        // ---------------------  error checking  ---------------------------
        Path tmpPath = Paths.get(filepath);
        assert Files.isRegularFile(tmpPath, LinkOption.NOFOLLOW_LINKS) : 
            "File does not exist";

        // ----------------------  store info  ------------------------------
        this.filepath = tmpPath.toAbsolutePath().toString();
        fileSize = Files.size(tmpPath);
    }

    /** Get the file path
     * @return The file path.
     */
    public String getFilePath(){return filepath;}

    /** Get the size of the current file
     * @return Get filesize in number of bytes.
     */
    public long getFileSize(){ return fileSize; }
}
