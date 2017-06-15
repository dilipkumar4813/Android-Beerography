package iamdilipkumar.com.beerography.utilities;

import iamdilipkumar.com.beerography.models.BeerDetail;
import iamdilipkumar.com.beerography.models.SelectedPage;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created on 04/06/17.
 *
 * @author dilipkumar4813
 * @version 1.5
 */

public interface BeersApiInterface {

    @GET("/v2/beers")
    Observable<SelectedPage> getBeersList(@Query("p") int page);

    @GET("/v2/beer/{id}")
    Observable<BeerDetail> getBeerDetails(@Path("id") String id);

    @GET("/v2/search")
    Observable<SelectedPage> getBeersSearch(@Query("q") String searchString);
}
