package com.demo.ranger.idreaderdemo;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.ranger.idreaderdemo.activity.SettingsActivity;
import com.demo.ranger.idreaderdemo.service.IDCardReaderService;
import com.demo.ranger.idreaderdemo.util.LogUtil;
import com.ranger.aidl.IDCardInfoData;
import com.ranger.aidl.IDManager;
import com.zkteco.android.IDReader.IDPhotoHelper;
import com.zkteco.android.IDReader.WLTService;

public class MainActivity extends AppCompatActivity {


    private MyReceiver receiver = null;
    private boolean mockDataEnable = false;



    //控件
    private TextView number,name,checkResult,countInfo,checkCount,prohibitCount;
    private ImageView photo;


    Intent intent = new Intent("android.intent.action.ClientTestService");


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
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main_1);

            number = (TextView) findViewById(R.id.number);
            name = (TextView) findViewById(R.id.name);
            photo = (ImageView) findViewById(R.id.photo);
            countInfo = (TextView) findViewById(R.id.countInfo);
            checkCount = (TextView) findViewById(R.id.checkCount);
            prohibitCount = (TextView) findViewById(R.id.prohibitCount);
            checkResult = (TextView) findViewById(R.id.checkResult);
            //startService();
            receiver=new MyReceiver();
            IntentFilter filter=new IntentFilter();
            filter.addAction("android.intent.action.ClientTestService");
            MainActivity.this.registerReceiver(receiver, filter);

//            //按钮事件
//            exit.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    exit();
//                }
//            });
//
//            //
//            setting.setOnClickListener(new View.OnClickListener(){
//
//                @Override
//                public void onClick(View v) {
//                    startService(intent);
//                    Toast.makeText(getApplicationContext(),"启动成功",Toast.LENGTH_SHORT).show();
//
//
//                }
//            });
//
//            checkInfo.setOnClickListener(new View.OnClickListener(){
//
//                @Override
//                public void onClick(View v) {
//                    byte photo[] = new byte[0];
//                    IDCardInfoData data = new IDCardInfoData(0,photo,"测试","11111111");
//                    try {
//                        String result = idManager.getCheckResult(data);
//                        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
//                    } catch (RemoteException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });


        }catch (Exception e){
            LogUtil.e("Exception", e);
        }
    }

    @Override
    protected void onDestroy() {
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
//                    stopService();
//                    startService();
                    startServiceGet();
                }catch (Exception e){
                    LogUtil.e("start service",e);
                }
                Toast.makeText(this,"身份识别服务已经开启",Toast.LENGTH_SHORT);
                break;

            case R.id.action_close:
                try{
                    stopService();
                }catch (Exception e){
                    LogUtil.e("stop service",e);
                }
                Toast.makeText(this,"身份识别服务已经关闭",Toast.LENGTH_SHORT);
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

    /**
     * 启动刷卡服务
     */
    private void startService(){
        Intent intentService = new Intent(MainActivity.this, IDCardReaderService.class);
        intentService.putExtra("mockDataEnable",mockDataEnable);
        startService(intentService);
    }

    private void startServiceGet(){

    }


    /**
     * 终止刷卡服务
     */
    private void stopService(){
        stopService(new Intent(MainActivity.this, IDCardReaderService.class));
    }

    /**
     * 退出
     */
    private void exit(){
        mockDataEnable = false;
        stopService();
        unregisterReceiver(receiver);
        stopService(intent);
        finish();
    }



    class MyReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            try{
                Bundle bundle = intent.getExtras();
                String id = bundle.getString("id");
                String name = bundle.getString("name");
                int counts = bundle.getInt("count");

                Toast.makeText(getApplicationContext(),name,Toast.LENGTH_SHORT).show();

                String checkResult = bundle.getString("checkResult");


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
                MainActivity.this.checkResult.setText(checkResult != null ? checkResult:id);
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



