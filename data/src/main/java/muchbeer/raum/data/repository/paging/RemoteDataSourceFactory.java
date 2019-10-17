package muchbeer.raum.data.repository.paging;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import io.reactivex.subjects.ReplaySubject;
import muchbeer.raum.data.model.Movie;
import muchbeer.raum.data.repository.datasource.RemoteDataSource;
import muchbeer.raum.data.service.MovieDataService;

public class RemoteDataSourceFactory extends DataSource.Factory<Long, Movie> {

    private static final String TAG = RemoteDataSourceFactory.class.getSimpleName();
    private MutableLiveData<RemoteDataSourcePageKey> networkStatus;
    private MutableLiveData<String> mError;
    private RemoteDataSourcePageKey moviesPageKeyedDataSource;


    public RemoteDataSourceFactory() {

        this.networkStatus=new MutableLiveData<>();
        this.mError = new MutableLiveData<>();
        moviesPageKeyedDataSource = new RemoteDataSourcePageKey();

    }


    @NonNull
    @Override
    public DataSource<Long, Movie> create() {
        networkStatus.postValue(moviesPageKeyedDataSource);
       // mError.postValue(moviesPageKeyedDataSource);
        return moviesPageKeyedDataSource;

    }

    public MutableLiveData<RemoteDataSourcePageKey> getNetworkStatus() {
        return networkStatus;
    }

    public MutableLiveData<String> getErrorMessage() {
        return moviesPageKeyedDataSource.getErrorStream();
    }
    public ReplaySubject<Movie> getMoviesPaging() {
        return moviesPageKeyedDataSource.getMoviesReplay();
    }
}
