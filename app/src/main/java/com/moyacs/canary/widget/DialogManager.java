package com.moyacs.canary.widget;
import android.content.Context;



public class DialogManager {

    private static LoadingDialog mDialog;


    public static boolean isShow() {
        if (mDialog == null) {
            return false;
        }

        return mDialog.isShowing();

    }


    public static void showWarningDialog(Context context) {
        if (mDialog != null) {
            mDialog = null;
        }
        mDialog = new LoadingDialog(context);
        mDialog.show();
    }

    public static void showProgressDialog(Context context) {
        if (mDialog != null) {
            mDialog = null;
        }
        mDialog = new LoadingDialog(context);
    }


    public static void hideProgressDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
        }
    }


}