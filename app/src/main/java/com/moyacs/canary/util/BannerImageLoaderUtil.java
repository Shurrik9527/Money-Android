package com.moyacs.canary.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe banner 图片加载工具
 * @date 2019/2/26
 * @email 252774645@qq.com
 */
public class BannerImageLoaderUtil extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        Glide.with(context).load(path).into(imageView);
    }
}
