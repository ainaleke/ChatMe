package com.app.chatme;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.List;

public class ChatListAdapter extends BaseAdapter {

    private Context mContext;
    private String mresourceId;
    private List<ParseUser> items;

    public ChatListAdapter(Context context, String resourceId, List<ParseUser> list) {
        //super(context, textViewResourceId, list);
        mContext = context;
        mresourceId = resourceId;
        items = list;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;//used to tag items for easy reference
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //((ListView) parent).setItemChecked(position, true);
        ViewHolder holder = new ViewHolder();
        if (convertView == null) { //brand new we arent reusing any new views
            convertView = LayoutInflater.from(mContext).inflate(R.layout.chat_list_view_item, parent, false);
            holder.profilePicture = (ImageView) convertView.findViewById(R.id.profile_picture);
            holder.usernameTextView = (TextView) convertView.findViewById(R.id.textView);
            convertView.setTag(holder);
        }
        else {
            //if we scroll, get the convertView which has scrolled up, identified by its tag to be reused in the View Holder
            holder = (ViewHolder) convertView.getTag();
//            if (items[position] != null) {
//                text.setText(items[position]);
//                text.setTextColor(Color.BLACK);
//            }
        }
        //place in the new values or data into viewholder
        holder.usernameTextView= (TextView) convertView.findViewById(R.id.textView);
        holder.profilePicture= (ImageView) convertView.findViewById(R.id.profile_picture);

        ParseUser userList = (ParseUser) getItem(position);
        //Set visibility
        holder.profilePicture.setVisibility(View.VISIBLE);
        holder.usernameTextView.setVisibility(View.VISIBLE);

        //final ImageView profileView=isMe ? holder.imageLeft:holder.imageRight;
        final ImageView profileView = holder.profilePicture;
        Picasso.with(mContext).load(getProfileUrl(userList.getUsername())).into(profileView);
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

    private static class ViewHolder {
        public ImageView profilePicture;
        private TextView usernameTextView;

    }
}