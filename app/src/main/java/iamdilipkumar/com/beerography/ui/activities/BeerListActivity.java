package iamdilipkumar.com.beerography.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import iamdilipkumar.com.beerography.R;
import iamdilipkumar.com.beerography.adapters.BeerListAdapter;
import iamdilipkumar.com.beerography.models.Datum;
import iamdilipkumar.com.beerography.models.Labels;

public class BeerListActivity extends AppCompatActivity {

    @BindView(R.id.beer_list_recycler)
    RecyclerView mBeerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer_list);

        ButterKnife.bind(this);

        mBeerList.setHasFixedSize(true);

        StaggeredGridLayoutManager gaggeredGridLayoutManager = new StaggeredGridLayoutManager(3, 1);
        mBeerList.setLayoutManager(gaggeredGridLayoutManager);

        List<Datum> gaggeredList = getListItemData();

        BeerListAdapter rcAdapter = new BeerListAdapter(BeerListActivity.this, gaggeredList);
        mBeerList.setAdapter(rcAdapter);
    }

    private List<Datum> getListItemData() {
        List<Datum> items = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            Datum item = new Datum();
            item.setName("Beer " + i);
            Labels label = new Labels();
            label.setMedium("https://s3.amazonaws.com/brewerydbapi/beer/tmEthz/upload_3Jl1St-medium.png");
            item.setLabels(label);
            items.add(item);
        }

        return items;
    }
}
