package muchbeer.raum.data.repository;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import muchbeer.raum.data.model.Movie;
import muchbeer.raum.data.model.NetworkState;
import muchbeer.raum.data.repository.paging.RemoteDataSourceFactory;
import muchbeer.raum.data.repository.paging.RemoteDataSourcePageKey;

public class MovieNetwork {

    public static final int LOADING_PAGE_SIZE = 20;

    final private static String TAG = MovieNetwork.class.getSimpleName();
    private static final int NUMBERS_OF_THREADS = 4;
    final private LiveData<PagedList<Movie>> moviesPaged;
    final private LiveData<NetworkState> networkState;


    public MovieNetwork(RemoteDataSourceFactory dataSourceFactory, PagedList.BoundaryCallback<Movie> boundaryCallback){

        PagedList.Config pagedListConfig = (new PagedList.Config.Builder())
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(LOADING_PAGE_SIZE)
                .setPageSize(LOADING_PAGE_SIZE).build();

        networkState = Transformations.switchMap(dataSourceFactory.getNetworkStatus(),
                                        (Function<RemoteDataSourcePageKey,
                                        LiveData<NetworkState>>)
                        RemoteDataSourcePageKey::getNetworkState);

        Executor executor = Executors.newFixedThreadPool(NUMBERS_OF_THREADS);

        LivePagedListBuilder livePagedListBuilder = new LivePagedListBuilder(dataSourceFactory, pagedListConfig);
        moviesPaged =       livePagedListBuilder.
                            setFetchExecutor(executor).
                             setBoundaryCallback(boundaryCallback).
                            build();

    }


    public LiveData<PagedList<Movie>> getPagedMoviesByPaging(){
        return moviesPaged;
    }

    public LiveData<NetworkState> getNetworkState() {
        return networkState;
    }
}
