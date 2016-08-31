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
    private Context context;

    public static void setSession(LoginModel login) {

    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public void setSharedPreferences(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(Constant.SHARE_PREFERENCE_KEY.LOGIN, Context.MODE_PRIVATE);
        this.editor = this.sharedPreferences.edit();
    }

    public static SessionManager getInstance(Context context) {
        return ourInstance;
    }

    public Boolean checkLogin() {

        if (isLoggedIn()) {
            return true;
        } else {
            return false;
        }
    };

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(Constant.SHARE_PREFERENCE_KEY.IS_LOGIN, false);
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

}
