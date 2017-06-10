package iamdilipkumar.com.beerography.ui.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.util.ArrayList;
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

    @BindView(R.id.loading_layout)
    LinearLayout loadingLayout;

    @BindView(R.id.progress_load_more)
    ProgressBar loadMore;

    private static final String BEER_LIST = "beerList";
    private static final String BEER_COUNT = "beerCount";

    CompositeDisposable mCompositeDisposable;
    private List<Datum> mList = new ArrayList<>();
    private GridLayoutManager mGridLayoutManager;
    private int mPageCount = 1;
    private boolean loading = true;
    private BeerListAdapter mAdapter;
    private int mScrollToPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer_list);

        Toolbar actionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        actionBarToolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(actionBarToolbar);

        mCompositeDisposable = new CompositeDisposable();
        ButterKnife.bind(this);

        mBeerList.setHasFixedSize(true);
        mGridLayoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.grid_columns));
        mBeerList.setLayoutManager(mGridLayoutManager);
        mAdapter = new BeerListAdapter(BeerListActivity.this, mList, this);

        if (savedInstanceState == null) {
            loadBeers();
        } else {
            loadingLayout.setVisibility(View.GONE);
            mList = savedInstanceState.getParcelableArrayList(BEER_LIST);
            mAdapter = new BeerListAdapter(BeerListActivity.this, mList, this);
            mPageCount = savedInstanceState.getInt(BEER_COUNT);
            mBeerList.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }

        mBeerList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    int visibleItemCount = mGridLayoutManager.getChildCount();
                    int pastVisiblesItems = mGridLayoutManager.findFirstVisibleItemPosition();
                    mScrollToPosition = visibleItemCount + pastVisiblesItems;

                    int totalItemCount = mGridLayoutManager.getItemCount();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loadMore.setVisibility(View.VISIBLE);
                            loading = false;
                            mPageCount++;
                            loadBeers();
                        }
                    }
                }
            }
        });
    }

    private void loadBeers() {
        BeersApiInterface moviesInterface = NetworkUtils.buildRetrofit().create(BeersApiInterface.class);

        mCompositeDisposable.add(moviesInterface.getBeersList(mPageCount)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::apiResponse, this::apiError));
    }

    private void apiResponse(SelectedPage selectedPage) {
        loadingLayout.setVisibility(View.GONE);

        if (loadMore.getVisibility() == View.VISIBLE) {
            mBeerList.scrollToPosition(mScrollToPosition);
            //mBeerList.smoothScrollToPosition(mScrollToPosition);
        }
        loadMore.setVisibility(View.GONE);

        mList.addAll(selectedPage.getData());
        mBeerList.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        loading = true;
    }

    private void apiError(Throwable throwable) {
        loadingLayout.setVisibility(View.GONE);
        loadMore.setVisibility(View.GONE);

        Log.d(TAG, throwable.getLocalizedMessage());
        loading = true;
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(BEER_LIST, new ArrayList<>(mList));
        outState.putInt(BEER_COUNT, mPageCount);
        super.onSaveInstanceState(outState);
    }
}
