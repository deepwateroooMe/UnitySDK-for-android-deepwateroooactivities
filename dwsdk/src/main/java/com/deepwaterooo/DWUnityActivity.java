package com.deepwaterooo;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ViewGroup;

import com.deepwaterooo.sdk.activities.BaseActivity;
import com.deepwaterooo.sdk.activities.DWBaseActivity;
import com.deepwaterooo.sdk.beans.PlayerDO;
import com.deepwaterooo.sdk.utils.PlayerUtil;
import com.unity3d.player.UnityPlayer;

// Referenced classes of package com.deepwaterooo: DWSDK
// 基类实现了LoginListener,这里就定义了这个接口类的两个回调方法
public class DWUnityActivity extends DWBaseActivity { // 主要负责桥接:　安卓SDK向游戏端 传送信号通知
    public static final String TAG = "DWUnityActivity";

    private static DWBaseActivity _instance;

    public static UnityPlayer mUnityPlayer;
    public static DWUnityActivity instance;

    private boolean _isScreenLocked;
    private boolean _fromBackground; // 是说: 来自 安卓SDK端?

    public DWUnityActivity() {
        _isScreenLocked = false;
        _fromBackground = false;
    }
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate() ");
        _isScreenLocked = false;
        requestWindowFeature(1);
        super.onCreate(savedInstanceState);
        if (mUnityPlayer == null) {
            getWindow().setFormat(2);
            mUnityPlayer = new UnityPlayer(this);
            instance = this;
        } else {
            ((ViewGroup)mUnityPlayer.getParent()).removeView(mUnityPlayer);
            UnityPlayer.currentActivity = this;
            instance = (DWUnityActivity)UnityPlayer.currentActivity;
        }
        setContentView(mUnityPlayer);
        mUnityPlayer.requestFocus();
    }
// TODO: 这里理解消化上的问题是: 感觉源码看得差不多了,可是还是没能想明白UnityPlayer游戏端的活动与普通安卓SDK中的有什么区别?    
    protected void onDestroy() { // 应用退出: 调用SDK等的时候，游戏只是暂停 
        Log.d(TAG, "onDestroy() ");
        mUnityPlayer.quit();
        super.onDestroy();
    }
// 接收处理某些活动的结果,并把结果通知给unity游戏端    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult() requestCode: " + requestCode + ", resultCode" + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1005) // 这里有点儿没有搞清楚,这些调用的前后逻辑,数字定义在常量管理里
            DWSDK.SendUnityMessage("UnlockPermissionResponse", "1");
        else if (resultCode == 1007)
            DWSDK.SendUnityMessage("UnlockPermissionResponse", "0");
    }
// 这里应该是从服务器触发得到的回调,它会进一步地回调给游戏端,传 1    
    protected void onSuccessLogoutEvent() { // 这里这个回调是如何触发的:　BaseActivity基类,可以去找一下
        Log.d(TAG, "onSuccessLogoutEvent() DWSDK.SendUnityMessage(SuccessLogout, 1)");
        DWSDK.SendUnityMessage("SuccessLogout", "1");
    }
    protected void onPause() {
        Log.d(TAG, "onPause() ");
        super.onPause();
        mUnityPlayer.pause();
    }
    protected void onResume() {
        Log.d(TAG, "onResume() mUnityPlayer.resume()");
        super.onResume();
        mUnityPlayer.resume();
        if (!_isScreenLocked) {
            if (_fromBackground) { // 要再想一下,这里这两个变量的设置
// // 这里会死掉: 因为公司服务器 登录 反馈逻辑定义为: 登录成功,则取出当前老师或是父母监护人帐户下的几个学生或是孩子用户供选一个当前玩家
//                 if (DWSDK.IsLoggedIn()) 
//                     PlayerUtil.startSelectPlayerActivity(instance, true, 1);
                _fromBackground = false;
            }
        } else {
            _isScreenLocked = false;
        }
    }
    public void gamePaused(boolean b) {
        Log.d(TAG, "gamePaused() DWSDK.SendUnityMessage(_onSDKScreenOpen, '')");
        DWSDK.SendUnityMessage("_onSDKScreenOpen", "");
        _isScreenLocked = b;
        _fromBackground = true; // 游戏暂停,说明SDK打开
    }

    public void onConfigurationChanged(Configuration newConfig) { // 安卓 ==＞ 游戏端
        super.onConfigurationChanged(newConfig);
        mUnityPlayer.configurationChanged(newConfig);
    }
    public void onWindowFocusChanged(boolean hasFocus) {
        Log.d(TAG, "onWindowFocusChanged() hasFocus: " + hasFocus);
        super.onWindowFocusChanged(hasFocus);
        mUnityPlayer.windowFocusChanged(hasFocus);
    }
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == 2)
            return mUnityPlayer.injectEvent(event);
        else
            return super.dispatchKeyEvent(event);
    }
    protected void didNavigatesToMainMenu() {
        Log.d(TAG, "didNavigatesToMainMenu() ==> _onSDKScreenClose");
        DWSDK.SendUnityMessage("_onSDKScreenClose", "");
    }
// LoginListener: 2 个方法    
    public void didFinishSdkUserConfiguration() {
        Log.d(TAG, "didFinishSdkUserConfiguration() ==> OnZPadFinishSDKUserConfig");
        DWSDK.SendUnityMessage("OnZPadFinishSDKUserConfig", ""); // <<<<<<<<<< 这里的名字好像是要改一下的
    }
    public void didSelectedChild(PlayerDO player) {
        PlayerUtil.setSelectedPlayer(instance, player);
        DWSDK.SendUnityMessage("_onProfileSelected", "");
        DWSDK.SendUnityMessage("_onSDKScreenClose", "");
    }
    public void didfinishSDKscreenflow() {
        Log.d(TAG, "didfinishSDKscreenflow() ==> _onSDKReady");
        DWSDK.SendUnityMessage("_onSDKReady", "");
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
}

// // 尝试源项目的方法,把流程再测一遍
// public class DWUnityActivity extends BaseActivity { 
//     private final String TAG = "DWUnityActivity";

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
// }