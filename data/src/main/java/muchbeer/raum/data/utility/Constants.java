package muchbeer.raum.data.utility;

import java.lang.reflect.Type;
import java.util.ArrayList;

import muchbeer.raum.data.model.Movie;

public class Constants {

    // network
    public static final String MOVIES_ARRAY_DATA_TAG = "results";
    public static final Type MOVIE_ARRAY_LIST_CLASS_TYPE = (new ArrayList<Movie>()).getClass();
    public static final String POPULAR_MOVIES_BASE_URL = "https://api.themoviedb.org/3/";
    private static final String IMAGE_URL_PREFIX = "https://image.tmdb.org/t/p/";
    public static final String SMALL_IMAGE_URL_PREFIX = IMAGE_URL_PREFIX + "w300";
    public static final String BIG_IMAGE_URL_PREFIX = IMAGE_URL_PREFIX + "w500";
    public static final String API_KEY_REQUEST_PARAM = "api_key";
    public static final String LANGUAGE_REQUEST_PARAM = "language";
    public static final String PAGE_REQUEST_PARAM = "page";
    public static final String API_KEY = "e2e2cae1c5b9256b4dc97f8c430165dc";
    public static final String LANGUAGE = "en";
    public static final int LOADING_PAGE_SIZE = 20;
    // DB
    public static final String DATA_BASE_NAME = "TMBb.db";
    public static final int NUMBERS_OF_THREADS = 3;
}
