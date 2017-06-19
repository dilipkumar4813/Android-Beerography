package iamdilipkumar.com.beerography.ui.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import iamdilipkumar.com.beerography.R;

/**
 * Created on 19/06/17.
 *
 * @author dilipkumar4813
 * @version 1.5.1
 */

public class TerminologiesFragment extends Fragment {

    @BindView(R.id.tv_terminologies)
    TextView terms;

    @BindView(R.id.pb_loading)
    ProgressBar loading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_terminologies, container, false);

        ButterKnife.bind(this, view);

        new LoadTerminologies().execute();

        return view;
    }

    private class LoadTerminologies extends AsyncTask<Void,Void,Void>{

        String builtData = "";

        @Override
        protected Void doInBackground(Void... params) {
            String data = null;
            try {
                InputStream inputStream = getActivity().getResources()
                        .getAssets().open("terminologies.json");

                int size = inputStream.available();
                byte[] buffer = new byte[size];
                inputStream.read(buffer);
                inputStream.close();
                data = new String(buffer, "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }


            if (data != null) {
                try {
                    JSONObject jsonData = new JSONObject(data);
                    JSONArray terminologies = jsonData.getJSONArray("terminologies");

                    for (int i = 0; i < terminologies.length(); i++) {
                        JSONObject term = terminologies.getJSONObject(i);
                        builtData += "<h3>"+term.getString("term") + "</h3>";
                        builtData += term.getString("definition") + "<br/><br/>";
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            loading.setVisibility(View.GONE);
            terms.setText(Html.fromHtml(builtData));
        }
    }
}

