package com.zrt.mybase.activity.viewdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.zrt.mybase.R;
import com.zrt.mybase.base.BaseActivity;
import com.zrt.mybase.utils.BitmapUtils;
import com.zrt.mybase.utils.ImageUtils.ImageDownload;
import com.zrt.mybase.utils.MyLogger;

/**
 * Bitmap转换为Base64:BitmapUtils.bitmapToBase64(bitmap2);
 * Base64转换为Bitmap:BitmapUtils.base64ToBitmap2(s,base64_image);
 */
public class ImageBase64Activity extends BaseActivity {
    String base64 = "iVBORw0KGgoAAAANSUhEUgAAAV4AAAFeAQAAAADlUEq3AAACW0lEQVR42u2bW44qMQxEvRPvf1deyEi%2BkLbLzsBIQ9w%2Fo1uNBCF98tMOflUQ%2F%2F31JYT%2FPGyyLn18M1F7vPz5ZjklmgThEfyc83X3cXvdeIzXijVYyxdBeAavB69rgToIyRkQhO%2BALY0Ru%2F7ipO4RvgUuE4STCZbwXXBzMuHEL3uUZ3%2FvkQh%2FBEdItJ9frxGW8McwrnDfHuFSc1rfZYyEP4UzHZErZEbM9LSI14jwBHYLrxLP37Cq0pJrDeEZHF4EhhB8CDw7jEL4GL6SP6%2FwmMm2tfVlQcJHcDz8ttcll7TE%2B%2FlJeAbH42%2B7XzJhqU6IZSZD%2BAyO3R5mifZTa4%2BkSTKTIXwIg1PPnp7BTlHerDfCIzhbpdU%2B1SoeBdW6OuEJbFIdvNj5GFdh6RU0CR%2FBZRT4G%2Bx8wbzbNwsS%2Fhiupl4UkaXGVC6Ynp%2FwIVwiS1aP%2BQNoRkESSPgYLpUldzlS7uypXsYiPIMFUgBipzSpqxfphI%2FhUrJgmAqfPRVUwiMYRw7QbXpRya85wiO4%2B5aKlTFwe0kCCZ%2FC1d4TiAJNX9wzRsJncImzGTrDsW%2BHPdChInwIQzu0veuULZBdeSR8Chts0hpPTVKUTRQgfA5Hle7V%2FsfOtzqcEEYhPIC7WqutTS11qiacOeEB3Irx5mcMZWNkg4Qn8JYFViVTHRGc9yA8gUstFK0DH%2FghiEAnIDyCcZ4Gbtug126iF%2BEZbO3I47bf%2FY1HInwD3E4yfVfGjfBdcPMtbki1U70VJTyD96Mdra7RXeYiPIJLAC9hK9qoukldhCcw%2F7X6H8H%2FAIcZK5WSieZPAAAAAElFTkSuQmCC";
    String base64_2 = "iVBORw0KGgoAAAANSUhEUgAAAPcAAADUCAYAAABAtLXLAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAAFiUAABYlAUlSJPAAAAWUSURBVHhe7ds9ktNIHMbhPRYpOenkhJMTExMTk3MBLsANuAS3GGBW\\/0at6lHZxst6mtW7z1OlGuvTTn7q9sf89QhEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEjeEEvckP758WR\\/9e99ev3789vbtuvbT90 f1kfXq3MeXrx4\\/P7u3bZODnFPUCG2iD58WLf8DKmiurScuyHUtSrwrq7btr15s265zhh3f1zX\\/fH163oERybuCX58\\/vz48PLlFlKpv7V aekjaQVX6109HuM df1rjHGXujm0a9\\/dtWtybOKeZAywIupxn9L3XRt3eRL4MEO4ZB93qdfW4jZ6H564J oBVny3jrt8\\/\\/ix7aulpvT1fHWNs8v79z vVTebcftynfor8GMT92Q9mH3cY7C\\/G3epG0efUvfzfnfpz88xiXuCUyPgc8U96jOEc8v2Hnu51qn9t\\/yEn\\/nE\\/cz6J9n798EVz7lg 75r4t5PvfuofY06vj3P8nzkEfcz6  zW5DDd9O3irvv60vffo322uqc \\/t1C0nEPcGTwJepcLlV3OPUe9x rd85h2MQ9yQ98D49v1Xco\\/32enxp2Ubu5TF5xD3R MHajLhr\\/dLS33OLO5O4\\/5AZcY\\/21 z21yaHuCeqH4c85\\/fc57YXcf\\/\\/iHui9qHaspQx7j49rm1tfWbc\\/Z9adts5PnFPsn14dXfX1p\\/Evfsu\\/BZx1wzh1Fdv 4j7T1D338NzfOKeZPsd9xpc\\/fpri\\/f \\/kl4fV fwv\\/TuFvYy01kPOZs3Mt6O2 4EZR6C8GxiXuSLeBdNH1Eb0t9VbaLr1R4Y6gtxnNxV9TL0h6v36mXc3GXfl63zTKG8zkecU\\/Sf8Qy6iNsC6ne 67HVIiXtOPPxN2XfZhb3PWjlyXwWu\\/H9BtP\\/y15H81\\/9Tr4bxP3BKd LNLCXuIat9dxW xLcH1avre\\/VulBtn3Ldevc9q ba8QPr15t 8elnbu 5  x13m17n34sYl7gjYqVyzrSFgj5Bbx8neM MloXvuWY8f9W4j798jryNwDrXNqfVzqenXTqGPb13LLzWQ7dp01tBtMf70npvAch7gn6KNmmw5XnGtI 7C7FvgSad9\\/atQ9Narup9H1Id4Y8SV9tN6W5TVybOKeoMLp0 ge0X7kPaWHX8fW X3ZR3wr443HlPz4xP0HnBqt4dbEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDaHEDZEeH\\/8Gyg6ctBvDaxAAAAAASUVORK5CYII=";

    ImageView base64_image;
    ImageView download_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_image_base64;
    }

    @Override
    protected void initView() {
        base64_image = findViewById(R.id.base64_image);
        download_image = findViewById(R.id.download_image);
    }

    @Override
    protected void initData() {
        Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.mipmap.erweima, null);
        base64_image.setImageBitmap(bitmap2);
        String s = BitmapUtils.bitmapToBase64(bitmap2);
//        MyLogger.Log().i("##bitmapToBase64="+s);
//        Bitmap bitmap = BitmapUtils.base64ToBitmap2(s,base64_image);
//        MyLogger.Log().i("##Bitmap="+bitmap.toString());
        Bitmap bitmap = BitmapUtils.base64ToBitmap(s);
        base64_image.setImageBitmap(bitmap);
        ImageDownload.downloadImage(download_image, ImageDownload.baiduURL);
    }
}