package com.deepwaterooo;
 
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

// TODO:　这里的主要考虑因素是，不同包裹的查找发现，封在安卓SDK中能否被游戏端识别？还是说直接改变它的包裹名叫com.unity3d.player呢？
// 这两个类只是为游戏unity端动态注册安卓广播提供了桥接，并不曾标明所想要注册的广播类型，会在游戏中根据需要来设定
public class UABroadcastReceiver extends BroadcastReceiver {
    String TAG = "UABroadcastReceiver";

    public static BroadcastReceiverInterface receiver;
    public UABroadcastReceiver() {  }
 
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Android has receive broadcast");
        Log.d(TAG, "(receiver != null): " + (receiver != null));
        if (receiver != null) {
            receiver.onReceive(context, intent);
        } else {
            Log.e(TAG, "BroadcastReceiverInterface receiver is null");
        }
    }
 
    public void setReceiver(BroadcastReceiverInterface unityReceiverProxy) {
        Log.d(TAG, "setReceiver");
        receiver = unityReceiverProxy;
    }
}
