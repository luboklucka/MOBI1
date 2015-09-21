package com.example.roman.geiger;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class LightSaberActivity extends AppCompatActivity implements SensorEventListener {
    public MediaPlayer mp1;
    public MediaPlayer mp2;
    public MediaPlayer mp3;
    public MediaPlayer mp4;
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 600;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    boolean isLighsaberOn=false;
    Button lightSaberBtn;
    Timer timer;
    TextView text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mp1 = MediaPlayer.create(LightSaberActivity.this, R.raw.lightsaberon);
        mp2 = MediaPlayer.create(LightSaberActivity.this, R.raw.lightsaberoff);
        mp4 = MediaPlayer.create(LightSaberActivity.this, R.raw.slowswing);


        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);

        setContentView(R.layout.activity_light_saber);
        text=(TextView)findViewById(R.id.force);
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
    }
    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void turnSaberOn(){
        lightSaberBtn=(Button)findViewById(R.id.lightSaber_btn);
        mp3 = MediaPlayer.create(LightSaberActivity.this, R.raw.humming);
         timer = new Timer();
        lightSaberBtn.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){

              if (isLighsaberOn==false){

                  if (mp1.isPlaying()==false) {
                      mp1.start();
                      isLighsaberOn = true;

                      mp3.start();
                  }
              }else{

                  if (mp1.isPlaying()==false){

                      mp2.start();
                      mp3.pause();
                      isLighsaberOn=false;
//                      TimerTask updateProfile = new CustomTimerTask(isLighsaberOn=false);
//                      timer.scheduleAtFixedRate(updateProfile , 0, 5000);
//

                  }

              }

            }

        });


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


                    text.setText(String.valueOf(netForce));

                if (isLighsaberOn==true) {
                    if (netForce > 1 & netForce < 3) {

                        if (mp4.isPlaying()) {

                            mp4.pause();
                            mp4.start();
                        } else {

                            mp4.start();
                        }

                    }
                    if (netForce < -0.1 & netForce < -3) {

                        if (mp4.isPlaying()) {

                            mp4.pause();
                            mp4.start();
                        } else {

                            mp4.start();
                        }

                    }
                }


//                if (last_y > 7.0000)
//                {
//                    mp1.start();
//                    if (mp2.isPlaying()== true)
//                    {
//                        mp2.pause();
//                    }
//                    if (mp1.isPlaying()== false)
//                    {
//                        mp1.start();
//                    }
//                    view.setImageResource(R.drawable.rad);
//                }
//                if ((last_y > 3.0000) && (last_y < 6.99))
//                {
//                    if (mp1.isPlaying() == true)
//                    {
//                        mp1.pause();
//                    }
//                    mp2.start();
//                    if (mp3.isPlaying())
//                    {
//                        mp3.pause();
//                    }
//                    if (mp2.isPlaying() == false)
//                    {
//                        mp1.start();
//                    }
//                    view.setImageResource(R.drawable.highrad);
//                }
//                if (last_y < 2.99)
//                {
//                    if (mp2.isPlaying()== true)
//                    {
//                        mp2.pause();
//                    }
//                    mp3.start();
//                    view.setImageResource(R.drawable.pip);
//                }
            }
        }
    }
}
