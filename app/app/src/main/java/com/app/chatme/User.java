package com.app.chatme;

import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Big O on 11/9/2015.
 */
@ParseClassName("User")
public class User extends ParseObject{

    public User() {}

    public String objectId(){

        return getString("userId");
    }
    public String getUsername()
    {
        return getString("username");
    }
}
