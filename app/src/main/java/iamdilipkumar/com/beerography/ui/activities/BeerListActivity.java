package iamdilipkumar.com.beerography.ui.activities;

import android.app.ActivityOptions;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import iamdilipkumar.com.beerography.R;
import iamdilipkumar.com.beerography.adapters.BeerListAdapter;
import iamdilipkumar.com.beerography.models.Datum;
import iamdilipkumar.com.beerography.models.SelectedPage;
import iamdilipkumar.com.beerography.ui.fragments.AboutFragment;
import iamdilipkumar.com.beerography.utilities.BeersApiInterface;
import iamdilipkumar.com.beerography.utilities.CommonUtils;
import iamdilipkumar.com.beerography.utilities.NetworkUtils;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class BeerListActivity extends AppCompatActivity implements BeerListAdapter.BeerClick, SearchView.OnQueryTextListener, NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = BeerListActivity.class.getSimpleName();

    public static final String GRID_POSITION = "gridPosition";

    @BindView(R.id.beer_list_recycler)
    RecyclerView mBeerList;

    @BindView(R.id.loading_layout)
    LinearLayout loadingLayout;

    @BindView(R.id.progress_load_more)
    ProgressBar loadMore;

    @BindView(R.id.tv_error_message)
    TextView mErrorMessage;

    @BindView(R.id.pb_loading)
    ProgressBar mLoading;

    private static final String BEER_LIST = "beerList";
    private static final String BEER_COUNT = "beerCount";

    CompositeDisposable mCompositeDisposable;
    private List<Datum> mList = new ArrayList<>();
    private List<Datum> mMainList = new ArrayList<>();
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, actionBarToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        SearchManager searchManager = (SearchManager)
                getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchMenuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();

        searchView.setSearchableInfo(searchManager.
                getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_main_list:
                removeFragment();

                if (mMainList.size() > 0) {
                    mList.clear();
                    mList.addAll(mMainList);
                    mBeerList.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }
                break;
        }
        return true;
    }

    private void loadBeers() {
        if (CommonUtils.checkNetworkConnectivity(this)) {
            BeersApiInterface moviesInterface = NetworkUtils.buildRetrofit().create(BeersApiInterface.class);

            mCompositeDisposable.add(moviesInterface.getBeersList(mPageCount)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::apiResponse, this::apiError));
        } else {
            CommonUtils.noNetworkPreActionDialog(this);
        }
    }

    private void apiResponse(SelectedPage selectedPage) {
        loadingLayout.setVisibility(View.GONE);

        if (loadMore.getVisibility() == View.VISIBLE) {
            mBeerList.scrollToPosition(mScrollToPosition);
        }
        loadMore.setVisibility(View.GONE);

        if (selectedPage.getData() != null) {
            mList.addAll(selectedPage.getData());
            mBeerList.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        } else {
            loadingLayout.setVisibility(View.VISIBLE);
            mErrorMessage.setText(getString(R.string.no_beers));
            mLoading.setVisibility(View.GONE);
        }

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
        if (CommonUtils.checkNetworkConnectivity(this)) {
            Intent detailsIntent = new Intent(BeerListActivity.this, BeerDetailActivity.class);
            detailsIntent.putExtra(GRID_POSITION, position);
            detailsIntent.putExtra(BeerDetailActivity.BEER_ID, mList.get(position).getId());
            Bundle bundle = new Bundle();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                if (!getResources().getBoolean(R.bool.isTablet)) {
                    bundle = ActivityOptions
                            .makeSceneTransitionAnimation(this,
                                    transitionImage,
                                    transitionImage.getTransitionName())
                            .toBundle();
                }
            }
            startActivity(detailsIntent, bundle);
        } else {
            CommonUtils.noNetworkPreActionDialog(this);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(BEER_LIST, new ArrayList<>(mList));
        outState.putInt(BEER_COUNT, mPageCount);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        loadingLayout.setVisibility(View.VISIBLE);

        mMainList.addAll(mList);

        mList.clear();
        mBeerList.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        try {
            query = URLEncoder.encode(query, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (CommonUtils.checkNetworkConnectivity(this)) {
            BeersApiInterface moviesInterface = NetworkUtils
                    .buildRetrofit()
                    .create(BeersApiInterface.class);

            mCompositeDisposable.add(moviesInterface.getBeersSearch(query)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::apiResponse, this::apiError));
        } else {
            CommonUtils.noNetworkPreActionDialog(this);
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_home:
                removeFragment();
                break;
            case R.id.nav_share:
                CommonUtils.shareData(this);
                break;
            case R.id.nav_about:
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.main_container_layout, new AboutFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case R.id.nav_feedback:
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType(getString(R.string.share_type));
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                        new String[]{"dilipkumar4813@gmail.com"});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Beerography feedback");

                try {
                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Log.e(TAG, ex.getLocalizedMessage());
                }
                break;
            case R.id.nav_exit:
                CommonUtils.exitAppDialog(this).show();
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void removeFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_container_layout);
        if (fragment != null) {
            fragmentTransaction.remove(fragment);
            fragmentTransaction.commit();
        }
    }
}
