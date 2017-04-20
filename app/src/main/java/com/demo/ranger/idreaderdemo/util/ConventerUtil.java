package com.demo.ranger.idreaderdemo.util;

import com.ranger.aidl.IDCardInfoData;
import com.zkteco.android.biometric.module.idcard.meta.IDCardInfo;

import java.util.List;

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

}
