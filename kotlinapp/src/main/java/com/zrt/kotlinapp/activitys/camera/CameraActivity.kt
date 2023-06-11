package com.zrt.kotlinapp.activitys.camera

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R
import kotlinx.android.synthetic.main.activity_camera.*
import java.io.File

class CameraActivity : BasicActivity() {
    val takeCamera = 1
    val takePicture = 2
    lateinit var imageUri: Uri
    lateinit var outputImage:File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun getLayoutResID(): Int {
        return R.layout.activity_camera
    }

    override fun initData() {
        a_c_takeCameraBtn.setOnClickListener {
            // 打开照相机
            startCamera()
        }
        a_c_takePictureBtn.setOnClickListener {
            // 从相册选择图片
            // 打开系统文件选择器：Intent.ACTION_OPEN_DOCUMENT
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            // 指定只显示图片
            intent.type = "image/*"
            startActivityForResult(intent, takePicture)
        }
    }

    /**
     * 打开照相机
     */
    fun startCamera(){
        // 创建File对象，用于存储拍照后的额图片
        // externalCacheDir: /sdcard/Android/data/<package name>/cache
        outputImage = File(externalCacheDir, "output_image.jpg")
        // 如果已存在则删除该文件
        if (outputImage.exists()){
            outputImage.delete()
        }
        outputImage.createNewFile()
        // Android7.0开始，使用本地真实路径的URI被认为是不安全的，会抛出FileUriExposeException异常
        imageUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            // 该file需要在AndroidManiFest。xml文件中注册
            FileProvider.getUriForFile(this, "com.zrt.kotlinapp.fileprovider", outputImage)
        }else{
            Uri.fromFile(outputImage)
        }
        val intent = Intent("android.media.action.IMAGE_CAPTURE")
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(intent, takeCamera)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            takeCamera -> {
                if (resultCode == Activity.RESULT_OK){
                    val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri))
                    a_c_iv.setImageBitmap(rotateIfRequired(bitmap, outputImage.path))
                }
            }
            takePicture -> {
                if (resultCode == Activity.RESULT_OK){
                    data?.data?.let { uri ->
                        // 将选择的图片显示
                        val bimapFromUri = getBimapFromUri(uri)
                        a_c_iv.setImageBitmap(bimapFromUri)
                    }
                }
            }
        }
    }

    private fun getBimapFromUri(uri: Uri):Bitmap? {
        return contentResolver.openFileDescriptor(uri, "r")?.use {
            BitmapFactory.decodeFileDescriptor(it.fileDescriptor)
        }
    }
//    // 获取图片角度，并对图片旋转
//    fun rotateIfRequired(bitmap: Bitmap):Bitmap{
//        val exif = ExifInterface(outputImage.path)
//        val attributeInt = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
//        return when(attributeInt){
//            ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap, 90)
//            ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap, 180)
//            ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap, 270)
//            else -> bitmap
//        }
//    }
//    // 旋转图片
//    private fun rotateBitmap(bitmap: Bitmap, degree: Int): Bitmap {
//        val matrix = Matrix()
//        matrix.postRotate(degree.toFloat())
//        val createBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
//        bitmap.recycle()
//        return createBitmap
//    }
}