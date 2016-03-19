package com.app.chatme;

import android.app.Application;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class StarterApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        //Register your parse models here
        ParseObject.registerSubclass(Message.class);
        ParseObject.registerSubclass(User.class);
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // Add your initialization code here by adding both the Client and Application Key here
        Parse.initialize(this, "7mSUEdHf3LOUwGrMko795rY9kYXUXXSk0v0dHw8j", "0DHkUAWeXffw61q7s008rOj2i2Myce7atkCVw1bp");


       // ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access.
        // defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }

}
