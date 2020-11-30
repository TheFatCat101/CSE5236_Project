package com.group10.cse5236project;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
    private Vibrator mVibrator;

    private String userName = infoClass.getInstance().getCurrentUserName();
    private String chatRoomName = infoClass.getInstance().getCurrentChatRoomName();
    private String chatRoomKey = infoClass.getInstance().getCurrentChatRoomKey();
    private DatabaseReference currentChatRoomMsgSubtree;
    private DatabaseReference currentChatRoomSubtree;
    private DatabaseReference userSubtree;
    private String tempKey;

    private String chatMsg,chatUserName;

    private static final int MIN_TIME_BETWEEN_SHAKES = 300;
    private static final float ACCEL_THRESHOLD = 5f;
    private float mMeasurementsTotal = 0,  mMeasurementsCount = 0;
    private int numOfVibrations;
    private boolean mIsShaking = false;

    private long lastShakeTime, lastUpdate, lastVibrateTime = -1;
    /*END OF TWEAK-ABLE VARIABLES */

    SensorManager sensorManager;
    private int shakeCount = 0;

    /*Vibrator object variables*/
    //pattern  =   {  delay ms  ,  vibrate ms  }
    long[] pattern = {    0   ,      250   , 500 };



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
        getActivity().setTitle("Chats");

        Connectivity.checkConnection(getActivity());

        mInviteMemberName = (EditText) v.findViewById(R.id.member_to_invite);
        mInviteMember = (Button) v.findViewById(R.id.invite);
        mInputMessage = (EditText) v.findViewById(R.id.new_message);
        mSendMessage = (Button) v.findViewById(R.id.send);
        mChat = (TextView) v.findViewById(R.id.current_chat);

        userSubtree = FirebaseDatabase.getInstance().getReference().child("Accounts");
        currentChatRoomSubtree = FirebaseDatabase.getInstance().getReference().child("Chatrooms").child(chatRoomKey);
        currentChatRoomMsgSubtree = FirebaseDatabase.getInstance().getReference().child("Chatrooms").child(chatRoomKey).child("Message");

        currentChatRoomMsgSubtree.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                //append_chat_conversation(dataSnapshot);
                //todo: the chatMsg is the newest recieved message, which needs to be converted to vibrate
                chatMsg = (String) dataSnapshot.child("Msg").getValue();
                chatUserName = (String) dataSnapshot.child("Name").getValue();

                if(chatMsg != null && chatUserName != null){
                    //note: here are the new message received
                    mChat.append(chatUserName + " : " + chatMsg + " \n");
                    numOfVibrations = 0;
                    try{
                        numOfVibrations = Integer.parseInt(chatMsg);
                    }
                    catch(Exception e){
                        //Toast.makeText(getActivity(), R.string.message_not_int, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                //append_chat_conversation(dataSnapshot);

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
        });
        if (mInviteMember != null) {
            mInviteMember.setOnClickListener(this);
        }
        if (mSendMessage != null) {
            mSendMessage.setOnClickListener(this);
        }


        return v;

    }

    /*
    SHAKE DETECTOR STUFF BELOW
    @TODO: FIX ME LATER
     */
    private final SensorEventListener mSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
                long currTime = System.currentTimeMillis();
                mMeasurementsTotal += (float) Math.sqrt(Math.pow(sensorEvent.values[0], 2) + Math.pow(sensorEvent.values[1], 2) + Math.pow(sensorEvent.values[2], 2));
                mMeasurementsCount++;

                if (shakeCount > 0 && currTime - lastShakeTime > 2000) {
                    /*
                    MESSAGE SHOULD BE SENT HERE
                    */
                    Map<String,Object> map = new HashMap<String, Object>();
                    tempKey = currentChatRoomMsgSubtree.push().getKey();
                    currentChatRoomMsgSubtree.updateChildren(map);

                    Map<String,Object> msgMap = new HashMap<String, Object>();
                    msgMap.put("Name", userName);

                    String shakeMessage = Integer.toString(shakeCount);
                    msgMap.put("Msg",shakeMessage);
                    shakeCount = 0;
                    currentChatRoomMsgSubtree.child(tempKey).updateChildren(msgMap);
                }

                if (numOfVibrations > 0 && currTime - lastVibrateTime > pattern[2]) {
                    mVibrator.vibrate(pattern, -1);
                    lastVibrateTime = currTime;
                    numOfVibrations--;
                }

                if (currTime - lastUpdate > 25) {
                    lastUpdate = currTime;

                    float accel = mMeasurementsTotal / mMeasurementsCount;
                    mMeasurementsTotal = 0;
                    mMeasurementsCount = 0;

                    if (accel > ACCEL_THRESHOLD / 3) {
                        if (!mIsShaking && currTime - lastShakeTime > MIN_TIME_BETWEEN_SHAKES && accel > ACCEL_THRESHOLD) {
                            // Shake has been detected
                            mIsShaking = true;
                            lastShakeTime = currTime;
                            shakeCount++;
                        }
                    } else {
                        mIsShaking = false;
                    }
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
            //MAYBE RELEVANT IN THE FUTURE
        }
    };

    /*
    END OF SHAKE DETECTOR STUFF HERE
     */

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
        /*
        SETUP THE PHONE'S  LOCAL VIBRATOR OBJECT
         */
        mVibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        //boolean hasVibrator = sensorManager.registerListener(mSensorListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        //if(!hasVibrator){
        //    Toast.makeText(getActivity(), R.string.vibrator_not_found, Toast.LENGTH_SHORT).show();
        //}
        /*
        SETUP THE PHONE LOCAL ACCELEROMETER
         */
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        boolean hasAccelSensor = sensorManager.registerListener(mSensorListener, sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION), SensorManager.SENSOR_DELAY_GAME);
        if(!hasAccelSensor){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity().getApplicationContext());
            // set title
            alertDialogBuilder.setTitle("Accelerometer Sensor could NOT be found!");

            // set dialog message
            alertDialogBuilder
                    .setMessage("Your App MUST quit")
                    .setCancelable(false)
                    .setPositiveButton("Quit",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            /*when button clicked, close activity*/
                            getActivity().finish();
                        }});

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
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
        sensorManager.unregisterListener(mSensorListener, sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION));
        sensorManager = null;
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

//                    DatabaseReference messageRoot = currentChatRoomMsgSubtree.child(tempKey);
                    Map<String,Object> msgMap = new HashMap<String, Object>();
                    msgMap.put("Name", userName);

                    //@TODO: need to use the step counter to generate the message to send
                    msgMap.put("Msg",mInputMessage.getText().toString());
                    mInputMessage.setText("");
                    //shakeCount = 0;
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
                    break;
            }
        }
    }


}