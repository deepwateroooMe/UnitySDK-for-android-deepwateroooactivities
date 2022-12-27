package com.deepwaterooo;

import android.app.Activity;
import android.media.AudioManager;
import android.util.Log;

import com.deepwaterooo.dwsdk.UnityasrEventCallback;
import com.deepwaterooo.sdk.beans.ParentInfoDO;
import com.deepwaterooo.sdk.beans.PlayerDO;
import com.deepwaterooo.sdk.networklayer.NetworkUtil;
import com.deepwaterooo.sdk.utils.ApiCallListener;
import com.deepwaterooo.sdk.utils.PlayerUtil;
import com.deepwaterooo.sdk.utils.Util;
import com.unity3d.player.UnityPlayer;

import java.io.IOException;
import okhttp3.ResponseBody;

// Referenced classes of package com.deepwaterooo: DWUnityActivity
public class DWSDK {
    public static final String TAG = "DWSDK"; // spu

    public static final String UnityGameobjectName = "Deepwaterooo";
    public static final int REQUEST_CODE_MANAGE_PLAYER = 0;
    public static final int REQUEST_CODE_SELECT_PLAYER = 1;

    public DWSDK() {}

    public static void SendUnityMessage(String methodName, String parameter) {
        Log.d(TAG, "SendUnityMessage() methodName: " + methodName + ", parameter: " + parameter);
        UnityPlayer.UnitySendMessage("Deepwaterooo", methodName, parameter != null ? parameter : "");
    }
    public static boolean IsChildSelected() {
        Log.d(TAG, "IsChildSelected() ret (PlayerUtil.getSelectedPlayer(DWUnityActivity.instance) != null) " + (PlayerUtil.getSelectedPlayer(DWUnityActivity.instance) != null)); 
        return PlayerUtil.getSelectedPlayer(DWUnityActivity.instance) != null;
    }

    public static void KeepAppAlive() {
        Log.d(TAG, "KeepAppAlive() ");
        Util.keepAppAlive();
    }
    public static boolean IsInternetConnected() {
        return NetworkUtil.checkInternetConnection(DWUnityActivity.mUnityPlayer.getContext());
    }
    public static void UploadFileWithName(String data, String name) {
        PlayerDO player = PlayerUtil.getSelectedPlayer(DWUnityActivity.instance);
        if(player == null) {
            return;
        } else {
            String n = (new StringBuilder()).append(player.getId()).append("_").append(name).append(".json").toString();
            byte d[] = data.getBytes();
            NetworkUtil.uploadFile(DWUnityActivity.instance, new ApiCallListener() {
                    public void onResponse(Object o) { }
                    public void onFailure(Object o) { }
                }
                , d, n);
            return;
        }
    }
    public static void DownloadFileWithName(String name) {
        PlayerDO player = PlayerUtil.getSelectedPlayer(DWUnityActivity.instance);
        if(player == null) {
            return;
        } else {
            String n = (new StringBuilder()).append(player.getId()).append("_").append(name).append(".json").toString();
            NetworkUtil.downloadFile(DWUnityActivity.instance, new ApiCallListener() {
                    public void onResponse(Object o) {
                        ResponseBody r = (ResponseBody)o;
                        try {
                            byte b[] = r.bytes();
                            String retrieved = new String(b);
                            DWSDK.SendUnityMessage("_onLoadFileSuccess", retrieved);
                        } catch(IOException e) {
                            e.printStackTrace();
                            DWSDK.SendUnityMessage("_onLoadFileFail", "");
                        }
                    }
                    public void onFailure(Object o) {
                        DWSDK.SendUnityMessage("_onLoadFileFail", "");
                    }
                }
                , n);
            return;
        }
    }
    public static void Init() {
        Log.d(TAG, "Init() PlayerUtil.startSplashScreenActivity");
        PlayerUtil.startSplashScreenActivity(DWUnityActivity.instance);
    }
    public static void StartSplashScreenActivity() {
        PlayerUtil.startSplashScreenActivity(DWUnityActivity.instance);
    }
    public static void StartGameActivity() {}

