package com.demo.ranger.idreaderdemo.service.test;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.demo.ranger.idreaderdemo.MainActivity;
import com.demo.ranger.idreaderdemo.service.IDCardConnectService;
import com.demo.ranger.idreaderdemo.util.ConventerUtil;
import com.demo.ranger.idreaderdemo.util.GsonUtil;
import com.demo.ranger.idreaderdemo.util.LogUtil;
import com.google.gson.Gson;
import com.ranger.aidl.IDCardInfoData;
import com.ranger.aidl.IDManager;
import com.zkteco.android.biometric.core.device.ParameterHelper;
import com.zkteco.android.biometric.core.device.TransportType;
import com.zkteco.android.biometric.module.idcard.IDCardReader;
import com.zkteco.android.biometric.module.idcard.IDCardReaderFactory;
import com.zkteco.android.biometric.module.idcard.meta.IDCardInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by hexinlei on 2017/4/13.
 */
public class IDCardReaderServiceTest extends Service{
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private boolean threadDisabe = false;

    private long times = 1000;

    private int count = 0;

    private static final int index = 20;

    private IDCardConnectService connect;

    private List<IDCardInfoData> idCardInfoDatas;

    private List<IDCardInfo> idCardInfos;

    private IDCardInfo idCardInfo;

    private Intent intent = new Intent("android.intent.action.AIDLService");

    private boolean mBound = false;

    private long timesRefash = 0;

    //AIDL
    private IDManager idManager;
    private ServiceConnection scon = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i("ServiceConnection", "onServiceConnected() called");
            idManager = IDManager.Stub.asInterface(service);
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i("ServiceConnection", "onServiceDisconnected() called");
            mBound = false;
        }
    };



    @Override
    public void onCreate() {

        super.onCreate();
        init();


        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!threadDisabe){
                    try {
                        Thread.sleep(times/2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    idCardInfo = connect.getIDCardInfo();

                    if (null!=idCardInfo){

                        //ConventerUtil.conventerIDCardInfoData(idCardInfoDatas,idCardInfo);

                        if (null!=idCardInfos){
                            idCardInfos.add(idCardInfo);
                        }else {
                            idCardInfos = new LinkedList<IDCardInfo>();
                            idCardInfos.add(idCardInfo);
                        }

                    }

                    execute();
                    count++;
                }
            }
        }).start();




    }

    /**
     * 初始化服务
     */
    private void init() {
        idCardInfoDatas = new ArrayList<IDCardInfoData>();
        intent.setPackage("com.demo.ranger.idreaderdemo");
        boolean flag = bindService(intent, scon, Context.BIND_AUTO_CREATE);
        Toast.makeText(getApplicationContext(), "启动数据校验服务:" + flag, Toast.LENGTH_SHORT).show();
        connect = new IDCardConnectService(getApplicationContext(),times);
        Toast.makeText(getApplicationContext(), "启动身份获取服务", Toast.LENGTH_SHORT).show();
    }


    private void execute(){
        //发送数据
        try{
            Iterator<IDCardInfo> iterator = idCardInfos.iterator();

            if (iterator.hasNext()){
                while (iterator.hasNext()){
                    IDCardInfo data = iterator.next();

                    String info = GsonUtil.toJson(data);


                    //String result = idManager.getCheckResult(data);
                    Intent intentForward = new Intent();
                    intentForward.putExtra("info",info);


//                    intentForward.putExtra("name", data.getName());
//                    intentForward.putExtra("id", data.getId());
//                    intentForward.putExtra("count", count);
//                    intentForward.putExtra("photo", data.getPhoto());
//                    intentForward.putExtra("checkResult",result);

                    intentForward.setAction("android.intent.action.ClientTestService");
                    sendBroadcast(intentForward);
                    iterator.remove();
                }
            }else {

                if (timesRefash>10){
                    Intent intentForward = new Intent();
                    intentForward.putExtra("name", "无数据");
                    intentForward.setAction("android.intent.action.ClientTestService");
                    timesRefash = 0;
                    sendBroadcast(intentForward);
                }else {
                    timesRefash++;
                }
            }

        } catch (Exception e) {
            LogUtil.e(this.getClass().getName(),e);
        }




    }


    @Override
    public void onDestroy() {
        if (mBound){
            unbindService(scon);
        }
        super.onDestroy();
    }
}
