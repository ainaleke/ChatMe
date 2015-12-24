package com.app.chatme;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.parse.ParseUser;

public class LoginSignUpActivity extends Activity {
    Button loginButton;
    Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_sign_up);

        loginButton=(Button)findViewById(R.id.loginButton);
        signUpButton=(Button)findViewById(R.id.signUpButton);

        //When this screen launches we want to check to see if there are users already logged in. If there are, go directly to the next activity
        ParseUser loggedInUser=ParseUser.getCurrentUser();
//
//        if(loggedInUser!=null)//there is a user already logged in
//        {
//            Intent loginIntent = new Intent(getApplicationContext(), ChatActivity.class);
//            startActivity(loginIntent);
//        }


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(loginIntent);
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signupIntent = new Intent(LoginSignUpActivity.this,SignUpActivity.class);
                startActivity(signupIntent);
            }
        });

    }

}
