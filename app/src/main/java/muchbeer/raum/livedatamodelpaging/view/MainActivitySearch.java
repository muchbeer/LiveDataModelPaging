package muchbeer.raum.livedatamodelpaging.view;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import java.util.List;

import muchbeer.raum.data.model.Movie;
import muchbeer.raum.livedatamodelpaging.R;
import muchbeer.raum.livedatamodelpaging.adapter.MovieAdapter;
import muchbeer.raum.livedatamodelpaging.databinding.ActivityMainBinding;
import muchbeer.raum.livedatamodelpaging.screen.MainScreen;
import muchbeer.raum.livedatamodelpaging.screen.MovieNotificationHelper;
import muchbeer.raum.livedatamodelpaging.viewmodel.MainActivityViewModel;
import muchbeer.raum.livedatamodelpaging.viewmodel.MainActivityViewModelSearch;

public class MainActivitySearch  extends AppCompatActivity implements MainScreen {


    private static final String LOG_TAG = MainActivitySearch.class.getSimpleName();
    private PagedList<Movie> moviesPaging;
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MainActivityViewModelSearch mainActivityViewModel;
    private ActivityMainBinding activityMainBinding;
    private final static int DATA_FETCHING_INTERVAL=5*1000; //5 seconds
    private long mLastFetchedDataTimeStamp;
    private String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(LOG_TAG, "tHE search has enter the new activity");
        activityMainBinding= DataBindingUtil.setContentView(this,R.layout.activity_main);

        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModelSearch.class);

        getPopularMovies();

        handleIntent(getIntent());

        swipeRefreshLayout = activityMainBinding.swRefresh;
               swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (System.currentTimeMillis() - mLastFetchedDataTimeStamp < DATA_FETCHING_INTERVAL) {
                    Log.d(LOG_TAG, "\tNot fetching from network because interval didn't reach");
                    swipeRefreshLayout.setRefreshing(false);
                    return;
                } else if(System.currentTimeMillis() == DATA_FETCHING_INTERVAL) {
                    Log.d(LOG_TAG, "\tclose the refresh");
                    swipeRefreshLayout.setRefreshing(false);
                    return;
                }
                getPopularMovies();
            }
        });

          }

    public void getPopularMovies() {
        Log.d(LOG_TAG, "This is where the searched activity called");
        mainActivityViewModel.getMoviesPaging().observe(this, new Observer<PagedList<Movie>>() {
            @Override
            public void onChanged(PagedList<Movie> moviesOnDBandNet) {

                MovieNotificationHelper.getInstance(getApplication()).createNotification();

                moviesPaging = moviesOnDBandNet;
                showOnRecyclerView();
                if(moviesPaging!=null) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
          query = intent.getStringExtra(SearchManager.QUERY);
            Log.d(LOG_TAG, "tHE search is : " + query);
            mainActivityViewModel.getSearchMovies().setValue("%" + query + "%");
                }
           }
    private void showErrorToastUpdate(String error) {
        Toast.makeText(this, "Error: " + error, Toast.LENGTH_LONG).show();
    }

    private void showOnRecyclerView() {
        recyclerView = activityMainBinding.rvMovies;
        movieAdapter = new MovieAdapter(this, moviesPaging);
        movieAdapter.submitList(moviesPaging);

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));  }
        else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 4));   }

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(movieAdapter);
        movieAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateData(List<Movie> data) {    }

    @Override
    public void setError(String msg) {
        showErrorToastUpdate(msg);
    }

}
