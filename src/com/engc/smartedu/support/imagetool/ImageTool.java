package com.engc.smartedu.support.imagetool;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.util.DisplayMetrics;

import com.engc.smartedu.support.file.FileDownloaderHttpHelper;
import com.engc.smartedu.support.file.FileLocationMethod;
import com.engc.smartedu.support.file.FileManager;
import com.engc.smartedu.support.http.HttpUtility;
import com.engc.smartedu.support.settinghelper.SettingUtility;
import com.engc.smartedu.support.utils.GlobalContext;
import com.engc.smartedu.support.utils.Utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * User: Jiang Qi
 * Date: 12-8-3
 */
public class ImageTool {

    /**
     * 1. convert gif to normal bitmap
     * 2. cut bitmap
     */
    private static Bitmap getMiddlePictureInTimeLineGif(String filePath, int reqWidth, int reqHeight) {

//        try {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        options.inPurgeable = true;
        options.inInputShareable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

        if (bitmap == null)
            return null;


        int height = options.outHeight;
        int width = options.outWidth;

        int cutHeight = 0;
        int cutWidth = 0;

        if (height >= reqHeight && width >= reqWidth) {
            cutHeight = reqHeight;
            cutWidth = reqWidth;

        } else if (height < reqHeight && width >= reqWidth) {

            cutHeight = height;
            cutWidth = (reqWidth * cutHeight) / reqHeight;

        } else if (height >= reqHeight && width < reqWidth) {

            cutWidth = width;
            cutHeight = (reqHeight * cutWidth) / reqWidth;

        } else if (height < reqHeight && width < reqWidth) {


            float betweenWidth = ((float) reqWidth - (float) width) / (float) width;
            float betweenHeight = ((float) reqHeight - (float) height) / (float) height;

            if (betweenWidth > betweenHeight) {
                cutWidth = width;
                cutHeight = (reqHeight * cutWidth) / reqWidth;

            } else {
                cutHeight = height;
                cutWidth = (reqWidth * cutHeight) / reqHeight;

            }

        }

        if (cutWidth <= 0 || cutHeight <= 0) {
            return null;
        }

        Bitmap region = Bitmap.createBitmap(bitmap, 0, 0, cutWidth, cutHeight);

        if (region != bitmap) {
            bitmap.recycle();
            bitmap = region;
        }

        if (bitmap.getHeight() < reqHeight && bitmap.getWidth() < reqWidth) {
            Bitmap scale = Bitmap.createScaledBitmap(bitmap, reqWidth, reqHeight, true);
            if (scale != bitmap) {
                bitmap.recycle();
                bitmap = scale;
            }
        }


        Bitmap cornerBitmap = ImageEdit.getRoundedCornerBitmap(bitmap);
        if (cornerBitmap != bitmap) {
            bitmap.recycle();
            bitmap = cornerBitmap;
        }
        return bitmap;

//        } catch (OutOfMemoryError ignored) {
//            return null;
//        }
    }

    private static boolean isGif(String path) {
        return path.endsWith(".gif");
    }

