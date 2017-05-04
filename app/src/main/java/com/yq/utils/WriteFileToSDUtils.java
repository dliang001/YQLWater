package com.yq.utils;


import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.RandomAccessFile;

public class WriteFileToSDUtils
{
    /**
     * 写文件到sd卡上
     *
     * @param context
     */
    public static void writeFileToSD(String context) {
        //使用RandomAccessFile 写文件 还是蛮好用的..推荐给大家使用...
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
            Log.d("TestFile", "SD card is not avaiable/writeable right now.");
            return;
        }
        try {
            String pathName = "/mnt/sdcard/";
            String fileName = "YQWater_log.txt";

            File path = new File(pathName);
            File file = new File(pathName + fileName);
            if (!path.exists()) {                                      //  创建路径（文件夹）
                Log.d("TestFile", "Create the path:" + pathName);
                path.mkdir();
            }
            if (!file.exists()) {                                       //  创建文件
                Log.d("TestFile", "Create the file:" + fileName);
                file.createNewFile();
            }

            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            raf.seek(file.length());
            raf.write(context.getBytes());
            raf.close();

            //注释的也是写文件..但是每次写入都会把之前的覆盖..

//            String pathName = "/sdcard/";
//            String fileName = "log.txt";
//            File path = new File(pathName);
//            File file = new File(pathName + fileName);
//            if (!path.exists()) {                                      //  创建路径（文件夹）
//                Log.d("TestFile", "Create the path:" + pathName);
//                path.mkdir();
//            }
//            if (!file.exists()) {                                      //  创建文件
//                Log.d("TestFile", "Create the file:" + fileName);
//                file.createNewFile();
//            }
//            FileOutputStream stream = new FileOutputStream(file);
//            String s = context;
//            byte[] buf = s.getBytes();
//            stream.write(buf);
//            stream.close();
        } catch (Exception e) {
            Log.e("TestFile", "Error on writeFilToSD.");
        }
    }
}
