package th.sut.cpe17.util;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import th.sut.cpe17.constant.Constant;
import th.sut.cpe17.model.ImageModel;

/**
 * Created by Song-rit Maleerat on 3/9/2559.
 */
public class BannerSlide {

    private void sendRequest(final Activity activity) {
        if (CheckNetworkConnection.isConnectionAvailable(activity)) {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    super.run();
                    OkHttpRequest httpRequest = new OkHttpRequest();
                    try {
                        final String responseString = httpRequest.HTTPGet(Constant.URL.URL_BANNER_IMAGE);
                        final Gson gson = new Gson();
//                        imageModel = gson.fromJson(responseString, ImageModel.class);
//
//                        activity.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(activity, imageModel.getProducts().get(0).getImageUrl(), Toast.LENGTH_SHORT).show();
//                            }
//                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            thread.start();
        }
    }
}
