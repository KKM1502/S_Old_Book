package com.example.semyung_old_book_kkm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MessageActivity extends AppCompatActivity {

    private Button btn_send;
    private EditText et_msg;
    private ListView lv_chating;

    private ArrayAdapter<String> arrayAdapter;

    private String str_name;
    private String str_msg;
    private String chat_user;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("chat");
    Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        mContext = this;
        lv_chating = (ListView) findViewById(R.id.lv_chating);
        btn_send = (Button) findViewById(R.id.btn_send);
        et_msg = (EditText) findViewById(R.id.et_msg);
        Intent intent = getIntent();



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

        //reference.child(intent.getStringExtra("seller")+intent.getStringExtra("photo")+intent.getStringExtra("buyer"));

        //String match = "[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]";
        //String room =(intent.getStringExtra("seller")+intent.getStringExtra("photo")+intent.getStringExtra("buyer")).replaceAll(match, "");
        String room = intent.getStringExtra("roomname");
        Log.e("HAN","roomname: "+room);

        reference = FirebaseDatabase.getInstance().getReference().child(room);

        arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1);
        lv_chating.setAdapter(arrayAdapter);

        // 리스트뷰가 갱신될때 하단으로 자동 스크롤
        lv_chating.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

        //str_name = "유저" + new Random().nextInt(10)+new Random().nextInt(10)+new Random().nextInt(10)+new Random().nextInt(10);
        str_name=data.getInstance(mContext).getID();
        // 전송 버튼 눌렀을때 불리는 부분
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {

                // map을 사용해 name과 메시지를 가져오고, key에 값 요청
                Map<String, Object> map = new HashMap<String, Object>();

                // key로 데이터베이스 오픈
                String key = reference.push().getKey();
                reference.updateChildren(map);

                DatabaseReference dbRef = reference.child(key);

                Map<String, Object> objectMap = new HashMap<String, Object>();

                objectMap.put("str_name", str_name);
                objectMap.put("text", et_msg.getText().toString());
                dbRef.updateChildren(objectMap);
                et_msg.setText("");
            }
        });


        /*
        addChildEventListener는 Child에서 일어나는 변화를 감지
        - onChildAdded()   : 리스트의 아이템을 검색하거나 추가가 있을 때 수신
        - onChildChanged() : 리스트의 아이템의 변화가 있을때 수신
        - onChildRemoved() : 리스트의 아이템이 삭제되었을때 수신
        - onChildMoved()   : 리스트의 순서가 변경되었을때 수신
         */

        reference.addChildEventListener(new ChildEventListener() {
            @Override public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                chatListener(dataSnapshot);
            }

            @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                chatListener(dataSnapshot);
            }

            @Override public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void chatListener(DataSnapshot dataSnapshot) {
        Iterator i = dataSnapshot.getChildren().iterator();

        while (i.hasNext()) {
            chat_user = (String) ((DataSnapshot) i.next()).getValue();
            str_msg = (String) ((DataSnapshot) i.next()).getValue();
            arrayAdapter.add(chat_user + " : " + str_msg);
        }

        // 변경된값으로 리스트뷰 갱신
        arrayAdapter.notifyDataSetChanged();
    }
}