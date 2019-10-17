package muchbeer.raum.data.repository.paging;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import muchbeer.raum.data.model.Movie;
import muchbeer.raum.data.repository.datasource.RemoteDataSource;
import muchbeer.raum.data.service.MovieDataService;

public class RemoteDataSourceFactory extends DataSource.Factory<Long, Movie> {

    private RemoteDataSource movieDataSource;
    private MovieDataService movieDataService;
    private Application application;
    private MutableLiveData<RemoteDataSource> mutableLiveData;


    public RemoteDataSourceFactory(MovieDataService movieDataService, Application application) {
        this.movieDataService = movieDataService;
        this.application= application;
        mutableLiveData=new MutableLiveData<>();
    }


    @NonNull
    @Override
    public DataSource<Long, Movie> create() {
        /*movieDataSource = new RemoteDataSource(movieDataService, application);
        mutableLiveData.postValue(movieDataSource);
        return movieDataSource;*/

        return null;
    }

    public MutableLiveData<RemoteDataSource> getMutableLiveData() {
        return mutableLiveData;
    }
}
