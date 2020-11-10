package com.group10.cse5236project;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.SensorEvent;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.hardware.SensorEventListener;
import android.hardware.Sensor;

import static androidx.core.content.ContextCompat.getSystemService;


public class ShakeDetector {

    /*
    THE FOLLOWING VALUES ARE OPEN TO BE TWEAKED FOR BETTER PERFORMANCE
     */
    private static final int SHAKE_THRESHOLD = 800;
    private long lastUpdate = -1;
    /*END OF TWEAK-ABLE VARIABLES */

    SensorManager sensorManager;
    private int shakeCount = 0;
    float lastX, lastY, lastZ;



    public OnCreate(){
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        boolean hasAccelSensor = sensorManager.registerListener(mSensorListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        if(!hasAccelSensor){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            // set title
            alertDialogBuilder.setTitle("Accelerometer Sensor could NOT be found!");

            // set dialog message
            alertDialogBuilder
                    .setMessage("Your App MUST quit")
                    .setCancelable(false)
                    .setPositiveButton("Quit",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            /*when button clicked, close activity*/
                            currActivity.this.finish();
                        }});

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    public void killShakeDetector(){
        sensorManager.unregisterListener(mSensorListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
        sensorManager = null;

        //@TODO: super.onPause() here
    }

    private final SensorEventListener mSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                long currTime = System.currentTimeMillis();
                //CHOOSE UPDATE TIME FRAME
                //chosen to update every 100ms for now
                if (currTime - lastUpdate > 100) {
                    long timeDiff = currTime - lastUpdate;
                    lastUpdate = currTime;

                    float currx = sensorEvent.values[0];
                    float curry = sensorEvent.values[1];
                    float currz = sensorEvent.values[2];
                    float speed = Math.abs(currx + curry + currz - lastX - lastY - lastZ) / timeDiff * 10000;
                    if (speed > SHAKE_THRESHOLD) {
                        // Shake has been detected
                        shakeCount++;
                    }
                    lastX = currx;
                    lastY = curry;
                    lastZ = currz;
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
            //MAYBE RELEVANT IN THE FUTURE
        }
    };
}
