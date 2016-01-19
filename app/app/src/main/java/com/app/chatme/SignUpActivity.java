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

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends Activity {
    EditText username;
    EditText password;
    EditText email;
    EditText confirmPassword;
    Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        username=(EditText)findViewById(R.id.usernameSignUpScreen);
        password=(EditText)findViewById(R.id.passwordSignUpScreen);
        confirmPassword=(EditText)findViewById(R.id.confirmPasswordSignUpScreen);
        email=(EditText)findViewById(R.id.emailSignUpScreen);
        signUpButton=(Button)findViewById(R.id.signUpBtnSignUpScreen);


        //StringBuilder
        findViewById(R.id.signUpBtnSignUpScreen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean validationError=false;
                StringBuilder validationErrorMessage=new StringBuilder("Please");
                if(isEmpty(username)){
                    validationError=true;
                    validationErrorMessage.append("Enter a Username");
                }
                if(isEmpty(password)||isEmpty(confirmPassword)){
                    if(validationError)//if validation Error is true, the username hasn't been entered
                    {
                        validationErrorMessage.append("and Password");
                    }
                    else
                    {
                        validationError = true; //if username has been entered
                        validationErrorMessage.append("Enter a password");
                    }
                }

                if(isEmpty(email)&& !validationError){
                    validationError=true;
                    validationErrorMessage.append("Enter your email");
                }
                if(isEmpty(username)&& isEmpty(password)&&isEmpty(email)){
                    validationError=false;
                    validationErrorMessage.append("Enter all the Fields");
                }
                if(isMatching(password,confirmPassword)&&!validationError){
                    Toast.makeText(getApplicationContext(),"Successfully Signed Up",Toast.LENGTH_LONG).show();

                }
                if(validationError){
                    Toast.makeText(getApplicationContext(),validationErrorMessage.toString(),Toast.LENGTH_LONG).show();
                    return;
                }

                //set a Progress Dialog
                final ProgressDialog dialog=new ProgressDialog(SignUpActivity.this);
                //dialog.setTitle("Please Wait");
                dialog.setMessage("Signing up Please wait");
                dialog.show();

                //Setup a New Parse User Object
                ParseUser user=new ParseUser();
                user.setUsername(username.getText().toString().toLowerCase());
                user.setPassword(password.getText().toString());
                user.setEmail(email.getText().toString());

                //call signupIn background so that it keeps the user's session on disk locally once it is done with signing up the user
                user.signUpInBackground(new SignUpCallback() { //we call the call back interface so it dismisses the dialog once its done with the signup, the
                    @Override
                    public void done(ParseException e) {
                        dialog.dismiss();

                        if(e!=null){
                            //if an error or exception or error exists
                            Toast.makeText(SignUpActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                        else {//if no errors proceed to the new activity
                            Intent chatWindowIntent=new Intent(SignUpActivity.this,LoginActivity.class);
                            //loose the app from all hogged resources
                            chatWindowIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
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

    private boolean isMatching(EditText textField1, EditText textField2)
    {
        if(textField1.getText().toString().trim().equals(textField2.getText().toString().trim()))
        {
            return true;
        }
        else
            return false;

    }

}
