package com.illuminati.iss.lightcontrol;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class RestartLightService extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("LIGHTSERVICERCVER", "Service Stops! Oooooooooooooppppssssss!!!!");
        context.startService(new Intent(context, LightService.class));;
    }
}
