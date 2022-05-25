package com.example.semyung_old_book_kkm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatFrag extends Fragment implements roomadapter.AdapterCallback{
    private View view;

    @BindView(R.id.template_recycler)
    RecyclerView template_recycler;

    Context mContext;
    ArrayList<String> mtitle = new ArrayList<>();
    ArrayList<String> mphoto = new ArrayList<>();
    ArrayList<String> mgroup0 = new ArrayList<>();
    ArrayList<String> mgroup1 = new ArrayList<>();
    ArrayList<String> mseller = new ArrayList<>();
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("room");
    String title,group0,group1,photo,room_name;

    public ChatFrag(Context mContet) {
        // Required empty public constructor
        this.mContext = mContet;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.chatfrag, container, false);
        ButterKnife.bind(this, view);


//        DatabaseReference reference;
//        reference = FirebaseDatabase.getInstance().getReference().child("room");
//        Map<String, Object> map = new HashMap<String, Object>();
//        String key = reference.push().getKey();
//        reference.updateChildren(map);
//        DatabaseReference dbRef = reference.child(key);
//        Map<String, Object> objectMap = new HashMap<String, Object>();
//
//
//        String match = "[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]";
//        String room =(Seller+photo_name+data.getInstance(mContext).getID()).replaceAll(match, "");
//        objectMap.put("room_name",room);
//        objectMap.put("photo",photo_name);
//        objectMap.put("seller",Seller);
//        objectMap.put("title",title);
//
//        dbRef.updateChildren(objectMap);
//        intent.putExtra("seller",Seller);
//        intent.putExtra("photo",photo_name);
//        intent.putExtra("buyer",data.getInstance(mContext).getID());
//        startActivity(intent);

        template_recycler.setLayoutManager(new LinearLayoutManager(mContext)) ;
        roomadapter adapter = new roomadapter(mtitle,mgroup0,mgroup1,mphoto,mseller,this,mContext) ;
        template_recycler.setAdapter(adapter) ;

        Log.e("HAN","onCreateView");
        mtitle = new ArrayList<>();
        mphoto = new ArrayList<>();
        mgroup0 = new ArrayList<>();
        mgroup1 = new ArrayList<>();
        mseller = new ArrayList<>();

        reference.addChildEventListener(new ChildEventListener() {
            @Override public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                NoticeListener(dataSnapshot);
                Log.e("HAN","onChildAdded");
            }

            @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                NoticeListener(dataSnapshot);
                Log.e("HAN","onChildChanged");
            }

            @Override public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

    private void NoticeListener(DataSnapshot dataSnapshot) {
        // dataSnapshot 밸류값 가져옴

        Iterator i = dataSnapshot.getChildren().iterator();
        Log.e("HAN","NoticeListener");
        String id =data.getInstance(mContext).getID();
        String match = "[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]";
        id =id.replaceAll(match, "");
        Log.e("HAN","id: "+id);
        while (i.hasNext()) {
            photo = (String) ((DataSnapshot) i.next()).getValue();
            room_name = (String) ((DataSnapshot) i.next()).getValue();
            String sell = (String) ((DataSnapshot) i.next()).getValue();
            title = (String) ((DataSnapshot) i.next()).getValue();
            if (room_name.contains(id)){
                mseller.add(sell);
                mtitle.add(title);
                mgroup0.add(room_name);
                mgroup1.add("group1");
                mphoto.add(photo);
            }
            Log.e("HAN","1111111111");
        }


        template_recycler.setLayoutManager(new LinearLayoutManager(mContext)) ;
        roomadapter adapter = new roomadapter(mtitle,mgroup0,mgroup1,mphoto,mseller,this,mContext) ;
        template_recycler.setAdapter(adapter) ;

    }

    @Override
    public void DoSomeThing(String roomname){
        Intent intent = new Intent(mContext,MessageActivity.class);
        intent.putExtra("roomname",roomname);
        Log.e("HAN: ","roomname: "+roomname);
        startActivity(intent);
    }

}
