package iamdilipkumar.com.beerography.utilities;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import iamdilipkumar.com.beerography.BuildConfig;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created on 04/06/17.
 *
 * @author dilipkumar4813
 * @version 1.0
 */

public class NetworkUtils {

    private final static String BEERS_BASE_URL = "http://api.brewerydb.com";

    final static String BEERS_PAGE = "page";

    /**
     * Method to create a new Retrofit instance
     * Add the API key {@link BeersApiInterface#getBeers(String)}
     * Show debug information only in DEBUG build
     *
     * @return Retrofit - used for building API calls
     */
    public static Retrofit buildRetrofit() {
        OkHttpClient.Builder httpClient =
                new OkHttpClient.Builder();
        httpClient.addInterceptor(new BeersApiInterceptor());

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(loggingInterceptor);
        }

        return new Retrofit.Builder()
                .baseUrl(BEERS_BASE_URL)
                .client(httpClient.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
