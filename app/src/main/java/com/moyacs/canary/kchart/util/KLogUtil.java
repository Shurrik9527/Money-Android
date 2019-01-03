package com.moyacs.canary.kchart.util;


/**
 * 注意 调试的时候
 * 华为 荣耀6 红米1s（HM1S）日志 只能显示
 * android.util.Log.e
 * 其他级别不显示
 *
 * 遇见类似情况都可 提升log级别到Log.e
 *
 */
public class KLogUtil {
	public final static boolean isLog = false;
	public static final int LEAVEL = android.util.Log.ERROR;

	public static void v(String TAG, String content) {
		if (isLog) {
			if (LEAVEL == android.util.Log.ERROR)
				android.util.Log.e(TAG, content);
			else
				android.util.Log.v(TAG, content);

		}
	}

	public static void v(String TAG, String content, Throwable tr) {
		if (isLog) {
			if (LEAVEL == android.util.Log.ERROR)
				android.util.Log.e(TAG, content, tr);
			else
				android.util.Log.v(TAG, content, tr);
		}
	}

	public static void d(String TAG, String content) {
		if (isLog) {
			android.util.Log.d(TAG, content);
		}
	}

	public static void d(String TAG, String content, Throwable tr) {
		if (isLog) {
			android.util.Log.d(TAG, content, tr);
		}
	}

	public static void e(String TAG, String content) {
		if (isLog) {
			android.util.Log.e(TAG, content);
		}
	}

	public static void e(String TAG, String content, Throwable tr) {
		if (isLog) {
			android.util.Log.e(TAG, content, tr);
		}
	}

	public static void i(String TAG, String content) {
		if (isLog) {
			android.util.Log.i(TAG, content);
		}
	}

	public static void i(String TAG, String content, Throwable tr) {
		if (isLog) {
			android.util.Log.i(TAG, content, tr);
		}
	}

	public static void w(String TAG, String content) {
		if (isLog) {
			android.util.Log.w(TAG, content);
		}
	}

	public static void w(String TAG, String content, Throwable tr) {
		if (isLog) {
			android.util.Log.w(TAG, content, tr);
		}
	}
}
