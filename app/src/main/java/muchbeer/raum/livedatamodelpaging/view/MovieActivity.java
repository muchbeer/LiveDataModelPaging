package muchbeer.raum.livedatamodelpaging.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import muchbeer.raum.data.model.Movie;
import muchbeer.raum.data.model.MoviePalette;
import muchbeer.raum.livedatamodelpaging.R;
import muchbeer.raum.livedatamodelpaging.databinding.ActivityMovieBinding;

public class MovieActivity  extends AppCompatActivity {

    private static final String LOG_TAG = MovieActivity.class.getSimpleName() ;
    private Movie movie;
    private ActivityMovieBinding activityMovieBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        activityMovieBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie);

        Intent intent = getIntent();

        if (intent.hasExtra("movie")) {

            movie = getIntent().getParcelableExtra("movie");
            activityMovieBinding.setMovie(movie);
            Log.i(LOG_TAG, "The reall data not shown are: " + movie);
            getSupportActionBar().setTitle(movie.getTitle());
            //  collapsingToolbarLayout.setTitle(movie.getTitle());

            if(movie.getPosterPath() != null) {

                String imagePath="https://image.tmdb.org/t/p/w500"+ movie.getPosterPath();
                setupBackgroundColor(imagePath);
            }
        }

    }

    private void setupBackgroundColor(String url) {


        Glide.with(this)
                .asBitmap()
                .load(url)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Palette.from(resource)
                                .generate(palette -> {
                                    int intColor = palette.getLightMutedSwatch().getRgb();
                                    MoviePalette myPalette = new MoviePalette(intColor);
                                    activityMovieBinding.setMoviePalette(myPalette);
                                });
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });


    }
}
