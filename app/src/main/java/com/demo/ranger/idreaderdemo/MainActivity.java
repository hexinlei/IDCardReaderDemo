package com.demo.ranger.idreaderdemo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
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
import com.demo.ranger.idreaderdemo.data.PostCardInfoData;
import com.demo.ranger.idreaderdemo.data.RequestData;
import com.demo.ranger.idreaderdemo.data.ResponseData;
import com.demo.ranger.idreaderdemo.data.StyleData;
import com.demo.ranger.idreaderdemo.entity.ResultInfoTable;
import com.demo.ranger.idreaderdemo.service.DBManagerService;
import com.demo.ranger.idreaderdemo.util.ConventerUtil;
import com.demo.ranger.idreaderdemo.util.GsonUtil;
import com.demo.ranger.idreaderdemo.util.LogUtil;
import com.demo.ranger.idreaderdemo.util.WriterFileUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ranger.aidl.IDManager;
import com.zkteco.android.IDReader.IDPhotoHelper;
import com.zkteco.android.IDReader.WLTService;
import com.zkteco.android.biometric.module.idcard.meta.IDCardInfo;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Date;

public class MainActivity extends AppCompatActivity {


    private MyReceiver receiver = null;
    private boolean mockDataEnable = false;

    private boolean mBound = false;

    public static final String GETINFO="getinfo";
    public static final String POSTCARDINFO="postcardinfo";

    //控件
//    private TextView number,name,checkResult,countInfo,checkCount,prohibitCount,resultText;
    private TextView resultText;

    private ImageView photo,resultPhoto;


    Intent intent = null;

    private String responseInfo;
    private Handler handler;
    private ResponseData responseData;
    ProgressDialog waitingDialog;
    private String status="0";

    private SharedPreferences preferences;

    private DBManagerService managerService;

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

    //6.0 读写sdcard权限
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            verifyStoragePermissions(this);
            setContentView(R.layout.main_layout);
            photo = (ImageView) findViewById(R.id.photo);
            resultPhoto = (ImageView) findViewById(R.id.resultPhoto);
            resultText = (TextView) findViewById(R.id.resultText);
            waitingDialog=
                  new ProgressDialog(MainActivity.this);
            intent = new Intent("android.intent.action.ClientTestService");

            x.Ext.init(getApplication());
            receiver=new MyReceiver();
            IntentFilter filter=new IntentFilter();
            filter.addAction("android.intent.action.ClientTestService");
            MainActivity.this.registerReceiver(receiver, filter);
            preferences = PreferenceManager.getDefaultSharedPreferences(this);
            managerService = new DBManagerService();
            managerService.delete(preferences.getInt("deleteDays", 30));

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
        RequestParams requestParams = new RequestParams(getUrl(GETINFO));
        //RequestParams requestParams = new RequestParams("http://124.117.209.133:29092/verificationInterface/person/personTypeQuery");
        final RequestData requestData= new RequestData();
        requestData.setDeviceCode("650105FL000001000001");
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