    public static void ManagePlayers() { // 这里都是说在游戏端UnityPlayer活动的上下文(不是上下文,是当前活动)中打开SDK界面
        Log.d(TAG, "ManagePlayers() ");
        PlayerUtil.startManagePlayerActivity(DWUnityActivity.instance, 0);
    }
    public static boolean IsLoggedIn() {
//        ParentInfoDO info = PlayerUtil.getParentInfo(DWUnityActivity.instance);
//        return info != null;
        // 这里想要把这里跳过去,就直接返回 登录成功
        return true;
    }
    public static void StartSelectPlayerActivity() {
        PlayerUtil.startSelectPlayerActivity(DWUnityActivity.instance, false, 1);
    }
    public static void GetProfileURL() {
        String url = PlayerUtil.getSelectedPlayer(DWUnityActivity.instance).getProfileURL();
        SendUnityMessage("profileURLResponse", url);
    }
    public static void Terms() {
        PlayerUtil.showTermsNconditions(DWUnityActivity.instance);
    }
    public static void Privacy() {
        PlayerUtil.showPrivacyPolicy(DWUnityActivity.instance);
    }
    public static void Credits() {
        PlayerUtil.showCredits(DWUnityActivity.instance);
    }
    public static void StartPlaygroundActivity() {
        PlayerUtil.startPlaygroundActivity(DWUnityActivity.instance);
    }
    public static void StartParentalCheckActivity() {
        PlayerUtil.startParentalCheckActivity(DWUnityActivity.instance, 0);
    }
    public static void Logout() {
        PlayerUtil.logoutUser(DWUnityActivity.instance);
    }
    public static String GetSelectedPlayer() {
        return PlayerUtil.getSelectedPlayer(DWUnityActivity.instance).toString();
    }

// // TODO: 就是下面的几个方法没太弄明白: 好像是java 7的什么写法,可以暂时不用管    
//     public static void ShowAlertWarning(String title, String msg, String btnText, String methodName) { // 这个方法真的是没有用到
//         Util.showAlertWarning(DWUnityActivity.mUnityPlayer.getContext(), title, msg, btnText,
//                               new android.view.View.OnClickListener(methodName) {
//                 final String val$methodName; 
//                 public void onClick(View v) {
//                     DWSDK.SendUnityMessage(methodName, "");
//                 }
// // 这里好像是我自己整的,当时没太搞明白是怎么回事                                  
//                 {
//                     //private OnClickListener init(String s) { // <<<<<<<<<<<<<<<<<<<< 应该是这行是不需要添加的
//                      methodName = s;
//                     super();
//                 }
//             }
//             );
//     }
//     public static void ShowAlert(String title, String msg, String btnText1, String btnText2, String methodName1, String methodName2) {
//         Util.showAlert(DWUnityActivity.mUnityPlayer.getContext(), title, msg, Text1, Text2, new android.view.View.OnClickListener(methodName1) {
//                 final String val$methodName1;
//                 public void onClick(View v) {
//                     DWSDK.SendUnityMessage(methodName1, "");
//                 }
//                 {
//                     super();
//                     methodName1 = s;
//                 }
//             }, new android.view.View.OnClickListener(methodName2) {
//                 final String val$methodName2;
//                 public void onClick(View v) {
//                     DWSDK.SendUnityMessage(methodName2, "");
//                 }
//                 {
//                     methodName2 = s;
//                     super();
//                 }
//             });
//     }
    
//     // 获取接口内容
//     public static void setCallback(UnityasrEventCallback callback){
//         Log.d("@@@", "UnityBatteryEventCallback setCallback start ");
//         mCallback = callback;
//         Log.d("@@@", "UnityBatteryEventCallback setCallback end ");
//         mCallback.Test1("I love my dear cousin~! Must marry him");
//         mCallback.Speechcontent(617);
//     }
//     public static int getMaxVolume() {
//         Log.d(TAG, "getMaxVolume() (mAudioManager == null): " + (mAudioManager == null));
//         int max = mAudioManager.getStreamMaxVolume( AudioManager.STREAM_MUSIC );
//         mCallback.onIntValReady(max, "max");
//         return max;
//     }
//     public static int getCurrentVolume() {
//         Log.d(TAG, "getCurrentVolume() (mAudioManager == null): " + (mAudioManager == null));
//         int current = mAudioManager.getStreamVolume( AudioManager.STREAM_MUSIC );
//         mCallback.onIntValReady(current, "cur");
//         return current;
//     }
//     public void setCurrentVolume(int val) {
//         Log.d(TAG, "setCurrentVolume() val: " + val);
// // 大概读取的时候,是不需要权限的;但如果要设置当前值,可能就需要权限.把权限检查放在设置函数里
//         NotificationManager mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
//         if (Build.VERSION.SDK_INT >= VERSION_CODES.N
//             && !mNotificationManager.isNotificationPolicyAccessGranted()) {
//             Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
//             startActivity(intent);
//         }
//         mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, val, AudioManager.FLAG_PLAY_SOUND);
// // TODO: 怎么知道是否设置成功了?
//     }
//     public void click(View view){
//         Log.d(TAG, "click() ");
//         Intent intent = new Intent(this, DWUnityActivity.class);
//         startActivity(intent);
//     }
}
