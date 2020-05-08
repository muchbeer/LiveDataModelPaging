package muchbeer.raum.livedatamodelpaging.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import muchbeer.raum.data.model.Movie;
import muchbeer.raum.livedatamodelpaging.R;
import muchbeer.raum.livedatamodelpaging.databinding.MovieListItemBinding;
import muchbeer.raum.livedatamodelpaging.view.MovieActivity;

public class MovieAdapter extends PagedListAdapter<Movie, MovieAdapter.MovieViewHolder>
            {

    private Context mcontext;
    private static String LOG_TAG = MovieAdapter.class.getSimpleName();
    PagedList<Movie> getMoviesFromActivity;

    //Paging here
    public MovieAdapter(Context context, PagedList<Movie> getMoviesFromActivity) {
        super(Movie.DIFF_CALLBACK);
        this.mcontext = context;
        this.getMoviesFromActivity = getMoviesFromActivity;
            }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MovieListItemBinding movieListItemBinding= DataBindingUtil.inflate(LayoutInflater.from(parent.getContext())
                , R.layout.movie_list_item
                , parent
                ,false);

        return new MovieViewHolder(movieListItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        //Movie movie=movieArrayList.get(position);

        //paging below
      Movie  movie =getItem(position);
       // PagedList<Movie> filtered = returnMoview.add(movie);
        Log.d(LOG_TAG, "The movie items are: " + movie);
        holder.movieListItemBinding.setMovie(movie);
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {

        private MovieListItemBinding movieListItemBinding;
        public MovieViewHolder(@NonNull MovieListItemBinding movieListItemBinding) {
            super(movieListItemBinding.getRoot());
            this.movieListItemBinding = movieListItemBinding;

            movieListItemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION) {
                      //  Movie selctedMovie = movieArrayList.get(position);

                        //paging here
                        Movie selctedMovie = getItem(position);
                        Intent intent=new Intent(mcontext, MovieActivity.class);
                        intent.putExtra("movie",selctedMovie);
                        mcontext.startActivity(intent);
                    }
                }
            });

        }
    }
}