/*
 * Copyright 2011 Robert Theis
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lab117.exchangecam;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.lab117.exchangecam.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Installs the language data required for OCR, and initializes the OCR engine using a background 
 * thread.
 */
final class OcrInitAsyncTask extends AsyncTask<String, String, Boolean> {
  private static final String TAG = OcrInitAsyncTask.class.getSimpleName();


  private CaptureActivity activity;
  private Context context;
  private TessBaseAPI baseApi;
  private ProgressDialog indeterminateDialog;
  private int ocrEngineMode;

  /**
   * AsyncTask to asynchronously download data and initialize Tesseract.
   * 
   * @param activity
   *          The calling activity
   * @param baseApi
   *          API to the OCR engine
   * @param indeterminateDialog
   *          Dialog box with indeterminate progress indicator
   *          
   */
  OcrInitAsyncTask(CaptureActivity activity, TessBaseAPI baseApi, ProgressDialog indeterminateDialog, int ocrEngineMode) {
    this.activity = activity;
    this.context = activity.getBaseContext();
    this.baseApi = baseApi;
    this.indeterminateDialog = indeterminateDialog;
    this.ocrEngineMode = ocrEngineMode;
  }

  @Override
  protected void onPreExecute() {
    super.onPreExecute();
  }

  /**
   * In background thread, perform required setup, and request initialization of
   * the OCR engine.
   * 
   * @param params
   *          [0] Pathname for the directory for storing language data files to the SD card
   */
  protected Boolean doInBackground(String... params) {
    // Tesseract data filename: "eng.traineddata"
    String destinationFilenameBase = "eng.traineddata";

    // Check for, and create if necessary, folder to hold model data
    String destinationDirBase = params[0]; // The storage directory, minus the "tessdata" subdirectory
    File tessdataDir = new File(destinationDirBase + File.separator + "tessdata");
    if (!tessdataDir.exists() && !tessdataDir.mkdirs()) {
      //Log.e(TAG, "Couldn't make directory " + tessdataDir);
      return false;
    }

    // Create a reference to the file to save the download in
    File installFile = new File(tessdataDir, destinationFilenameBase);

    //Check for any (possibly half-unzipped) Tesseract data files that may be there
    //and delete them
    File tesseractTestFile = new File(tessdataDir, "eng.traineddata");
    
    // If language data files are not present, install them
    boolean installSuccess = false;
    if (!tesseractTestFile.exists()) {
      //Log.d(TAG, "Language data for eng not found in " + tessdataDir.toString());

      // Check assets for language data to install. If not present, download from Internet
      try {
        //Log.d(TAG, "Checking for language data (" + destinationFilenameBase
            //+ ".zip) in application assets...");
        // Check for a file like "eng.traineddata.zip" or "tesseract-ocr-3.01.eng.tar.zip"
        installSuccess = installFromAssets(destinationFilenameBase + ".zip", tessdataDir, 
            installFile);
      } catch (IOException e) {
        //Log.e(TAG, "IOException", e);
      } catch (Exception e) {
        //Log.e(TAG, "Got exception", e);
      }

      if (!installSuccess) {
        // File was not packaged in assets, so download it
        //Log.d(TAG, "Install language data failed.");
      }

    } else {
      //Log.d(TAG, "Language data for eng already installed in" 
          //+ tessdataDir.toString());
      installSuccess = true;
    }

    // If OSD data file is not present, download it
    File osdFile = new File(tessdataDir, CaptureActivity.OSD_FILENAME_BASE);
    boolean osdInstallSuccess = false;
    if (!osdFile.exists()) {
      // Check assets for language data to install. If not present, download from Internet
      try {
        // Check for, and delete, partially-downloaded OSD files
        String[] badFiles = { CaptureActivity.OSD_FILENAME + ".gz.download", 
            CaptureActivity.OSD_FILENAME + ".gz", CaptureActivity.OSD_FILENAME };
        for (String filename : badFiles) {
          File file = new File(tessdataDir, filename);
          if (file.exists()) {
            file.delete();
          }
        }
        
        //Log.d(TAG, "Checking for OSD data (" + CaptureActivity.OSD_FILENAME_BASE
          //  + ".zip) in application assets...");
        // Check for "osd.traineddata.zip"
        osdInstallSuccess = installFromAssets(CaptureActivity.OSD_FILENAME_BASE + ".zip", 
            tessdataDir, new File(CaptureActivity.OSD_FILENAME));
      } catch (IOException e) {
        //Log.e(TAG, "IOException", e);
      } catch (Exception e) {
        //Log.e(TAG, "Got exception", e);
      }

      if (!osdInstallSuccess) {
        //Log.d(TAG, "Intalling OSD Data File Failed.");
      }

    } else {
      //Log.d(TAG, "OSD file already installed in " + tessdataDir.toString());
      osdInstallSuccess = true;
    }
    
    

    // Initialize the OCR engine
    if (baseApi.init(destinationDirBase + File.separator, "eng", ocrEngineMode)) {
      return installSuccess && osdInstallSuccess;
    }
    return false;
  }


