package com.demo.ranger.idreaderdemo;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.ranger.idreaderdemo.activity.SettingsActivity;
import com.demo.ranger.idreaderdemo.constans.CheckTypeEnum;
import com.demo.ranger.idreaderdemo.data.RequestData;
import com.demo.ranger.idreaderdemo.data.ResponseData;
import com.demo.ranger.idreaderdemo.util.GsonUtil;
import com.demo.ranger.idreaderdemo.util.LogUtil;
import com.demo.ranger.idreaderdemo.util.TtsUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ranger.aidl.IDManager;
import com.zkteco.android.IDReader.IDPhotoHelper;
import com.zkteco.android.IDReader.WLTService;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;

public class MainActivity extends AppCompatActivity {


    private MyReceiver receiver = null;
    private boolean mockDataEnable = false;

    private boolean mBound = false;


    //控件
    private TextView number,name,checkResult,countInfo,checkCount,prohibitCount;
    private ImageView photo;


    Intent intent = null;

    private String responseInfo;
    private Handler handler;
    private ResponseData responseData;
    ProgressDialog waitingDialog;
    private String status="0";

    private SharedPreferences preferences;

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
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main_1);
            x.Ext.init(getApplication());
            number = (TextView) findViewById(R.id.number);
            name = (TextView) findViewById(R.id.name);
            photo = (ImageView) findViewById(R.id.photo);
            countInfo = (TextView) findViewById(R.id.countInfo);
            checkCount = (TextView) findViewById(R.id.checkCount);
            prohibitCount = (TextView) findViewById(R.id.prohibitCount);
            checkResult = (TextView) findViewById(R.id.checkResult);

            waitingDialog=
                    new ProgressDialog(MainActivity.this);
            intent = new Intent("android.intent.action.ClientTestService");

            receiver=new MyReceiver();
            IntentFilter filter=new IntentFilter();
            filter.addAction("android.intent.action.ClientTestService");
            MainActivity.this.registerReceiver(receiver, filter);
            preferences = PreferenceManager.getDefaultSharedPreferences(this);
        }catch (Exception e){
            LogUtil.e("Exception", e);
        }
    }

    @Override
    protected void onDestroy() {
        stopService();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case R.id.action_start:
                try{
                    startService();
                }catch (Exception e){
                    LogUtil.e("start service",e);
                }
                Toast.makeText(this,"身份识别服务已经开启",Toast.LENGTH_SHORT).show();
                break;

            case R.id.action_close:
                try{
                    stopService();
                }catch (Exception e){
                    LogUtil.e("stop service",e);
                }
                Toast.makeText(this,"身份识别服务已经关闭",Toast.LENGTH_SHORT).show();
                break;

            case R.id.action_mockData:
                mockDataEnable = true;
                Toast.makeText(this,"开启测试数据",Toast.LENGTH_SHORT);
                break;
            case R.id.action_exit:
                finish();
                break;

            case R.id.action_setting:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void startService(){
        intent.setPackage("com.demo.ranger.idreaderdemo");
        boolean ret = bindService(intent, scon, Context.BIND_AUTO_CREATE);
        Toast.makeText(this,"服务开启:"+ret,Toast.LENGTH_SHORT).show();
    }


    /**
     * 终止刷卡服务
     */
    private void stopService(){
        if (mBound){
            unbindService(scon);
        }
        unregisterReceiver(receiver);
    }

    public  void getCartInfo(String id) {
        RequestParams requestParams = new RequestParams(getUrl());
        final RequestData requestData= new RequestData();
        requestData.setDeviceCode("7650100100000001");
        requestData.setCardNo(id);
        String json= GsonUtil.toJson(requestData);//上传数据
        requestParams.setAsJsonContent(true);
        requestParams.setBodyContent(json);
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Message msg = Message.obtain();
                msg.obj = result;
                handler.sendMessage(msg);
                waitingDialog.dismiss();
            }

            @Override
            public void onFinished() {
                //dia.dismiss();//加载完成
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }
        });
    }

    private String getUrl()
    {
        String ip = preferences.getString("pre_reader_server","124.117.209.133");
        String port = preferences.getString("pre_reader_serverport","29092");
        return "http://"+ip+":"+port+"/verificationInterface/redList/personRedList";

    }

    class MyReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            try{
                Bundle bundle = intent.getExtras();
                String id = bundle.getString("id");
                if (null!=id) {
                    getCartInfo(id);
                    handler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            super.handleMessage(msg);
                            responseInfo = (String) msg.obj;
                            Gson gson = new Gson();
                            Type type = new TypeToken<ResponseData>() {
                            }.getType();
                            responseData = gson.fromJson(responseInfo, type);
                            status = responseData.getIsRedList();
                            String msgInfo = responseData.getMessage();
                            String resultMsg = null;
                            LogUtil.e("responseData",responseData.toString());
                            String showSetting = preferences.getString("showSetting","2");

                            if (showSetting.equals("1")){
                                //1.原文输出
                                resultMsg = msgInfo;
                            }else if (showSetting.equals("2")){
                                //2.自定义输出
                                LogUtil.e("status--------",status);
                                if (CheckTypeEnum.textMap.containsKey(status)){
                                    resultMsg = CheckTypeEnum.textMap.get(status);
                                    TtsUtil.read(getApplicationContext(),CheckTypeEnum.valuesMap.get(status));
                                }else {
                                    resultMsg =CheckTypeEnum.textMap.get("FAIL");
                                    TtsUtil.read(getApplicationContext(),CheckTypeEnum.valuesMap.get("FAIL"));
                                }
                            }

                            MainActivity.this.checkResult.setText(resultMsg);

                        }
                    };
                }

                String name = bundle.getString("name");
                int counts = bundle.getInt("count");

                byte photo[] = bundle.getByteArray("photo");

                Bitmap bitmap = null;

                if (null!= photo){

                    byte[] buf = new byte[WLTService.imgLength];

                    if(1 == WLTService.wlt2Bmp(photo, buf))
                    {
                        bitmap = IDPhotoHelper.Bgr2Bitmap(buf);
                    }

                }
                MainActivity.this.number.setText(String.valueOf(counts));
                MainActivity.this.name.setText(name);
                MainActivity.this.checkResult.setText(null);
                if (null != bitmap){
                    MainActivity.this.photo.setImageBitmap(bitmap);
                    MainActivity.this.photo.setVisibility(View.VISIBLE);
                }else {
                    MainActivity.this.photo.setVisibility(View.INVISIBLE);
                }
            }catch (Exception e){
                e.printStackTrace();
                LogUtil.e("Exception", e);
            }
        }


    }



}



