package iamdilipkumar.com.beerography.utilities;


import java.io.IOException;

import iamdilipkumar.com.beerography.BuildConfig;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Class to attach API key
 * For making API calls to Brewery
 * <p>
 * Created on 04/06/17.
 *
 * @author dilipkumar4813
 * @version 1.0
 */

class BeersApiInterceptor implements Interceptor {

    private final static String PARAM_API = "key";

    private final static String API_KEY = BuildConfig.API;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        HttpUrl originalHttpUrl = original.url();

        HttpUrl url = originalHttpUrl.newBuilder()
                .addQueryParameter(PARAM_API, API_KEY)
                .build();

        Request.Builder requestBuilder = original.newBuilder()
                .url(url);

        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}

