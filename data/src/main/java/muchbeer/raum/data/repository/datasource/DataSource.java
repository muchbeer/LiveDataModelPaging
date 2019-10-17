package muchbeer.raum.data.repository.datasource;

import androidx.lifecycle.LiveData;

public interface DataSource<T> {

    LiveData<T> getMovieDataStream();
    LiveData<String> getErrorStream();
}
