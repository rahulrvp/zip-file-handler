package ch.bullfin.android.ZipFileHandler;

import android.util.Log;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created with IntelliJ IDEA.
 * User: Rahul
 * Date: 12/10/13
 * Time: 1:25 PM
 */
public class ZipFileHandler {
    private static final String LOG_TAG = "ZipFileHandler";

    private ArrayList<File> inputFiles = null;
    private File inputFile = null;
    private URL inputUrl = null;
    private String outputPath = null;

    private ZipFileHandler() {}
    private static ZipFileHandler instance = null;
    public static ZipFileHandler getInstance() {
        if (instance == null) {
            instance = new ZipFileHandler();
        }
        return instance;
    }

    public void unZip(URL inputUrl, String outputDirectoryPath) {
        this.inputUrl   = inputUrl;
        this.outputPath = outputDirectoryPath;
        unZip();
    }

    public void unZip(File inputFile, String outputDirectoryPath) {
        this.inputFile  = inputFile;
        this.outputPath = outputDirectoryPath;
        unZip();
    }

    private void unZip() {
        ZipInputStream zipInputStream = null;
            try {
                if (inputUrl != null) {
                    zipInputStream = new ZipInputStream(inputUrl.openStream());
                } else if (inputFile != null) {
                    zipInputStream = new ZipInputStream(new FileInputStream(inputFile));
                }
            } catch (IOException e) {
                Log.v(LOG_TAG, e.getMessage());
            }

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
                    FileOutputStream fileOutputStream = new FileOutputStream(outputPath + File.separator + zipEntry.getName());
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
            Log.v(LOG_TAG, e.getMessage());
        }
    }

    private void addDirectory(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.isDirectory()){
            Log.v(LOG_TAG, "Directory (" + dir.getAbsolutePath() + ((dir.mkdirs()) ? ") created successfully." : ") creation failed.") );
        }
    }

    public void zip(ArrayList<File> inputFiles, String outputFilePath) {
        this.inputFiles = inputFiles;
        this.outputPath = outputFilePath;
        File outputFile = new File(outputFilePath);
        if (!outputFile.exists()){
            try {
                if (!outputFile.createNewFile()) { return; }
            } catch (IOException e) {
                Log.v(LOG_TAG, e.getMessage());
            }
        }
        zip();
    }

    private void zip() {
        if (inputFiles != null && outputPath != null) {
            try {
                BufferedInputStream bufferedInputStream;
                FileOutputStream zipFileOutputStream = new FileOutputStream(outputPath);
                ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(zipFileOutputStream));
                byte[] buffer = new byte[1024];
                for (File inputFile : inputFiles) {
                    ZipEntry zipEntry = new ZipEntry(inputFile.getName());
                    zipOutputStream.putNextEntry(zipEntry);

                    FileInputStream fileInputStream = new FileInputStream(inputFile);
                    bufferedInputStream = new BufferedInputStream(fileInputStream, 1024);
                    int length;
                    while ((length = bufferedInputStream.read(buffer)) != -1) {
                        zipOutputStream.write(buffer, 0, length);
                    }
                }
                zipOutputStream.close();
            } catch (FileNotFoundException e) {
                Log.v(LOG_TAG, e.getMessage());
            } catch (IOException e) {
                Log.v(LOG_TAG, e.getMessage());
            }
        }
    }
}
