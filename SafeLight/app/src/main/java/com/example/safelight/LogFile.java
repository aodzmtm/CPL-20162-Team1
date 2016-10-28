package com.example.safelight;

import android.os.Environment;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by user on 2015-05-08.
 */
public class LogFile {


    final String TAG = getClass().getSimpleName();
    String filename;
    String downLoadPath = Environment.getExternalStorageDirectory() + "/Download/";

    public static FileOutputStream logFileStream = null;

    public LogFile(){}
    public void init(){
        filename =
                downLoadPath + "BLE Log " +
                        new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss").format(
                                Calendar.getInstance().getTime()).toString()
                        +".txt";

        Log.i(TAG,"LOG : " + filename);
    }
    public boolean openFileStream()
    {
        if(logFileStream == null)
        {
            try {
                logFileStream = new FileOutputStream(filename);
            }
            catch(Exception e){
                Log.i(TAG,"LOG : " + e.toString());
                return false;
            }
        }
        Log.i(TAG,"LOG : openFileStream");

        return true;
    }
    public boolean closeFileStream(){

        if(logFileStream != null)
        {
            try {
                logFileStream.close();
            }
            catch(Exception e){
                e.printStackTrace();
                return false;
            }
            finally {
                logFileStream = null;
            }
        }
        Log.i(TAG,"LOG : closeFileStream");

        return true;
    }

    public void recodeLogFile(String log){
        try {
            byte[] logDate= new SimpleDateFormat("[yyyy:MM:dd:HH:mm:ss] ").format(
                    Calendar.getInstance().getTime()).toString().getBytes();
            logFileStream.write(logDate, 0, logDate.length);
            logFileStream.write(log.getBytes(),0,log.getBytes().length);
        } catch (IOException e) {
            Log.i(TAG,"LOG : recode" + e.toString());
        }
        Log.i(TAG,"LOG : recode");

    }

}