    /**
     * @brief-des:TODO〈上传身份信息〉
     * @method:
     * @description:TODO〈上传身份信息〉
     * @author:
     * @date:   17/5/8 - 下午11:19
     * @version: V 1.0
     * @params:
     * @modification History:
     * @17/5/8  ——— version ——— HYBRIS- ———
     * @Tags:
     */
    private void postCardInfo(IDCardInfo idCardInfo,String jobid)
    {
        //RequestParams requestParams = new RequestParams(getUrl(POSTCARDINFO));
        RequestParams requestParams = new RequestParams("http://124.117.209.133:29092/verificationInterface/passlog/personLog");
        PostCardInfoData postCardInfoData = new PostCardInfoData();
        ConventerUtil.conventerPostIDCardInfoData(postCardInfoData, idCardInfo, jobid);
        String json= GsonUtil.toJson(postCardInfoData);//上传数据
        requestParams.setAsJsonContent(true);
        requestParams.setBodyContent(json);
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

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

    private String getUrl(String type)
    {
        String ip = preferences.getString("pre_reader_server", "124.117.209.133");
        String port = preferences.getString("pre_reader_serverport","29092");
        if (type.equals(GETINFO))
        {
            return "http://" + ip + ":" + port + "/verificationInterface/person/personTypeQuery";
        }else if (type.equals(POSTCARDINFO)){
            return "http://" + ip + ":" + port + "/verificationInterface/passlog/personLog";
        }
        return null;
    }

    /**
     * 保存检查结果
     */
    private void saveResult(ResponseData responseData){
        String status = responseData.getListType();
        ResultInfoTable infoTable = new ResultInfoTable();
        switch (status){
            case "1":
                infoTable.setCheckType("pass");
                infoTable.setCreateTime(new Date());
                managerService.insert(infoTable);
                break;
            case "2":
                infoTable.setCheckType("pass");
                infoTable.setCreateTime(new Date());
                managerService.insert(infoTable);
                break;
            case "3":
                infoTable.setCheckType("forbid");
                infoTable.setCreateTime(new Date());
                managerService.insert(infoTable);
                break;
        }
    }

    class MyReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            try{
                Bundle bundle = intent.getExtras();
                String statusResult = bundle.getString("status");
                final StyleData styleData = new StyleData();

                if ("success".equals(statusResult)){
                    String info = bundle.getString("info");
                    final IDCardInfo idCardInfo = GsonUtil.jsonToBean(info, IDCardInfo.class);
                    final String id = idCardInfo.getId();

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
                                status = responseData.getListType();
                                String msgInfo = responseData.getMessage();
                                String resultMsg = null;
                                LogUtil.e("responseData",responseData.toString());
                                String showSetting = preferences.getString("showSetting","2");
                                //保存识别结果
                                //saveResult(responseData);
                                createStyle(styleData, status);
                                MainActivity.this.resultText.setText(styleData.getTextValue());
                                MainActivity.this.resultText.setTextColor(android.graphics.Color.parseColor(styleData.getTextColor()));
                                MainActivity.this.resultPhoto.setImageResource(styleData.getImagePath());
                                MainActivity.this.resultPhoto.setVisibility(View.VISIBLE);

                                // TODO: 2017/5/9 添加页面展示
                                byte photo[] = idCardInfo.getPhoto();

                                Bitmap bitmap = null;

                                if (null!= photo){

                                    byte[] buf = new byte[WLTService.imgLength];

                                    if(1 == WLTService.wlt2Bmp(photo, buf))
                                    {
                                        bitmap = IDPhotoHelper.Bgr2Bitmap(buf);
                                    }

                                }
                                if (null != bitmap){
                                    MainActivity.this.photo.setImageBitmap(bitmap);
                                    try {
                                        postBitMap(WriterFileUtil.saveBitmap(bitmap, id));
                                    }catch (Exception e){
                                        LogUtil.e("error",e);
                                    }
                                    MainActivity.this.photo.setVisibility(View.VISIBLE);
                                }else {
                                    MainActivity.this.photo.setVisibility(View.INVISIBLE);
                                }


                                // TODO: 17/5/8 上传身份信息
                                postCardInfo(idCardInfo, responseData.getJobId());

//                            if (showSetting.equals("1")){
//                                //1.原文输出
//                                resultMsg = msgInfo;
//                            }else if (showSetting.equals("2")){
//                                //2.自定义输出
//                                LogUtil.e("status--------",status);
//                                if (CheckTypeEnum.textMap.containsKey(status)){
//                                    resultMsg = CheckTypeEnum.textMap.get(status);
//                                    TtsUtil.read(getApplicationContext(),CheckTypeEnum.valuesMap.get(status));
//                                }else {
//                                    resultMsg =CheckTypeEnum.textMap.get("FAIL");
//                                    TtsUtil.read(getApplicationContext(),CheckTypeEnum.valuesMap.get("FAIL"));
//                                }
//                            }
//
//                            MainActivity.this.checkResult.setText(resultMsg);
                            }
                        };
                    }
                }else if ("fail".equals(statusResult)){
                    MainActivity.this.photo.setImageResource(R.drawable.default_photo);
                    MainActivity.this.photo.setVisibility(View.VISIBLE);
                    MainActivity.this.resultPhoto.setVisibility(View.INVISIBLE);
                    createStyle(styleData, "0");
                    MainActivity.this.resultText.setText(styleData.getTextValue());
                    MainActivity.this.resultText.setTextColor(android.graphics.Color.parseColor(styleData.getTextColor()));
                }
            }catch (Exception e) {
                e.printStackTrace();
                LogUtil.e("Exception", e);
            }
        }


    }


    private void postBitMap(File file) {
        RequestParams requestParams = new RequestParams("http://124.117.209.133:29092/verificationInterface/passlog/personLog");

        requestParams.addBodyParameter("status", "1");
        requestParams.addBodyParameter("FilePath", file);

        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(getApplicationContext(),"图片上传成功",Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getApplicationContext(),"图片上传失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 创建展示样式
     * @param styleData
     * @param result
     */
    private void createStyle(StyleData styleData,String result){
        String value = CheckTypeEnum.resultMap.get(result);
        switch (result){
            case "1":
                styleData.setTextColor("#D82B2B");
                styleData.setImagePath(R.drawable.check);
                styleData.setTextValue(value);
                break;
            case "2":
                styleData.setTextColor("#2BD848");
                styleData.setImagePath(R.drawable.fast_check);
                styleData.setTextValue(value);
                break;
            case "3":
                styleData.setTextColor("#D82B2B");
                styleData.setImagePath(R.drawable.check);
                styleData.setTextValue(value);
                break;
            default:
                styleData.setTextColor("#3C536C");
                styleData.setTextValue(value);
                break;
        }
    }

    /**
     * 检查文件读写权限,添加权限
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}



