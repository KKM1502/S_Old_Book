package com.example.semyung_old_book_kkm;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;


public class data {

    private final static String PREF_NAME = "pref_sharedpreferences_data";
    private static final String NEW_NOTIFY_YN_KEY = "new_notify_yn_key";
    private static final String NEW_ID_KEY = "new_id_key";

    private static SharedPreferences mSharedPreferences;
    private static SharedPreferences.Editor mEditor;
    private static Context mContext;

    private static data mInstance;
    public synchronized static data getInstance(Context ctx) {
        mContext = ctx;
        if (mInstance == null) {
            mInstance = new data();
            mSharedPreferences = ctx.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
            mEditor = mSharedPreferences.edit();
        }
        return mInstance;
    }

    /*------ this is for new notify  --------*/
    public void setID(String flag){
        mEditor.putString(NEW_ID_KEY, flag);
        mEditor.commit();
    }

    public String getID() {
        return mSharedPreferences.getString(NEW_ID_KEY, "N");
    }


}
