package com.deepwaterooo;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import com.deepwaterooo.dwsdk.R;
import com.deepwaterooo.dwsdk.UnityasrEventCallback;
import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

// 安卓SDK与unity游戏端最底层的双向桥接层:

// 那么这里桥接层两个类实现了双向调用,就可以达到互通目的了
// 但是可以去测试,游戏端可以借助桥接方法调用这个类里所定义的方法,实现游戏调用安卓端
// public class DWSDK extends AppCompatActivity { // 暂只设一个按钮,但是SDK这端的浓缩,这个按钮可转至游戏界面
// 这里就是急于模仿造成的: 必须得有活动上下文才能成为一个真正的活动呀!!! 但是这个上下文也仍然是可以从游戏端传过来的   
// public class DWSDK { // 暂只设一个按钮,但是SDK这端的浓缩,这个按钮可转至游戏界面
// 两个改成是类似的,可能会绕过这个问题,可是目的意义是什么呢? 活动的原生安卓SDK视图 与 兼容视图的区别 ?
public class DWSDK extends UnityPlayerActivity { // 暂只设一个按钮,但是SDK这端的浓缩,这个按钮可转至游戏界面
    private static final String TAG = "MainActivity";

// 自身的引用: 会跳至安卓SDK的一端,就不需要下面的这个索引了
    public static Activity mActivity;

    private static UnityasrEventCallback mCallback;
    private static AudioManager mAudioManager;
        
    // DWUnityActivity extends AppCompatActivity: 晚点儿的时候,我可能可以把它们合并,还是说在SDK中实现登录等等
    // public static UnityPlayer mUnityPlayer; // 只需要拿到它的视图并销毁就可以了
    // public static DWUnityActivity instance; // 相当于是单例模式

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
         Log.d(TAG, "onCreate() (mAudioManager == null): " + (mAudioManager == null));
        mActivity = this;
    }

    public static void SendUnityMessage(String methodName, String parameter) {
        Log.d(TAG, "SendUnityMessage() ");
        UnityPlayer.UnitySendMessage("MenuViewPanel", methodName, parameter != null ? parameter : ""); // for tmp
    }

    public void click(View view){
        Log.d(TAG, "click() ");
        Intent intent = new Intent(this, DWUnityActivity.class);
        startActivity(intent);
    }
    public static int getMaxVolume() {
        Log.d(TAG, "getMaxVolume() (mAudioManager == null): " + (mAudioManager == null));
        int max = mAudioManager.getStreamMaxVolume( AudioManager.STREAM_MUSIC );
        Log.d(TAG, "getMaxVolume() max: " + max);
        mCallback.onIntValReady(max, "max");
        return max;
    }
    public static int getCurrentVolume() {
        Log.d(TAG, "getCurrentVolume() (mAudioManager == null): " + (mAudioManager == null));
        int current = mAudioManager.getStreamVolume( AudioManager.STREAM_MUSIC );
        Log.d(TAG, "getCurrentVolume() current: " + current);
        mCallback.onIntValReady(current, "cur");
        return current;
    }
    public void setCurrentVolume(int val) {
        Log.d(TAG, "setCurrentVolume() val: " + val);
// 大概读取的时候,是不需要权限的;但如果要设置当前值,可能就需要权限.把权限检查放在设置函数里
        NotificationManager mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= VERSION_CODES.N
            && !mNotificationManager.isNotificationPolicyAccessGranted()) {
            Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            startActivity(intent);
        }
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, val, AudioManager.FLAG_PLAY_SOUND);
// TODO: 需要监听系统广播，需要得知是否设置成功了        
    }
    public static int add(int x, int y) { // 好像这个方法必须是静态的,那回去再试一下先前的项目
        Log.d(TAG, "add() ");
        String v = (x+y) + "";
        DWSDK.SendUnityMessage("onAddResultReady", v);
        return  x+y;
    }
    
    // 获取接口内容
    public static void setCallback(UnityasrEventCallback callback){
        Log.d("@@@", "UnityBatteryEventCallback setCallback start ");
        mCallback = callback;
        Log.d("@@@", "UnityBatteryEventCallback setCallback end ");
        mCallback.Test1("I love my dear cousin~! Must marry him");
        mCallback.Speechcontent(617);
    }
}