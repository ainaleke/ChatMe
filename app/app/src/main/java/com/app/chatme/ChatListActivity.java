package com.app.chatme;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.util.ArrayList;
import java.util.List;


public class ChatListActivity extends AppCompatActivity {
    Button logout;
    ParseUser currentUser;
    ListView lvChatList;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> chatList;
    int MAX_CHAT_FRIENDS_TO_SHOW=10;
    String currentUserId;
    protected List<ParseUser> mUser;
    private Handler chatListHandler=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        setTitle("Chats");

        //getActionBar().setIcon(R.drawable.chatapp_logo);

        lvChatList = (ListView) findViewById(R.id.chat_listView);
        //lvChatList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        queryFriendsList();

        lvChatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent= new Intent(getApplicationContext(),ChatActivity.class);
                intent.putExtra("EXTRA_MESSAGE",(String)lvChatList.getItemAtPosition(position));
                startActivity(intent);
            }
        });

        chatListHandler.postDelayed(runnable,3000);
    }

    private Runnable runnable =new Runnable()
    {
        @Override
        public void run() {
            refreshFriendsList();
            chatListHandler.postDelayed(this, 1000);
        }
    };

    private void refreshFriendsList()
    {
        queryFriendsList();
    }

    private void queryFriendsList() {
        ParseQuery<ParseUser> query=ParseUser.getQuery();
        //query all users excluding yourself
        query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());//orderByAscending("username");  //
        query.setLimit(MAX_CHAT_FRIENDS_TO_SHOW);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> usersList, ParseException e) {
                if (e == null) {
                    mUser = usersList;
                    //chatList.clear();
                    String[] usernames = new String[usersList.size()];
                    int i = 0;
                    for (ParseUser user : mUser) {
                        usernames[i] = user.getUsername();
                        i++;
                    }
                    arrayAdapter = new ChatListAdapter(getApplicationContext(), android.R.layout.simple_list_item_checked, usernames);
                    arrayAdapter.notifyDataSetChanged();

                    lvChatList.setAdapter(arrayAdapter);
                } else {
                    Toast.makeText(getApplicationContext(), "Error Loading Chat List", Toast.LENGTH_LONG).show();
                }
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id==R.id.action_logout){
          //  if(currentUser!=null) {

                currentUser.logOut();
                currentUser=null;
                Intent intent = new Intent(ChatListActivity.this, LoginSignUpActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            //        }
                }
        return super.onOptionsItemSelected(item);
    }
}
