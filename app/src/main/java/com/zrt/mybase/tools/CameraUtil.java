package com.zrt.mybase.tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;

/**
 * @author：Zrt
 * @date: 2021/7/20
 */
public class CameraUtil {
    private final String FILE_PARENT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    public final String FILE_PATH = "picture/escortpicture.jpg";
    private final int REQUEST_CODE_TAKE_PICTURE = 0x10000001;
//    private final String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "picture/escortpicture.jpg";

    private void requestCamera(Context context) {
        File outputImage = new File(FILE_PARENT_PATH, FILE_PATH);
        /*
        创建一个File文件对象，用于存放摄像头拍下的图片，我们把这个图片命名为output_image.jpg
        并把它存放在应用关联缓存目录下，调用getExternalCacheDir()可以得到这个目录，为什么要
        用关联缓存目录呢？由于android6.0开始，读写sd卡列为了危险权限，使用的时候必须要有权限，
        应用关联目录则可以跳过这一步
                 */
        try//判断图片是否存在，存在则删除在创建，不存在则直接创建
        {
            if (!outputImage.getParentFile().exists()) {
                outputImage.getParentFile().mkdirs();
            }
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
            Uri imageUri;
            if (Build.VERSION.SDK_INT >= 24) {
//                imageUri = FileProvider.getUriForFile(context,
//                        "com.zpd.mobilesdk.fileprovider", outputImage);
                imageUri = FileProvider.getUriForFile(context,
                        "com.zrt.mybase.fileprovider", outputImage);
            } else {
                imageUri = Uri.fromFile(outputImage);
            }
            //使用隐示的Intent，系统会找到与它对应的活动，即调用摄像头，并把它存储
            startCameraActivity(context, imageUri);
            //调用会返回结果的开启方式，返回成功的话，则把它显示出来
        } catch (IOException e) {
//            MyLogger.Log().i("##Camera Error="+e.toString());
        }
    }

    private void startCameraActivity(Context context, Uri imageUri) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        ((Activity) context).startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);
    }
}
