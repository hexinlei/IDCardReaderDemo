package com.demo.ranger.idreaderdemo.service;

import android.content.Context;
import android.widget.Toast;

import com.demo.ranger.idreaderdemo.util.LogUtil;
import com.zkteco.android.biometric.core.device.ParameterHelper;
import com.zkteco.android.biometric.core.device.TransportType;
import com.zkteco.android.biometric.module.idcard.IDCardReader;
import com.zkteco.android.biometric.module.idcard.IDCardReaderFactory;
import com.zkteco.android.biometric.module.idcard.exception.IDCardReaderException;
import com.zkteco.android.biometric.module.idcard.meta.IDCardInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hexinlei on 2017/4/19.
 */
public class IDCardConnectService {

    private static final int VID = 1024;    //IDR VID
    private static final int PID = 50010;     //IDR PID

    private long times;

    private IDCardReader idCardReader = null;

    private boolean bopen = false;

    private Context context;

    public IDCardConnectService(Context context, long times) {
        this.context = context;
        this.times = times;
        startIDCardReader();
    }

    /**
     * 初始化读卡器
     */
    private synchronized void  startIDCardReader() {
        Map idrParams = new HashMap();
        idrParams.put(ParameterHelper.PARAM_KEY_VID, VID);
        idrParams.put(ParameterHelper.PARAM_KEY_PID, PID);
        idCardReader = IDCardReaderFactory.createIDCardReader(context, TransportType.USB, idrParams);
        try {
            if (!bopen) {
                idCardReader.open(0);
                bopen = true;
            }
        } catch (IDCardReaderException ie) {
            String emsg = "连接设备失败, 错误码：" + ie.getErrorCode() + "\n错误信息：" + ie.getMessage() + "\n 内部错误码=" + ie.getInternalErrorCode();
            Toast.makeText(context,emsg,Toast.LENGTH_SHORT).show();
            LogUtil.e("IDCardReaderException", emsg);
        } catch (Exception e) {
            LogUtil.e("Exception", e);
        }
    }

    /**
     * 获取身份信息
     * @return IDCardInfo
     */
    public IDCardInfo getIDCardInfo(){
        IDCardInfo idCardInfo = null;

        try {

            idCardReader.open(0);

            idCardReader.findCard(0);

            idCardReader.selectCard(0);

            idCardInfo = new IDCardInfo();

            boolean ref = idCardReader.readCard(0, 1, idCardInfo);

            if (!ref) {
                Toast.makeText(context,"读卡失败",Toast.LENGTH_SHORT).show();
            }
        } catch (IDCardReaderException e) {
            LogUtil.e("IDCardReaderException", e);
        }

        return idCardInfo;
    }


}
