package com.example.semyung_old_book_kkm;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailActivity extends AppCompatActivity {

    Context mContext;

    @BindView(R.id.iv_photo) ImageView iv_photo;
    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.tv_trade) TextView tv_trade;
    @BindView(R.id.tv_status) TextView tv_status;
    @BindView(R.id.tv_upload) TextView tv_upload;
    String Seller="",photo_name="",title="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        mContext=this;
        Intent intent = getIntent();
        Seller = intent.getStringExtra("sell");
        photo_name=intent.getStringExtra("photo");
//
//        intent.putExtra("title",title);
//        intent.putExtra("contents",contents);
//        intent.putExtra("link",link);
//        intent.putExtra("photo",photo);
//
        title=intent.getStringExtra("title");
        tv_title.setText(intent.getStringExtra("title"));
//        tv_status.setText(intent.getStringExtra("contents"));
//        tv_trade.setText(intent.getStringExtra("link"));


        if(Integer.parseInt(intent.getStringExtra("contents"))==0){
            tv_status.setText("미개봉");
        }else if(Integer.parseInt(intent.getStringExtra("contents"))==1){
            tv_status.setText("거의 새 것");
        }else{
            tv_status.setText("사용감 있음");
        }

        if(Integer.parseInt(intent.getStringExtra("link"))==0){
            tv_trade.setText("직거래");
        }else{
            tv_trade.setText("택배 거래");
        }


        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        String path;
        path="photo/"+intent.getStringExtra("photo");;
        StorageReference pathReference = storageReference.child(path);
        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.e("HAN","uri: "+uri);
                Log.e("HAN","mContext: "+mContext);

                Glide.with(mContext).load(uri).into(iv_photo);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


    }

    @OnClick(R.id.tv_upload) void Chart(){
        Intent intent = new Intent(mContext,MessageActivity.class);
        Log.e("HAN","Seller"+Seller);
        if(data.getInstance(mContext).getID().equals(Seller)){
            Toast.makeText(mContext,"자기 자신과 대화 할 수 없습니다.",Toast.LENGTH_SHORT).show();
        }else{
            DatabaseReference reference;
            reference = FirebaseDatabase.getInstance().getReference().child("room");
            Map<String, Object> map = new HashMap<String, Object>();
            String key = reference.push().getKey();
            reference.updateChildren(map);
            DatabaseReference dbRef = reference.child(key);
            Map<String, Object> objectMap = new HashMap<String, Object>();


            String match = "[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]";
            String room =(Seller+photo_name+data.getInstance(mContext).getID()).replaceAll(match, "");
            objectMap.put("room_name",room);
            objectMap.put("photo",photo_name);
            objectMap.put("seller",Seller);
            objectMap.put("title",title);

            dbRef.updateChildren(objectMap);
//            intent.putExtra("seller",Seller);
//            intent.putExtra("photo",photo_name);
//            intent.putExtra("buyer",data.getInstance(mContext).getID());

            //match = "[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]";
            //room =(intent.getStringExtra("seller")+intent.getStringExtra("photo")+intent.getStringExtra("buyer")).replaceAll(match, "");
            intent.putExtra("roomname",room);


            startActivity(intent);
        }
    }


}