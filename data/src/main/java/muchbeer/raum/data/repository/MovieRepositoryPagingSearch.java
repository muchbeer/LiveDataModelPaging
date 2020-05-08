package muchbeer.raum.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PagedList;
import muchbeer.raum.data.db.RoomDb;
import muchbeer.raum.data.model.Movie;

public class MovieRepositoryPagingSearch {
    private static final String TAG = MovieRepositoryPaging.class.getSimpleName();
    private static MovieRepositoryPagingSearch instance;

    final private RoomDb database;

    public MovieRepositoryPagingSearch(Context mContext) {

        database = RoomDb.getDatabase(mContext.getApplicationContext());
     }

    public static MovieRepositoryPagingSearch getInstance(Context context){
        if(instance == null){
            instance = new MovieRepositoryPagingSearch(context);
        }
        return instance;
    }

    public LiveData<PagedList<Movie>> getMoviesPaging(){
        return  database.getSearchedRoom();
    }

    public MutableLiveData<String> getSearchMovies() { return database.getMoviesPagingSearch();}
}
