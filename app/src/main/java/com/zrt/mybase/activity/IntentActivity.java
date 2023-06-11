package com.zrt.mybase.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zrt.mybase.R;
import com.zrt.mybase.base.BaseActivity;
import com.zrt.mybase.utils.AssistUtil;
import com.zrt.mybase.utils.JsonUtils;
import com.zrt.mybase.utils.MyLogger;
import com.zrt.mybase.utils.support.async.AsyncTaskOwner;
import com.zrt.mybase.utils.support.async.AsyncTaskUtil;
import com.zrt.mybase.utils.support.async.Callable;
import com.zrt.mybase.utils.support.async.Callback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static com.zrt.mybase.utils.AssistUtil.ExistSDCard;
import static com.zrt.mybase.utils.AssistUtil.getSDFreeSize;

/**
 * 1、添加读写权限：
 * <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
 * AndroidManiFast中application节点中添加android:requestLegacyExternalStorage="true"
 *
 * 2、apk安装权限：<!-- 安装 -->
 *     <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />
 * 各种Intent跳转
 */
public class IntentActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_intent;
    }

    @Override
    protected void initView() {
        start_xicoo_text = findViewById(R.id.start_xicoo_text);
    }

    @Override
    protected void initData() {

    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.start_show_icon_app:
                startShowIconAPP();
                break;
            case R.id.start_hide_icon_app:
                startHideIconAPP();
                break;
            case R.id.start_ydhl_app:
                startYDHLAPP();
                break;
            case R.id.start_wifi:
                // 跳转系统WIFI设置界面
                Intent intent = new Intent();
                if(android.os.Build.VERSION.SDK_INT >= 11){
                    intent .setClassName("com.android.settings", "com.android.settings.Settings$WifiSettingsActivity");
                }else{
                    intent .setClassName("com.android.settings" ,"com.android.settings.wifi.WifiSettings");
                }
                startActivity( intent);
                break;
            case R.id.start_calculator:
                    PackageInfo pack = getAllApps("Calculator","calculator");
                    if(pack!=null){
                        Intent intent2 = this.getPackageManager().getLaunchIntentForPackage(pack.packageName);
                        startActivity(intent2);
                    }else {
                        Toast.makeText(this,"不存在计算器",Toast.LENGTH_SHORT).show();
                    }
//                Intent intent2 = new Intent();
//                intent2.setClassName("com.android.calculator2", "com.android.calculator2.Calculator");
//                startActivity(intent2);
                break;
            case R.id.start_xicoo:
                gotoVSMeasureApp();
                break;
        }
    }

    /**
     * 跳转App图标显示的APP
     */
    public void startShowIconAPP(){
        if (!isAppInstalled(this, "com.zpdydhl_phz.mobileemr")){
            Toast.makeText(this, "未安装移动护理App, 无法跳转！ show", Toast.LENGTH_SHORT).show();
            return;
        }
        // 跳转至未隐藏图标的APP， 此处跳转至移动护理apk
        // getLaunchIntentForPackage("填写跳转App包名")
        Intent intent = getPackageManager()
                .getLaunchIntentForPackage("com.zpdydhl_phz.mobileemr");

        if (intent != null){
            intent.putExtra("user_number", "003");
            intent.putExtra("password", "123");
            startActivity(intent);
        }else {
            Toast.makeText(this, "未安装移动护理App, 无法跳转！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 跳转APP图标被隐藏的APP
     * 例：其他应用应设置APP图标隐藏，<data android:host="" android:scheme=""/>标签
     * <intent-filter>
     *      <action android:name="android.intent.action.MAIN" />
     *      <category android:name="android.intent.category.LAUNCHER" />
     *      <data android:host="SplashActivity" android:scheme="com.zpdydhl_phz.mobileemr"
     *            tools:ignore="AppLinkUrlError" />
     * </intent-filter>
     */
    public void startHideIconAPP(){
        if (!isAppInstalled(this, "com.zpdydhl_phz.mobileemr")){
//            Toast.makeText(this, "安装移动护理App, 无法跳转！", Toast.LENGTH_SHORT).show();
            installedPhzApp(this);
            return;
        }
        Intent intent = new Intent();
        ComponentName componentName =
                new ComponentName("com.zpdydhl_phz.mobileemr",
                        "com.zpdydhl_phz.mobileemr.SplashActivity");
        intent.setComponent(componentName);
        Uri uri = Uri.parse("com.zpdydhl_phz.mobileemr.SplashActivity");
        intent.setData(uri);
//        intent.setAction("android.intent.action.MAIN");
        intent.putExtra("user_number", "U1007");
        intent.putExtra("password", "123");
        startActivity(intent);
    }

    public void startYDHLAPP() {
        if (!isAppInstalled(this, "com.zpdydhl_phz.mobileemr")){
//            Toast.makeText(this, "安装移动护理App, 无法跳转！", Toast.LENGTH_SHORT).show();
            installedPhzApp(this);
            return;
        }
        // 跳转至未隐藏图标的APP， 此处跳转至移动护理apk
        // getLaunchIntentForPackage("填写跳转App包名")
        Intent intent = getPackageManager()
                .getLaunchIntentForPackage("com.zpdydhl_phz.mobileemr");
        if (intent != null){
            intent.putExtra("user_number", "U1007");
            intent.putExtra("password", "123");
            startActivity(intent);
        }else {
            Intent intentHide = new Intent();
            ComponentName componentName =
                    new ComponentName("com.zpdydhl_phz.mobileemr",
                            "com.zpdydhl_phz.mobileemr.SplashActivity");
            intentHide.setComponent(componentName);
            Uri uri = Uri.parse("com.zpdydhl_phz.mobileemr.SplashActivity");
            intentHide.setData(uri);
            intentHide.putExtra("user_number", "U1007");
            intentHide.putExtra("password", "123");
            startActivity(intentHide);
        }
    }

    /**
     * 判断应用是否安装
     * @param context
     * @param packageName 应用包名地址
     * @return
     */
    public boolean isAppInstalled(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                if (pinfo.get(i).packageName.contains(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }
    public PackageInfo getAllApps(String packageName, String packages) {
        final PackageManager packageManager = getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                if (pinfo.get(i).packageName.contains(packageName)
                    ||   pinfo.get(i).packageName.contains(packages)) {
                    return pinfo.get(i);
                }
            }
        }
        return null;
    }

    public final String appName = "phz.apk";
    public void installedPhzApp(final Context context){
        AsyncTaskUtil.getInstance().execute(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return copyApkFromAssets(context, appName, getMemoryPath(appName));
            }
        }, new Callback<Boolean>() {
            @Override
            public void onCallback(Boolean aBoolean) {
                if (aBoolean){
                    openPhzApp(context);
//                    Intent intent = new Intent(Intent.ACTION_VIEW);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent.setDataAndType(Uri.parse("file://"
//                                    + Environment.getExternalStorageDirectory().getAbsolutePath()
//                                    + File.separator + appName),
//                            "application/vnd.android.package-archive");
//                    context.startActivity(intent);
                }else {
                    Toast.makeText(context, "陪护证App获取失败, 无法跳转！", Toast.LENGTH_SHORT).show();
                }
            }
        }, new AsyncTaskOwner() {});

    }

    /**
     * Android 7.0 安装无反应，需要添加权限
     *     <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />
     * @param context
     */
    public void openPhzApp(Context context) {
        //Environment.getExternalStorageDirectory().getAbsolutePath() = /storage/emulated/0
        Log.i(">>>>", "##authorith="+ context.getPackageName() + ".fileprovider");
        String type = "application/vnd.android.package-archive";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            String existsApkPath = getDownloadApkFilePath(context, appName);
//            intent.setAction(Intent.ACTION_INSTALL_PACKAGE);
            String existsApkPath = getMemoryPath(appName);
            Log.i(">>>>", "##existsApkPath="+ existsApkPath);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uri = FileProvider.getUriForFile(context,
                    context.getPackageName() + ".fileprovider", new File(existsApkPath));
            intent.setDataAndType(uri, type);
        }else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse("file://"
                            + Environment.getExternalStorageDirectory().getAbsolutePath()
                            + File.separator + appName),
                    type );
        }
        context.startActivity(intent);
    }

    /**
     * 添加读写权限
     * <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
     * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
     * AndroidManiFast中application节点中添加android:requestLegacyExternalStorage="true"
     * @param context
     * @param fileName
     * @param path
     * @return
     */
    public boolean copyApkFromAssets(Context context, String fileName, String path) {
        boolean isSucess = false;
        InputStream open = null;
        FileOutputStream outputStream = null;
        try {
            open = getAssets().open("phz.apk");
            File file = new File(path);
            file.createNewFile();
            outputStream = new FileOutputStream(file);
            byte[] tem = new byte[1024];
            int i = 0;
            while ((i = open.read(tem)) >0){
                outputStream.write(tem,0, i);
            }
            outputStream.close();
            open.close();
            outputStream = null;
            open = null;
            isSucess = true;
        } catch (IOException e) {
            MyLogger.Log().e("## error ="+e.toString());
        }finally {
            try {
                if (open != null){
                    open.close();
                }
                if (outputStream != null){
                    outputStream.close();
                }
            } catch (IOException e) {
                MyLogger.Log().e("## stream close error ="+e.toString());
            }
        }
        return isSucess;
    }

    public static String getMemoryPath(String appName){
        String path = "";//路径规范：sd卡、机身存储空间/njry/项目名/日志、错误日志等文件夹
//        if(ExistSDCard() == true && getSDFreeSize() > 5){
//            path = File.separator+"sdcard"+File.separator+appName;
//        }else{
            path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + appName;
//        }
        return path;
    }

    public static final String THIRD_PARTY_FLAG = "thirdPartyFlag";
    private static final int REQUEST_CODE_1 = 101;
    private TextView start_xicoo_text;
    private void gotoVSMeasureApp() {
        try {
            Intent intent = new Intent("android.intent.action.MAIN");
            ComponentName name = new ComponentName("com.xicoo.vitalsignsmonitor", "com.xicoo.vitalsignsmonitor.MonitorActivity");
            intent.setComponent(name);
            Bundle bundle = new Bundle();
            bundle.putBoolean(THIRD_PARTY_FLAG, true);              //第三方APP调用
            PatientInfo patientInfo =new PatientInfo();
            patientInfo.title="查房";
            patientInfo.name="王小明";
            patientInfo.info="测量 血压、血氧、体温、脉搏";
            patientInfo.bedNo="+11";
            patientInfo.levelOfCare="二级护理";
            String s = JsonUtils.toJson(patientInfo, PatientInfo.class);
            Log.i(">>>>", "##PatientInfo json="+s);
            bundle.putString("PatientInfo", s);  //病人腕带ID

            intent.putExtras(bundle);
            startActivityForResult(intent, REQUEST_CODE_1);
        } catch (Exception e) {
            Toast.makeText(this, "请先安装体征测量APP", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_1) {
            if (resultCode == Activity.RESULT_OK) {
//                {"bodyTemp":0.0,"bpUnit":null,"dia":0,"map":0,"pi":0.0,"prBp":0,
//                        "prSpo2":0,"resp":0,"spo2":0,"sys":0,"tempUnit":null}

                String res = data.getStringExtra("result");
                VsResult vsResult = JsonUtils.parseGsonObject(res, VsResult.class);
//                JSONObject jsonObj = (JSONObject) JSON.parse(res);
//                VsResult model = JSONObject.toJavaObject(jsonObj, VsResult.class);

                start_xicoo_text.setText(vsResult.toString());
                Log.i("Recevier1", "接收到result:"+res);
                Log.i("Recevier1", "接收到result2:"+vsResult.toString());
            }else{
                start_xicoo_text.setText("无数据");
                Toast.makeText(this, "返回 无结果", Toast.LENGTH_SHORT).show();
            }
        }
    }


    /**
     * Created by Gaopingdong on 2018/11/19.
     */
    public class PatientInfo {
        public String title="体征测量";   // 测量界面 标题
        public String name="未知";    //患者名字
        public String info="";     //患者附件信息
        public String bedNo="?";    //床号
        public String levelOfCare="";  //护理等级 最好 四个字
        public String levelBgColor="#fd6f68"; //护理等级背景色
    }
    public class VsResult {
        public  float  bodyTemp;//体温
        public  String tempUnit;//体温单位

        public  int  spo2;    //血氧
        public  float  pi;      //灌注
        public  int  prSpo2; //血氧脉率

        public  int  sys;   //高压
        public  int  dia;   //低压
        public  int  map;   //平均压
        public  String bpUnit;//血压单位
        public  int  prBp;  //血压脉率

        public  int  resp;    //呼吸率脉率

        @Override
        public String toString() {
            String str;

            str="体温："+bodyTemp+tempUnit+"\r\n";
            str+= "血氧："+ spo2+"%   PI："+pi+"   脉率："+prSpo2+"次/分\r\n";
            str+= "血压："+ sys+"/"+dia+"("+map+")"+bpUnit+ "  脉率："+prBp+"次/分\r\n";
            str+= "呼吸："+ resp+"次/分\r\n";
            return str;
        }
    }

}