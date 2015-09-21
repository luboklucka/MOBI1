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
import android.widget.ImageView;

public class GeigerActivity extends AppCompatActivity implements SensorEventListener {
    public MediaPlayer mp1;
    public MediaPlayer mp2;
    public MediaPlayer mp3;
    private boolean mInitialized;
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 600;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ImageView view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geiger);
        mp1 = MediaPlayer.create(GeigerActivity.this, R.raw.low_rad);
        mp2 = MediaPlayer.create(GeigerActivity.this, R.raw.med_rad);
        mp3 = MediaPlayer.create(GeigerActivity.this, R.raw.high_rad);
        view = (ImageView) findViewById(R.id.geiger_view);
        mInitialized = false;
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
    protected void onDestroy() {
        super.onDestroy();
        if (mp1.isPlaying()) {
            mp1.stop();
        }
        if (mp2.isPlaying()) {
            mp2.stop();
        }
        if (mp3.isPlaying()) {
            mp3.stop();
        }
    }
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
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

                if (last_y > 7.0000)
                {
                    mp1.start();
                    if (mp2.isPlaying()== true)
                    {
                        mp2.pause();
                    }
                    if (mp1.isPlaying()== false)
                    {
                        mp1.start();
                    }
                    view.setImageResource(R.drawable.rad);
                }
                if ((last_y > 3.0000) && (last_y < 6.99))
                {
                    if (mp1.isPlaying() == true)
                    {
                        mp1.pause();
                    }
                    mp2.start();
                    if (mp3.isPlaying())
                    {
                        mp3.pause();
                    }
                    if (mp2.isPlaying() == false)
                    {
                        mp1.start();
                    }
                    view.setImageResource(R.drawable.highrad);
                }
                if (last_y < 2.99)
                {
                    if (mp2.isPlaying()== true)
                    {
                        mp2.pause();
                    }
                    mp3.start();
                    view.setImageResource(R.drawable.pip);
                }
            }
        }
    }
    @Override

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
// can be safely ignored for this demo
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
//
        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
