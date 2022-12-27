package com.deepwaterooo.tetris3d;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void click(View view){
        Log.d(TAG, "click() ");
        // 点击跳转到 UnityActivity
        Intent intent = new Intent(this, TestUnityActivity.class);
        startActivity(intent);
    }

    @Override
    public  void onStop() {
        Log.d(TAG, "onStop() ");
        super.onStop();
    }
    @Override
    public  void onDestroy() {
        Log.d(TAG, "onDestroy() ");
        super.onDestroy();
    }
}