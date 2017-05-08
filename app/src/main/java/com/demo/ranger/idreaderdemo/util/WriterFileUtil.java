package com.demo.ranger.idreaderdemo.util;

import android.graphics.Bitmap;
import com.demo.ranger.idreaderdemo.constans.IDCardReaderConstans;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hexinlei on 2017/4/5.
 */
public class WriterFileUtil {
    private static String MYLOGFILEName = "_IDCardInfo.txt";// 本类输出的文件名称
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");// 文件格式

    /**
     * 获取信息输入到文件
     * @param idCardInfo
     */
    public static void WriteLogtoFile(final String idCardInfo){

        //判断目录是否存在
        File directory = new File(IDCardReaderConstans.MYLOG_PATH_SDCARD_DIR);
        if (!directory.exists()){
            directory.mkdirs();
        }

        File directoryInfo = new File(IDCardReaderConstans.MYLOG_PATH_SDCARD_DIR_INFO);
        if (!directoryInfo.exists()){
            directoryInfo.mkdirs();
        }


        try{
            Date date = new Date();
            String dateString = format.format(date);



            File file = new File(IDCardReaderConstans.MYLOG_PATH_SDCARD_DIR_INFO, dateString
                    + MYLOGFILEName);
            FileWriter fileWriter = new FileWriter(file,true);
            BufferedWriter bufWriter = new BufferedWriter(fileWriter);
            bufWriter.write(idCardInfo);
            bufWriter.newLine();
            bufWriter.close();
            fileWriter.close();
        }catch (Exception e){
            LogUtil.e("error",e);
        }
    }
    /** 保存方法 */
    public static  void saveBitmap(Bitmap bm,String card) {
        File f = new File( IDCardReaderConstans.MYLOG_PATH_SDCARD_DIR+card + ".jpg");
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bm.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
