package com.app.chatme;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.List;

public class ChatScreenAdapter extends ArrayAdapter {
   public  String mUserId;
    private int resourceId;
    private List itemMessages;

    public ChatScreenAdapter(Context context,String resourceId, List messages) {
        super(context, 0, messages);//
        this.mUserId=resourceId;
        this.itemMessages=messages;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if(convertView==null){
            //Since convert View is null inflate the view
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.chat_item,parent,false);
            //initialize or create the view holder for each chat item
            final ViewHolder holder=new ViewHolder();
            holder.imageLeft=(ImageView)convertView.findViewById(R.id.ivProfileLeft);
            holder.imageRight=(ImageView)convertView.findViewById(R.id.ivProfileRight);
            holder.body=(TextView)convertView.findViewById(R.id.tvBody);
            holder.username=(TextView)convertView.findViewById(R.id.userId);
            //holder.userId=(TextView)convertView.findViewById(R.id.userId);
            //store the holder with the view
            convertView.setTag(holder);
        }

        Message message =(Message)getItem(position);
        final ViewHolder holder=(ViewHolder)convertView.getTag();
        //User chatUser=new User();
        // we've just avoided calling findViewById() on resource everytime
        // just use the viewHolder
        //holder = (ViewHolder)convertView.getTag();
        //final boolean isMe= message.getUsername().equals(mUserId); //.getUserId().equals(mUserId);//ParseUser.getCurrentUser().getObjectId());
        //final boolean isMe= message.getUserId().equals(mUserId);
        //Show-hide image based on the logged-in user.
        //Display the profile  image to the right for our user, left for other users
        //if(isMe) {
            holder.imageLeft.setVisibility(View.VISIBLE);
            holder.imageRight.setVisibility(View.GONE);
            holder.username.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            holder.body.setGravity(Gravity.AXIS_X_SHIFT |Gravity.RIGHT);

        //}

//       else
//        {
//            holder.imageLeft.setVisibility(View.VISIBLE);
//            holder.imageRight.setVisibility(View.GONE);
//            holder.username.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
//            holder.body.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER);
//        }


        //final ImageView profileView=isMe ? holder.imageLeft:holder.imageRight;
        final ImageView profileView=holder.imageLeft;

        final boolean isMe= message.getUserId().equals(mUserId);
        //Show-hide image based on the logged-in user.
        //Display the profile  image to the right for our user, left for other users
        if(isMe) {
            holder.imageLeft.setVisibility(View.VISIBLE);
            holder.imageRight.setVisibility(View.GONE);
            holder.username.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            holder.body.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);

        }

       else
        {
            holder.imageLeft.setVisibility(View.VISIBLE);
            holder.imageRight.setVisibility(View.GONE);
            holder.username.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            holder.body.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER);
        }
        Picasso.with(getContext()).load(getProfileUrl(message.getUserId())).into(profileView);
        holder.body.setText(message.getBody());
        holder.username.setText(message.getUsername());
        return convertView;

    }//End getView Method

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
        public ImageView imageLeft;
        public ImageView imageRight;
        public TextView body;
        public TextView userId;
        public TextView username;
    }

}
