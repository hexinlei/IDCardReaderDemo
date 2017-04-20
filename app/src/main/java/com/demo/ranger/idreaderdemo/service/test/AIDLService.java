package com.demo.ranger.idreaderdemo.service.test;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ranger.aidl.IDCardInfoData;
import com.ranger.aidl.IDManager;

/**
 * Created by hexinlei on 2017/4/13.
 */
public class AIDLService extends Service{

    private static final String TAG = "AIDLService";

    IDManager.Stub stub = new IDManager.Stub() {
        @Override
        public String getCheckResult(IDCardInfoData info) throws RemoteException {

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.i(TAG,"getCheckResult");
            if (null!=info){
                Log.i(TAG,"getCheckResult"+info.getId());
                return "success";
            }else {
                return "fail";
            }
        }
    };


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind() called");
        return stub;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "onUnbind() called");
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy() called");
    }
}
