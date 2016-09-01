package th.sut.cpe17.activity;

import android.content.Intent;
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
    private long time = 3000L;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        session = SessionManager.getInstance();
        if (session.getSharedPreferences() == null) {
            session.setSharedPreferences(getApplicationContext());
        }
        
        if (session.checkLoginValidate()) {
            startActivityMain();
        }

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {

                 session = SessionManager.getInstance();
                if (session.getSharedPreferences() == null) {
                    session.setSharedPreferences(getApplicationContext());
                }
                session.checkLoginValidate();

            }
        };
    }

    private void startActivityMain() {
        Intent intent = new Intent(SplashScreen.this, MainActivity.class);

        // Closing all the Activities
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        // Add new Flag to start new Activity
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);

        // Call this to finish the current activity
        finish();
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
