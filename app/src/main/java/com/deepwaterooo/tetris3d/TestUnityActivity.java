package com.deepwaterooo.tetris3d;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.deepwaterooo.tetris3d.UnityPlayerActivity;
import com.unity3d.player.UnityPlayer;

// public class TestUnityActivity extends UnityPlayerActivity {
public class TestUnityActivity extends Activity {
    private final String TAG = "TestUnityActivity";

    protected UnityPlayer mUnityPlayer;

// 自己子类中的方法    
    public void click(View view){
        Log.d(TAG, "click() ");
        callMainActivity();
    }
    private void callMainActivity(){
        Log.d(TAG, "callMainActivity() ");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    // @Override
    protected void onCreate(Bundle bundle) {
        this.requestWindowFeature(1);
        super.onCreate(bundle);
        this.getWindow().setFormat(2);
        // this.mUnityPlayer = new UnityPlayer(this); // <<<<<<<<<<<<<<<<<<<< 
        this.mUnityPlayer = new MyUnityPlayer(this);  // <<<<<<<<<<<<<<<<<<<< 如假包换
        // this.setContentView(this.mUnityPlayer); // <<<<<<<<<< 这里需要换成自己定义的视图
        // this.mUnityPlayer.requestFocus();
        
        // super.onCreate(bundle);
 
        setContentView(R.layout.activity_unity);
        LinearLayout ll_unity_container = (LinearLayout) findViewById(R.id.ll_unity_container);
        View unity_view = mUnityPlayer.getView();
        ll_unity_container.addView(unity_view);

        this.mUnityPlayer.requestFocus(); // <<<<<<<<<< 我觉得这里是需要的
    }

    protected void onNewIntent(Intent var1) {
        this.setIntent(var1);
    }

    protected void onDestroy() {
        Log.d(TAG, "onDestroy() ");
        this.mUnityPlayer.quit();
        super.onDestroy();
    }

    protected void onPause() {
        super.onPause();
        this.mUnityPlayer.pause();
    }

    protected void onResume() {
        super.onResume();
        this.mUnityPlayer.resume();
    }

    protected void onStart() {
        super.onStart();
        this.mUnityPlayer.start();
    }

    protected void onStop() {
        Log.d(TAG, "onStop() ");
        super.onStop();
        this.mUnityPlayer.stop();
    }

    public void onLowMemory() {
        super.onLowMemory();
        this.mUnityPlayer.lowMemory();
    }

    public void onTrimMemory(int var1) {
        super.onTrimMemory(var1);
        if (var1 == 15) {
            this.mUnityPlayer.lowMemory();
        }
    }

    public void onConfigurationChanged(Configuration var1) {
        super.onConfigurationChanged(var1);
        this.mUnityPlayer.configurationChanged(var1);
    }

    public void onWindowFocusChanged(boolean var1) {
        super.onWindowFocusChanged(var1);
        this.mUnityPlayer.windowFocusChanged(var1);
    }

    public boolean dispatchKeyEvent(KeyEvent var1) {
        return var1.getAction() == 2 ? this.mUnityPlayer.injectEvent(var1) : super.dispatchKeyEvent(var1);
    }

    public boolean onKeyUp(int var1, KeyEvent var2) {
        return this.mUnityPlayer.injectEvent(var2);
    }

    public boolean onKeyDown(int var1, KeyEvent var2) {
        return this.mUnityPlayer.injectEvent(var2);
    }

    public boolean onTouchEvent(MotionEvent var1) {
        return this.mUnityPlayer.injectEvent(var1);
    }

    public boolean onGenericMotionEvent(MotionEvent var1) {
        return this.mUnityPlayer.injectEvent(var1);
    }
}