package com.example.semyung_old_book_kkm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UploadFrag extends Fragment {
    private View view;

    @BindView(R.id.iv_photo) ImageView iv_photo;
    @BindView(R.id.et_title) EditText et_title;
    @BindView(R.id.rb0) RadioButton rb0;
    @BindView(R.id.rb1) RadioButton rb1;
    @BindView(R.id.rb2) RadioButton rb2;
    @BindView(R.id.rb_offline) RadioButton rb_offline;
    @BindView(R.id.rb_online) RadioButton rb_online;
    @BindView(R.id.tv_upload) TextView tv_upload;

    //프로필 사진 요청코드
    private static final int RESULT_OK = -1;
    private static final int REQUEST_CODE = 0;
    Context mContext;
    private FirebaseStorage storage;
    Uri file;
    Intent img_data;

    //firebase
    private DatabaseReference reference;

    public UploadFrag(Context mContet) {
        // Required empty public constructor
        this.mContext = mContet;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.uploadfrag, container, false);
        ButterKnife.bind(this, view);
        EditListener();
        storage=FirebaseStorage.getInstance();
        storage=FirebaseStorage.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("write");
        return view;
    }

    @OnClick({R.id.iv_photo, R.id.tv_upload}) void Click(View v){
        if(v.getId()== R.id.iv_photo){
            gallery();
        }else if(v.getId()== R.id.tv_upload){
            Upload();
        }
    }


    int RadioGroupCehck0(){
        if(rb0.isChecked()){
            return 0;
        }else if(rb0.isChecked()){
            return 1;
        }else{
            return 2;
        }
    }

    int RadioGroupCehck1(){
        if(rb_offline.isChecked()) {
            return 0;
        }else{
            return 1;
        }
    }

    void gallery(){
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(intent, REQUEST_CODE);
//        Log.e("HAN","gallery: ");

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, REQUEST_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("HAN","requestCode: "+requestCode);
        if (requestCode == REQUEST_CODE) {
//            Log.e("HAN","resultCode: "+resultCode);
//            if (resultCode == RESULT_OK) {
//                try {
//                    InputStream in = mContext.getContentResolver().openInputStream(data.getData());
//                    Bitmap img = BitmapFactory.decodeStream(in);
//                    in.close();
//                    iv_photo.setImageBitmap(img);
//                } catch (Exception e) {
//                    Log.e("HAN","exception: "+e);
//                }
//            }

            file = data.getData();
            img_data=data;

            try {
                InputStream in = mContext.getContentResolver().openInputStream(img_data.getData());
                Bitmap img = BitmapFactory.decodeStream(in);
                in.close();
                iv_photo.setImageBitmap(img);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    void Upload(){
        StorageReference storageRef = storage.getReference();
        String name =dateName(System.currentTimeMillis());
        String file_name = name;
        name = "photo/"+name;
        //StorageReference riversRef = storageRef.child("photo/1.png");
        StorageReference riversRef = storageRef.child(name);
        UploadTask uploadTask = riversRef.putFile(file);

//        try {
//            InputStream in = mContext.getContentResolver().openInputStream(img_data.getData());
//            Bitmap img = BitmapFactory.decodeStream(in);
//            in.close();
//            iv_photo.setImageBitmap(img);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(mContext, "사진이 정상적으로 업로드 되지 않았습니다." ,Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(mContext, "사진이 정상적으로 업로드 되었습니다." ,Toast.LENGTH_SHORT).show();
            }
        });

        int Group0 = RadioGroupCehck0();
        int Group1 = RadioGroupCehck1();

        Map<String, Object> map = new HashMap<String, Object>();
        String key = reference.push().getKey();
        reference.updateChildren(map);
        DatabaseReference dbRef = reference.child(key);
        Map<String, Object> objectMap = new HashMap<String, Object>();
        objectMap.put("Group0", String.valueOf(Group0));
        objectMap.put("Group1", String.valueOf(Group1));
        objectMap.put("file", file_name);
        objectMap.put("title",et_title.getText().toString());
        //objectMap.put("text1", et_msg.getText().toString());
        objectMap.put("seller",data.getInstance(mContext).getID());
        dbRef.updateChildren(objectMap);
    }

    private String dateName(long dateTaken){
        Date date = new Date(dateTaken);
        SimpleDateFormat dateFormat =
                new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");
        return dateFormat.format(date)+".png";
    }


    void EditListener() {
        et_title.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                switch (keyCode) {
                    // 엔터치면 키보드 내려오게끝 작업
                    case KeyEvent.KEYCODE_ENTER:
                        Log.e("HAN", "entert");
                        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(et_title.getWindowToken(), 0);
                        return true;
                }
                return false;
            }
        });
    }
}
