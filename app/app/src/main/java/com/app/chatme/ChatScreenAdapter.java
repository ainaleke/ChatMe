package com.app.chatme;
import android.content.Context;
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
        ViewHolder holder=new ViewHolder();
        ImageView profileView;
        if(convertView==null){
            //Since convert View is null inflate the view
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.chat_item,parent,false);
            //initialize or create the view holder for each chat item
            holder.imageLeft=(ImageView)convertView.findViewById(R.id.ivProfileLeft);
            holder.imageRight=(ImageView)convertView.findViewById(R.id.ivProfileRight);
            holder.body=(TextView)convertView.findViewById(R.id.tvBody);
            holder.username=(TextView)convertView.findViewById(R.id.username);
            //store the holder with the view
            convertView.setTag(holder);
        }
        else{
            holder=(ViewHolder)convertView.getTag();
        }

        Message message =(Message)getItem(position);
       
        if(message.getSender().equals(ParseUser.getCurrentUser().getUsername())){
            holder.imageLeft.setVisibility(View.GONE);
            holder.imageRight.setVisibility(View.VISIBLE);
            //holder.body.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);

            //if file type is an Image
            if(message.getString(ParseConstantsClass.KEY_FILE_TYPE).equals(ParseConstantsClass.IMAGE_FILE_TYPE))//if filetype coming is of type image
            {
                holder.imageRight.setImageResource(R.drawable.ic_action_picture);
            }
            //if file type is video

            else if(message.getString(ParseConstantsClass.KEY_FILE_TYPE).equals(ParseConstantsClass.VIDEO_FILE_TYPE)) {
                holder.imageRight.setImageResource(R.drawable.ic_action_play);
            }
            //if file is text
            else{
                profileView=holder.imageRight;
                holder.body.setGravity(Gravity.RIGHT);
                Picasso.with(getContext()).load(getProfileUrl(message.getUserId())).into(profileView);
                holder.body.setText(message.getBody());
            }
        }
        else {
            holder.imageLeft.setVisibility(View.VISIBLE);
            holder.imageRight.setVisibility(View.GONE);
            //holder.body.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            //if file type is an Image
            if (message.getString(ParseConstantsClass.KEY_FILE_TYPE).equals(ParseConstantsClass.IMAGE_FILE_TYPE)) {
                holder.imageLeft.setImageResource(R.drawable.ic_action_picture);
            }
            //if file type is video
            else if(message.getString(ParseConstantsClass.KEY_FILE_TYPE).equals(ParseConstantsClass.VIDEO_FILE_TYPE)) {
                holder.imageLeft.setImageResource(R.drawable.ic_action_play);
            }
            //if file type is text
            else{
                profileView=holder.imageLeft;
                holder.body.setGravity(Gravity.LEFT);
                Picasso.with(getContext()).load(getProfileUrl(message.getUserId())).into(profileView);
                holder.body.setText(message.getBody());
            }
        }
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
            final BigInteger bigInteger = new BigInteger(hash);
            hex = bigInteger.abs().toString(16);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return "http://www.gravatar.com/avatar/"+hex +"?d=identicon";
    }

    final class ViewHolder {
        public ImageView imageLeft;
        public ImageView imageRight;
        public TextView body;
        public TextView username;
    }

}
