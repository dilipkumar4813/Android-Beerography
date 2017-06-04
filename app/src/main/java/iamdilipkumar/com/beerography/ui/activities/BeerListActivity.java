package iamdilipkumar.com.beerography.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

import butterknife.BindView;
import butterknife.ButterKnife;
import iamdilipkumar.com.beerography.R;
import iamdilipkumar.com.beerography.adapters.BeerListAdapter;
import iamdilipkumar.com.beerography.models.SelectedPage;
import iamdilipkumar.com.beerography.utilities.BeersApiInterface;
import iamdilipkumar.com.beerography.utilities.NetworkUtils;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class BeerListActivity extends AppCompatActivity {

    private static final String TAG = BeerListActivity.class.getSimpleName();

    @BindView(R.id.beer_list_recycler)
    RecyclerView mBeerList;

    CompositeDisposable mCompositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer_list);

        mCompositeDisposable = new CompositeDisposable();
        ButterKnife.bind(this);

        BeersApiInterface moviesInterface = NetworkUtils.buildRetrofit().create(BeersApiInterface.class);

        mCompositeDisposable.add(moviesInterface.getBeersList(1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::apiResponse, this::apiError));
    }

    private void apiResponse(SelectedPage selectedPage) {
        mBeerList.setHasFixedSize(true);

        GridLayoutManager gaggeredGridLayoutManager = new GridLayoutManager(this,2);
        mBeerList.setLayoutManager(gaggeredGridLayoutManager);

        BeerListAdapter rcAdapter = new BeerListAdapter(BeerListActivity.this, selectedPage.getData());
        mBeerList.setAdapter(rcAdapter);
    }

    private void apiError(Throwable throwable) {
        Log.d(TAG, throwable.getLocalizedMessage());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
    }
}
