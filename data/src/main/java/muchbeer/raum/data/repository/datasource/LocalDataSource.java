package muchbeer.raum.data.repository.datasource;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import muchbeer.raum.data.db.RoomDb;
import muchbeer.raum.data.model.Movie;

public class LocalDataSource   implements DataSource<List<Movie>> {

    private final RoomDb mDb;
    private final MutableLiveData<String> mError=new MutableLiveData<>();

    public LocalDataSource(Context mContextApplication) {
        mDb = RoomDb.getDatabase(mContextApplication);
    }

    @Override
    public LiveData<List<Movie>> getMovieDataStream() {
        return mDb.movieDao().getAllMoviesLive();
    }

    @Override
    public LiveData<String> getErrorStream() {
        return mError;
    }

    public void insertMoviesOnline2Local(List<Movie> movies) {
        try {
            mDb.movieDao().insertMovies(movies);
        }catch(Exception e)
        {
            e.printStackTrace();
            mError.postValue(e.getMessage());
        }
    }


}
