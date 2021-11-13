package com.example.justreview;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceConfig {

    private SharedPreferences sharedPreferences, sharedPreferencesAdminStatus, sharedPreferencesUserID;
    private Context context;

    public SharedPreferenceConfig(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.login_shared_preference), Context.MODE_PRIVATE);
        sharedPreferencesAdminStatus = context.getSharedPreferences(context.getResources().getString(R.string.is_admin_preference), Context.MODE_PRIVATE);
        sharedPreferencesUserID = context.getSharedPreferences(context.getResources().getString(R.string.id_user_preference), Context.MODE_PRIVATE);
    }


    public void login_status (boolean status) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(context.getResources().getString(R.string.login_status_shared_preference), status);
        editor.commit();
    }

    public boolean read_login_status() {
        boolean status = false;
        status = sharedPreferences.getBoolean(context.getResources().getString(R.string.login_status_shared_preference), false);
        return status;
    }

    public void is_admin_status (boolean status) {
        SharedPreferences.Editor editor = sharedPreferencesAdminStatus.edit();
        editor.putBoolean(context.getResources().getString(R.string.is_admin_status_shared_preference), status);
        editor.commit();
    }

    public boolean read_admin_status() {
        boolean status = false;
        status = sharedPreferencesAdminStatus.getBoolean(context.getResources().getString(R.string.is_admin_status_shared_preference), false);
        return status;
    }

    public void set_user_id (int id) {
        SharedPreferences.Editor editor = sharedPreferencesUserID.edit();
        editor.putInt(context.getResources().getString(R.string.id_user_shared_preference), id);
        editor.commit();
    }

    public int read_user_id() {
        int id = 0;
        id = sharedPreferencesUserID.getInt(context.getResources().getString(R.string.id_user_shared_preference), id);
        return id;
    }
}
