package th.sut.cpe17.util;

import android.annotation.TargetApi;
import android.os.Build;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Developer on 31/8/2559.
 */
public class OkHttpRequest {

    public String HTTPGet(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
}
