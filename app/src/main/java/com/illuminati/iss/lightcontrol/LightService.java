package com.illuminati.iss.lightcontrol;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class LightService extends Service implements SensorEventListener {

    private int counter=0;
    private CallAPIs callAPIs=new CallAPIs();
    private SensorManager mSensorManager = null;

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

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mSensorManager.registerListener(this, sensor,
                SensorManager.SENSOR_DELAY_NORMAL);
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
        return null;
    }

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
                        //callAPIs.SendValue(Integer.toString(counter));
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

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType()==Sensor.TYPE_LIGHT)
        {
            Log.i("LIGHTSERVICE", "ligh sensor is "+ event.values[0]);

            final Float value = event.values[0];

            SharedPreferences settings = getSharedPreferences("BarValue", 0);
            final String memoryString = settings.getString("BarValue", "10");

            Runnable apiRunnable = new Runnable() {
                @Override
                public void run() {
                    callAPIs.Regulate(value, Double.valueOf(memoryString));
                }
            };

            Thread thread = new Thread(apiRunnable);
            thread.start();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
