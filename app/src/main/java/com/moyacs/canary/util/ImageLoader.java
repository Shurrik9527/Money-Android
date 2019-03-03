/*
 * 乡邻小站
 *  Copyright (c) 2016 XiangLin,Inc.All Rights Reserved.
 */

package com.moyacs.canary.util;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;


/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/2/27
 * @email 252774645@qq.com
 */
public class ImageLoader {

    public static final String ANDROID_RESOURCE = "android.resource://";
    public static final String FOREWARD_SLASH = "/";


    private ImageLoader() {
    }

    private static class ImageLoaderHolder {
        private static final ImageLoader INSTANCE = new ImageLoader();
    }

    public static final ImageLoader getInstance() {
        return ImageLoaderHolder.INSTANCE;
    }

    /**
     * 加载网络图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    public void displayImage(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .centerCrop()
                .crossFade()
                .into(imageView);
    }

    /**
     * 加载网络图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    public void displayImage(Context context, String url, int errResID, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .centerCrop()
                .crossFade()
                .error(errResID)
                .into(imageView);
    }

    public void displayImageNocenterCrop(Context context, String url, int errResID, ImageView imageView) {
        Glide.with(context)
                .load(url)
                //.centerCrop()
                .crossFade()
                .error(errResID)
                .into(imageView);
    }

    /**
     * 加载SD卡图片
     *
     * @param context
     * @param file
     * @param imageView
     */
    public void displayImage(Context context, File file, ImageView imageView) {
        Glide.with(context)
                .load(file)
                .centerCrop()
                .into(imageView);
    }

    /**
     * 加载SD卡图片并设置大小
     *
     * @param context
     * @param file
     * @param imageView
     * @param width
     * @param height
     */
    public void displayImage(Context context, File file, ImageView imageView, int width, int height) {
        Glide.with(context)
                .load(file)
                .override(width, height)
                .centerCrop()
                .into(imageView);

    }

    /**
     * 加载网络图片并设置大小
     *
     * @param context
     * @param url
     * @param imageView
     * @param width
     * @param height
     */
    public void displayImage(Context context, String url, ImageView imageView, int width, int height) {
        Glide.with(context)
                .load(url)
                .centerCrop()
                .override(width, height)
                .crossFade()
                .into(imageView);
    }

    /**
     * 加载drawable图片
     *
     * @param context
     * @param resId
     * @param imageView
     */
    public void displayImage(Context context, int resId, ImageView imageView) {
        Glide.with(context)
                .load(resourceIdToUr(context, resId))
                .crossFade()
                .into(imageView);
    }

    /**
     * 加载drawable图片显示为圆形图片
     *
     * @param context
     * @param resId
     * @param imageView
     */
    public void displayCircleImage(Context context, int resId, ImageView imageView) {
        Glide.with(context)
                .load(resourceIdToUr(context, resId))
                .crossFade()
                .transform(new GlideCircleTransform(context))
                .into(imageView);
    }

    /**
     * 加载drawable图片显示为圆形图片(有默认图片)
     * @param context
     * @param resId
     * @param errResID
     * @param imageView
     */
    public void displayCircleImage(Context context, int resId, int errResID, ImageView imageView) {
        Glide.with(context)
                .load(resourceIdToUr(context, resId))
                .error(errResID)
                .crossFade()
                .transform(new GlideCircleTransform(context))
                .into(imageView);
    }

    /**
     * 加载网络图片显示为圆形图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    public void displayCircleImage(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .transform(new GlideCircleTransform(context))
                .crossFade()
                .into(imageView);
    }

    /**
     * 加载网络图片显示为圆形图片(有默认图)
     * @param context
     * @param url
     * @param errResID
     * @param imageView
     */
    public void displayCircleImage(Context context, String url, int errResID, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .error(errResID)
                .transform(new GlideCircleTransform(context))
                .crossFade()
                .into(imageView);
    }

    /**
     * 加载SD卡图片显示为圆形图片
     *
     * @param context
     * @param file
     * @param imageView
     */
    public void displayCircleImage(Context context, File file, ImageView imageView) {
        Glide.with(context)
                .load(file)
                .transform(new GlideCircleTransform(context))
                .into(imageView);
    }

    /**
     * 加载SD卡图片显示为圆形图片(带默认图)
     * @param context
     * @param file
     * @param errResID
     * @param imageView
     */
    public void displayCircleImage(Context context, File file, int errResID, ImageView imageView) {
        Glide.with(context)
                .load(file)
                .error(errResID)
                .transform(new GlideCircleTransform(context))
                .into(imageView);
    }


    public Uri resourceIdToUr(Context context, int resourceId) {
        return Uri.parse(ANDROID_RESOURCE + context.getPackageName() + FOREWARD_SLASH + resourceId);
    }


//    /**
//     * 四周圆角
//     *
//     * @param context
//     * @param url
//     * @param imageView
//     */
//    public void displayRounedCornersImage(Context context, String url, ImageView imageView) {
//        Glide.with(context)
//                .load(url)
//                .centerCrop()
//                .crossFade()
//                .bitmapTransform(new RoundedCornersTransformation(context, 30, 0))
//                .into(imageView);
//    }


}
