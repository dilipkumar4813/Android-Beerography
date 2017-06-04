package iamdilipkumar.com.beerography.adapters;

import android.content.Context;
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

/**
 * Created on 04/06/17.
 *
 * @author dilipkumar4813
 * @version 1.0
 */

public class BeerListAdapter extends RecyclerView.Adapter<BeerListAdapter.BeerItemViewHolder> {

    private List<Datum> beerList;
    private Context mContext;

    public BeerListAdapter(Context context, List<Datum> itemList) {
        this.beerList = itemList;
        this.mContext = context;
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

        String beerImage = "";
        if (beerItem.getLabels() != null) {
            if (beerItem.getLabels().getMedium() != null) {
                beerImage = beerItem.getLabels().getMedium();
            }

            Picasso.with(mContext)
                    .load(beerImage)
                    .error(R.drawable.container_1)
                    .placeholder(R.drawable.container_1)
                    .resize(200, 200)
                    .centerCrop()
                    .into(holder.beerImage);
            Log.d("image", beerImage);
        } else {
            int resource = position % 2 == 0 ? R.drawable.container_1 : R.drawable.container_2;
            holder.beerImage.setImageResource(resource);
        }
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
            Toast.makeText(view.getContext(), "Clicked Position = " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
        }
    }
}
