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
import com.ranger.aidl.IDCardInfoData;
import com.ranger.aidl.IDManager;
import com.zkteco.android.biometric.core.device.ParameterHelper;
import com.zkteco.android.biometric.core.device.TransportType;
import com.zkteco.android.biometric.module.idcard.IDCardReaderFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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

    private static final int VID = 1024;    //IDR VID
    private static final int PID = 50010;     //IDR PID

    private static final int index = 20;

    private List<IDCardInfoData> idCardInfoDatas;

    private Intent intent = new Intent("android.intent.action.AIDLService");

    //AIDL
    private IDManager idManager;
    private ServiceConnection scon = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i("ServiceConnection", "onServiceConnected() called");
            idManager = IDManager.Stub.asInterface(service);

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i("ServiceConnection", "onServiceDisconnected() called");
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
                        Thread.sleep(times);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    //mock数据
                    try{
                        if (count<10){
                            int photoLength = 10;
                            byte photo[] = new byte[0];
                            String name = "test1"+count;
                            String id = "111111"+count;
                            IDCardInfoData idCardInfoData = new IDCardInfoData(photoLength,photo,name,id);
                            idCardInfoDatas.add(idCardInfoData);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }


                    execute();
                    count++;
                }
            }
        }).start();




    }

    private void init() {
        idCardInfoDatas = new ArrayList<>();
        boolean flag = bindService(intent, scon, Context.BIND_AUTO_CREATE);
        Toast.makeText(getApplicationContext(), "" + flag, Toast.LENGTH_SHORT).show();

    }


    private void execute(){
        //发送数据
        try{
            Iterator<IDCardInfoData> iterator = idCardInfoDatas.iterator();
            if (iterator.hasNext()){
                while (iterator.hasNext()){
                    IDCardInfoData data = iterator.next();

                    String result = idManager.getCheckResult(data);
                    Intent intentForward = new Intent();
                    intentForward.putExtra("name", result);
                    intentForward.setAction("android.intent.action.ClientTestService");
                    sendBroadcast(intentForward);
                    if ("success".equals(result)){
                        iterator.remove();
                    }
                }
            }else {
                Intent intentForward = new Intent();
                intentForward.putExtra("name", "无数据");
                intentForward.setAction("android.intent.action.ClientTestService");
                sendBroadcast(intentForward);
            }


        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unbindService(scon);
    }
}
