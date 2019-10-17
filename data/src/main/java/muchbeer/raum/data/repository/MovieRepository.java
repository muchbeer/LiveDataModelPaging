package muchbeer.raum.data.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import muchbeer.raum.data.model.Movie;
import muchbeer.raum.data.repository.datasource.LocalDataSource;
import muchbeer.raum.data.repository.datasource.RemoteDataSource;

public class MovieRepository implements MovieRepositoryInterface{

    private static final String TAG = MovieRepository.class.getSimpleName();
    private ExecutorService mExecutor = Executors.newFixedThreadPool(5);
    private RemoteDataSource mRemoteDataSource;
    private LocalDataSource mLocalDataSource;


    MediatorLiveData<List<Movie>> mMovieDataMerger = new MediatorLiveData<>();
    MediatorLiveData<String> mMovieErrorMerger = new MediatorLiveData<>();

    private MovieRepository(RemoteDataSource mRemoteDataSource, final LocalDataSource mLocalDataSource) {

        Log.d(TAG, "Entered the MovieRepository:: ");
        this.mRemoteDataSource = mRemoteDataSource;
        this.mLocalDataSource = mLocalDataSource;

        mMovieDataMerger.addSource(this.mRemoteDataSource.getMovieDataStream(), new Observer<List<Movie>>() {
            @Override
            public void onChanged(final List<Movie> onlineMovies) {
                mExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "mDataLiveDataMerger onChange: " + onlineMovies);
                        mMovieDataMerger.postValue(onlineMovies);
                        mLocalDataSource.insertMoviesOnline2Local(onlineMovies);
                    }
                });
            }
        });

        mMovieDataMerger.addSource(this.mLocalDataSource.getMovieDataStream(), new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> locaMovies) {
                Log.d(TAG, "mDataMerger Local on change invoke with value: "+ locaMovies);
                mMovieDataMerger.postValue(locaMovies);
            }
        });

        mMovieErrorMerger.addSource(this.mRemoteDataSource.getErrorStream(), new Observer<String>() {
            @Override
            public void onChanged(String loadError) {
                Log.d(TAG, "mDataMerger\tmRemote on Error Remote Fetch onChange invoked");
                mMovieErrorMerger.postValue(loadError);
            }
        });

        mMovieErrorMerger.addSource(this.mLocalDataSource.getErrorStream(), new Observer<String>() {
            @Override
            public void onChanged(String erorString) {
                mMovieErrorMerger.setValue(erorString);
            }
        });

    }

    public static MovieRepositoryInterface create(Application application) {
        final RemoteDataSource remoteDataSource = new RemoteDataSource(application);
        final LocalDataSource localDataSource = new LocalDataSource(application);

        return new MovieRepository(remoteDataSource, localDataSource);
    }

    @Override
    public LiveData<List<Movie>> getMovieData() {
        return mMovieDataMerger;
    }

    @Override
    public LiveData<String> getErrorStream() {
        return mMovieErrorMerger;
    }

    @Override
    public void fetchData() {
        mRemoteDataSource.fetch();
    }
}
