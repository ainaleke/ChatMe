package com.app.chatme;

import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by Big O on 11/9/2015.
 */
@ParseClassName("User")
public class User extends ParseUser {

    public User() {}

    public String getObjectId(){

        return getString("objectId");
    }
    public String getUsername()
    {
        return getString("username");
    }
}
