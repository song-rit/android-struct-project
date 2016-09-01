package th.sut.cpe17.activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import th.sut.cpe17.R;
import th.sut.cpe17.session.SessionManager;

/**
 * Created by Song-rit Maleerat on 31/8/2559.
 */

public class SplashScreen extends AppCompatActivity {

    private Handler handler;
    private Runnable runnable;
    private long delay_time;
    private long time = 1800;
    private SessionManager session;
    private boolean loginStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        session = SessionManager.getInstance();
        if (session.getSharedPreferences() == null) {
            session.setSharedPreferences(getApplicationContext());
        }

        // Get login status
        loginStatus = session.checkLoginValidate();

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (loginStatus) {
                    session.startMainActivity();
                } else {
                    session.startLoginActivity();
                }
                finish();
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        delay_time = time;
        handler.postDelayed(runnable, delay_time);
        time = System.currentTimeMillis();
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
        time = delay_time - (System.currentTimeMillis() - time);
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }
}
