package muchbeer.raum.data.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.paging.PagedList;

import io.reactivex.schedulers.Schedulers;
import muchbeer.raum.data.db.RoomDb;
import muchbeer.raum.data.model.Movie;
import muchbeer.raum.data.model.NetworkState;
import muchbeer.raum.data.repository.paging.LocalDataSourceFactory;
import muchbeer.raum.data.repository.paging.RemoteDataSourceFactory;

public class MovieRepositoryPaging {

    private static final String TAG = MovieRepositoryPaging.class.getSimpleName();
    private static MovieRepositoryPaging instance;
    final private MovieNetwork network;
    final private RoomDb database;
    final private MediatorLiveData liveDataMerger;

    public MovieRepositoryPaging(Context mContext) {
        RemoteDataSourceFactory netDataSourceFactory = new RemoteDataSourceFactory();

        network = new MovieNetwork(netDataSourceFactory, boundaryCallback);

        database = RoomDb.getDatabase(mContext.getApplicationContext());
        // when we get new movies from net we set them into the database
        liveDataMerger = new MediatorLiveData<>();

        liveDataMerger.addSource(network.getPagedMoviesByPaging(), onlineValue -> {
            liveDataMerger.setValue(onlineValue);
            Log.d(TAG, onlineValue.toString());
        });

        // save the movies into db
        netDataSourceFactory.getMoviesPaging().
                observeOn(Schedulers.io()).
                subscribe(movie -> {
                    database.movieDao().insertMoviePaging(movie);
                });
    }

    private PagedList.BoundaryCallback<Movie> boundaryCallback = new PagedList.BoundaryCallback<Movie>() {
        @Override
        public void onZeroItemsLoaded() {
            super.onZeroItemsLoaded();
            liveDataMerger.addSource(database.getMoviesPagingLocal(), value -> {
                liveDataMerger.setValue(value);
                liveDataMerger.removeSource(database.getMoviesPagingLocal());
            });
        }
    };

    public static MovieRepositoryPaging getInstance(Context context){
        if(instance == null){
            instance = new MovieRepositoryPaging(context);
        }
        return instance;
    }

    public LiveData<PagedList<Movie>> getMoviesPaging(){
        return  liveDataMerger;
    }

    public LiveData<NetworkState> getNetworkState() {
        return network.getNetworkState();
    }
}
