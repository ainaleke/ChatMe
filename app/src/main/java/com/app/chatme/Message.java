package com.app.chatme;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;


@ParseClassName("Message")

public class Message extends ParseObject{
    //default constructor is required

    public Message() {
        super();
    }

    public String getUsername()
    {
        return getString("sender");//getParseUser("username");
      //  return ParseUser.getCurrentUser().getUsername();//getParseUser("username");
    }
    public String get(String value)
    {

        return getString("value");
    }
    public String getUserId(){

        return getString("userId");
    }
    public String getBody(){

        return getString("body");
    }

    public String receiver(){

        return getString("receiver");
    }

    public void setUserid(String userId){

        put("userId",userId);
    }
    public void setUsername(String username){

        put("username",username);
    }

    public void setBody(String body){
        put("body",body);
    }



}
