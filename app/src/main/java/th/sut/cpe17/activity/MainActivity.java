package th.sut.cpe17.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import th.sut.cpe17.R;
import th.sut.cpe17.fragment.HomeFragment;
import th.sut.cpe17.fragment.SettingFragment;
import th.sut.cpe17.model.ImageModel;
import th.sut.cpe17.session.SessionManager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private LinearLayout signOut;
    private SessionManager session;

    private NavigationView navigationView;

    private ActionBar actionBar;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private ViewPager pager;

    private ImageModel imageModel;

    private Handler handler;
    private Runnable animateViewPager;

    private boolean stopSliding = false;

    int idNavMenu = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Customize the ActionBar
        initActionBar();

        infixView();

        navigationView.setNavigationItemSelectedListener(this);

        // Get session
        session = SessionManager.getInstance();
        if (session == null) {
            session.setSharedPreferences(getApplicationContext());
        }

        // Sign Out Event
        signOut.setOnClickListener(signOutListener());

        // Init value on NavigationDrawable
        initValueUser();

        // Show banner slide
        showBannerSlide();
    }

    private void showBannerSlide() {

    }

    private void initValueUser() {

        // Get view from "nav_header_main.xml"
        View navHeaderView = navigationView.getHeaderView(0);

        // Set image profile
        Bitmap bm = BitmapFactory.decodeResource(getResources(),
                R.drawable.user);
        ImageView mImage = (ImageView) navHeaderView.findViewById(R.id.image_profile);
        mImage.setImageBitmap(bm);

        // Set profile name
        TextView textViewProfileName = (TextView) navHeaderView.findViewById(R.id.text_view_profile_name);
        textViewProfileName.setText(session.getName() + " " + session.getLastName());

        // Set profile email
        TextView textViewProfileEmail = (TextView) navHeaderView.findViewById(R.id.text_view_profile_email);
        textViewProfileEmail.setText(session.getEmail());

        // Open HomeFragment
        HomeFragment homeFragment = HomeFragment.newInstance();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_main, homeFragment);
        fragmentTransaction.commit();
    }

    private View.OnClickListener signOutListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(MainActivity.this, "Sign Out Success", Toast.LENGTH_SHORT).show();
                drawer.closeDrawer(GravityCompat.END);
                SessionManager session = new SessionManager();

                // Sign Out
                session.logOut();
                finish();
            }
        };
    }

    private void infixView() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        signOut = (LinearLayout) findViewById(R.id.sign_out);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        pager = (ViewPager) findViewById(R.id.view_pager_home_banner_slide);

    }

    private void initActionBar() {
        actionBar = getSupportActionBar();
        View viewActionBar = getLayoutInflater().inflate(R.layout.toolbar, null);

        // Set center TextView in the ActionBar
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        TextView textViewTitle = (TextView) viewActionBar.findViewById(R.id.text_view_title_actionbar);
        textViewTitle.setText("MainActivity");
        actionBar.setCustomView(viewActionBar, params);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_right) {
            if (drawer.isDrawerOpen(Gravity.RIGHT)) {
                drawer.closeDrawer(Gravity.RIGHT);
            } else {
                drawer.openDrawer(Gravity.RIGHT);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Log.d("ItmeId", String.valueOf(id));
        Log.d("idNavMenu", String.valueOf(idNavMenu));

        if (id == R.id.nav_home) {

            if (idNavMenu != id && idNavMenu != 0) {
                HomeFragment homeFragment = HomeFragment.newInstance();
                fragmentManager = getSupportFragmentManager();
                fragmentManager.popBackStack();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout_main, homeFragment);
                fragmentTransaction.commit();
            }

        } else if (id == R.id.setting) {
            if (idNavMenu != id) {
                SettingFragment settingFragment = SettingFragment.newInstance();
                fragmentManager = getSupportFragmentManager();
                fragmentManager.popBackStack();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout_main, settingFragment);
                fragmentTransaction.commit();
            }
        }

        idNavMenu = id;

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }
}
