package com.zrt.mybase.utils.ImageUtils;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.net.URL;

/**
 * @author：Zrt
 * @date: 2022/3/8
 */
public class ImageDownload {
    public final static String baiduURL = "https://www.baidu.com/img/pc_9c5c85e6b953f1d172e1ed6821618b91.png";
    /**
     * download image from network using @urladdress
     * https://www.baidu.com/img/pc_9c5c85e6b953f1d172e1ed6821618b91.png
     * @param urladdr http://10.81.2.38:8109/Public/image.png
     * @return
     */
    private static Drawable loadImageFromNetwork(String urladdr) {
        Drawable drawable = null;
        try{
            //judge if has picture locate or not according to filename
            drawable = Drawable.createFromStream(new URL(urladdr).openStream(), "image.png");
        }catch(IOException e){
            Log.d("test", "## image error="+e.getMessage());
        }
        if(drawable == null){
            Log.d("test","null drawable");
        }else{
            Log.d("test","not null drawable");
        }
        return drawable;
    }

    public static void downloadImage(final ImageView imageView, final String urladdress){
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Drawable drawable = loadImageFromNetwork(urladdress);
                //post() is quite important,update pictures in UI main thread
                imageView.post(new Runnable() {
                    @Override
                    public void run() {
                        //TODO Auto-generated method stub
                        if (drawable != null) {
                            imageView.setImageDrawable(drawable);
                        }else {
                            Log.d("test","## null drawable");
                        }
                    }

                });

            }
        }).start();
    }
}
