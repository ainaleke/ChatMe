package com.app.chatme;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {
    private static final String USER_ID_KEY="userId";
    private static String currentUserId;
    private List<ParseObject> arrayListMessages;
    private Button btSend;
    private String recipientName=null;
    private ChatScreenAdapter chatArrayAdapter;
    private EditText etMessage;
    private ListView lvChat;
    private boolean firstLoad;
    private static final int MAX_CHAT_MESSAGES_TO_SHOW=50;
    private Handler handler=new Handler();
    private String username=null;
    private String mediaFiletype=null;
    public static final int TAKE_PICTURE_REQ_CODE = 0;
    public static final int TAKE_VIDEO_REQ_CODE = 1;
    ImageView imagePreview;
    public static final int CHOOSE_PICTURE_REQ_CODE = 2;
    public static final int CHOOSE_VIDEO_REQ_CODE = 3;
    public static final int MEDIA_TYPE_IMAGE = 4;
    public static final int MEDIA_TYPE_VIDEO = 5;
    public static final int VIDEO_FILE_SIZE_LIMIT=1024*1024*10; //1024 bytes * 1024 bytes=1MB * 10=10MB
    protected Uri mediaUri;
    final String TAG = getClass().getName();

    protected DialogInterface.OnClickListener mediaDialogListener = new DialogInterface.OnClickListener()
    {
        public void onClick(DialogInterface dialog, int which)
        {
            switch (which)
            {
                case 0://TAKE PICTURE
                    Intent takePictureIntent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    mediaUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                    if(mediaUri==null) {
                        Toast.makeText(getApplicationContext(),R.string.error_external_storage, Toast.LENGTH_SHORT).show();
                    }

                    else {
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mediaUri);
                        startActivityForResult(takePictureIntent, TAKE_PICTURE_REQ_CODE);
//                        Intent imageUploadActivity=new Intent(ChatActivity.this,ImageUploadActivity.class);
//                        startActivity(imageUploadActivity);
                    }
                    break;
                case 1://TAKE VIDEO
                    Intent videoIntent= new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    mediaUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
                    if(mediaUri==null) {

                        Toast.makeText(getApplicationContext(), R.string.error_external_storage, Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        videoIntent.putExtra(MediaStore.EXTRA_OUTPUT,mediaUri);
                        videoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT,10);
                        videoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,0);
                        startActivityForResult(videoIntent,TAKE_VIDEO_REQ_CODE);
                    }
                    break;
                case 2: //CHOOSE PICTURE
                    Intent choosePictureIntent=new Intent(Intent.ACTION_GET_CONTENT);
                    choosePictureIntent.setType("image/*");
                    startActivityForResult(choosePictureIntent,CHOOSE_PICTURE_REQ_CODE);

                    break;
                case 3: //CHOOSE VIDEO
                    Intent chooseVideoIntent=new Intent(Intent.ACTION_GET_CONTENT);
                    chooseVideoIntent.setType("video/*");
                    Toast.makeText(ChatActivity.this, "The Selected Video Must be Less than 10MB", Toast.LENGTH_LONG).show();
                    startActivityForResult(chooseVideoIntent, CHOOSE_VIDEO_REQ_CODE);
                    break;
            }

        }
        //Anonymous Inner class
        private Uri getOutputMediaFileUri(int mediaType) {
            File mediaFile=null;
            //To be safe you ,must check whether external storage output is available
            //Using Environment.getExternalPublicStorage
            if(isExternalStorageAvailable())
            {
                //get the URi

                //1. Get the external storage directory
                String appName=ChatActivity.this.getString(R.string.app_name);
                File mediaStorageDir=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

                if(!mediaStorageDir.exists())
                {
                    //then create a new one
                    boolean file=mediaStorageDir.mkdirs();
                    if(!file)
                    {
                        Log.e(TAG,"Error! Could not create Picture Direcrory");
                        return null; //if it cant create file then exit
                    }
                }
                File appSubdirectory=new File(mediaStorageDir,appName);
                Date now=new Date();
                String timeStamp=new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(now);

                //2. Create our subdirectory
                String path=mediaStorageDir.getPath() + File.separator;
                if(mediaType==MEDIA_TYPE_IMAGE)
                {
                    mediaFile=new File(path +"IMG"+timeStamp+".jpg");
                }
                else if(mediaType==MEDIA_TYPE_VIDEO)
                {
                    mediaFile=new File(path +"VID"+timeStamp+".mp4");
                }
                //3. Create a file name
                //4.Create a File
                //5. Return the file's URi
                Log.d(TAG,"File: "+Uri.fromFile(mediaFile));

                return Uri.fromFile(mediaFile);
            }
            else {
                return null;
            }
        }
        private boolean isExternalStorageAvailable()
        {
            String state = Environment.getExternalStorageState();
            if(state.equals(Environment.MEDIA_MOUNTED))
            {
                return true;
            }
            else
            {
                return false;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);//ParseUser.getCurrentUser().toString()
        //Get intent Data
        Intent intent = getIntent();
        username=intent.getStringExtra("EXTRA_MESSAGE");
        //set the title bar to the Clicked on user name
        recipientName=username.toString().toLowerCase();
        setTitle(username);
        //getActionBar().setDisplayHomeAsUpEnabled(true);

        //Display Toast text-For debugging purposes
        Toast.makeText(getApplicationContext(), ParseUser.getCurrentUser().getUsername(), Toast.LENGTH_LONG).show();
        //Set default Public Read Access for everyone
        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);

        currentUserId =ParseUser.getCurrentUser().getObjectId();
        //Find the text field and button
        etMessage=(EditText)findViewById(R.id.etMessage);
        btSend=(Button)findViewById(R.id.btSend);
        lvChat=(ListView)findViewById(R.id.lvChat);

        //create an array List which contains list of message data which would be bound to the view
        arrayListMessages=new ArrayList<ParseObject>();

        //Automatically scroll to the bottom when a data set change notification is received
        lvChat=(ListView)findViewById(R.id.lvChat);
        lvChat.setTranscriptMode(1);
        firstLoad=true;
        //bind adapter to List View
        chatArrayAdapter=new ChatScreenAdapter(getApplicationContext(), currentUserId,arrayListMessages);
        //chatArrayAdapter.getView();
        lvChat.setAdapter(chatArrayAdapter);
        receiveMessages();

        //when send button is clicked, create message object on parse
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = etMessage.getText().toString();
                ParseObject message = ParseObject.create("Message");
                message.put("userId", currentUserId);
                //message.put("username", ParseUser.getCurrentUser().getUsername());
                message.put("sender", ParseUser.getCurrentUser().getUsername());
                message.put("recipient", recipientName);
                message.put("chat_identifier", stringSort(ParseUser.getCurrentUser().getUsername(),recipientName));
                message.put(ParseConstantsClass.KEY_FILE_TYPE, "text");
                message.put("body", data);
                //createAndSendMessage(data, message);
                //Since the local datastore is enabled, you can store an object by pinning it.the data
                //is saved by recursively storing every object and file that message points to.
                message.pinInBackground();

                message.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {

                        }
                        else {
                            //error occured
                            //Toast.makeText(getApplicationContext(), "Something went wrong.Please check Connection", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                //String chatText=etMessage.getText().toString();
                etMessage.setText("");
            }
        });
        handler.postDelayed(runnable, 2000);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            refreshMessages();
            handler.postDelayed(this, 2000);
        }
    };



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK) {
            //This if indicates if a picture or video is taken with the camera
            if (requestCode == CHOOSE_PICTURE_REQ_CODE || requestCode == CHOOSE_VIDEO_REQ_CODE) {
                if (data == null) {
                    Toast.makeText(getApplicationContext(), "Oh Snap! Something went wrong", Toast.LENGTH_LONG).show();
                } else {
                    mediaUri = data.getData(); //if the result code indicates that we are picking either a picture or video from the gallery then get the data
                    //after getting the data we want to check if its a video and if its within the given range
                    //imagePreview.setImageURI(mediaUri);
                    Log.i(TAG, "Media URI: " + mediaUri);
                    {
                        //To Make sure or calculate if the file is less than 10MB
                        int fileSize = 0;
                        InputStream inputStream = null;
                        try {
                            //we will use an input stream to read the files byte by byte, so once it gets to 10MB we will know
                            inputStream = getContentResolver().openInputStream(mediaUri);//read file into stream from uri location
                            fileSize = inputStream.available();
                        } catch (FileNotFoundException e) {
                            if (requestCode == CHOOSE_VIDEO_REQ_CODE)
                                Toast.makeText(this, "There was a problem with the selected File", Toast.LENGTH_LONG).show();
                            return;//since file cant be found we dont need to execute the code block below
                        } catch (IOException e) {
                            Toast.makeText(this, "There was a problem retrieving file from Gallery", Toast.LENGTH_LONG).show();
                            return;
                        } finally {
                            try {
                                inputStream.close();
                            } catch (IOException e) {//Left Intentionally blank

                            }
                        }
                        if (fileSize >= VIDEO_FILE_SIZE_LIMIT) {
                            Toast.makeText(this, "File Size Greater Than 10MB", Toast.LENGTH_LONG).show();
                            return;
                        }

                    }

                }
            }
            //the else indicates broadcasting pictures and videos taken, into the gallery.
            else {
                //For the gallery to be aware that a new media is available we need to broadcast an intent
                // and we do that by first creating an intent and then broadcasting it
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(mediaUri);
                //imagePreview.setImageURI(mediaUri);
                sendBroadcast(mediaScanIntent);//send broadcast to the Gallery
            }
            //this is to pick the media files and send to parse
            if (requestCode == CHOOSE_PICTURE_REQ_CODE || requestCode == TAKE_PICTURE_REQ_CODE)//then we know we have taken a picture
            {
                mediaFiletype = ParseConstantsClass.IMAGE_FILE_TYPE;

            } else if (requestCode == CHOOSE_VIDEO_REQ_CODE || requestCode == TAKE_VIDEO_REQ_CODE) {
                mediaFiletype = ParseConstantsClass.VIDEO_FILE_TYPE;
            }
            //upload media file to parse
            if(mediaFiletype!=null){
                ParseObject uploadMediaMessage=ParseObject.create("Message");//new ParseObject("Message");
                uploadMediaFileToParse(uploadMediaMessage);
            }

        }//end if
       else if(resultCode!=RESULT_CANCELED)//RESULT_CANCELED implies that either the back button was pressed or the app was closed without taking a picture
       {
           //an error has occured
            Toast.makeText(getApplicationContext(),R.string.camera_error,Toast.LENGTH_LONG).show();
        }
    }

    public void refreshMessages() {
        receiveMessages();
    }
    public void startWithCurrentUser()
    {
        currentUserId =ParseUser.getCurrentUser().getObjectId();
    }

    public void receiveMessages()
    {   //construct query to execute
        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
        //senderMessages.whereEqualTo("filetype", "text");
        String chatIdentifier=stringSort(ParseUser.getCurrentUser().getUsername(),recipientName).toLowerCase();
        query.whereEqualTo("chat_identifier", chatIdentifier);
        //Log.d(TAG,recipientName);//debug
        query.orderByAscending("createdAt");
        //configure limit and sort order of queried output
        query.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);
        query.findInBackground(new FindCallback<Message>() {
            @Override
            public void done(List<Message> messages, ParseException e) {
                if (e == null)//if no error then clear all the messages already on screen and readd the newly fetched messages
                {
                   /// Toast.makeText(ChatActivity.this,"Yes! There is Data",Toast.LENGTH_LONG).show();
                    arrayListMessages.clear();
                    //arrayListMessages=messages;
                    arrayListMessages.addAll(messages);
                    chatArrayAdapter.notifyDataSetChanged();//notify adapter that data set has changed, adapter then refreshes list view
                    //scroll to the bottom of the list to initialize load
                    if (firstLoad) {
                        lvChat.setSelection(chatArrayAdapter.getCount());
                        firstLoad = false;
                    }
                } else {
                    Log.d("message", "Error:" + e.getMessage());
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch(id){
            case R.id.action_settings:
                return true;

            case R.id.action_back_button:
                Intent intent = new Intent(getApplicationContext(), ChatListActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_camera:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setItems(R.array.camera_options,mediaDialogListener);//the field where the null is signifies an OnClickListener
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            case R.id.action_upload:
//                ParseObject uploadMediaMessage=ParseObject.create("Message");//new ParseObject("Message");
//                uploadMediaFileToParse(uploadMediaMessage);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void uploadMediaFileToParse(ParseObject uploadMediaMessage) {
        try {
           // if (mediaFiletype != null) {
                byte[] fileBytes = FileHelper.getByteArrayFromFile(ChatActivity.this, mediaUri);
                if (fileBytes == null) {
                    Toast.makeText(ChatActivity.this, "Error with uploading file", Toast.LENGTH_LONG).show();
                    //place toast here return true; //if there is an issue with reading the File
                } else {
                    if (mediaFiletype.equals(ParseConstantsClass.IMAGE_FILE_TYPE)) {
                        fileBytes = FileHelper.reduceImageForUpload(fileBytes);
                    }
//                    else if(mediaFiletype.equals(ParseConstantsClass.VIDEO_FILE_TYPE))
//                    {
//
//                    }
                    String fileName = FileHelper.getFileName(this, mediaUri, mediaFiletype);
                    ParseFile parseFile = new ParseFile(fileName, fileBytes);
                    uploadMediaMessage.put("sender", ParseUser.getCurrentUser().getUsername());
                    uploadMediaMessage.put("userId", currentUserId);
                    //uploadMediaMessage.put("username", ParseUser.getCurrentUser().getUsername());
                    uploadMediaMessage.put("recipient", recipientName);
                    uploadMediaMessage.put("chat_identifier", stringSort(ParseUser.getCurrentUser().getUsername(), recipientName));
                    uploadMediaMessage.put(ParseConstantsClass.KEY_FILE, parseFile);
                    uploadMediaMessage.put(ParseConstantsClass.KEY_FILE_TYPE, mediaFiletype);
                    uploadMediaMessage.pinInBackground();
                    mediaFiletype=null;
                    uploadMediaMessage.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Toast.makeText(ChatActivity.this, "success", Toast.LENGTH_LONG).show();
                            } else {
                                //error occured
                                Toast.makeText(getApplicationContext(), "Bummer!Something went wrong.Please check Connection", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    try {
                        if (!fileName.isEmpty() && !parseFile.isDataAvailable()) {
                            AlertDialog.Builder uploadBuilder = new AlertDialog.Builder(this);
                            uploadBuilder.setMessage("Bummer! An Error occurred. Please Select Another File").setTitle("We're Sorry");
                            AlertDialog dialogUpload = uploadBuilder.create();
                            dialogUpload.show();
                        }
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
              //  }
            }
        }//end try
        catch(Exception e){
            Toast.makeText(ChatActivity.this,"Error Uploading Media File",Toast.LENGTH_LONG).show();
        }

    }//end Method to upload media file to Parse

    //String sort is a method which creates a unique identifier to sift out the right chats to be queried for on parse, using
    //both sender and receiver names-which are taken as string arguments
    public  String stringSort(String str1,String str2)
    {
        String concat=str1+str2;
        char[] stringToChar=concat.toCharArray();
        java.util.Arrays.sort(stringToChar);
        
        return new String(stringToChar);
    }
}
