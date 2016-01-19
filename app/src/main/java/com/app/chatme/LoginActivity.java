package com.app.chatme;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LoginActivity extends Activity {
    EditText username;
    EditText password;
    Button loginBtn;
    String usernameData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //getActionBar().setTitle("Login");

        username=(EditText)findViewById(R.id.usernameLoginScreen);
        password=(EditText)findViewById(R.id.passwordLoginScreen);
        loginBtn=(Button)findViewById(R.id.loginBtnLoginScreen);



        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        boolean validationError=false;
                        StringBuilder validationErrorMessage=new StringBuilder("Please");

                        if(isEmpty(username)){
                            validationError=true;
                            validationErrorMessage.append("Enter a Username");
                        }
                        if(isEmpty(password)){
                            if(validationError)//if validation Error is true, the username hasnt been entered
                            {
                                validationErrorMessage.append("and Password");
                            }
                            else
                            {
                                validationError = true; //if username has been entered
                                validationErrorMessage.append("Enter a password");
                            }
                        }
                        if(validationError){
                            Toast.makeText(getApplicationContext(),validationErrorMessage.toString(),Toast.LENGTH_LONG).show();
                            return;
                        }

                        //set a Progress Dialog
                        final ProgressDialog dialog=new ProgressDialog(LoginActivity.this);
                        //dialog.setTitle("Please Wait");
                        dialog.setMessage("Logging in. Please wait");
                        dialog.show();
                        if(username!=null)
                        {
                            usernameData=username.getText().toString();
                        }
                        // Call the ParseUser Class which logs in the user and checks the credentials in the cloud
                        ParseUser.logInInBackground(username.getText().toString().toLowerCase(), password.getText().toString(), new LogInCallback() {
                            @Override
                            public void done(ParseUser parseUser, ParseException e) {
                                dialog.dismiss();//dismiss dialog
                                if (e != null) {
                                    //if an error or exception or error exists
                                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                } else {//if no errors proceed to the new activity
                                    Intent chatWindowIntent = new Intent(LoginActivity.this, ChatListActivity.class);
                                    chatWindowIntent.putExtra("username", usernameData);
                                    //loose the app from all hogged resources
                                    chatWindowIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(chatWindowIntent);
                                }
                            }
                        });
                    }
                });
            }
    private boolean isEmpty(EditText editText)
    {
        if(editText.getText().toString().trim().length()>0){
            return false;
        }
        else return true;
    }

}
