package ch.bullfin.android.ZipFileHandler;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created with IntelliJ IDEA.
 * User: Rahul
 * Date: 12/10/13
 * Time: 1:25 PM
 */
public class ZipFileHandler {
    private static final String LOG_TAG = "ZipFileHandler";

    private File inputFile = null;
    private URL inputUrl = null;
    private String outputPath = null;

    public ZipFileHandler(URL inputUrl, String outputPath) {
        this.inputUrl = inputUrl;
        this.outputPath = outputPath;
    }

    public ZipFileHandler(File inputFile, String outputPath) {
        this.inputFile = inputFile;
        this.outputPath = outputPath;
    }

    public void unZip() {
        ZipInputStream zipInputStream = null;
            try {
                if (inputUrl != null) {
                    zipInputStream = new ZipInputStream(inputUrl.openStream());
                } else if (inputFile != null) {
                    zipInputStream = new ZipInputStream(new FileInputStream(inputFile));
                }
            } catch (IOException e) { e.printStackTrace(); }

        if (zipInputStream != null){
            unZip(zipInputStream);
        }
    }

    private void unZip(ZipInputStream zipInputStream) {
        ZipEntry zipEntry;
        try {
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                if (zipEntry.isDirectory()) {
                    addDirectory(outputPath + File.separator + zipEntry.getName());
                } else {
                    FileOutputStream fileOutputStream = new FileOutputStream(outputPath);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = zipInputStream.read(buffer)) != -1){
                        fileOutputStream.write(buffer, 0, length);
                    }
                    zipInputStream.closeEntry();
                    fileOutputStream.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addDirectory(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.isDirectory()){
            Log.v(LOG_TAG, "Directory (" + dir.getAbsolutePath() + ((dir.mkdirs()) ? ") created successfully." : ") creation failed.") );
        }
    }
}
