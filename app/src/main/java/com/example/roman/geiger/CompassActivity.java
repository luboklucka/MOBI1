package com.example.roman.geiger;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class CompassActivity extends AppCompatActivity implements SensorEventListener {

    // define the display assembly compass picture
    private ImageView image;

    // record the compass picture angle turned
    private float currentDegree = 0;
    private long lastUpdate = 0;
    private static final int SHAKE_THRESHOLD = 600;
    // device sensor manager
    private SensorManager mSensorManager;
    private Sensor magnetometer;
    private Sensor accelerometer;

    private float[] readingMagnetometer = new float[3];
    private float[] readingAccelerometer = new float[3];
    private float[] lastReadingAccelerometer = new float[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_compass);

        //
        image = (ImageView) findViewById(R.id.imageViewCompass);

        // initialize your android device sensor capabilities
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // for the system's orientation sensor registered listeners
        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // to stop the listener and save battery
        mSensorManager.unregisterListener(this, accelerometer);
        mSensorManager.unregisterListener(this, magnetometer);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        float[] rotation = new float[9];
        float[] orientation = new float[3];

        if(event.sensor == accelerometer)
        {
            readingAccelerometer[0] = event.values[0];
            readingAccelerometer[1] = event.values[1];
            readingAccelerometer[2] = event.values[2];
        }
        if(event.sensor == magnetometer)
        {
            readingMagnetometer[0] = event.values[0];
            readingMagnetometer[1] = event.values[1];
            readingMagnetometer[2] = event.values[2];
        }

        long curTime = System.currentTimeMillis();

        if ((curTime - lastUpdate) > 100) {
            long diffTime = (curTime - lastUpdate);
            lastUpdate = curTime;

            float speed = Math.abs(readingAccelerometer[0] + readingAccelerometer[1] + readingAccelerometer[2] - lastReadingAccelerometer[0] - lastReadingAccelerometer[1] - lastReadingAccelerometer[2])/ diffTime * 10000;

            if (speed > SHAKE_THRESHOLD) {
                lastReadingAccelerometer[0] = readingAccelerometer[0];
                lastReadingAccelerometer[1] = readingAccelerometer[1];
                lastReadingAccelerometer[2] = readingAccelerometer[2];
            }


                mSensorManager.getRotationMatrix(rotation, null, lastReadingAccelerometer, readingMagnetometer);
                mSensorManager.getOrientation(rotation, orientation);

                float degree = (float) (Math.toDegrees(orientation[0]) + 360) % 360;

                doAnimation(currentDegree, degree, image);

                currentDegree = -degree;

        }
    }

    private void doAnimation(float from, float to, View rot)
    {
        // create a rotation animation (reverse turn degree degrees)
        RotateAnimation ra = new RotateAnimation(
                from,
                -to,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);

        // how long the animation will take place
        ra.setDuration(210);

        // set the animation after the end of the reservation status
        ra.setFillAfter(true);

        // Start the animation
        rot.startAnimation(ra);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_compass, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
