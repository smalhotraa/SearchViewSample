package in.codingninjas.android.searchviewsample;

import com.google.gson.annotations.SerializedName;

public class SearchResult implements Item{

    @SerializedName("original_name")
    String content_name;

    @SerializedName("media_type")
    String category;

    String title;

    String name;

    @Override
    public int getType() {
        return 1;
    }
}