  /**
   * Install a file from application assets to device external storage.
   * 
   * @param sourceFilename
   *          File in assets to install
   * @param modelRoot
   *          Directory on SD card to install the file to
   * @param destinationFile
   *          File name for destination, excluding path
   * @return True if installZipFromAssets returns true
   * @throws IOException
   */
  private boolean installFromAssets(String sourceFilename, File modelRoot,
      File destinationFile) throws IOException {
    String extension = sourceFilename.substring(sourceFilename.lastIndexOf('.'), 
        sourceFilename.length());
    try {
      if (extension.equals(".zip")) {
        return installZipFromAssets(sourceFilename, modelRoot, destinationFile);
      } else {
        throw new IllegalArgumentException("Extension " + extension
            + " is unsupported.");
      }
    } catch (FileNotFoundException e) {
      //Log.d(TAG, sourceFilename + " not packaged in application assets.");
    }
    return false;
  }

  /**
   * Unzip the given Zip file, located in application assets, into the given
   * destination file.
   * 
   * @param sourceFilename
   *          Name of the file in assets
   * @param destinationDir
   *          Directory to save the destination file in
   * @param destinationFile
   *          File to unzip into, excluding path
   * @return
   * @throws IOException
   * @throws FileNotFoundException
   */
  private boolean installZipFromAssets(String sourceFilename,
      File destinationDir, File destinationFile) throws IOException,
      FileNotFoundException {
    // Attempt to open the zip archive
    publishProgress("Uncompressing data for eng ...", "0");
    ZipInputStream inputStream = new ZipInputStream(context.getAssets().open("data/" + sourceFilename));

    // Loop through all the files and folders in the zip archive (but there should just be one)
    for (ZipEntry entry = inputStream.getNextEntry(); entry != null; entry = inputStream
        .getNextEntry()) {
      destinationFile = new File(destinationDir, entry.getName());

      if (entry.isDirectory()) {
        destinationFile.mkdirs();
      } else {
        // Note getSize() returns -1 when the zipfile does not have the size set
        long zippedFileSize = entry.getSize();

        // Create a file output stream
        FileOutputStream outputStream = new FileOutputStream(destinationFile);
        final int BUFFER = 8192;

        // Buffer the output to the file
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream, BUFFER);
        int unzippedSize = 0;

        // Write the contents
        int count = 0;
        Integer percentComplete = 0;
        Integer percentCompleteLast = 0;
        byte[] data = new byte[BUFFER];
        while ((count = inputStream.read(data, 0, BUFFER)) != -1) {
          bufferedOutputStream.write(data, 0, count);
          unzippedSize += count;
          percentComplete = (int) ((unzippedSize / (long) zippedFileSize) * 100);
          if (percentComplete > percentCompleteLast) {
            publishProgress("Uncompressing data for eng..." + 
                percentComplete.toString(), "0");
            percentCompleteLast = percentComplete;
          }
        }
        bufferedOutputStream.close();
      }
      inputStream.closeEntry();
    }
    inputStream.close();
    return true;
  }

  /**
   * Update the dialog box with the latest incremental progress.
   * 
   * @param message
   *          [0] Text to be displayed
   * @param message
   *          [1] Numeric value for the progress
   */
  @Override
  protected void onProgressUpdate(String... message) {
    super.onProgressUpdate(message);
    int percentComplete = 0;

    percentComplete = Integer.parseInt(message[1]);
  }

  @Override
  protected void onPostExecute(Boolean result) {
    super.onPostExecute(result);
    
    try {
      indeterminateDialog.dismiss();
    } catch (IllegalArgumentException e) {
      // Catch "View not attached to window manager" error, and continue
    }

    if (result) {
      // Restart recognition
      activity.resumeOCR();
    } else {
      activity.showErrorMessage("Error", "Network is unreachable - cannot download language data. "
          + "Please enable network access and restart this app.", true);
    }
  }
}