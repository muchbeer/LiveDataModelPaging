package muchbeer.raum.data.db;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import muchbeer.raum.data.model.Movie;

@Dao
public interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovies(List<Movie> movies);

    @Query("SELECT * FROM movietbl")
    LiveData<List<Movie>> getAllMoviesLive();

    @Query("SELECT * FROM movietbl")
    List<Movie> getAllMovieLocal();

    @Query("SELECT * FROM movietbl LIMIT :limit")
    LiveData<List<Movie>>getMovie(int limit);

    @Query("SELECT * FROM movietbl WHERE title=:title")
    LiveData<Movie>getSearchedMovie(String title);


    // object, with position-based loading under the hood.
    @Query("SELECT * FROM movietbl")
    DataSource.Factory<Integer, Movie> getAllMovieOnPaging();

}
