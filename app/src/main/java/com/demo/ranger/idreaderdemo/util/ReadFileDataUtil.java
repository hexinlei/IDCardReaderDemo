package com.demo.ranger.idreaderdemo.util;


import com.demo.ranger.idreaderdemo.constans.IDCardReaderConstans;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

/**
 * Created by hexinlei on 2017/4/9.
 */
public class ReadFileDataUtil {

    /**
     * 读取txt中的mock数据
     *
     * @param mockDataMap
     */
    public static void readMockData(Map<String, String> mockDataMap) {
        String mockDataFile = IDCardReaderConstans.MOCK_DATA_FILE;

        File file = new File(mockDataFile);
        if (!file.exists()) {
            file.mkdirs();
            LogUtil.e("ReadFileDataUtil readMockData", "数据目录不存在");
        } else {
            File files[] = file.listFiles();
            if (null != files && files.length > 0) {
                for (File f : files) {
                    try {

                        String fileName = f.getName();
                        if (null != fileName && !fileName.contains("mock")){
                            continue;
                        }

                        BufferedReader reader = new BufferedReader(new FileReader(f));
                        String line = "";
                        while (true) {
                            line = reader.readLine();
                            if (null == line){
                                break;
                            }

                            String result[] = line.split(";");
                            if (result.length != 2 ){
                                LogUtil.e("ReadFileDataUtil readMockData","数据["+result[0]+"]有异常，请检查数据!");
                                continue;
                            }


                            if (mockDataMap.containsKey(result[0])){
                                continue;
                            }

                            mockDataMap.put(result[0].trim(),result[1].trim().toUpperCase());

                        }
                        reader.close();
                    } catch (IOException e) {
                        LogUtil.e("ReadFileDataUtil readMockData",e);

                    }


                }


            } else {
                LogUtil.e("ReadFileDataUtil readMockData", "数据目录中不存在mock数据");
            }
        }
    }


}
