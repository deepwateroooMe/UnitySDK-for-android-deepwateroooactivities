package com.deepwaterooo;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ViewGroup;
import com.deepwaterooo.dwsdk.activities.BaseActivity;
import com.deepwaterooo.dwsdk.utils.PlayerUtil;
import com.unity3d.player.UnityPlayer;

// 尝试源项目的方法,把流程再测一遍
public class DWUnityActivity extends BaseActivity {
    private final String TAG = "DWUnityActivity";

    private static BaseActivity _instance; // 安卓SDK 端

    public static UnityPlayer mUnityPlayer; // 游戏端
    public static DWUnityActivity Instance;

    private boolean _isScreenLocked;
    private boolean _fromBackground;

    public DWUnityActivity() {
        _isScreenLocked = false;
        _fromBackground = false;
    }

    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate() ");
        _isScreenLocked = false;
        this.requestWindowFeature(1);
        super.onCreate(savedInstanceState);
        if (mUnityPlayer == null) {
            this.getWindow().setFormat(2);
            mUnityPlayer = new UnityPlayer(this);
            Instance = this;
        } else {
            ((ViewGroup)mUnityPlayer.getParent()).removeView(mUnityPlayer);
            UnityPlayer.currentActivity = this;
            Instance = (DWUnityActivity)UnityPlayer.currentActivity;
        }
        setContentView(mUnityPlayer);
        mUnityPlayer.requestFocus();
    }
    protected void onDestroy() { // 这里就只适用于应用退出的时候 
        mUnityPlayer.quit();
        super.onDestroy();
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1005)
            DWSDK.SendUnityMessage("UnlockPermissionResponse", "1");
        else if (resultCode == 1007)
            DWSDK.SendUnityMessage("UnlockPermissionResponse", "0");
    }
// 这里应该是从服务器触发得到的回调,它会进一步地回调给游戏端,传 1    
    protected void onSuccessLogoutEvent() { // 这里这个回调是如何触发的:　BaseActivity基类
        DWSDK.SendUnityMessage("SuccessLogout", "1");
    }
    protected void onPause() {
        super.onPause();
        mUnityPlayer.pause();
    }
    protected void onResume() {
        super.onResume();
        mUnityPlayer.resume();
// 这里只作测试: 让它开一个小SDK中的登录界面 好了
        PlayerUtil.startSplashScreenActivity(Instance);
        // if (!_isScreenLocked) {
        //     if (_fromBackground) {
        //         if (DWSDK.IsLoggedIn()) // 只里只作测试
        //             // PlayerUtil.startSelectPlayerActivity(Instance, true, 1);
        //         _fromBackground = false;
        //     }
        // } else {
        //     _isScreenLocked = false;
        // }
    }
    public void gamePaused(boolean b) {
        DWSDK.SendUnityMessage("_onSDKScreenOpen", "");
        _isScreenLocked = b;
        _fromBackground = true;
    }
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mUnityPlayer.configurationChanged(newConfig);
    }
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        mUnityPlayer.windowFocusChanged(hasFocus);
    }
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(event.getAction() == 2)
            return mUnityPlayer.injectEvent(event);
        else
            return super.dispatchKeyEvent(event);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return mUnityPlayer.injectEvent(event);
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mUnityPlayer.injectEvent(event);
    }
    public boolean onTouchEvent(MotionEvent event) {
        return mUnityPlayer.injectEvent(event);
    }
    public boolean onGenericMotionEvent(MotionEvent event) {
        return mUnityPlayer.injectEvent(event);
    }
    public void batteryLevel(String s) {
        DWSDK.SendUnityMessage("_onBatteryLevel", s);
    }
    public void availableServices() {
    }
    protected void didNavigatesToMainMenu() {
        DWSDK.SendUnityMessage("_onSDKScreenClose", "");
    }
    public void didFinishSdkUserConfiguration() {
        DWSDK.SendUnityMessage("OnZPadFinishSDKUserConfig", "");
    }
    public void didfinishSDKscreenflow() {
        DWSDK.SendUnityMessage("_onSDKReady", "");
    }

    // public void didSelectedChild(PlayerDO player) {
    //     PlayerUtil.setSelectedPlayer(Instance, player);
    //     DWSDK.SendUnityMessage("_onProfileSelected", ""); // onUserLogin 强迫登录模式下才使用吧
    //     DWSDK.SendUnityMessage("_onSDKScreenClose", "");
    // }
}

