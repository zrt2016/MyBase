package com.zrt.mybase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 启动页面
 */
public class SplashActivity extends AppCompatActivity {
    int MSG_UPDATA_UI = 0x00000001;

    private final int SDK_PERMISSION_REQUEST = 0000000;

    private final int ACTION_MANAGE_OVERLAY_PERMISSION = 0000001; // 程序悬浮框权限
    private final int ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS = 0000002; // 勿扰模式权限设置

    private final int WHITE_PERMISSION_REQUEST = 0000003;

    private NotificationManager mNotificationManager = null;
    // 重复询问次数（超过两次询问，并且未“允许”，直接退出应用程序 ）
    private int repeatInquiryCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // 延时1.5秒后跳转MainActivity
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(MSG_UPDATA_UI);
            }
        },150);
//        mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
//
//        repeatInquiryCount = 0;
//        checkNotificationAccess();
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }
    };

    /**
     * 检查勿扰模式设置权限
     */
    private void checkNotificationAccess(){
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
//                && (mNotificationManager != null && !mNotificationManager.isNotificationPolicyAccessGranted())) {
////            ToastUtil.showMsg("请开启“移动护理”允许勿扰模式");
//            Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
//            startActivityForResult(intent, ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
//        }else{
            checkManageOverlayPersimmons();
//        }
    }

    private void checkManageOverlayPersimmons(){
        // 悬浮最上层权限
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if(!Settings.canDrawOverlays(getApplicationContext())) {
////                ToastUtil.showMsg("请开启“移动护理”允许显示在其他应用的上层");
//                //启动Activity让用户授权
//                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
//                intent.setData(Uri.parse("package:" + getPackageName()));
//                startActivityForResult(intent,ACTION_MANAGE_OVERLAY_PERMISSION);
//            }else {
//                getPersimmions();
//            }
//        }else {
            getPersimmions();
//        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS){
            checkNotificationAccess();
        }else if(requestCode == ACTION_MANAGE_OVERLAY_PERMISSION){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    getPersimmions();
                }else {
                    // Toast.makeText(this,"悬浮窗权限已被拒绝",Toast.LENGTH_SHORT).show();
                    SplashActivity.this.finish();
                    System.exit(0);
                }
            }
        }
    }

    ArrayList<String> permissionsList = null;
    @TargetApi(23)
    private void getPersimmions() {

        if(null == permissionsList){
            permissionsList = new ArrayList<String>();
        }else{
            permissionsList.clear();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            repeatInquiryCount ++;

            Log.i("zpd", "## 检查权限.. repeatInquiryCount=="+repeatInquiryCount);
            // 读写本地文件权限
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                permissionsList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                permissionsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            // 读取手机状态
            if(checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
                permissionsList.add(Manifest.permission.READ_PHONE_STATE);
            }
            // 拍照权限
            if(checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                permissionsList.add(Manifest.permission.CAMERA);
            }
            // 读取拨打电话日志权限
            if(checkSelfPermission(Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED){
                permissionsList.add(Manifest.permission.READ_CALL_LOG);
            }
            // 写入拨打电话日志权限
            if(checkSelfPermission(Manifest.permission.WRITE_CALL_LOG) != PackageManager.PERMISSION_GRANTED){
                permissionsList.add(Manifest.permission.WRITE_CALL_LOG);
            }
            // 蓝牙权限
            if(checkSelfPermission(Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED){
                permissionsList.add(Manifest.permission.BLUETOOTH);
            }

            // 拨打电话权限
            if(checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                permissionsList.add(Manifest.permission.CALL_PHONE);
            }

            // 录音权限
            if(checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
                permissionsList.add(Manifest.permission.RECORD_AUDIO);
            }

//			// NFC权限
//			if(checkSelfPermission(Manifest.permission.NFC) != PackageManager.PERMISSION_GRANTED){
//				permissionsList.add(Manifest.permission.NFC);
//			}
            /*
             * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
             */
            // 读取手机状态权限
            if (addPermission(permissionsList, Manifest.permission.READ_PHONE_STATE)) {
//				permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
            }
            // 读写权限
            if (addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//				permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
            }
            // 读取电话状态权限
            if (addPermission(permissionsList, Manifest.permission.READ_PHONE_STATE)) {
//				permissionInfo += "Manifest.permission.READ_PHONE_STATE Deny \n";
            }
            // 拍照权限
            if (addPermission(permissionsList, Manifest.permission.CAMERA)) {
//				permissionInfo += "Manifest.permission.READ_PHONE_STATE Deny \n";
            }
            // 读取拨打电话日志权限
            if (addPermission(permissionsList, Manifest.permission.READ_CALL_LOG)) {
//				permissionInfo += "Manifest.permission.READ_PHONE_STATE Deny \n";
            }
            // 写入拨打电话日志权限
            if (addPermission(permissionsList, Manifest.permission.WRITE_CALL_LOG)) {
//				permissionInfo += "Manifest.permission.READ_PHONE_STATE Deny \n";
            }

            // 使用蓝牙权限
            if (addPermission(permissionsList, Manifest.permission.BLUETOOTH)) {
//				permissionInfo += "Manifest.permission.READ_PHONE_STATE Deny \n";
            }


            if (addPermission(permissionsList, Manifest.permission.ACCESS_COARSE_LOCATION)) {
//				permissionInfo += "Manifest.permission.READ_PHONE_STATE Deny \n";
            }


            // 使用拨打电话权限
            if (addPermission(permissionsList, Manifest.permission.CALL_PHONE)) {
//				permissionInfo += "Manifest.permission.READ_PHONE_STATE Deny \n";
            }

//			// 使用NFC权限
//			if (addPermission(permissionsList, Manifest.permission.NFC)) {
////				permissionInfo += "Manifest.permission.READ_PHONE_STATE Deny \n";
//			}

            // 使用录音权限
            if (addPermission(permissionsList, Manifest.permission.RECORD_AUDIO)) {
//				permissionInfo += "Manifest.permission.READ_PHONE_STATE Deny \n";
            }

            if (permissionsList.size() > 0) {

                // 权限第二次拒绝后，退出应用程序
                if(repeatInquiryCount >=3){

//                    ToastUtil.showMsg("您拒绝了该应用的相关使用权限，请允许权限后使用！");
                    new Handler().postDelayed(new Runnable()
                    {
                        public void run()
                        {
                            SplashActivity.this.finish();
                        }
                    } , 3000L);
                    return;
                }
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]), SDK_PERMISSION_REQUEST);
            }else{
                goMain();
            }
        }else{
            goMain();
        }
    }

    @TargetApi(23)
    private boolean addPermission(ArrayList<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) { // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
            if (shouldShowRequestPermissionRationale(permission)){
                return true;
            }else{
                permissionsList.add(permission);
                return false;
            }
        }else{
            return true;
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode==SDK_PERMISSION_REQUEST){
            getPersimmions();
        }else if(requestCode == WHITE_PERMISSION_REQUEST){
            // 文件写入功能
            if (grantResults.length>0&&grantResults[0]== PackageManager.PERMISSION_GRANTED){
                goMain();
            }else{
//                ToastUtil.showMsg("您拒绝文件写入权限！");
                new Handler().postDelayed(new Runnable()
                {
                    public void run()
                    {
                        SplashActivity.this.finish();
                    }
                } , 2000L);
            }
        }
    }

//	//动态获取内存存储权限
//	public boolean verifyStoragePermissions() {
//		if (Build.VERSION.SDK_INT >= 23) {
//			//打开系统相机，判断是否有权限
//			if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
//				ActivityCompat.requestPermissions(this, new String[]{
//						Manifest.permission.WRITE_EXTERNAL_STORAGE }, WHITE_PERMISSION_REQUEST);
//				Log.i("cs","## 申请写入权限..."+Build.VERSION.SDK_INT);
//				return false;
//			}
//		}
//		return true;
//	}


    private void goMain(){
        //第一次加载时显示介绍页
//        if(1 == utils.getIsFirstInstall()){
//            utils.setIsFirstInstall(0);
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
//        }else{
//            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//            startActivity(intent);
//            finish();
//        }
    }
}