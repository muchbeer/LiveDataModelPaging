package muchbeer.raum.data.repository.paging;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import muchbeer.raum.data.db.MovieDao;
import muchbeer.raum.data.db.RoomDb;

public class LocalDataSourceFactory extends DataSource.Factory implements muchbeer.raum.data.repository.datasource.DataSource {

    private RoomDb mDb;
    private static final String TAG = LocalDataSourceFactory.class.getSimpleName();
    private LocalDataSourcePageKey moviesPageKeyedDataSource;


    public LocalDataSourceFactory(MovieDao dao) {
        //    mDb = RoomDbPaging.getDatabase(mContextApplication);
        moviesPageKeyedDataSource = new LocalDataSourcePageKey(dao);
    }

    private final MutableLiveData<String> mError=new MutableLiveData<>();
    @NonNull
    @Override
    public DataSource create() {
        return null;
    }

    @Override
    public LiveData getMovieDataStream() {
        return mDb.movieDao().getAllMoviesLive();
    }

    @Override
    public LiveData<String> getErrorStream() {
        return mError;
    }
}
