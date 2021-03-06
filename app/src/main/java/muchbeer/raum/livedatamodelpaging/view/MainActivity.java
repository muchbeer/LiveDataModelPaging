package muchbeer.raum.livedatamodelpaging.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import muchbeer.raum.data.model.Movie;
import muchbeer.raum.livedatamodelpaging.R;
import muchbeer.raum.livedatamodelpaging.adapter.MovieAdapter;
import muchbeer.raum.livedatamodelpaging.databinding.ActivityMainBinding;
import muchbeer.raum.livedatamodelpaging.screen.MainScreen;
import muchbeer.raum.livedatamodelpaging.screen.MovieNotificationHelper;
import muchbeer.raum.livedatamodelpaging.viewmodel.MainActivityViewModel;

public class MainActivity extends AppCompatActivity implements MainScreen {


    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private PagedList<Movie> moviesPaging;
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MainActivityViewModel mainActivityViewModel;
    private ActivityMainBinding activityMainBinding;
    private final static int DATA_FETCHING_INTERVAL=5*1000; //5 seconds
    private long mLastFetchedDataTimeStamp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

     //   getSupportActionBar().setTitle("Gianna Movie Theater");

        activityMainBinding= DataBindingUtil.setContentView(this,R.layout.activity_main);

        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        getPopularMovies();

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

        mainActivityViewModel.getMoviesPaging().observe(this, new Observer<PagedList<Movie>>() {
            @Override
            public void onChanged(PagedList<Movie> moviesOnDBandNet) {
                Log.d(LOG_TAG, "This is where the notification called");
                MovieNotificationHelper.getInstance(getApplication()).createNotification();

                moviesPaging = moviesOnDBandNet;
                showOnRecyclerView();
                if(moviesPaging!=null) {
                    swipeRefreshLayout.setRefreshing(false);
                }

            }
        });

        mainActivityViewModel.getErrorUpdates().observe(this, errorInfo-> {
            setError(errorInfo);
        });
    }

    private void showErrorToastUpdate(String error) {
        Toast.makeText(this, "Error: " + error, Toast.LENGTH_LONG).show();
    }

    private void showOnRecyclerView() {

        recyclerView = activityMainBinding.rvMovies;
    //    movieAdapter = new MovieAdapter(this, moviesMain);
//paging here below
        movieAdapter = new MovieAdapter(this, moviesPaging);
        movieAdapter.submitList(moviesPaging);

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {


            recyclerView.setLayoutManager(new GridLayoutManager(this, 4));


        }

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(movieAdapter);
        movieAdapter.notifyDataSetChanged();


    }

    @Override
    public void updateData(List<Movie> data) {
        //This is when you don't need Binding adapter
      /*  mLastFetchedDataTimeStamp=System.currentTimeMillis();
        mAdapter.setItems(data);
        mSwipeRefreshLayout.setRefreshing(false);*/
    }

    @Override
    public void setError(String msg) {
        showErrorToastUpdate(msg);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.list_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint(getResources().getString(R.string.search_hint));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(searchView.getContext(), MainActivitySearch.class);
                intent.setAction(Intent.ACTION_SEARCH);
                intent.putExtra(SearchManager.QUERY,query);
                startActivity(intent);
                     return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.actionSettings: {
             /*   if (isAdded()) {
                    Navigation.findNavController(getView()).navigate(ListFragmentDirections.actionSetting());
                }*/
                Toast.makeText(getApplicationContext(), "This is where you open SettingFragment", Toast.LENGTH_LONG).show();

                break;
            }

        }
        return super.onOptionsItemSelected(item);
    }
}