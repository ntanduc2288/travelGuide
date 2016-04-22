package com.travel.travelguide.Ulti;

import android.util.Log;

import com.travel.travelguide.BuildConfig;

public class LogUtils {
	public static void logV(String TAG, String Message) {
		if (BuildConfig.DEBUG && Message!=null) {
			Log.v(TAG, Message);

		}

	}

	public static void logD(String TAG, String Message) {
		if (BuildConfig.DEBUG && Message!=null) {
			Log.d(TAG, Message);

		}

	}

	public static void logE(String TAG, String Message) {
		if (BuildConfig.DEBUG && Message!=null) {
			Log.e(TAG, Message);

		}

	}

	public static void logI(String TAG, String Message) {
		if (BuildConfig.DEBUG && Message!=null) {
			Log.i(TAG, Message);

		}

	}

	public static void logW(String TAG, String Message) {
		if (BuildConfig.DEBUG && Message!=null) {
			Log.w(TAG, Message);

		}

	}

	public static void logW(String TAG, String Message, Exception e) {
		if (BuildConfig.DEBUG && Message!=null) {
			Log.i(TAG, Message, e);
		}
	}

}
