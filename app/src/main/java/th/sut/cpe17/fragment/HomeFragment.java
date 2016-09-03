package th.sut.cpe17.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import th.sut.cpe17.R;
import th.sut.cpe17.adapter.BannerSlidePagerAdapter;
import th.sut.cpe17.constant.Constant;
import th.sut.cpe17.model.ImageModel;
import th.sut.cpe17.util.CheckNetworkConnection;
import th.sut.cpe17.util.OkHttpRequest;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private ImageModel imageModel;

    private FragmentActivity activity;

    private ViewPager pager;

    private Thread thread;
    private Handler handler;
    private Runnable animateViewPager;
    private boolean stopSliding = false;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        infixView(view);

        pager.setOnTouchListener(onTouchListener);
        return view;
    }

    private void infixView(View view) {
        pager = (ViewPager) view.findViewById(R.id.view_pager_home_banner_slide);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (imageModel == null) {
            sendRequest();
        } else {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    handler.postDelayed(animateViewPager, Constant.BANNER_SLIDE.ANIM_VIEWPAGER_DELAY);
                }
            });
        }
    }

    private void sendRequest() {

        if (CheckNetworkConnection.isConnectionAvailable(activity)) {

            thread = new Thread() {

                @Override
                public void run() {
                    super.run();
                    try {
                        final String responseString = new OkHttpRequest().HTTPGet(Constant.URL.URL_BANNER_IMAGE);
                        final Gson gson = new Gson();
                        imageModel = gson.fromJson(responseString, ImageModel.class);

                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity, gson.toJson(imageModel.getProducts().get(0).getName()), Toast.LENGTH_LONG).show();
                                BannerSlidePagerAdapter adapter = new BannerSlidePagerAdapter(activity, imageModel.getProducts());
                                pager.setAdapter(adapter);
                                handler = new Handler();
                                runnable(imageModel.getProducts().size());
                            }
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            thread.start();

        } else {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void runnable(final int size) {
        animateViewPager = new Runnable() {
            @Override
            public void run() {
                int currentItem = pager.getCurrentItem();
                if (!stopSliding) {
                    if (currentItem == size - 1) {
                        pager.setCurrentItem(0);
                    } else {
                        pager.setCurrentItem(currentItem + 1, true);
                    }
                    handler.postDelayed(animateViewPager, Constant.BANNER_SLIDE.ANIM_VIEWPAGER_DELAY);
                }
            }
        };
        handler.postDelayed(animateViewPager, Constant.BANNER_SLIDE.ANIM_VIEWPAGER_DELAY);
    }

    private ViewPager.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            v.getParent().requestDisallowInterceptTouchEvent(true);

            switch (event.getAction()) {

                case MotionEvent.ACTION_UP:
                    Toast.makeText(activity, "ACTION_UP", Toast.LENGTH_SHORT).show();
                    stopSliding = false;
                    handler.postDelayed(animateViewPager, Constant.BANNER_SLIDE.ANIM_VIEWPAGER_DELAY_USER_VIEW);
                    break;

                case MotionEvent.ACTION_MOVE:
                    Toast.makeText(activity, "ACTION_MOVE", Toast.LENGTH_SHORT).show();
                    stopSliding = true;
                    handler.removeCallbacks(animateViewPager);
                    break;
            }
            return false;
        }
    };

    @Override
    public void onPause() {
        super.onStop();
        handler.removeCallbacks(animateViewPager);
    }
}
