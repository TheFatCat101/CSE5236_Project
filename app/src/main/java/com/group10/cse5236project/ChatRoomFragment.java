package com.group10.cse5236project;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringDef;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class ChatRoomFragment extends Fragment implements  View.OnClickListener{
    private static final String TAG = "ChatRoomFragment";

    private Button mSendMessage;
    private EditText mInputMessage;
    private Button mInviteMember;
    private EditText mInviteMemberName;
    private TextView mChat;

    private String userName = infoClass.getInstance().getCurrentUserName();
    private String chatRoomName = infoClass.getInstance().getCurrentChatRoomName();
    private String chatRoomKey = infoClass.getInstance().getCurrentChatRoomKey();
    private DatabaseReference currentChatRoomMsgSubtree;
    private DatabaseReference currentChatRoomSubtree;
    private DatabaseReference userSubtree;
    private String tempKey;

    private String chatMsg,chatUserName;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d(TAG, "OnAttach() called");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "OnCreate() called");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView() called");
        View v = inflater.inflate(R.layout.fragment_chat_room, container, false);

        mInviteMemberName = (EditText) v.findViewById(R.id.member_to_invite);
        mInviteMember = (Button) v.findViewById(R.id.invite);
        mInputMessage = (EditText) v.findViewById(R.id.new_message);
        mSendMessage = (Button) v.findViewById(R.id.send);
        mChat = (TextView) v.findViewById(R.id.current_chat);

        getActivity().setTitle(chatRoomName);

        userSubtree = FirebaseDatabase.getInstance().getReference().child("Accounts");
        currentChatRoomSubtree = FirebaseDatabase.getInstance().getReference().child("Chatrooms").child(chatRoomKey);
        currentChatRoomMsgSubtree = FirebaseDatabase.getInstance().getReference().child("Chatrooms").child(chatRoomKey).child("Message");

        currentChatRoomMsgSubtree.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                append_chat_conversation(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                append_chat_conversation(dataSnapshot);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

            private void append_chat_conversation(DataSnapshot dataSnapshot) {

                Iterator i = dataSnapshot.getChildren().iterator();

                //note: here are the new message received

                while (i.hasNext()) {
                    DataSnapshot nextMessage = (DataSnapshot) i.next();
                    chatMsg =  (String) nextMessage.child("Msg").getValue();
                    chatUserName = (String) nextMessage.child("Name").getValue();

                    mChat.append(chatUserName + " : " + chatMsg + " \n");
                }


            }
        });
        if (mInviteMember != null) {
            mInviteMember.setOnClickListener(this);
        }
        if (mSendMessage != null) {
            mSendMessage.setOnClickListener(this);
        }


        return v;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "OnActivityCreated() called");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "OnStart() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "OnResume() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "OnPause() called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "OnStop() called");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "OnDestroyView() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "OnDestroy() called");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "OnDetach() called");
    }

    @Override
    public void onClick(View v) {
        FragmentActivity activity = getActivity();
        FragmentManager fm = getFragmentManager();
        if (activity != null) {
            switch (v.getId()) {
                case R.id.send:
                    Map<String,Object> map = new HashMap<String, Object>();
                    tempKey = currentChatRoomMsgSubtree.push().getKey();
                    currentChatRoomMsgSubtree.updateChildren(map);

                    DatabaseReference messageRoot = currentChatRoomMsgSubtree.child(tempKey);
                    Map<String,Object> msgMap = new HashMap<String, Object>();
                    msgMap.put("Name", userName);
                    msgMap.put("Msg",mInputMessage.getText().toString());

                    currentChatRoomMsgSubtree.child(tempKey).updateChildren(msgMap);
                    break;

                case R.id.invite:
                    final Map<String,Object> memberMap = new HashMap<String, Object>();
                    final String newMember = mInviteMemberName.getText().toString().trim();
                    memberMap.put(newMember, newMember);
                    final boolean[] isUser = {false};
                    //check if user exist
                    userSubtree.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Iterator i = snapshot.getChildren().iterator();
                            while(i.hasNext()){
                                String j = ((DataSnapshot) i.next()).getKey();
                                //Toast.makeText(getActivity(), j , Toast.LENGTH_SHORT).show();
                                if(j.equals(newMember)){
                                    isUser[0] = true;
                                    currentChatRoomSubtree.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            Iterator i = snapshot.child("Members").getChildren().iterator();
                                            while(i.hasNext()){
                                                if(((DataSnapshot) i.next()).getKey().equals(newMember)){
                                                    Toast.makeText(getActivity(), "This user is already in chat room" , Toast.LENGTH_SHORT).show();
                                                }
                                                else{
                                                    currentChatRoomSubtree.child("Members").updateChildren(memberMap);
                                                    Toast.makeText(getActivity(), (newMember + " is in chat room now") , Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                    //currentChatRoomSubtree.child("Members").updateChildren(memberMap);
                                    //Toast.makeText(getActivity(), (newMember + " is in chat room now") , Toast.LENGTH_SHORT).show();
                                }
                            }
                            if(isUser[0] == false){
                                Toast.makeText(getActivity(), "User not exist." , Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



                    /*
                    //check if user is in chat room
                    if(isUser == true){
                        Toast.makeText(getActivity(), "exists" , Toast.LENGTH_SHORT).show();

                        currentChatRoomSubtree.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Iterator i = snapshot.child("Members").getChildren().iterator();
                                while(i.hasNext()){
                                    if(((DataSnapshot) i.next()).getKey().equals(newMember)){
                                        Toast.makeText(getActivity(), "This user is already in chat room" , Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        currentChatRoomSubtree.child("Members").updateChildren(memberMap);
                                        Toast.makeText(getActivity(), (newMember + " is in chat room now") , Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }
                    else {
                        Toast.makeText(getActivity(), "User not exist." , Toast.LENGTH_SHORT).show();
                    }

                    */
                    break;
            }
        }
    }


}