package iamdilipkumar.com.beerography.ui.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import iamdilipkumar.com.beerography.R;
import iamdilipkumar.com.beerography.adapters.BeerListAdapter;
import iamdilipkumar.com.beerography.models.Datum;
import iamdilipkumar.com.beerography.models.SelectedPage;
import iamdilipkumar.com.beerography.utilities.BeersApiInterface;
import iamdilipkumar.com.beerography.utilities.NetworkUtils;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class BeerListActivity extends AppCompatActivity implements BeerListAdapter.BeerClick {

    private static final String TAG = BeerListActivity.class.getSimpleName();

    public static final String GRID_POSITION = "gridPosition";

    @BindView(R.id.beer_list_recycler)
    RecyclerView mBeerList;

    CompositeDisposable mCompositeDisposable;
    private List<Datum> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer_list);

        Toolbar actionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        actionBarToolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(actionBarToolbar);

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

        GridLayoutManager gaggeredGridLayoutManager = new GridLayoutManager(this, 2);
        mBeerList.setLayoutManager(gaggeredGridLayoutManager);

        mList = selectedPage.getData();
        BeerListAdapter rcAdapter = new BeerListAdapter(BeerListActivity.this, mList, this);
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

    @Override
    public void onBeerItemClicked(int position, ImageView transitionImage) {
        Intent detailsIntent = new Intent(BeerListActivity.this, BeerDetailActivity.class);
        detailsIntent.putExtra(GRID_POSITION, position);
        detailsIntent.putExtra(BeerDetailActivity.BEER_ID, mList.get(position).getId());
        Bundle bundle = new Bundle();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            bundle = ActivityOptions
                    .makeSceneTransitionAnimation(this,
                            transitionImage,
                            transitionImage.getTransitionName())
                    .toBundle();
        }
        startActivity(detailsIntent, bundle);
    }
}
