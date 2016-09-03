package th.sut.cpe17.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import th.sut.cpe17.R;
import th.sut.cpe17.fragment.BannerSlideFragment;
import th.sut.cpe17.model.ProductModel;

/**
 * Created by Song-rit Maleerat on 2/9/2559.
 */
public class BannerSlidePagerAdapter extends PagerAdapter{
    private Activity activity;
    private ArrayList<ProductModel> productModels;
    BannerSlideFragment slideFragment;

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;
    ImageLoadingListener imageListener;

    LayoutInflater inflater;

    public BannerSlidePagerAdapter(Activity activity, ArrayList<ProductModel> productModels) {
        this.activity = activity;
        this.productModels = productModels;

        imageListener = new ImageDisplayListener();
        imageLoader.init(ImageLoaderConfiguration.createDefault(activity.getApplicationContext()));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = (LayoutInflater) activity
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.fragment_banner_slide, container, false);

        ImageView image = (ImageView) view.findViewById(R.id.image_view_slide);
        image.setImageResource(R.mipmap.ic_launcher);
        imageLoader.displayImage((productModels.get(position)).getImageUrl(), image, options, imageListener);
        container.addView(view);

        return view;
    }


    @Override
    public int getCount() {
        return productModels.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    private static class ImageDisplayListener extends
            SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections
                .synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view,
                                      Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }
}
