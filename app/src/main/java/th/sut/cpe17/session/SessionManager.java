package th.sut.cpe17.session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import th.sut.cpe17.activity.Login;
import th.sut.cpe17.constant.Constant;
import th.sut.cpe17.model.LoginModel;

/**
 * Created by Song-rit Maleerat on 31/8/2559.
 */
public class SessionManager {
    private static SessionManager ourInstance = new SessionManager();
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static Context context;

    public static void setSession(LoginModel login) {
        // Save session status login = true
        editor.putBoolean(Constant.SHARE_PREFERENCE_KEY.IS_LOGIN, login.getStatus());

        // Save session user data
        editor.putString(Constant.SHARE_PREFERENCE_KEY.NAME, login.getData().getName());
        editor.putString(Constant.SHARE_PREFERENCE_KEY.USER_NAME, login.getData().getLastName());
        editor.putString(Constant.SHARE_PREFERENCE_KEY.LAST_NAME, login.getData().getLastName());
        editor.putString(Constant.SHARE_PREFERENCE_KEY.UNIVERSITY, login.getData().getUniversity());
        editor.putString(Constant.SHARE_PREFERENCE_KEY.MAJOR, login.getData().getMajor());

        // Commit changes
        editor.commit();
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public void setSharedPreferences(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(Constant.SHARE_PREFERENCE_KEY.SHARE_PREF, Context.MODE_PRIVATE);
        this.editor = this.sharedPreferences.edit();
    }

    public static SessionManager getInstance() {
        return ourInstance;
    }

    public boolean checkLoginValidate() {

        if (!isLoggedIn()){
            startActivityLogin();
            return false;
        }
        return true;
    }

    private void startActivityLogin() {
        Intent intent = new Intent(context, Login.class);

        // Closing all the Activities
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(intent);
    }

    public void logOut() {
        editor.clear();
        editor.commit();

        Intent intent = new Intent(context, Login.class);

        // Closing all the Activities
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(intent);
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(Constant.SHARE_PREFERENCE_KEY.IS_LOGIN, false);
    }
    public String getUserName() {
        return sharedPreferences.getString(Constant.SHARE_PREFERENCE_KEY.USER_NAME, "");
    }

    public String getName() {
        return sharedPreferences.getString(Constant.SHARE_PREFERENCE_KEY.NAME, "");
    }

    public String getLastName() {
        return sharedPreferences.getString(Constant.SHARE_PREFERENCE_KEY.LAST_NAME, "");
    }

    public String getMajor() {
        return sharedPreferences.getString(Constant.SHARE_PREFERENCE_KEY.MAJOR, "");
    }

    public String getUniversity() {
        return sharedPreferences.getString(Constant.SHARE_PREFERENCE_KEY.UNIVERSITY, "");
    }
}
