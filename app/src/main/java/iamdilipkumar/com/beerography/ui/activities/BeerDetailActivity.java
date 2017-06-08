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

    CompositeDisposable mCompositeDisposable;
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    @BindView(R.id.tv_beer_title)
    TextView mTitle;

    @BindView(R.id.tv_beer_description)
    TextView mDescription;

    @BindView(R.id.tv_extra_info)
    TextView mExtraInfo;

    @BindView(R.id.loading_layout)
    LinearLayout mLoading;

    @BindView(R.id.iv_beer_poster)
    ImageView mBeerPoster;

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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        BeersApiInterface moviesInterface = NetworkUtils.buildRetrofit().create(BeersApiInterface.class);

        mCompositeDisposable.add(moviesInterface.getBeerDetails("oeGSxs")
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

        Datum data = beerDetail.getData();

        if (data != null) {
            mLoading.setVisibility(View.GONE);

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

            if (description != null) {
                mDescription.setVisibility(View.VISIBLE);
                mDescription.setText(description);
            }

            String extraInfo = CommonUtils.buildExtraInfo(this,data);
            mExtraInfo.setText(extraInfo);

            if (data.getLabels() != null) {
                String mediumUrl = data.getLabels().getMedium();
                if (mediumUrl != null) {
                    Picasso.with(this)
                            .load(mediumUrl)
                            .error(R.drawable.ic_image_black_24dp)
                            .placeholder(R.drawable.ic_image_black_24dp)
                            .into(mBeerPoster);
                }
            }

        }
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
