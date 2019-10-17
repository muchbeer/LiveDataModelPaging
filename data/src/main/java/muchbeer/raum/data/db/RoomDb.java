package muchbeer.raum.data.db;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import muchbeer.raum.data.model.Movie;

@Database(entities = {Movie.class},version = 1, exportSchema = false)
public abstract class RoomDb extends RoomDatabase {

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

        }
        return INSTANCE;
    }

    private void init() {
      /*  PagedList.Config pagedListConfig = (new PagedList.Config.Builder()).setEnablePlaceholders(false)
                .setInitialLoadSizeHint(Integer.MAX_VALUE).setPageSize(Integer.MAX_VALUE).build();
        Executor executor = Executors.newFixedThreadPool(NUMBERS_OF_THREADS);
        LocalDataSource dataSourceFactory = new LocalDataSource(movieDao(), context);
        LivePagedListBuilder livePagedListBuilder = new LivePagedListBuilder(dataSourceFactory, pagedListConfig);
        moviesPaged = livePagedListBuilder.setFetchExecutor(executor).build();*/
    }

    /*public LiveData<PagedList<Movie>> getMovies() {
        return moviesPaged;
    }*/
    //addCallBack used to add dummy data
    //addMigrations is when you add other field and want to change the version
}
