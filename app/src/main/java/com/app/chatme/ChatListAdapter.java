package com.app.chatme;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.List;

public class ChatListAdapter extends ArrayAdapter {

    private Context mContext;
    private int resourceId;
    private String[] items;

    public ChatListAdapter(Context context, int textViewResourceId, String[] list) {
        super(context, textViewResourceId, list);
        mContext = context;
        resourceId = textViewResourceId;
        items = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ((ListView) parent).setItemChecked(position, true);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.chat_list_view_item, parent, false);
            final ViewHolder holder = new ViewHolder();
            holder.profilePicture = (ImageView) convertView.findViewById(R.id.chat_list_imageView);
            holder.usernameTextView = (TextView) convertView.findViewById(R.id.textView);
            convertView.setTag(holder);
        }

        //TextView text = (TextView) convertView.findViewById(R.id.textView);

//        if (items[position] != null) {
//            text.setText(items[position]);
//        }
        User userList =(User) getItem(position);
        final ViewHolder holder=(ViewHolder)convertView.getTag();

        holder.profilePicture.setVisibility(View.VISIBLE);

        //final ImageView profileView=isMe ? holder.imageLeft:holder.imageRight;
        final ImageView profileView=holder.profilePicture;
        Picasso.with(getContext()).load(getProfileUrl(userList.getUsername())).into(profileView);
        holder.usernameTextView.setText(userList.getUsername());
        return convertView;
    }

    //create a gravatar image based on the hash value obtained from userid
    private static String getProfileUrl(final String userId)
    {
        String hex="";
        try
        {
            final MessageDigest securityDigest =MessageDigest.getInstance("MD5");
            final byte[] hash = securityDigest.digest(userId.getBytes());
            final BigInteger bigInt = new BigInteger(hash);
            hex = bigInt.abs().toString(16);

        }
        catch(Exception e){
            e.printStackTrace();
        }
//        return "http://www.gravatar.com/avatar/" +hex + "?d=identicon";
        return "http://www.gravatar.com/avatar/"+hex +"?d=identicon";
    }

    final class ViewHolder {
        public ImageView profilePicture;
        private TextView usernameTextView;

    }

}