package com.group10.cse5236project;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class SelectChatRoomFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = "SelectChatRoomFragment";

    private Button mCreateChatRoomButton;
    private ListView mChatRoomListView;
    private EditText mNewChatRoomName;

    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_rooms = new ArrayList<>();
    private String currentUser = infoClass.getInstance().getData();
    private DatabaseReference chatRoomSubtree = FirebaseDatabase.getInstance().getReference().child("Chatrooms");


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
        View v = inflater.inflate(R.layout.fragment_select_chat_room, container, false);

        mCreateChatRoomButton = (Button) v.findViewById(R.id.start_chat_room);
        mChatRoomListView= (ListView) v.findViewById(R.id.chat_room_list);
        mNewChatRoomName = (EditText) v.findViewById(R.id.new_chat_room_name);

        arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,list_of_rooms);

        mChatRoomListView.setAdapter(arrayAdapter);

        chatRoomSubtree.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Toast.makeText(getActivity(), currentUser, Toast.LENGTH_SHORT).show();
                Set<String> set = new HashSet<String>();
                Iterator i = dataSnapshot.child("Chatrooms").getChildren().iterator();

                while (i.hasNext()){
                    Iterator j = ((DataSnapshot)i.next()).child("Members").getChildren().iterator();
                    while (j.hasNext()){
                        if(((DataSnapshot)j.next()).getKey().equals(currentUser)){
                            set.add(((DataSnapshot)j.next()).getKey());
                        }
                    }
                }

                list_of_rooms.clear();
                list_of_rooms.addAll(set);

                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if (mCreateChatRoomButton != null) {
            mCreateChatRoomButton.setOnClickListener(this);
        }
        if (mChatRoomListView != null) {
            mChatRoomListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //todo: fill in the method
                }
            });
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
                case R.id.start_chat_room:
                    creatChatRoom();

                    break;
                case R.id.account_settings_button:
                    Toast.makeText(getActivity(), R.string.account_delete_error_toast, Toast.LENGTH_SHORT).show();
                    //Fragment fragment = new AccountSettingsFragment();
                    //fm.beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack("account_settings_fragment").commit();
                    break;
            }
        }
    }

    private void creatChatRoom(){
        final String chatRoomName = mNewChatRoomName.getText().toString().trim();

        //create a new chatroom subtree with a random key
        Map<String,Object> map = new HashMap<String, Object>();
        String tempKey = chatRoomSubtree.push().getKey();
        chatRoomSubtree.updateChildren(map);

        //save name to it
        Map<String,Object> name = new HashMap<String, Object>();
        name.put("ChatRoomName", chatRoomName);
        chatRoomSubtree.child(tempKey).updateChildren(name);
        //todo: open chat room
    }

}