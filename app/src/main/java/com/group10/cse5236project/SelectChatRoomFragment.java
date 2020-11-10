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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
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
    private String currentUser = infoClass.getInstance().getCurrentUserName();
    private DatabaseReference chatRoomSubtree = FirebaseDatabase.getInstance().getReference("Chatrooms");
    private Map<Integer, String> keyOfChatRoom = new HashMap<Integer, String>();


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

        getActivity().setTitle("Select chat room");

        mCreateChatRoomButton = (Button) v.findViewById(R.id.start_chat_room);
        mChatRoomListView= (ListView) v.findViewById(R.id.chat_room_list);
        mNewChatRoomName = (EditText) v.findViewById(R.id.new_chat_room_name);

        arrayAdapter = new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_list_item_1,list_of_rooms);

        mChatRoomListView.setAdapter(arrayAdapter);

        arrayAdapter.notifyDataSetChanged();
        
        chatRoomSubtree.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Set<String> set = new HashSet<String>();
                Iterator i = dataSnapshot.getChildren().iterator();
                int m = 0;
                //Toast.makeText(getActivity(), String.valueOf(s) , Toast.LENGTH_SHORT).show();

                while (i.hasNext()){
                    DataSnapshot currentChatRoom = (DataSnapshot)i.next();
                    String chatRoom = (String) currentChatRoom.child("ChatRoomName").getValue();
                    Iterator j = currentChatRoom.child("Members").getChildren().iterator();
                    while (j.hasNext()){
                        if(((DataSnapshot)j.next()).getKey().equals(currentUser)){
                            m++;
                            set.add(String.valueOf(m)+". " + chatRoom);
                            keyOfChatRoom.put(m,(String) currentChatRoom.getKey() );
                        }
                    }
                    /*
                    set.add(chatRoom);
                    boolean inChatRoom = false;
                    while (j.hasNext()){
                        if(((DataSnapshot)j.next()).getKey().equals(currentUser)){
                            inChatRoom = true;
                        }
                    }
                    if(inChatRoom != true){
                        set.remove(chatRoom);
                    }
                    */
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
                    Toast.makeText(getActivity(), "Item clicked : " + position, Toast.LENGTH_SHORT).show();

                    String itemSelected = ((TextView)view).getText().toString();
                    int m = 0;
                    char[] chars = itemSelected.toCharArray();
                    for(char ch: chars){
                        if(ch == '.'){
                            break;
                        }
                        else{
                            m = m*10 + Integer.valueOf(String.valueOf(ch));
                        }
                    }

                    infoClass.getInstance().setCurrentChatRoomKey(keyOfChatRoom.get(m));

                    Fragment cfragment = new ChatRoomFragment();
                    FragmentManager fm = getFragmentManager();
                    fm.beginTransaction().replace(R.id.fragment_container, cfragment).addToBackStack("select_chat_room_fragment").commit();

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
                    createChatRoom();
                    Fragment fragment = new ChatRoomFragment();
                    fm.beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack("select_chat_room_fragment").commit();
                    break;

                /*
                case R.id.chat_room_list:
                    /*todo: get the room id and start the chatroom need help here.
                    //But the problem is that i don't know how active onitemclicklistener

                    //infoClass.getInstance().setCurrentChatRoomKey(((TextView)view).getText().toString());

                    //suppose string itemSelected is the selected chatroom

                    String itemSelected = "1. test1";
                    int m = 0;
                    char[] chars = itemSelected.toCharArray();
                    for(char ch: chars){
                        if(ch == '.'){
                            break;
                        }
                        else{
                            m = m*10 + Integer.valueOf(String.valueOf(ch));
                        }
                    }

                    infoClass.getInstance().setCurrentChatRoomKey(keyOfChatRoom.get(m));

                    Fragment cfragment = new ChatRoomFragment();
                    fm.beginTransaction().replace(R.id.fragment_container, cfragment).addToBackStack("select_chat_room_fragment").commit();

                    break;

                 */
            }
        }
    }


    private void createChatRoom(){
        final String chatRoomName = mNewChatRoomName.getText().toString().trim();
        infoClass.getInstance().setCurrentChatRoomName(chatRoomName);


        //create a new chat room subtree with a random key
        Map<String,Object> map = new HashMap<String, Object>();
        String tempKey = chatRoomSubtree.push().getKey();
        chatRoomSubtree.updateChildren(map);
        infoClass.getInstance().setCurrentChatRoomKey(tempKey);


        //save name to it
        Map<String,Object> name = new HashMap<String, Object>();
        name.put("ChatRoomName", chatRoomName);

        chatRoomSubtree.child(tempKey).updateChildren(name);

        //save the member to it
        Map<String,Object> members = new HashMap<String, Object>();
        members.put("Members", "");
        chatRoomSubtree.child(tempKey).updateChildren(members);

        Map<String,Object> member = new HashMap<String, Object>();
        member.put(currentUser, currentUser);
        chatRoomSubtree.child(tempKey).child("Members").updateChildren(member);

        //create message subtree
        Map<String,Object> msg = new HashMap<String, Object>();
        msg.put("Message", "");
        chatRoomSubtree.child(tempKey).updateChildren(msg);


    }

}