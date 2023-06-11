package com.zrt.mybase.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.media.ExifInterface;
import android.util.Base64;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public final class BitmapUtils {

	/**
	 * bitmap转为base64
	 * 
	 * @param bitmap
	 * @return
	 */
	public static String bitmapToBase64(Bitmap bitmap) {
		String result = null;
		ByteArrayOutputStream baos = null;
		try {
			if (bitmap != null) {
				baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);

				baos.flush();
				baos.close();

				byte[] bitmapBytes = baos.toByteArray();
				result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (baos != null) {
					baos.flush();
					baos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * base64转为bitmap
	 * 
	 * @param base64Data
	 * @return
	 */
	public static Bitmap base64ToBitmap(String base64Data) {
		try {
			byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
			return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		}catch(Exception ex) {
			ex.printStackTrace();
			MyLogger.Log().w("## 签名图片数据大小："+base64Data.length());
			MyLogger.Log().e("## 数据转换图片字节错误！"+ex.getMessage());
		}
		return null;
	}

	/**
	 *
	 * @Title: base64ToBitmap
	 * @Description: TODO(base64l转换为Bitmap)
	 * @param @param base64String
	 * @param @return 设定文件
	 * @param base64_image
	 * @return Bitmap 返回类型
	 * @throws
	 */
	public static Bitmap base64ToBitmap2(String base64String, ImageView base64_image) {
		int size = 100;
		byte[] decode = Base64.decode(base64String, Base64.DEFAULT);
		YuvImage yuvimage = new YuvImage(decode, ImageFormat.NV21,
				size, size, null);
		//
		//		 20、20分别是图的宽度与高度
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		yuvimage.compressToJpeg(new Rect(0, 0, size, size), 80, baos);// 80--JPG图片的质量[0-100],100最高
		byte[] jdata = baos.toByteArray();
		Bitmap bitmap = BitmapFactory.decodeByteArray(jdata, 0, jdata.length);


//		byte[] decode = Base64.decode(base64String, Base64.DEFAULT);
//		Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);

		return bitmap;
	}

		private static final String TAG = BitmapUtils.class.getSimpleName();
	/**
	 * 尺寸压缩（通过缩放图片像素来减少图片占用内存大小）
	 *
	 * @param bmp
	 * @param file
	 */
	public static void sizeCompress(Bitmap bmp, File file) {
		// 尺寸压缩倍数,值越大，图片尺寸越小
		int ratio = 8;
		// 压缩Bitmap到对应尺寸
		Bitmap result = Bitmap.createBitmap(bmp.getWidth() / ratio, bmp.getHeight() / ratio, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(result);
		Rect rect = new Rect(0, 0, bmp.getWidth() / ratio, bmp.getHeight() / ratio);
		canvas.drawBitmap(bmp, null, rect, null);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// 把压缩后的数据存放到baos中
		result.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(baos.toByteArray());
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static Bitmap sizeCompress(String path){
		return sizeCompress(path, 100f, 100f);
	}
	public static Bitmap sizeCompress(String path, float newWidth, float newHeight){
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		float originalW = options.outWidth;
		float originalH = options.outHeight;
		int be = 1;
		if (originalW > originalH && originalW > newWidth) {
			be = (int) (originalW / newWidth);
		}else if (originalW < originalH && originalH > newHeight) {
			be = (int) (originalH / newHeight);
		}
		if (be <= 0) be = 1;
		options.inSampleSize = be;
		options.inPreferredConfig = Bitmap.Config.RGB_565;//降低图片从ARGB888到RGB565
		options.inJustDecodeBounds = false;
		Bitmap resultBitmap = BitmapFactory.decodeFile(path, options);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// 把压缩后的数据存放到baos中
		resultBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte[] bytes = baos.toByteArray();
		resultBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//		return resultBitmap;

		//获取图片角度
		int readPictureDegree = readPictureDegree(path);
		MyLogger.Log().i("##readPictureDegree="+readPictureDegree);
		return rotaingImageView(readPictureDegree, resultBitmap);
	}

	/**
	 * 旋转图片
	 * @param angle
	 * @param bitmap
	 * @return Bitmap
	 */
	public static Bitmap rotaingImageView(int angle , Bitmap bitmap) {
		//旋转图片 动作
		Matrix matrix = new Matrix();;
		matrix.postRotate(angle);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}

	/**
	 * 读取图片属性：旋转的角度
	 * @param path 图片绝对路径
	 * @return degree旋转的角度
	 */
	public static int readPictureDegree(String path) {
		int degree  = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
			}
		} catch (IOException e) {
			MyLogger.Log().i("##readPictureDegree-Error="+e.toString());
		}
		return degree;
	}

	public static void releaseBitmap(Bitmap bitmap) {
		if (bitmap != null && !bitmap.isRecycled()) {
			bitmap.recycle();
			bitmap = null;
		}
	}

}
