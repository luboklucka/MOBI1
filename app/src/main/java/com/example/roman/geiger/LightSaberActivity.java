package com.example.roman.geiger;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.hardware.Camera;

import android.widget.LinearLayout;

import java.io.IOException;
import java.util.Timer;
/*
*
* sebastian kumor
* */
public class LightSaberActivity extends AppCompatActivity implements SensorEventListener {
    public MediaPlayer mp1;
    public MediaPlayer mp2;
    public MediaPlayer mp3;
    public MediaPlayer mp4;
    public MediaPlayer mp5;
    public MediaPlayer mp6;
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 600;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    boolean isLighsaberOn=false;
    Button lightSaberBtn;
    Timer timer;
    public static Camera cam = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_light_saber);

        mp1 = MediaPlayer.create(LightSaberActivity.this, R.raw.lightsaberon);
        mp2 = MediaPlayer.create(LightSaberActivity.this, R.raw.lightsaberoff);
        mp3 = MediaPlayer.create(LightSaberActivity.this, R.raw.humming);
        mp4 = MediaPlayer.create(LightSaberActivity.this, R.raw.slowswing);
        mp5 =  MediaPlayer.create(LightSaberActivity.this, R.raw.fastswing2);
        mp6 =  MediaPlayer.create(LightSaberActivity.this, R.raw.clash);

//        try {
//            mp2.prepare();
//        } catch (IOException ex) {
//            // Ignore
//        }


        cam = Camera.open();


        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        turnSaberOn();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_light_saber, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
// can be safely ignored for this demo
    }
    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
        pauseStuff();
    }
    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        resumetuff();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        getRidOfresources();


    }


    public void turnSaberOn(){
        if (mp2.isPlaying()){
            mp2.stop();
        }
        lightSaberBtn=(Button)findViewById(R.id.lightSaber_btn);
        final float width =TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
        final float heigthSaber =TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics());

        lightSaberBtn.setLayoutParams(new LinearLayout.LayoutParams(Math.round(width), Math.round(heigthSaber)));
        lightSaberBtn.setBackgroundResource(R.drawable.lightoff);

         timer = new Timer();
        lightSaberBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                if (!isLighsaberOn) {

                    if (!mp1.isPlaying()) {
                        mp1.start();
                        isLighsaberOn = true;
                        lightSaberBtn.setLayoutParams(new LinearLayout.LayoutParams(Math.round(width), Math.round(heigthSaber)));
                        lightSaberBtn.setBackgroundResource(R.drawable.lighton);

                        mp3.start();


                    }
                } else {

                    if (!mp1.isPlaying()) {

                        mp2.start();

                        mp3.pause();
                        isLighsaberOn = false;
                        lightSaberBtn.setLayoutParams(new LinearLayout.LayoutParams(Math.round(width), Math.round(heigthSaber)));
                        lightSaberBtn.setBackgroundResource(R.drawable.lightoff);


                    }

                }

            }

        });


    }

    public void pauseStuff(){


        if(isLighsaberOn){

            mp4.pause();
            mp5.pause();
            mp3.pause();
        }

    }

    public void resumetuff(){

        if(isLighsaberOn){


            //humming
            mp3.start();
        }

    }

    public void getFlash(){


        Camera.Parameters p = cam.getParameters();
        p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        cam.setParameters(p);
       SurfaceTexture  mPreviewTexture = new SurfaceTexture(0);
        try {
            cam.setPreviewTexture(mPreviewTexture);
        } catch (IOException ex) {
            // Ignore
        }
        cam.startPreview();

    }

    public void getRidOfresources(){
        mp1.release();
        mp2.release();
        mp3.release();
        mp4.release();
        mp5.release();
        cam.release();


    }

    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];


            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float speed = Math.abs(x + y + z - last_x - last_y - last_z)/ diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {

                }

                last_x = x;
                last_y = y;
                last_z = z;
                double netForce=x*x;    //X axis
                netForce+=y*y;    //Y axis
                netForce+=z*z;    //Z axis (upwards)

                netForce = Math.sqrt(netForce)-mSensorManager.GRAVITY_EARTH;    //Take the square root, minus gravity
                // im setting the sound of the sword based on force calculated using accelerometer and adding the light effect when certain angle is reached

                if (isLighsaberOn) {

                    if (!mp5.isPlaying()) {
                        if (netForce > 1 & netForce < 4) {

                            if (mp4.isPlaying()) {

                                mp4.pause();
                                mp4.start();
                            } else {

                                mp4.start();
                            }

                        }
                        if (netForce < -0.1 & netForce < -4) {

                            if (mp4.isPlaying()) {

                                mp4.pause();
                                mp4.start();
                            } else {

                                mp4.start();
                            }

                        }
                    }
                     if(!mp4.isPlaying()) {
                         if (netForce > 4) {

                             if (mp5.isPlaying()) {

                                 mp5.pause();
                                 mp5.start();
                                 getFlash();
                                 cam.stopPreview();
                             } else {

                                 mp5.start();
                             }


                         }
                         if (netForce < -4) {

                             if (mp5.isPlaying()) {

                                 mp5.pause();
                                 mp5.start();
                                 getFlash();
                                 cam.stopPreview();


                             } else {

                                 mp5.start();
                             }


                         }

                     }
                    if (last_y < - 4.000)
                    {

                        mp6.start();
                    }

                }

            }
        }
    }
}
