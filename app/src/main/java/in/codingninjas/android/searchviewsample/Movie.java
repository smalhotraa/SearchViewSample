package in.codingninjas.android.searchviewsample;

public class Movie implements Item{

    int id;
    String title;
    String vote_count;

    @Override
    public int getType() {
        return 0;
    }
}
