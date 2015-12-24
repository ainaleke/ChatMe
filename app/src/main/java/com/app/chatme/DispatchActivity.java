package com.app.chatme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


import com.parse.ParseUser;

import static com.parse.ParseUser.getCurrentUser;

/**
 * Created by Big O on 10/18/2015.
 */
public class DispatchActivity extends Activity {
    private static final String TAG=ChatActivity.class.getName();
    private static String useExistingUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ParseUser.getCurrentUser() != null)
        {
            //if user is logged in then start with existing user
            startWithCurrentUser();
            // Start an intent for the logged in activity
            startActivity(new Intent(getApplicationContext(), ChatActivity.class));
        }
        else {
            // prompt user to login or signup
            startActivity(new Intent(getApplicationContext(), LoginSignUpActivity.class));
        }
    }
    private void startWithCurrentUser(){
        useExistingUser = ParseUser.getCurrentUser().getObjectId();
    }



}
