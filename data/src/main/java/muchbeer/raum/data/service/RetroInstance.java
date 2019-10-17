package muchbeer.raum.data.service;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static muchbeer.raum.data.utility.Constants.POPULAR_MOVIES_BASE_URL;

public class RetroInstance {

    private static Retrofit retrofit = null;
    private static String BASE_URL="https://api.themoviedb.org/3/";

    public static MovieDataService getService() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        // create OkHttpClient and register an interceptor
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        if(retrofit==null){

            retrofit=new Retrofit
                    .Builder()
                    .baseUrl(POPULAR_MOVIES_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();

        }

        return retrofit.create(MovieDataService.class);

    }
}