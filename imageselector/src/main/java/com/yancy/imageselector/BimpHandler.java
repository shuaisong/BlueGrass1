package com.yancy.imageselector;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;


public class BimpHandler {
    public static ArrayList<CameraImage> tempSaveBitmap = new ArrayList<CameraImage>();
    public static ArrayList<String> tempAddPhoto = new ArrayList<>();
    public static ArrayList<String> haveCompress = new ArrayList<>();
    public static ArrayList<String> mPhotoNum = new ArrayList<>();
    public static int max = 0;
    public static int num = 9;
    public static ArrayList<CameraImage> tempSelectBitmap = new ArrayList<CameraImage>();

    //public static HashMap<Integer, Bitmap> listSelectBitmap = new HashMap();
    public static Bitmap revitionImageSize(String path) throws IOException {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(
                new File(path)));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(in, null, options);
        in.close();
        int i = 0;
        Bitmap bitmap = null;
        while (true) {
            if ((options.outWidth >> i <= 1000)
                    && (options.outHeight >> i <= 1000)) {
                in = new BufferedInputStream(
                        new FileInputStream(new File(path)));
                options.inSampleSize = (int) Math.pow(2.0D, i);
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeStream(in, null, options);
                break;
            }
            i += 1;
        }
        return bitmap;
    }
}
