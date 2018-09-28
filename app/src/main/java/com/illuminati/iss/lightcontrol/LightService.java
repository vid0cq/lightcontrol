package com.illuminati.iss.lightcontrol;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class LightService extends Service {

    private int counter=0;
    private CallAPIs callAPIs=new CallAPIs();

    public LightService(Context applicationContext) {
        super();
        Log.i("LIGHTSERVICE", "instantiated");
    }

    public LightService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        super.onStartCommand(intent, flags, startId);
        Toast.makeText(getApplicationContext(),"this is the light service",Toast.LENGTH_LONG).show();
        Log.i("LIGHTSERVICE", "onstartcommand!");
        startTimer();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("LIGHTSERVICE", "ondestroy!");
        Intent broadcastIntent = new Intent("com.illuminati.iss.lightcontrol.RestartLightService");
        sendBroadcast(broadcastIntent);
        stoptimertask();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

//    @Override
//    public void onTaskRemoved(Intent rootIntent){
//
//        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
//        restartServiceIntent.setPackage(getPackageName());
//        startService(restartServiceIntent);
//        super.onTaskRemoved(rootIntent);
//    }


    private Timer timer;
    private TimerTask timerTask;
    long oldTime=0;
    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, to wake up every 1 second
        timer.schedule(timerTask, 1000, 1000); //
    }

    /**
     * it sets the timer to print the counter every x seconds
     */
    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                Log.i("LIGHTSERVICE", "in timer ++++  "+ (counter+=10));
                Runnable apiRunnable = new Runnable() {
                    @Override
                    public void run() {
                        callAPIs.SendVslue(Integer.toString(counter));
                    }
                };

                Thread thread = new Thread(apiRunnable);
                thread.start();
            }
        };
    }

    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

}
