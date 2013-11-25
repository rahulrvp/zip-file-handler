package ch.bullfin.android.ZipFileHandler;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Rahul
 * Date: 12/10/13
 * Time: 1:26 PM
 */
public class ZipFileHandlerActivity extends Activity {
    private static final String DOWNLOAD_URL = "https://dl.dropboxusercontent.com/u/72084171/sample.zip";
    private static final String SD_CARD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zip_file_handler_home);
    }

    public void unzipFromUrl(View view) {
        try {
            URL url = new URL(DOWNLOAD_URL);
            ZipFileHandler.getInstance().unZip(url, SD_CARD_PATH);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void unzipFromFile(View view) {
        ZipFileHandler.getInstance().unZip(new File(SD_CARD_PATH + "/sample.zip"), SD_CARD_PATH);
    }

    public void zipFilesIntoOne(View view) {
        ArrayList<File> inputFiles = new ArrayList<File>();
        inputFiles.add(new File(SD_CARD_PATH + File.separator + "sample.txt"));
        ZipFileHandler.getInstance().zip(inputFiles, SD_CARD_PATH + "/sample.zip");
    }
}