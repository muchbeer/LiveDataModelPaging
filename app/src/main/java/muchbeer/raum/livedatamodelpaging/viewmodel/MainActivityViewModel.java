package muchbeer.raum.livedatamodelpaging.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

import muchbeer.raum.data.model.Movie;
import muchbeer.raum.data.repository.MovieRepositoryPaging;
import muchbeer.raum.livedatamodelpaging.info.SharedPreferenceHelper;

public class MainActivityViewModel extends AndroidViewModel {

    private static final String TAG = MainActivityViewModel.class.getSimpleName();


    private MovieRepositoryPaging mMovieRepositoryInterface;
    private SharedPreferenceHelper prefHelper;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);

        mMovieRepositoryInterface = MovieRepositoryPaging.getInstance(application);
    }

    @Override
    protected void onCleared() {
        Log.d(TAG, "onCleared() called");
        super.onCleared();
    }

    public LiveData<PagedList<Movie>> getMoviesPaging() {

        return mMovieRepositoryInterface.getMoviesPaging();
    }

    public LiveData<String> getErrorUpdates() {
        return mMovieRepositoryInterface.getErrorMessage();
    }


    //To apply the Preference setting here
    private void checkCacheDuration() {
        String cachePreference = prefHelper.getChachefromPreference();

        if(!cachePreference.equals("")) {
            try {
                int cachePreferenceInt = Integer.parseInt(cachePreference);
                //login here since we have chache the time
            }catch (NumberFormatException e){
                e.printStackTrace();
Log.d(TAG, "tHE error inputed from preference is: " + e.getMessage());
            }
        }
    }

}