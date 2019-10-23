package muchbeer.raum.livedatamodelpaging.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
    private static final int PERMISSION_SEND_SMS = 2016;
    private Movie movie;
    private ActivityMovieBinding activityMovieBinding;
    private boolean sendSmsStarted;


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_id_send: {
                Toast.makeText(getApplication(), "Send message", Toast.LENGTH_LONG).show();

                if(!sendSmsStarted) {
                    sendSmsStarted = true;
                    checkSmsPermission();
                }
                break;
            }case R.id.menu_id_share: {
                Toast.makeText(getApplication(), "Share message", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Check out this dog breed");
                intent.putExtra(Intent.EXTRA_STREAM, movie.getTitle());
            //    intent.putExtra(Intent.EXTRA_STREAM, imageUrl);
                startActivity(Intent.createChooser(intent, "Share with"));
                break;
            }

        }
        return super.onOptionsItemSelected(item);
    }


        private void checkSmsPermission() {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)
                     if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {
                         new AlertDialog.Builder(this)
                        .setTitle("Send SMS Permission")
                        .setMessage("This app require perm")
                        .setPositiveButton("Ask me", (dialog, which) -> {
                            requestSmsPermission();
                        })
                        .setNegativeButton("No", ((dialog, which) -> {
                            notifyDetailActivity(false);
                        })).show();
            }else {
                requestSmsPermission();
            }
            else {
                    notifyDetailActivity(true);
             }
            }

    private void requestSmsPermission() {

        String[] permission = {Manifest.permission.SEND_SMS};
        ActivityCompat.requestPermissions(this, permission, PERMISSION_SEND_SMS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
      //  super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode) {
            case PERMISSION_SEND_SMS: {
                if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)  {
                    notifyDetailActivity(true);
                }else {
                    notifyDetailActivity(false);
                }
                break;
            }

        }
    }

    private void notifyDetailActivity(Boolean permissionGranted) {
        sendSmsStarted = permissionGranted;
        if(sendSmsStarted && permissionGranted) {
            Toast.makeText(getApplication(), "I will send later the news",Toast.LENGTH_LONG).show();
        }
    }



}
