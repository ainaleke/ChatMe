package com.app.chatme;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
       // ActionBar actionBar=getActionBar();

        //hide Action Bar
        //        actionBar.hide();

        Context context=getApplicationContext();
        CharSequence text="Loading...";
        int duration= Toast.LENGTH_LONG;
        Toast toast=Toast.makeText(getApplicationContext(), "Loading", duration);
        toast.show();

        Thread logoTimer = new Thread()
        { //Initialize the thread
            public void run()
            {
                try
                {
                    sleep(5000);//this thread should sleep for 5secs
                    //start the new Activity
                    startActivity(new Intent(getApplicationContext(),LoginSignUpActivity.class));
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    finish();
                }
            }
        };
        logoTimer.start();
    }
}



