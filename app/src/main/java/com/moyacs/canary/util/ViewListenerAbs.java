package com.moyacs.canary.util;

import android.view.View;

/**
 * @Author: Akame
 * @Date: 2019/1/8
 * @Description:
 */
public interface ViewListenerAbs {
    interface ItemClickListener {
        void onItemClickListener(View view, int pos);
    }
}
