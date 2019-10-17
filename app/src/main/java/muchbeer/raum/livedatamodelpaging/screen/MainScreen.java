package muchbeer.raum.livedatamodelpaging.screen;

import java.util.List;

import muchbeer.raum.data.model.Movie;

public interface MainScreen {

    void updateData(List<Movie> data);
    void setError(String msg);
}
