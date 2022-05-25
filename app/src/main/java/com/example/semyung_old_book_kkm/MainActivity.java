package com.example.semyung_old_book_kkm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView; //바텀 네비게이션 뷰
    private FragmentManager fm;
    private FragmentTransaction ft;
    private ChatFrag chatFrag;
    private MainFrag mainFrag;
    private UploadFrag uploadFrag;
    Context mContext;
    private Button button_v;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext=this;

        bottomNavigationView = findViewById(R.id.bottomNavi);

        Button button_v = findViewById(R.id.button_v);
        button_v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),PhoneVerify.class);
                startActivity(intent);
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()){
                case R.id.action_like:
                    setFrag(0);
                    break;
                case R.id.action_home:
                    setFrag(1);
                    break;
                case R.id.action_account:
                    setFrag(2);

            }
            return true;
        });
        chatFrag = new ChatFrag(mContext);
        mainFrag = new MainFrag(mContext);
        uploadFrag = new UploadFrag(mContext);

        setFrag(1); // 첫 fragment 화면 지정할 것인지 선택.
    }

    //fragment 교체가 일어나는 실행문
    private void setFrag(int n){
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        switch (n) {
            case 0:
                ft.replace(R.id.main_frame, chatFrag);
                ft.commit();
                break;
            case 1:
                ft.replace(R.id.main_frame, mainFrag);
                ft.commit();
                break;
            case 2:
                ft.replace(R.id.main_frame, uploadFrag);
                ft.commit();
                break;
        }
    }



}