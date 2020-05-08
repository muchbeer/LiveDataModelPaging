package muchbeer.raum.livedatamodelpaging.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PagedList;
import muchbeer.raum.data.model.Movie;
import muchbeer.raum.data.repository.MovieRepositoryPagingSearch;
import muchbeer.raum.livedatamodelpaging.info.SharedPreferenceHelper;

public class MainActivityViewModelSearch extends AndroidViewModel {

    private static final String TAG = MainActivityViewModel.class.getSimpleName();
    private MovieRepositoryPagingSearch mMovieRepositoryInterface;
    private SharedPreferenceHelper prefHelper;

    public MainActivityViewModelSearch(@NonNull Application application) {
        super(application);

        mMovieRepositoryInterface = MovieRepositoryPagingSearch.getInstance(application);

    }
    @Override
    protected void onCleared() {
        Log.d(TAG, "onCleared() called");
        super.onCleared();
    }

    public LiveData<PagedList<Movie>> getMoviesPaging() {
        return mMovieRepositoryInterface.getMoviesPaging();
    }

    public MutableLiveData<String> getSearchMovies() {
      return mMovieRepositoryInterface.getSearchMovies();
    }

}
