package com.demo.ranger.idreaderdemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.demo.ranger.idreaderdemo.MainActivity;
import com.demo.ranger.idreaderdemo.activity.WaitingActivity;
import com.demo.ranger.idreaderdemo.constans.CheckTypeEnum;
import com.demo.ranger.idreaderdemo.util.LogUtil;
import com.demo.ranger.idreaderdemo.util.ReadFileDataUtil;
import com.demo.ranger.idreaderdemo.util.TtsUtil;
import com.zkteco.android.IDReader.WLTService;
import com.zkteco.android.biometric.core.device.ParameterHelper;
import com.zkteco.android.biometric.core.device.TransportType;
import com.zkteco.android.biometric.module.idcard.IDCardReader;
import com.zkteco.android.biometric.module.idcard.IDCardReaderFactory;
import com.zkteco.android.biometric.module.idcard.exception.IDCardReaderException;
import com.zkteco.android.biometric.module.idcard.meta.IDCardInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hexinlei on 2017/4/8.
 */
public class IDCardReaderService extends Service {

    private boolean threadDisabe = false;

    private long times = 1000;

    private int count = 0;

    private int wait = 0;

    private int waitMin = 60;

    private boolean bopen = false;

    private IDCardReader idCardReader = null;

    private static final int VID = 1024;    //IDR VID
    private static final int PID = 50010;     //IDR PID

    private static IDCardInfo idCardInfo;

    private Map<String,String> mockDataMap;

    private String SECURATY = "SECURATY";
    private String SUCCESS = "SUCCESS";
    private String FAIL = "FAIL";



    @Override
    public void onCreate() {
        try{
            super.onCreate();
            //初始化
            startIDCardReader();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!threadDisabe) {

                        try {
                            Thread.sleep(times);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        try{

                            //读取
                            readInfo();

                            //分发给activity
                            Intent intent = new Intent();
                            String id = null;
                            String name = null;
                            String checkResult = null;


                            if (null!=idCardInfo){
                                id = idCardInfo.getId();
                                name = idCardInfo.getName();
                                CheckTypeEnum checkTypeEnum = null;
                                if (null!=mockDataMap){
                                    checkTypeEnum = CheckIDCardService.checkMockData(id, mockDataMap);
                                }else {
                                    //TODO 服务器验证身份信息
                                }

                                if (null != checkTypeEnum){
                                    String checkType = checkTypeEnum.getValue();

                                    checkResult = CheckTypeEnum.textMap.get(checkType);

                                    TtsUtil.read(getApplicationContext(), CheckTypeEnum.valuesMap.get(checkType));

                                }

                                if (idCardInfo.getPhotolength() > 0){

                                    byte[] buf = new byte[WLTService.imgLength];

                                    if(1 == WLTService.wlt2Bmp(idCardInfo.getPhoto(), buf))
                                    {
                                        intent.putExtra("photo", idCardInfo.getPhoto());
                                    }
                                }
                                if (wait/waitMin>0){
                                    Intent intentForward = new Intent(getApplicationContext(), MainActivity.class);
                                    intentForward.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intentForward);
                                }
                                intent.putExtra("id",id);
                                intent.putExtra("name", name);
                                intent.putExtra("count", count);
                                intent.putExtra("checkResult", checkResult);
                                intent.setAction("com.demo.ranger.idreaderdemo.service.IDCardReaderService");
                                sendBroadcast(intent);
                                idCardInfo = null;
                                checkResult = null;
                                count++;
                                wait = 0;
                            }else {
                                //TODO 做activity跳转

                                if (wait/waitMin>0){
                                    Intent intentForward = new Intent(getApplicationContext(), WaitingActivity.class);
                                    intentForward.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intentForward);
                                }else {
                                    wait++;
                                }
                            }


                        }catch (Exception e){
                            LogUtil.e("Exception",e);
                        }
                    }
                }
            }).start();
        }catch (Exception e){
            LogUtil.e("Exception",e);
        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        threadDisabe = true;
        count = 0;
        if (null!=idCardReader){
            idCardReader.destroy();
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean mockDataEnable = intent.getBooleanExtra("mockDataEnable",false);
        if (mockDataEnable){
            mockDataMap = new HashMap<String,String>();
            ReadFileDataUtil.readMockData(mockDataMap);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 初始化IDCaderReader
     */
    private synchronized void  startIDCardReader() {
        Map idrParams = new HashMap();
        idrParams.put(ParameterHelper.PARAM_KEY_VID, VID);
        idrParams.put(ParameterHelper.PARAM_KEY_PID, PID);
        idCardReader = IDCardReaderFactory.createIDCardReader(this, TransportType.USB, idrParams);
        try {
            if (!bopen) {
                idCardReader.open(0);
                bopen = true;
            }
        } catch (IDCardReaderException ie) {
            String emsg = "连接设备失败, 错误码：" + ie.getErrorCode() + "\n错误信息：" + ie.getMessage() + "\n 内部错误码=" + ie.getInternalErrorCode();
            LogUtil.e("IDCardReaderException", emsg);
        } catch (Exception e) {
            LogUtil.e("Exception", e);
        }



    }

    /**
     * 读取信息
     */
    private void readInfo() {
        try {

            idCardReader.open(0);

            LogUtil.i("info getSAMID",idCardReader.getSAMID(0));

            idCardReader.findCard(0);
            LogUtil.i("info", "获取到卡片");

            idCardReader.selectCard(0);
            LogUtil.i("info", "选择了卡片");

            IDCardInfo info = new IDCardInfo();
            boolean ref = idCardReader.readCard(0, 1, info);
            if (ref) {
                LogUtil.i("info", "读取了信息：" + info.getId());
                if (null != idCardInfo) {
                    String id_old = idCardInfo.getId();
                    String id_new = info.getId();
                    if (id_old.equals(id_new)) {
                        LogUtil.i("info", "已经识别完成，请取回卡片");
                        idCardInfo = null;
                    } else {
                        idCardInfo = info;
                    }
                } else {
                    idCardInfo = info;
                }
            }
        } catch (IDCardReaderException e) {
            LogUtil.e("IDCardReaderException", e);
        }

    }


}


