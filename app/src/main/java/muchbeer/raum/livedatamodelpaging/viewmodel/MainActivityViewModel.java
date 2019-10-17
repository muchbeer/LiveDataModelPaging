package muchbeer.raum.livedatamodelpaging.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import java.util.List;

import muchbeer.raum.data.db.MovieDao;
import muchbeer.raum.data.model.Movie;
import muchbeer.raum.data.model.NetworkState;
import muchbeer.raum.data.repository.MovieRepository;
import muchbeer.raum.data.repository.MovieRepositoryInterface;
import muchbeer.raum.data.repository.MovieRepositoryPaging;

public class MainActivityViewModel extends AndroidViewModel {

    private static final String TAG = MainActivityViewModel.class.getSimpleName();


    private MovieRepositoryPaging mMovieRepositoryInterface;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);

        mMovieRepositoryInterface = MovieRepositoryPaging.getInstance(application);


    }

    @Override
    protected void onCleared() {
        Log.d(TAG, "onCleared() called");
        super.onCleared();
    }

    public LiveData<PagedList<Movie>> getMoviesPaging() {
        return mMovieRepositoryInterface.getMoviesPaging();
    }

    public LiveData<String> getErrorUpdates() {
        return mMovieRepositoryInterface.getErrorMessage();
    }

}