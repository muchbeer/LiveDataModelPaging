package muchbeer.raum.data.repository.paging;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.subjects.ReplaySubject;
import muchbeer.raum.data.model.Movie;
import muchbeer.raum.data.model.MovieDbResponse;
import muchbeer.raum.data.model.NetworkState;
import muchbeer.raum.data.service.MovieDataService;
import muchbeer.raum.data.service.RetroInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static muchbeer.raum.data.utility.Constants.API_KEY;
import static muchbeer.raum.data.utility.Constants.LANGUAGE;

public class RemoteDataSourcePageKey extends PageKeyedDataSource<Long, Movie> {


    private static final String LOG_TAG = RemoteDataSourcePageKey.class.getSimpleName();;
    private final MovieDataService moviesService;
    private final MutableLiveData networkState;
    private  MutableLiveData<String> mError=new MutableLiveData<>();
    private final ReplaySubject<Movie> moviesObservable;

    RemoteDataSourcePageKey() {
        moviesService = RetroInstance.getService();
        networkState = new MutableLiveData();
        mError = new MutableLiveData<>();
        moviesObservable = ReplaySubject.create();
    }
    public MutableLiveData getNetworkState() {
        return networkState;
    }

    public ReplaySubject<Movie> getMoviesReplay() {
        return moviesObservable;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Long> params, @NonNull final LoadInitialCallback<Long, Movie> callback) {
        //this is paging
        Call<MovieDbResponse> call =  moviesService.getPopularMoviesWithPaging(API_KEY,
                LANGUAGE,
                1);
        //this below is without paging
        // Call<MovieDbResponse> call = movieDataService.getPopularMovies(application.getApplicationContext().getString(R.string.apiKey));
        call.enqueue(new Callback<MovieDbResponse>() {
            @Override
            public void onResponse(Call<MovieDbResponse> call, Response<MovieDbResponse> response) {
                MovieDbResponse movieDbResponse = response.body();
                if (movieDbResponse != null && movieDbResponse.getMovies() != null) {

                   // moview = (ArrayList<Movie>) movieDbResponse.getMovies();
                    Log.d(LOG_TAG, "All the movies listed as : "+ movieDbResponse.getMovies());

                   // mDataApi.setValue(moview);

                    callback.onResult(movieDbResponse.getMovies(), (long)1, (long)2);
                    networkState.postValue(NetworkState.LOADED);
                    movieDbResponse.getMovies().forEach(moviesObservable::onNext);
                } else {
                    Log.e("API CALL FAILURE: ", response.toString());
                    networkState.postValue(new NetworkState(NetworkState.Status.FAILED, response.toString()));
                    mError.postValue(response.toString());

                }
            }

            @Override
            public void onFailure(Call<MovieDbResponse> call, Throwable response) {
                String errorMessage;
                if (response.getMessage() == null) {
                    errorMessage = "unknown error";
                    mError.postValue(errorMessage);
                    networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorMessage));
                    Log.d(LOG_TAG, "The error catched is as follows : "+ errorMessage);


                } else {
                    errorMessage = response.getMessage();
                    mError.postValue(errorMessage);
                    Log.d(LOG_TAG, "The error catched is as follows : "+ errorMessage);
                    networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorMessage));

                }



                callback.onResult(new ArrayList<>(), (long) 1, (long) 1);
            }
        });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, Movie> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, Movie> callback) {

        Log.i(LOG_TAG, "Loading page " + params.key );
        networkState.postValue(NetworkState.LOADING);
        final AtomicInteger page = new AtomicInteger(0);
        try {
            page.set(Integer.parseInt(String.valueOf(params.key)));
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
        //this is paging
        Call<MovieDbResponse> call =  moviesService.getPopularMoviesWithPaging(API_KEY,
                LANGUAGE,
                1);
        //this below is without paging
        // Call<MovieDbResponse> call = movieDataService.getPopularMovies(application.getApplicationContext().getString(R.string.apiKey));
        call.enqueue(new Callback<MovieDbResponse>() {
            @Override
            public void onResponse(Call<MovieDbResponse> call, Response<MovieDbResponse> response) {
                MovieDbResponse movieDbResponse = response.body();
                if (movieDbResponse != null && movieDbResponse.getMovies() != null) {

                    // moview = (ArrayList<Movie>) movieDbResponse.getMovies();
                    Log.d(LOG_TAG, "All the movies listed as : "+ movieDbResponse.getMovies());

                    // mDataApi.setValue(moview);

                    callback.onResult(movieDbResponse.getMovies(), params.key+1);
                    networkState.postValue(NetworkState.LOADED);
                    movieDbResponse.getMovies().forEach(moviesObservable::onNext);
                } else {
                    Log.e("API CALL FAILURE: ", response.toString());
                    networkState.postValue(new NetworkState(NetworkState.Status.FAILED, response.toString()));
                    mError.postValue(response.toString());
                }
            }

            @Override
            public void onFailure(Call<MovieDbResponse> call, Throwable response) {
                String errorMessage;
                if (response.getMessage() == null) {
                    errorMessage = "unknown error";
                     mError.postValue(errorMessage);
                    Log.d(LOG_TAG, "The error catched is as follows : "+ errorMessage);
                    networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorMessage));
                } else {
                    errorMessage = response.getMessage();
                    mError.postValue(errorMessage);
                    Log.d(LOG_TAG, "The error catched is as follows : "+ errorMessage);
                    networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorMessage));
                }

                callback.onResult(new ArrayList<>(), (long)(page.get()));
              //  mError.postValue(response.toString());

            }
        });
    }

    public MutableLiveData<String> getErrorStream() {
        return mError;
    }

}