// // 尝试源项目的方法,把流程再测一遍
// public class DWUnityActivity extends BaseActivity { 
// // public class DWUnityActivity extends UnityPlayerActivity { // 有个bug: 找不到DWSDK,换另一种方法试
// // public class DWUnityActivity extends AppCompatActivity { // 感觉这里面有什么地方没有适配好,无法实例化启动程序
//     private final String TAG = "DWUnityActivity";
//     // public static UnityPlayer mUnityPlayer;
//     // public static DWUnityActivity Instance; // 相当于是单例模式
//     // // 自身的引用:
//     // public static Activity mActivity;
//     @Override
//     protected void onCreate(Bundle bundle) {
//         super.onCreate(bundle);
// //         setContentView(R.layout.activity_unity);
// // // 想要加载的游戏界面的布局: 可以游戏的过程中动态添加
// //         LinearLayout ll_unity_container = (LinearLayout) findViewById(R.id.ll_unity_container);
// // // 说的是:基类的静态成员变量,但是它是可以拿到视图显示的,它拿到的应该是游戏端的视图,嵌套在安卓界面中(一个按钮)       
// //         View unity_view = mUnityPlayer.getView(); // <<<<<<<<<<<<<<<<<<<< 感觉这里可能就是那个前不着村后不着店的死槛
// //         ll_unity_container.addView(unity_view);
// // //        mActivity = this;
// // 手动适配 UnityPlayer 显示示图的过程        
//         requestWindowFeature(1);
//         super.onCreate(bundle);
//         if (mUnityPlayer == null) {
//             getWindow().setFormat(2);
//             mUnityPlayer = new UnityPlayer(this);
//             instance = this;
//         } else {
//             ((ViewGroup)mUnityPlayer.getParent()).removeView(mUnityPlayer);
//             UnityPlayer.currentActivity = this;
//             instance = (DWUnityActivity)UnityPlayer.currentActivity;
//         }
//         setContentView(mUnityPlayer); // 这里直接设置成游戏或是安卓视图,不再两端各带一个按钮
//         mUnityPlayer.requestFocus();
// //         setContentView(R.layout.activity_unity);
// // // 想要加载的游戏界面的布局: 可以游戏的过程中动态添加
// //         LinearLayout ll_unity_container = (LinearLayout) findViewById(R.id.ll_unity_container);
// // // 说的是:基类的静态成员变量,但是它是可以拿到视图显示的,它拿到的应该是游戏端的视图,嵌套在安卓界面中(一个按钮)       
// //         View unity_view = mUnityPlayer.getView(); // <<<<<<<<<<<<<<<<<<<< 感觉这里可能就是那个前不着村后不着店的死槛
// //         ll_unity_container.addView(unity_view);
//     }
//     // public void click(View view){
//     //     Log.d(TAG, "click() ");
//     //     callMainActivity();
//     // }
//     // @Override
//     // public boolean onKeyDown(int i, KeyEvent keyEvent) {
//     //     // 添加返回键返回 MainActivity
//     //     if (i == KeyEvent.KEYCODE_BACK){
//     //         callMainActivity();
//     //     }
//     //     return super.onKeyDown(i, keyEvent);
//     // }
//     // private void callMainActivity(){
//     //     Log.d(TAG, "callMainActivity() ");
//     //     Intent intent = new Intent(this, com.deepwaterooo.DWSDK.class);
//     //     startActivity(intent);
//     //     finish();
//     // }
// }