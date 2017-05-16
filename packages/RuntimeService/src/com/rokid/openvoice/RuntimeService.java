package com.rokid.openvoice;

import android.app.Service;
import android.content.Intent;
import android.content.Context;
import android.os.IBinder;
import android.util.Log;
import android.net.NetworkInfo;
import android.net.ConnectivityManager;

public class RuntimeService extends Service{

	String TAG = getClass().getSimpleName();
	RuntimeNative mRuntimeNative = null;
	
	public RuntimeService(){
		Log.e(TAG, "RuntimeService  created ");
		mRuntimeNative = RuntimeNative.asInstance();
		mRuntimeNative.init();
		mRuntimeNative.addBinder(new RuntimeProxy());

		ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mNetworkInfo = cm.getActiveNetworkInfo();
		if(mNetworkInfo != null){
			mRuntimeNative.networkStateChange(true);
		}
	}

	@Override
	public void onCreate(){
		mUEventObserver.startObserving("/sys/class/android_usb/android0/f_audio_source/pcm");
	}

	class RuntimeProxy extends IRuntimeService.Stub{

		@Override
		public void nativeNlpMessage(String asr, String nlp, String action, int type){
			Log.e(TAG, "asr\t" + asr);
			Log.e(TAG, "nlp\t" + nlp);
			Log.e(TAG, "action\t" + action);
		}
	
		@Override
		public void sirenEvent(int event, double sl_degree, int has_sl){
			Log.e(TAG, event+" ,has_sl : " + has_sl + " ,sl_degree : " + (float)sl_degree);
		}
	}	

	private final android.os.UEventObserver mUEventObserver = new android.os.UEventObserver() {
		 
		@Override
		public void onUEvent(android.os.UEventObserver.UEvent event){
			Log.e(TAG, event.toString());
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}