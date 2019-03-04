package com.moyacs.canary.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import com.moyacs.canary.MyApplication;
import java.io.File;

import www.moyacs.com.myapplication.BuildConfig;


/**
 * @Author: Administrator
 * @Date: 2018/11/9
 * @Description: 图片选择
 */
public class ChoicePhotoManger {
    public static final int TACK_ALBUM_CODE = 0X11;
    public static final int TACK_CAMERA_CODE = 0X12;
    public static final int CROP_IMAGE = 0X13;
    public static final String PICTURE_AUTHORITY = "com.roong.metlhome.fileprovider";
    public File takePath;
    public File cropPath;

    /**
     * 获取选择图片
     *
     * @return 选择图片
     */
    public File getTakeFile() {
        if (takePath == null) {
            takePath = new File(getCachePath(MyApplication.instance), System.currentTimeMillis() + ".jpg");
        }
        return takePath;
    }

    /**
     * 获取裁切图片
     *
     * @return 裁切图片
     */
    public File getCropFile() {
        if (cropPath == null) {
            cropPath = new File(getCachePath(MyApplication.instance), System.currentTimeMillis() + ".jpg");
        }
        return cropPath;
    }

    /**
     * 打开相册
     */
    public void takeAlbum(Activity activity) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        photoPickerIntent.setType("image/*");
        activity.startActivityForResult(photoPickerIntent, TACK_ALBUM_CODE);
    }

    /**
     * 拍照
     */
    public void takeCamera(Activity activity) {
        if (!hasSdCard()) {
            ToastUtils.showShort("请插入SD卡");
            return;
        }
        Uri fileUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            fileUri = FileProvider.getUriForFile(activity, "com.moyacs.canary.FileProvider", getTakeFile());
        } else {
            fileUri = Uri.fromFile(getTakeFile());
        }
        Intent intentCamera = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intentCamera.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        }
        intentCamera.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        activity.startActivityForResult(intentCamera, TACK_CAMERA_CODE);
    }

    /**
     * 剪切图片
     */
    public void cropImageUri(Activity activity, Uri fileUri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(fileUri, "image/*");
        intent.putExtra("crop", "true");// 设置裁剪
        intent.putExtra("circleCrop", "true"); //设置裁剪类型为圆形
        intent.putExtra("aspectX", 1); // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", ScreenUtil.dip2px(activity, 200));// outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputY", ScreenUtil.dip2px(activity, 200));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getCropFile()));//将剪切的图片保存到目标Uri中
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        activity.startActivityForResult(intent, CROP_IMAGE);
    }

    public void cropImageUri2(Activity activity, Uri fileUri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(fileUri, "image/*");
        intent.putExtra("crop", "true");// 设置裁剪
        intent.putExtra("aspectX", 1); // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectY", 1);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getCropFile()));//将剪切的图片保存到目标Uri中
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        activity.startActivityForResult(intent, CROP_IMAGE);
    }

    private static String getCachePath(Context context) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }

    private static boolean hasSdCard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 有存储的SDCard
            return true;
        } else {
            return false;
        }
    }
}
