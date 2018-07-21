package in.codingninjas.android.searchviewsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    String key = "63c643047f96b93c103905a07a6a0992";

    SearchView searchView;
    ItemAdapter itemAdapter;
    ListView listView;
    ArrayList<Item> items = new ArrayList<>();
    ProgressBar progressBar;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main,menu);
        MenuItem menuItem = menu.findItem(R.id.searchItem);
        searchView = (SearchView) menuItem.getActionView();

        searchView.setQueryHint("Type your query");
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(!s.isEmpty()) {
                    updateListOfResults(s);
                }
                return true;
            }
        });

        menuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                fetchPopularMovies();
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    private void updateListOfResults(String s) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/search/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieApi movieApi = retrofit.create(MovieApi.class);
        progressBar.setVisibility(View.VISIBLE);
        Call<SearchResponse> call = movieApi.search(key,s);
        call.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {

                SearchResponse searchResponse = response.body();

                if(searchResponse != null){

                    ArrayList<SearchResult> searchResultsApi = searchResponse.results;
                    items.clear();
                    items.addAll(searchResultsApi);
                    itemAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);



                }else{
                    Toast.makeText(MainActivity.this, "No results to show", Toast.LENGTH_LONG).show();
                    items.clear();
                    itemAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    public void fetchPopularMovies(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/movie/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieApi movieApi = retrofit.create(MovieApi.class);

        Call<MovieResponse> call = movieApi.getMovies("popular",key);
        progressBar.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                MovieResponse MovieResponse = response.body();

                if(MovieResponse != null){

                    ArrayList<Movie> results = MovieResponse.results;
                    items.clear();
                    items.addAll(results);
                    itemAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.d("lalala",t.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        itemAdapter = new ItemAdapter(this,0,items);
        listView.setAdapter(itemAdapter);
        progressBar = findViewById(R.id.progressBar);

        fetchPopularMovies();

    }
}
