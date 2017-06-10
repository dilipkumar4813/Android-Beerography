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
    public static final String BEER_STYLE = "beerStyle";
    public static final String BEER_EXTRAS = "beerExtras";
    public static final String BEER_TITLE = "beerTitle";
    public static final String BEER_DESCRIPTION = "beerDescription";
    public static final String BEER_IMAGE_URL = "beerImageUrl";

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

    @BindView(R.id.tv_beer_description_title)
    TextView mBeerDescriptionHeading;

    @BindView(R.id.tv_beer_style_title)
    TextView mBeerStyleHeading;

    @BindView(R.id.loading_layout)
    LinearLayout mLoading;

    @BindView(R.id.iv_beer_poster)
    ImageView mBeerPoster;

    @BindView(R.id.beer_image)
    ImageView mBeerBanner;

    private String mBeerStyle, mBeerExtras, mBeerTitle, mBeerDescription, mBeerImageUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer_detail);

        mCompositeDisposable = new CompositeDisposable();
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        setSupportActionBar(toolbar);
        mCollapsingToolbarLayout.setTitle(getString(R.string.app_name));

        int position = getIntent().getIntExtra(BeerListActivity.GRID_POSITION, 0);
        String beerId = getIntent().getStringExtra(BEER_ID);
        int bannerResource = CommonUtils.getBeerImageDrawable(position);
        mBeerBanner.setImageResource(bannerResource);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            BeersApiInterface moviesInterface = NetworkUtils.buildRetrofit().create(BeersApiInterface.class);

            mCompositeDisposable.add(moviesInterface.getBeerDetails(beerId)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::apiResponse, this::apiError));
        } else {
            mLoading.setVisibility(View.GONE);

            mBeerStyle = savedInstanceState.getString(BEER_STYLE);
            mBeerExtras = savedInstanceState.getString(BEER_EXTRAS);
            mBeerTitle = savedInstanceState.getString(BEER_TITLE);
            mBeerDescription = savedInstanceState.getString(BEER_DESCRIPTION);
            mBeerImageUrl = savedInstanceState.getString(BEER_IMAGE_URL);
            mBeerPoster.setVisibility(View.VISIBLE);

            displayDetails();
        }
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
        mBeerPoster.setVisibility(View.VISIBLE);

        Datum data = beerDetail.getData();

        if (data != null) {

            mBeerTitle = data.getNameDisplay();
            mBeerDescription = data.getDescription();

            if (data.getLabels() != null) {
                mBeerImageUrl = data.getLabels().getMedium();
            }

            mBeerExtras = CommonUtils.getExtraInfo(this, data);

            mBeerStyle = CommonUtils.getStyleInfo(this, data);

            displayDetails();
        }
    }

    private void apiError(Throwable throwable) {
        mBeerPoster.setVisibility(View.VISIBLE);
        mLoading.setVisibility(View.GONE);
        Log.d(TAG, throwable.getLocalizedMessage());
    }

    private void displayDetails() {

        if (mBeerTitle != null) {
            mTitle.setVisibility(View.VISIBLE);
            mTitle.setText(mBeerTitle);
        }

        if (mBeerImageUrl != null) {
            Picasso.with(this)
                    .load(mBeerImageUrl)
                    .error(R.drawable.no_image)
                    .placeholder(R.drawable.no_image)
                    .into(mBeerPoster);
        }

        if (mBeerDescription != null) {
            if (!mBeerDescription.isEmpty()) {
                mBeerDescriptionHeading.setVisibility(View.VISIBLE);
                mDescription.setVisibility(View.VISIBLE);
                mDescription.setText(mBeerDescription);
            }
        }

        if (!mBeerExtras.isEmpty()) {
            mExtraInfo.setText(mBeerExtras);
        } else {
            mExtraInfo.setText(getString(R.string.extras_empty));
        }

        if (!mBeerStyle.isEmpty()) {
            mBeerStyleHeading.setVisibility(View.VISIBLE);
            mStyleInfo.setVisibility(View.VISIBLE);
            mStyleInfo.setText(mBeerStyle);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(BEER_STYLE, mBeerStyle);
        outState.putString(BEER_EXTRAS, mBeerExtras);
        outState.putString(BEER_TITLE, mBeerTitle);
        outState.putString(BEER_DESCRIPTION, mBeerDescription);
        outState.putString(BEER_IMAGE_URL, mBeerImageUrl);
        super.onSaveInstanceState(outState);
    }
}
