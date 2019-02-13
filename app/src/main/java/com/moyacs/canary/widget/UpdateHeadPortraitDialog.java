package com.moyacs.canary.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import www.moyacs.com.myapplication.R;


/**
 * @Author: Administrator
 * @Date: 2018/11/9
 * @Description: 修改头像选择提示框
 */
public class UpdateHeadPortraitDialog extends Dialog {
    private ItemClickListen itemClickListen;
    private View view;

    public UpdateHeadPortraitDialog(Context context) {
        super(context, R.style.MyDialog);
        initView(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(view);
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.BOTTOM);
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
    }

    private void initView(Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.dialog_update_head_portrait, null);
        view.setBackgroundColor(Color.WHITE);
        TextView tvCamera = view.findViewById(R.id.tv_camera);
        TextView tvAlbum = view.findViewById(R.id.tv_album);
        tvCamera.setOnClickListener(v -> {
            dismiss();
            if (itemClickListen != null) {
                itemClickListen.cameraClickListen();
            }
        });

        tvAlbum.setOnClickListener(v -> {
            dismiss();
            if (itemClickListen != null) {
                itemClickListen.albumClickListen();
            }
        });
    }

    public interface ItemClickListen {
        void cameraClickListen();

        void albumClickListen();
    }

    public void setItemClickListen(ItemClickListen itemClickListen) {
        this.itemClickListen = itemClickListen;
    }
}
