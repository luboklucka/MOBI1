package com.example.roman.geiger;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button compassBtn;
    Button geigerBtn;
    Button lightSaberBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonClicks();
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


        return super.onOptionsItemSelected(item);
    }

    public void buttonClicks(){

        compassBtn= (Button)findViewById(R.id.btn_compass);
        lightSaberBtn= (Button)findViewById(R.id.btn_lightSabers);
        geigerBtn= (Button)findViewById(R.id.btn_geiger);

        compassBtn.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){

//                Intent intent =new Intent(MainActivity.this,CompassActivity.class);
//                startActivity(intent);

            }

        });

        geigerBtn.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){

                Intent intent =new Intent(MainActivity.this,GeigerActivity.class);
                startActivity(intent);

            }

        });

        lightSaberBtn.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){

                Intent intent =new Intent(MainActivity.this,LightSaberActivity.class);
                startActivity(intent);

            }

        });



    }
}
