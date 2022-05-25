package com.example.semyung_old_book_kkm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

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

public class MainFrag extends Fragment implements Adapter.AdapterCallback{
    private View view;
    @BindView(R.id.template_recycler)
    RecyclerView template_recycler;
    @BindView(R.id.et_search)
    EditText et_search;

    Context mContext;
    ArrayList<String> mtitle = new ArrayList<>();
    ArrayList<String> mphoto = new ArrayList<>();
    ArrayList<String> mgroup0 = new ArrayList<>();
    ArrayList<String> mgroup1 = new ArrayList<>();
    ArrayList<String> mseller = new ArrayList<>();

    ArrayList<String> Searchmtitle = new ArrayList<>();
    ArrayList<String> Searchmphoto = new ArrayList<>();
    ArrayList<String> Searchmgroup0 = new ArrayList<>();
    ArrayList<String> Searchmgroup1 = new ArrayList<>();
    ArrayList<String> Searchmseller = new ArrayList<>();

    String title,group0,group1,photo;

    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("write");

    public MainFrag(Context mContet) {
        // Required empty public constructor
        this.mContext = mContet;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.mainfrag, container, false);
        ButterKnife.bind(this, view);

        template_recycler.setLayoutManager(new LinearLayoutManager(mContext)) ;
        Adapter adapter = new Adapter(mtitle,mgroup0,mgroup1,mphoto,mseller,this,mContext) ;
        template_recycler.setAdapter(adapter) ;
        EditListener();
        Log.e("HAN","onCreateView");
        mtitle = new ArrayList<>();
        mphoto = new ArrayList<>();
        mgroup0 = new ArrayList<>();
        mgroup1 = new ArrayList<>();
        mseller = new ArrayList<>();

        Searchmseller = new ArrayList<>();
        Searchmtitle = new ArrayList<>();
        Searchmphoto = new ArrayList<>();
        Searchmgroup0 = new ArrayList<>();
        Searchmgroup1 = new ArrayList<>();


        ((Activity)mContext).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        // db값이 추가 되거나 변화될때 불리는 리스너
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

    void EditListener(){
        et_search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                switch (keyCode) {
                    case KeyEvent.KEYCODE_ENTER:
                        Log.e("HAN","entert");
                        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow( et_search.getWindowToken(), 0);
                        Search();
                        return true;

                }

                return false;

            }

        });
    }

    void Search(){

        Searchmtitle = new ArrayList<>();
        Searchmphoto = new ArrayList<>();
        Searchmgroup0 = new ArrayList<>();
        Searchmgroup1 = new ArrayList<>();
        Searchmseller = new ArrayList<>();

        for(int i=0 ; i<mtitle.size() ; i++) {
            if(mtitle.get(i).contains(et_search.getText().toString())){
                Searchmtitle.add(mtitle.get(i));
                Searchmphoto.add(mphoto.get(i));
                Searchmgroup0.add(mgroup0.get(i));
                Searchmgroup1.add(mgroup1.get(i));
                Searchmseller.add(mseller.get(i));
            }
        }
        template_recycler.setLayoutManager(new LinearLayoutManager(mContext)) ;
        Adapter adapter = new Adapter(Searchmtitle,Searchmgroup0,Searchmgroup1,Searchmphoto,Searchmseller,this,mContext) ;
        template_recycler.setAdapter(adapter) ;
    }

    //RecyclerView
    @Override
    public void DoSomeThing(String title, String contents, String link, String photo, String sell){
        Intent intent = new Intent(mContext,DetailActivity.class);
        intent.putExtra("title",title);
        intent.putExtra("contents",contents);
        intent.putExtra("link",link);
        intent.putExtra("photo",photo);
        intent.putExtra("sell",sell);
        startActivity(intent);
    }

//
//            objectMap.put("Group0", Group0);
//        objectMap.put("Group1", Group1);
//        objectMap.put("file", file_name);
//        objectMap.put("title",et_title.getText().toString());
//
    private void NoticeListener(DataSnapshot dataSnapshot) {
        // dataSnapshot 밸류값 가져옴

        Iterator i = dataSnapshot.getChildren().iterator();
        Log.e("HAN","NoticeListener");
        while (i.hasNext()) {
            group0 = (String) ((DataSnapshot) i.next()).getValue();
            group1 = (String) ((DataSnapshot) i.next()).getValue();
            photo = (String) ((DataSnapshot) i.next()).getValue();
            String sell = (String) ((DataSnapshot) i.next()).getValue();
            title = (String) ((DataSnapshot) i.next()).getValue();

            mseller.add(sell);
            mtitle.add(title);
            mgroup0.add(group0);
            mgroup1.add(group1);
            mphoto.add(photo);
            Log.e("HAN","1111111111");
        }


        template_recycler.setLayoutManager(new LinearLayoutManager(mContext)) ;
        Adapter adapter = new Adapter(mtitle,mgroup0,mgroup1,mphoto,mseller,this,mContext) ;
        template_recycler.setAdapter(adapter) ;

    }

}
