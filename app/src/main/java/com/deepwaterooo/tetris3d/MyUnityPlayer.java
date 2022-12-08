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
        // 如果永远不杀进程,这个游戏端的进程是否会像存在于荒野中的流民一样造成内存泄露[这里整个游戏只有一个进程,该杀的时候它自然会被杀的.....]
    }
}
