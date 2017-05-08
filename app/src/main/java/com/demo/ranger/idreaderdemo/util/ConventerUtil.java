package com.demo.ranger.idreaderdemo.util;

import com.demo.ranger.idreaderdemo.data.PostCardInfoData;
import com.ranger.aidl.IDCardInfoData;
import com.zkteco.android.biometric.module.idcard.meta.IDCardInfo;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by hexinlei on 2017/4/19.
 */
public class ConventerUtil {

    /**
     * 转换IDCardInfo
     * @param infoDataList
     * @param idCardInfo
     */
    public static void conventerIDCardInfoData(List<IDCardInfoData> infoDataList,IDCardInfo idCardInfo){
            if (null!=idCardInfo){
                IDCardInfoData idCardInfoData = new IDCardInfoData(idCardInfo.getPhotolength(),idCardInfo.getPhoto(),idCardInfo.getName(),idCardInfo.getId());
                infoDataList.add(idCardInfoData);
            }
    }


    public static void conventerPostIDCardInfoData(PostCardInfoData postCardInfoData,IDCardInfo idCardInfo,String jobid){
       postCardInfoData.setToken("XXXXXXXXXXXXXX");
       postCardInfoData.setDeviceCode("650105FL000001000001");
       postCardInfoData.setBlackQueryJobId(jobid);
       postCardInfoData.setCollectionType(1);
       postCardInfoData.setCardNo(idCardInfo.getId());
       postCardInfoData.setPassNo("00111333");
       postCardInfoData.setInOrOut(0);
       postCardInfoData.setVerifyResult(0);
       postCardInfoData.setPassTime(DateUtil.formatDate(new Date()));
       postCardInfoData.setName(idCardInfo.getName());
       postCardInfoData.setGenderCode("1");
       postCardInfoData.setNationCode(idCardInfo.getNation());
       postCardInfoData.setNationality(idCardInfo.getNation());
       String reg = "[\u4e00-\u9fa5]";
       Pattern pat = Pattern.compile(reg);
       Matcher mat=pat.matcher(idCardInfo.getBirth());
       String repickStr = mat.replaceAll("");
       postCardInfoData.setBirthday(repickStr);
       postCardInfoData.setAddr(idCardInfo.getAddress());
       postCardInfoData.setCardNo(idCardInfo.getId());
       String[] s =idCardInfo.getValidityTime().split("-");
       List list = java.util.Arrays.asList(s);
       postCardInfoData.setCardStartTime(list.get(0).toString().replace(".",""));
       postCardInfoData.setCardEndTime(list.get(1).toString().replace(".",""));
       postCardInfoData.setCardType(1);
       postCardInfoData.setIssuingUnit(idCardInfo.getDepart());
       postCardInfoData.setHasFingerFea(1);


    }
}
