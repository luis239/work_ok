package com.immagine.workok;

import android.content.Context;
import android.content.SharedPreferences;

import com.immagine.workok.model.User;


/**
 * Utility class for easy access to commonly used preference data units.
 *
 * @author Eduardo Naveda
 */
public class PreferencesUtil {

    private static final String WORK_OK_PREFERENCES = "workok_preferences";

    private Context context;

    public PreferencesUtil(Context context) {
        this.context = context;
    }

    public void SetUser(User user) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(WORK_OK_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", user.getFullname());
        editor.putInt("id", user.getUser_id());
        editor.commit();
    }

    public int getUserId() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(WORK_OK_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getInt("id", 0);
    }
    public String getUserName() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(WORK_OK_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString("name", "");
    }

    public void setToken(String token){

        SharedPreferences sharedPreferences = context.getSharedPreferences(WORK_OK_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", token);
        editor.commit();
    }

    public String getToken(){

        SharedPreferences sharedPreferences = context.getSharedPreferences(WORK_OK_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString("token", "");
    }

    public void setTokenUser(int id){
        SharedPreferences sharedPreferences = context.getSharedPreferences(WORK_OK_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("UserTokenID", id);
        editor.commit();
    }

    public int getTokenUser() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(WORK_OK_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getInt("UserTokenID", 0);
    }
}
