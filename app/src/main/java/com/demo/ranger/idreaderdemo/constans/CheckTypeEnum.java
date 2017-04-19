package com.demo.ranger.idreaderdemo.constans;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hexinlei on 2017/4/9.
 * 案件类型
 */
public enum CheckTypeEnum {
    SECURATYEnum("SECURATY"),SUCCESSEnum("SUCCESS"),FAILEnum("FAIL");




    private String typeEnum;

    private String SECURATY = "SECURATY";
    private String SUCCESS = "SUCCESS";
    private String FAIL = "FAIL";

    private static String SECURATY_VOICE = "请接受安检";
    private static String SUCCESS_VOICE = "请通行";
    private static String FAIL_VOICE = "禁止通行";


    private static String SECURATY_TEXT = "安检";
    private static String SUCCESS_TEXT = "通行";
    private static String FAIL_TEXT= "禁止";


    public static Map<String,String> valuesMap = new HashMap<>();
    static {
        valuesMap.put("SECURATY",SECURATY_VOICE);
        valuesMap.put("SUCCESS",SUCCESS_VOICE);
        valuesMap.put("FAIL",FAIL_VOICE);
    }

    public static Map<String,String> textMap = new HashMap<>();
    static {
        textMap.put("SECURATY",SECURATY_TEXT);
        textMap.put("SUCCESS",SUCCESS_TEXT);
        textMap.put("FAIL",FAIL_TEXT);
    }


    private CheckTypeEnum(String value){
        this.typeEnum = value.toUpperCase();
    }


    public String getValue(){
        return this.typeEnum;
    }





}
