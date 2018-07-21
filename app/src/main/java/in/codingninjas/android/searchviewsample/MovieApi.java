package in.codingninjas.android.searchviewsample;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApi {


    @GET("{type}")
    Call<MovieResponse> getMovies(@Path("type") String name, @Query("api_key") String key);

    @GET("multi")
    Call<SearchResponse> search(@Query("api_key") String key, @Query("query") String query);

}
