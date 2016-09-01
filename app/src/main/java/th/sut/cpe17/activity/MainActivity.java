package th.sut.cpe17.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import th.sut.cpe17.R;
import th.sut.cpe17.session.SessionManager;

/**
 * Created by Song-rit Maleerat on 31/8/2559.
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SessionManager session = SessionManager.getInstance();
                if (session.getSharedPreferences() == null) {
                    session.setSharedPreferences(getApplicationContext());
                }
                session.logOut();
                finish();
                Toast.makeText(MainActivity.this,"Logout Success", Toast.LENGTH_SHORT).show();
                Snackbar.make(view, "log out", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        SessionManager session = SessionManager.getInstance();
        if (session.getSharedPreferences() == null) {
            session.setSharedPreferences(getApplicationContext());
        }
        Toast.makeText(this, session.getName(), Toast.LENGTH_SHORT).show();

    }

}