    public static Bitmap getMiddlePictureInTimeLine(String url, int reqWidth, int reqHeight, FileDownloaderHttpHelper.DownloadListener downloadListener) {
//        try {

        String filePath = FileManager.getFilePathFromUrl(url, FileLocationMethod.picture_bmiddle);

        File file = new File(filePath);

        if (!file.exists() && !SettingUtility.isEnablePic()) {
            return null;
        }

        if (!file.exists()) {
            getBitmapFromNetWork(url, filePath, downloadListener);
        }

        if (isGif(filePath)) {
            return getMiddlePictureInTimeLineGif(filePath, reqWidth, reqHeight);
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        int height = options.outHeight;
        int width = options.outWidth;

        int cutHeight = 0;
        int cutWidth = 0;

        if (height >= reqHeight && width >= reqWidth) {
            cutHeight = reqHeight;
            cutWidth = reqWidth;

        } else if (height < reqHeight && width >= reqWidth) {

            cutHeight = height;
            cutWidth = (reqWidth * cutHeight) / reqHeight;

        } else if (height >= reqHeight && width < reqWidth) {

            cutWidth = width;
            cutHeight = (reqHeight * cutWidth) / reqWidth;

        } else if (height < reqHeight && width < reqWidth) {


            float betweenWidth = ((float) reqWidth - (float) width) / (float) width;
            float betweenHeight = ((float) reqHeight - (float) height) / (float) height;


            if (betweenWidth > betweenHeight) {
                cutWidth = width;
                cutHeight = (reqHeight * cutWidth) / reqWidth;

            } else {
                cutHeight = height;
                cutWidth = (reqWidth * cutHeight) / reqHeight;

            }

        }


        if (cutWidth > 0 && cutHeight > 0) {

            int startX = 0;

            if (cutWidth < width) {
                startX = (width - cutWidth) / 2;
            }


            try {
                BitmapRegionDecoder decoder = BitmapRegionDecoder.newInstance(filePath, false);
                if (decoder != null) {
                    Bitmap bitmap = decoder.decodeRegion(new Rect(startX, 0, startX + cutWidth, cutHeight), null);
                    if (bitmap.getHeight() < reqHeight && bitmap.getWidth() < reqWidth) {
                        Bitmap scale = Bitmap.createScaledBitmap(bitmap, reqWidth, reqHeight, true);
                        if (scale != bitmap) {
                            bitmap.recycle();
                            bitmap = scale;
                        }
                    }
                    if (bitmap != null) {
                        Bitmap roundedCornerBitmap = ImageEdit.getRoundedCornerBitmap(bitmap);
                        if (roundedCornerBitmap != bitmap) {
                            bitmap.recycle();
                            bitmap = roundedCornerBitmap;
                        }

                        return bitmap;
                    }
                }
            } catch (IOException ignored) {

            }

        }


        return null;
//        } catch (OutOfMemoryError ignored) {
//            ignored.printStackTrace();
//            return null;
//        }
    }


    public static Bitmap getRoundedCornerPic(String url, int reqWidth, int reqHeight, FileLocationMethod method) {
//        try {

        if (!FileManager.isExternalStorageMounted()) {
            return null;
        }

        String filePath = FileManager.getFilePathFromUrl(url, method);
        if (!filePath.endsWith(".jpg") && !filePath.endsWith(".gif"))
            filePath = filePath + ".jpg";

        boolean fileExist = new File(filePath).exists();

        if (!fileExist && !SettingUtility.isEnablePic()) {
            return null;
        }

        if (!fileExist) {
            boolean result = getBitmapFromNetWork(url, filePath, null);
            if (!result)
                return null;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        options.inPurgeable = true;
        options.inInputShareable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

        if (bitmap == null) {
            //this picture is broken,so delete it
            new File(filePath).delete();
            return null;
        }


        int[] size = calcResize(bitmap.getWidth(), bitmap.getHeight(), reqWidth, reqHeight);
        if (size[0] > 0 && size[1] > 0) {
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, size[0], size[1], true);
            if (scaledBitmap != bitmap) {
                bitmap.recycle();
                bitmap = scaledBitmap;
            }
        }

        Bitmap roundedBitmap = ImageEdit.getRoundedCornerBitmap(bitmap);
        if (roundedBitmap != bitmap) {
            bitmap.recycle();
            bitmap = roundedBitmap;
        }

        return bitmap;
//        } catch (OutOfMemoryError ignored) {
//            ignored.printStackTrace();
//            return null;
//        }
    }


    public static Bitmap getMiddlePictureInBrowserMSGActivity(String url, FileDownloaderHttpHelper.DownloadListener downloadListener) {

//        try {

        String filePath = FileManager.getFilePathFromUrl(url, FileLocationMethod.picture_bmiddle);

        File file = new File(filePath);

        if (!file.exists() && !SettingUtility.isEnablePic()) {
            return null;
        }

        if (!file.exists()) {
            getBitmapFromNetWork(url, filePath, downloadListener);

        }
        file = new File(filePath);
        if (file.exists()) {
            DisplayMetrics displayMetrics = GlobalContext.getInstance().getDisplayMetrics();
            return decodeBitmapFromSDCard(filePath, displayMetrics.widthPixels, 900);
        }
        return null;
//        } catch (OutOfMemoryError ignored) {
//            return null;
//        }
    }


    public static String getLargePictureWithoutRoundedCorner(String url, FileDownloaderHttpHelper.DownloadListener downloadListener, FileLocationMethod fileLocationMethod) {


        String absoluteFilePath = FileManager.getFilePathFromUrl(url, fileLocationMethod);

        File file = new File(absoluteFilePath);

        if (file.exists()) {
            return absoluteFilePath;

        } else {
            getBitmapFromNetWork(url, absoluteFilePath, downloadListener);

            if (isThisBitmapCanRead(absoluteFilePath)) {
                return absoluteFilePath;
            } else {
                return "about:blank";
            }


        }

    }

    public static boolean isThisBitmapCanRead(String path) {
        File file = new File(path);

        if (!file.exists()) {
            return false;
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int width = options.outWidth;
        int height = options.outHeight;
        if (width == -1 || height == -1) {
            return false;
        }

        return true;
    }


    public static int[] getBitmapSize(String path) {
        int[] result = {-1, -1};
        File file = new File(path);

        if (!file.exists()) {
            return result;
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int width = options.outWidth;
        int height = options.outHeight;
        if (width > 0 && height > 0) {
            result[0] = width;
            result[1] = height;
        }

        return result;
    }


    public static Bitmap decodeBitmapFromSDCard(String path,
                                                int reqWidth, int reqHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);

    }


    private static boolean getBitmapFromNetWork(String url, String path, FileDownloaderHttpHelper.DownloadListener downloadListener) {
        for (int i = 0; i < 3; i++) {
            if (HttpUtility.getInstance().executeDownloadTask(url, path, downloadListener)) {
                return true;
            }
        }
        return false;
    }


    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (height > reqHeight && reqHeight != 0) {
                inSampleSize = (int) Math.floor((double) height / (double) reqHeight);
            }

            int tmp = 0;

            if (width > reqWidth && reqWidth != 0) {
                tmp = (int) Math.floor((double) width / (double) reqWidth);
            }

            inSampleSize = Math.max(inSampleSize, tmp);

        }
        int roundedSize;
        if (inSampleSize <= 8) {
            roundedSize = 1;
            while (roundedSize < inSampleSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (inSampleSize + 7) / 8 * 8;
        }

        return roundedSize;
    }


    private static int[] calcResize(int actualWidth, int actualHeight, int reqWidth, int reqHeight) {


        int height = actualHeight;
        int width = actualWidth;


        float betweenWidth = ((float) reqWidth) / (float) actualWidth;
        float betweenHeight = ((float) reqHeight) / (float) actualHeight;

        float min = Math.min(betweenHeight, betweenWidth);

        height = (int) (min * actualHeight);
        width = (int) (min * actualWidth);

        return new int[]{width, height};
    }


    public static String compressPic(Context context, String picPath) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = 1;

        switch (SettingUtility.getUploadQuality()) {
            case 1:
                return picPath;
            case 2:
                options.inSampleSize = 2;
                break;
            case 3:
                options.inSampleSize = 4;
                break;
            case 4:
                options.inSampleSize = 2;

                if (Utility.isWifi(context)) {
                    return picPath;
                }
                break;
        }

        Bitmap bitmap = BitmapFactory.decodeFile(picPath, options);
        FileOutputStream stream = null;
        String tmp = FileManager.getUploadPicTempFile();
        try {
            new File(tmp).getParentFile().mkdirs();
            new File(tmp).createNewFile();
            stream = new FileOutputStream(new File(tmp));
        } catch (IOException ignored) {

        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        if (stream != null) {
            try {
                stream.close();
                bitmap.recycle();
            } catch (IOException ignored) {

            }
        }
        return tmp;
    }
}


