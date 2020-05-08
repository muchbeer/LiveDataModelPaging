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

    @Query("SELECT * FROM movietbl where title LIKE  :name order by title")
    DataSource.Factory<Integer, Movie> loadAllMovieFromSearch(String name);

    @Query("SELECT * FROM movietbl order by title")
    DataSource.Factory<Integer, Movie> loadAllMovie();

    @Query("SELECT * FROM movietbl")
    List<Movie> getMoviesPaging();

    /**
     * Insert a movie in the database. If the movie already exists, replace it.
     *
     * @param movie the movie to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertMoviePaging(Movie movie);

    @Query("DELETE FROM movietbl")
    abstract void deleteAllMoviesPaging();

}
