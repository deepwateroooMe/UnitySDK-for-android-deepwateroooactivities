package com.deepwaterooo.tetris3d;

import android.content.Context;
import android.content.ContextWrapper;

import com.unity3d.player.UnityPlayer;

public class MyUnityPlayer extends UnityPlayer {
    private final String TAG = "MyUnityPlayer";

    public MyUnityPlayer(Context context) {
        super(context);
    }

    @Override protected void kill() {
        // super.kill();
    }
}