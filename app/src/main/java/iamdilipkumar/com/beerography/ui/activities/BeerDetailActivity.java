package iamdilipkumar.com.beerography.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import iamdilipkumar.com.beerography.R;
import iamdilipkumar.com.beerography.models.BeerDetail;
import iamdilipkumar.com.beerography.models.Datum;
import iamdilipkumar.com.beerography.utilities.BeersApiInterface;
import iamdilipkumar.com.beerography.utilities.CommonUtils;
import iamdilipkumar.com.beerography.utilities.NetworkUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created on 04/06/17.
 *
 * @author dilipkumar4813
 * @version 1.0
 */

public class BeerDetailActivity extends AppCompatActivity {

    private static final String TAG = BeerDetailActivity.class.getSimpleName();

    public static final String BEER_ID = "beerid";
    private String mBeerId;

    CompositeDisposable mCompositeDisposable;
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    @BindView(R.id.tv_beer_title)
    TextView mTitle;

    @BindView(R.id.tv_beer_description)
    TextView mDescription;

    @BindView(R.id.tv_extra_info)
    TextView mExtraInfo;

    @BindView(R.id.tv_beer_style)
    TextView mStyleInfo;

    @BindView(R.id.loading_layout)
    LinearLayout mLoading;

    @BindView(R.id.iv_beer_poster)
    ImageView mBeerPoster;

    @BindView(R.id.beer_image)
    ImageView mBeerBanner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer_detail);

        mCompositeDisposable = new CompositeDisposable();
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        setSupportActionBar(toolbar);
        mCollapsingToolbarLayout.setTitle("Beer Details");

        int position = getIntent().getIntExtra(BeerListActivity.GRID_POSITION, 0);
        mBeerId = getIntent().getStringExtra(BEER_ID); //"oeGSxs"
        int bannerResource = CommonUtils.getBeerImageDrawable(position);
        mBeerBanner.setImageResource(bannerResource);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        BeersApiInterface moviesInterface = NetworkUtils.buildRetrofit().create(BeersApiInterface.class);

        mCompositeDisposable.add(moviesInterface.getBeerDetails(mBeerId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::apiResponse, this::apiError));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    private void apiResponse(BeerDetail beerDetail) {
        mLoading.setVisibility(View.GONE);

        Datum data = beerDetail.getData();

        if (data != null) {

            String name = data.getName();
            String displayName = data.getNameDisplay();
            String description = data.getDescription();

            if (name != null) {
                mCollapsingToolbarLayout.setTitle(name);
            }

            if (displayName != null) {
                mTitle.setVisibility(View.VISIBLE);
                mTitle.setText(displayName);
            }

            if (data.getLabels() != null) {
                String mediumUrl = data.getLabels().getMedium();
                if (mediumUrl != null) {
                    Picasso.with(this)
                            .load(mediumUrl)
                            .error(R.drawable.no_image)
                            .placeholder(R.drawable.no_image)
                            .into(mBeerPoster);
                }
            }

            String extraInfo = CommonUtils.getExtraInfo(this, data);
            if (!extraInfo.isEmpty()) {
                mExtraInfo.setText(extraInfo);
            } else {
                mExtraInfo.setText(getString(R.string.extras_empty));
            }

            if (description != null) {
                if (!description.isEmpty()) {
                    mDescription.setVisibility(View.VISIBLE);
                    mDescription.setText(description);
                }
            }

            String styleInfo = CommonUtils.getStyleInfo(this, data);
            if (!styleInfo.isEmpty()) {
                mStyleInfo.setVisibility(View.VISIBLE);
                mStyleInfo.setText(styleInfo);
            }

        }
    }

    private void apiError(Throwable throwable) {
        mLoading.setVisibility(View.GONE);
        Log.d(TAG, throwable.getLocalizedMessage());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
    }
}
