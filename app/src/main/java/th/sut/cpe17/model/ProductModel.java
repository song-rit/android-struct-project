package th.sut.cpe17.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Song-rit Maleerat on 2/9/2559.
 */
public class ProductModel {

    private int id;
    private String name;

    @SerializedName("image_url")
    private String imageUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
