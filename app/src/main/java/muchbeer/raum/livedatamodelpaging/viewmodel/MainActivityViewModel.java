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
import muchbeer.raum.data.repository.MovieRepository;
import muchbeer.raum.data.repository.MovieRepositoryInterface;

public class MainActivityViewModel extends AndroidViewModel {

    private static final String TAG = MainActivityViewModel.class.getSimpleName();
    private MovieRepositoryInterface mMovieRepositoryInterface;

    private MovieDao concertDao;
    public final LiveData<PagedList<Movie>> concertList;

    public LiveData<List<Movie>> getAllMovieLocalData() {
        return mMovieRepositoryInterface.getMovieData();
    }

    public LiveData<String> getErrorUpdates() {
        return mMovieRepositoryInterface.getErrorStream();
    }

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        this.concertDao = concertDao;

        mMovieRepositoryInterface = MovieRepository.create(application);

        concertList = new LivePagedListBuilder<Integer, Movie>(
                concertDao.getAllMovieOnPaging(), 50).build();
    }

    @Override
    protected void onCleared() {
        Log.d(TAG, "onCleared() called");
        super.onCleared();
    }

    public void fetchData() {
        mMovieRepositoryInterface.fetchData();
    }

}