package com.demo.ranger.idreaderdemo.service;

import com.demo.ranger.idreaderdemo.constans.CheckTypeEnum;
import com.demo.ranger.idreaderdemo.util.LogUtil;

import java.util.Map;

/**
 * Created by hexinlei on 2017/4/9.
 * 查询ID是否合法
 */
public class CheckIDCardService {



    /**
     * 通过身份证号检查
     * @param id
     * @return
     */
    public static CheckTypeEnum checkMockData(String id,Map<String,String> map){
        String idNum = id.trim();
        CheckTypeEnum checkTypeEnum = null;
//        if (map.containsKey(idNum)){
//            String checkResult = map.get(idNum);
//            LogUtil.d("CheckIDCardService checkMockData map",checkResult);
//
//            switch (checkResult){
//                case "SECURATY":
//                    checkTypeEnum = CheckTypeEnum.SECURATYEnum;
//                    break;
//                case "SUCCESS":
//                    checkTypeEnum = CheckTypeEnum.SUCCESSEnum;
//                    break;
//                case "FAIL":
//                    checkTypeEnum = CheckTypeEnum.FAILEnum;
//                    break;
//            }
//            LogUtil.d("CheckIDCardService checkMockData",checkTypeEnum.getValue());
//        }
        return checkTypeEnum;
    }


}
