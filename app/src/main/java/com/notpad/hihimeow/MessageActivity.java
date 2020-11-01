package com.notpad.hihimeow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.os.Bundle;
import android.view.View;


import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.notpad.hihimeow.adapters.MessageAdapter;
import com.notpad.hihimeow.utils.Message;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private List<Message> resultMessages;
    DatabaseReference mDatabase;
    DatabaseReference mDatabaseRoomsChat;
    DatabaseReference mDatabaseMessage;
    Date currDate;
    private String currMeowID, coupleMeowID,coupleMeowName, roomID, coupleMeowImage;

    private EditText mInputMessage;
    private Button mSendMessage;
    private TextView mMeowName;
    private ImageView mMeowImage;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_message);
        toolbar = (Toolbar) findViewById(R.id.tbMessage);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");


        mDatabase = FirebaseDatabase.getInstance().getReference();


        currMeowID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        coupleMeowID = getIntent().getExtras().getString("matchID");
        coupleMeowName = getIntent().getExtras().getString("matchName");
        coupleMeowImage = getIntent().getExtras().getString("matchImage");
        mDatabaseRoomsChat = mDatabase.child("Meows").child(currMeowID).child("connections").child("matches").child(coupleMeowID).child("RoomID");


        resultMessages = new ArrayList<>();
        getRoomID();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mInputMessage = (EditText) findViewById(R.id.etMessageToSend);
        mSendMessage = (Button) findViewById(R.id.btSendMessage);
        mMeowImage = (ImageView) findViewById(R.id.imgImageInMessage);
        mMeowName = (TextView) findViewById(R.id.tvMeowNameInMessage);

        mMeowName.setText(coupleMeowName);
        Glide.with(getApplication()).load(coupleMeowImage).into(mMeowImage);

        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(MessageActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MessageAdapter(resultMessages, MessageActivity.this);
        mRecyclerView.setAdapter(mAdapter);


        mSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private void sendMessage() {
        String messageText = mInputMessage.getText().toString();
        if(!messageText.trim().isEmpty()){
            currDate = new Date();
            String currTime = String.valueOf(currDate.getTime());
            if(!messageText.isEmpty()){
                Map newMessage =  new HashMap();
                newMessage.put("createdByMeow", currMeowID);
                newMessage.put("message", messageText);
                mDatabaseMessage.child(currTime).updateChildren(newMessage);
                Map history = new HashMap();
                history.put("lastTime", currTime);
                history.put("lastMessage", messageText);
                mDatabase.child("Meows").child(currMeowID).child("connections").child("matches").child(coupleMeowID).updateChildren(history);
                mDatabase.child("Meows").child(coupleMeowID).child("connections").child("matches").child(currMeowID).updateChildren(history);




            }
            mInputMessage.setText("");
        }


    }

    private void getRoomID(){
        mDatabaseRoomsChat.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    roomID = dataSnapshot.getValue().toString();

                    //tạo đường dẫn tới phòng chat
                    mDatabaseMessage = mDatabase.child("Messages").child(roomID);
                    getChatMessages();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getChatMessages() {
        mDatabaseMessage.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()){
                    String message = null;
                    String createByMeow = null;

                    if(dataSnapshot.child("message").getValue() != null){
                        message = dataSnapshot.child("message").getValue().toString();
                    }

                    if(dataSnapshot.child("createdByMeow").getValue() != null){
                        createByMeow = dataSnapshot.child("createdByMeow").getValue().toString();
                    }

                    if(message != null && createByMeow != null){
                        boolean messOfCurrMeow = false;
                        if(createByMeow.equals(currMeowID)){
                            messOfCurrMeow = true;
                        }
                        Message newMessage = new Message(message, messOfCurrMeow);
                        resultMessages.add(newMessage);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
