package com.app.chatme;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
        ((ListView)parent).setItemChecked(position, true);
        if (convertView == null) {
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.chat_list_view_item, parent, false);
        }

        TextView text = (TextView) convertView.findViewById(R.id.textView);

        if (items[position] != null) {
            text.setText(items[position]);
        }

        return convertView;
    }
}