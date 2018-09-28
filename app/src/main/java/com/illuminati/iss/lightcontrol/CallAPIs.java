package com.illuminati.iss.lightcontrol;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CallAPIs extends AsyncTask<String, Integer, Object[]> {

    private  double previousError = 0;
    private  double dt = 0.2;


    protected boolean SendValue(String value) {

        Object[] result = doInBackground("http://192.168.0.109/lux?value=", value);

        return true;
    }


    @Override
    protected Object[] doInBackground(String... params) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            URL url = new URL(params[0] + params[1]);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                return new String[]{stringBuilder.toString()};
            } finally {
                urlConnection.disconnect();
            }
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }


    public void Regulate(double currValue, double expectedValue)
    {
        double error = expectedValue-currValue;
        double kp = 0.25;
        double kd = 0;

        double proportionalError = kp*error;
        double derivativeError = kd*(error-previousError)/dt;
        previousError = error;

        double output = proportionalError+derivativeError;
        if(output>100)
        {
            output=100;
        }
        else if(output<0)
        {
            output = 0;
        }

        SendValue(Double.toString(output));
    }

}
