package com.example.loopv7.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.loopv7.models.User;

public class SessionManager {
    private static final String PREF_NAME = "LOOP_SESSION";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_ROLE = "user_role";
    private static final String KEY_USER_PHONE = "user_phone";
    private static final String KEY_USER_JSON = "user_json";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void createLoginSession(User user) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putInt(KEY_USER_ID, user.getId());
        editor.putString(KEY_USER_EMAIL, user.getEmail());
        editor.putString(KEY_USER_NAME, user.getName());
        editor.putString(KEY_USER_ROLE, user.getRole());
        editor.putString(KEY_USER_PHONE, user.getPhone());
        editor.commit();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public User getCurrentUser() {
        if (!isLoggedIn()) {
            return null;
        }

        User user = new User();
        user.setId(pref.getInt(KEY_USER_ID, -1));
        user.setEmail(pref.getString(KEY_USER_EMAIL, ""));
        user.setName(pref.getString(KEY_USER_NAME, ""));
        user.setRole(pref.getString(KEY_USER_ROLE, ""));
        user.setPhone(pref.getString(KEY_USER_PHONE, ""));
        return user;
    }

    public int getCurrentUserId() {
        return pref.getInt(KEY_USER_ID, -1);
    }

    public String getCurrentUserRole() {
        return pref.getString(KEY_USER_ROLE, "");
    }

    public boolean isCliente() {
        return "cliente".equals(getCurrentUserRole());
    }

    public boolean isSocia() {
        return "socia".equals(getCurrentUserRole());
    }

    public void updateUserInfo(User user) {
        editor.putString(KEY_USER_NAME, user.getName());
        editor.putString(KEY_USER_PHONE, user.getPhone());
        editor.commit();
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();
    }

    public void clearSession() {
        editor.clear();
        editor.commit();
    }
    
    /**
     * Actualiza el usuario actual en la sesión
     * @param user Usuario actualizado
     */
    public void updateCurrentUser(User user) {
        editor.putString(KEY_USER_NAME, user.getName());
        editor.putString(KEY_USER_EMAIL, user.getEmail());
        editor.putString(KEY_USER_PHONE, user.getPhone());
        editor.putString(KEY_USER_ROLE, user.getRole());
        editor.commit();
    }
}
