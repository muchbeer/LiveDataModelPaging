package muchbeer.raum.data.repository.paging;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import muchbeer.raum.data.db.MovieDao;
import muchbeer.raum.data.db.RoomDb;

public class LocalDataSourceFactory extends DataSource.Factory  {

    private RoomDb mDb;
    private static final String TAG = LocalDataSourceFactory.class.getSimpleName();
    private LocalDataSourcePageKey moviesPageKeyedDataSource;


    public LocalDataSourceFactory(MovieDao dao) {

        moviesPageKeyedDataSource = new LocalDataSourcePageKey(dao);
    }

    @NonNull
    @Override
    public DataSource create() {
        return moviesPageKeyedDataSource;
    }


}
