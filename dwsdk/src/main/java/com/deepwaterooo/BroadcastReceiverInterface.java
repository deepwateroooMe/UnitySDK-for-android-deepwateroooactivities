package com.deepwaterooo;
 
import android.content.Context;
import android.content.Intent;
 
// TODO:　这里的主要考虑因素是，不同包裹的查找发现，封在安卓SDK中能否被游戏端识别？还是说直接改变它的包裹名叫com.unity3d.player呢？
public abstract interface BroadcastReceiverInterface {
    public abstract void onReceive(Context var1, Intent var2);
}