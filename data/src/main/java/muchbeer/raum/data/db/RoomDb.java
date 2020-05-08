package muchbeer.raum.data.db;


import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import muchbeer.raum.data.model.Movie;
import muchbeer.raum.data.repository.paging.LocalDataSourceFactory;

@Database(entities = {Movie.class},version = 1, exportSchema = false)
public abstract class RoomDb extends RoomDatabase {

    private static final String LOG_TAG = RoomDb.class.getSimpleName();
    //paging declaration
    private static final Object sLock = new Object();
    private LiveData<PagedList<Movie>> moviesPaged;
    public LiveData<PagedList<Movie>> listAllMovies;
    private LiveData<PagedList<Movie>> getListAllMoviesInDb;
    public MutableLiveData<String> filterMovieName = new MutableLiveData<>();

    static final String DATABASE_NAME = "movie_db";
    private static final int NUMBERS_OF_THREADS = 4;
    private static RoomDb INSTANCE;

    public abstract MovieDao movieDao();


    private static final Migration MIGRATION_1_TO_2 = new Migration(1,2) {

        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE tablename "
                    +  " ADD COLUMN description TEXT");
        }
    };

    public static synchronized RoomDb getDatabase(Context context) {

        if (INSTANCE == null) {
            INSTANCE= Room.databaseBuilder(context.getApplicationContext(),
                    RoomDb.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    //  .addMigrations(MIGRATION_1_TO_2)
                    //  .addCallback(callback)
                    .build();
            INSTANCE.init();
            INSTANCE.initSearchMovie();;
        }
        return INSTANCE;
    }

    private void init() {
        PagedList.Config pagedListConfig = (new PagedList.Config.Builder())
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(Integer.MAX_VALUE)
                .setPageSize(Integer.MAX_VALUE).build();

        Executor executor = Executors.newFixedThreadPool(NUMBERS_OF_THREADS);

        LocalDataSourceFactory dataSourceFactory = new LocalDataSourceFactory(movieDao());
        LivePagedListBuilder livePagedListBuilder = new LivePagedListBuilder(dataSourceFactory, pagedListConfig);
        moviesPaged = livePagedListBuilder.setFetchExecutor(executor).build();

    }

    private void initSearchMovie() {
        PagedList.Config pagedListConfigSearch = (new PagedList.Config.Builder())
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(Integer.MAX_VALUE)
                .setPageSize(Integer.MAX_VALUE).build();
        listAllMovies = Transformations.switchMap(new DebouncedLiveData<>(filterMovieName, 400), input -> {
            if (input == null || input.equals("") || input.equals("%%")) {
                //check if the current value is empty load all data else search
                synchronized (this) {
                    //check data is loaded before or not
                    if (getListAllMoviesInDb == null)
                        getListAllMoviesInDb = new LivePagedListBuilder<>(
                                movieDao().loadAllMovie(), pagedListConfigSearch)
                                .build();
                }
                return getListAllMoviesInDb;
            } else {
                return new LivePagedListBuilder<>(
                        movieDao().loadAllMovieFromSearch("%" + input + "%"), pagedListConfigSearch)
                        .build();
            }
        });

    }

    public LiveData<PagedList<Movie>> getMoviesPagingLocal() {
        return moviesPaged;
    }

    public LiveData<PagedList<Movie>> getSearchedRoom() { return listAllMovies;  }

    public MutableLiveData<String> getMoviesPagingSearch() {
        return filterMovieName;
    }

}
