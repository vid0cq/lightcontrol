package com.illuminati.iss.lightcontrol;

import android.app.Service;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    SensorManager sensorManager;
    Sensor sensor;
    TextView tvLux;
    CallAPIs callapis = new CallAPIs();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvLux = (TextView) findViewById(R.id.tvLux);
        sensorManager = (SensorManager) getSystemService(Service.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);


//        try{
//            UDPClient.send();
//        }
//        catch (Exception e)
//        {
//            Toast.makeText(this,e.toString(),1000);
//        }

    }

    @Override
    protected void onPause(){
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume(){
        super.onResume();
        sensorManager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType()==Sensor.TYPE_LIGHT)
        {
            tvLux.setText(""+event.values[0]);

            final Float value = event.values[0];


            Runnable apiRunnable = new Runnable() {
                @Override
                public void run() {
                    callapis.Regulate(value, 30);
                    try {
                        Thread.sleep(250);
                    }
                    catch (Exception e) {}
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
