package iamdilipkumar.com.beerography.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import iamdilipkumar.com.beerography.R;
import iamdilipkumar.com.beerography.models.Datum;
import iamdilipkumar.com.beerography.ui.activities.BeerDetailActivity;

/**
 * Created on 04/06/17.
 *
 * @author dilipkumar4813
 * @version 1.0
 */

public class BeerListAdapter extends RecyclerView.Adapter<BeerListAdapter.BeerItemViewHolder> {

    private List<Datum> beerList;
    private Context mContext;
    private BeerClick mBeerClick;

    public BeerListAdapter(Context context, List<Datum> itemList, BeerClick beerClick) {
        this.beerList = itemList;
        this.mContext = context;
        this.mBeerClick = beerClick;
    }

    public interface BeerClick{
        void onBeerItemClicked(int position);
    }

    @Override
    public BeerItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_beer, null);
        return new BeerItemViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(BeerItemViewHolder holder, int position) {

        Datum beerItem = beerList.get(position);

        holder.beerName.setText(beerItem.getName());

        int resource = getDrawableResource(position);

        /*String beerImage = "";
        if (beerItem.getLabels() != null) {
            if (beerItem.getLabels().getMedium() != null) {
                beerImage = beerItem.getLabels().getMedium();
            }

            Picasso.with(mContext)
                    .load(beerImage)
                    .error(resource)
                    .placeholder(resource)
                    .into(holder.beerImage);
            Log.d("image", beerImage);
        } else {*/
        holder.beerImage.setBackgroundResource(resource);
        //}
    }

    @Override
    public int getItemCount() {
        return this.beerList.size();
    }

    class BeerItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        @BindView(R.id.beer_image)
        ImageView beerImage;

        @BindView(R.id.tv_beer_name)
        TextView beerName;

        public BeerItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mBeerClick.onBeerItemClicked(getAdapterPosition());
        }
    }

    private int getDrawableResource(int position) {
        int resource = R.drawable.item_container_3;

        while (position > 10) {
            position = position - 10;
        }

        switch (position) {
            case 0:
                resource = R.drawable.item_container_1;
                break;
            case 1:
                resource = R.drawable.item_container_2;
                break;
            case 2:
                resource = R.drawable.item_container_3;
                break;
            case 3:
                resource = R.drawable.item_container_4;
                break;
            case 4:
                resource = R.drawable.item_container_5;
                break;
            case 5:
                resource = R.drawable.item_container_6;
                break;
            case 6:
                resource = R.drawable.item_container_7;
                break;
            case 7:
                resource = R.drawable.item_container_8;
                break;
            case 8:
                resource = R.drawable.item_container_5;
                break;
        }

        return resource;
    }
}
