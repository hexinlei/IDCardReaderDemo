package com.demo.ranger.idreaderdemo.util;

import com.demo.ranger.idreaderdemo.constans.IDCardReaderConstans;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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

}
