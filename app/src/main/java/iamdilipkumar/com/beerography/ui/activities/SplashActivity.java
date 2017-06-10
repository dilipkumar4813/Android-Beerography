package iamdilipkumar.com.beerography.ui.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import iamdilipkumar.com.beerography.R;
import iamdilipkumar.com.beerography.utilities.CommonUtils;

/**
 * Activity to display splash screen
 *
 * @author dilipkumar4813
 * @version 1.0
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        loadMainScreen();
    }

    private void loadMainScreen() {
        if (CommonUtils.checkNetworkConnectivity(this)) {
            Intent mainList = new Intent(this, BeerListActivity.class);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    SplashActivity.this.finish();
                    startActivity(mainList);
                }
            }, 1000);
        } else {
            Dialog network = CommonUtils.noNetworkDialog(this);

            Button btnCancel = (Button) network.findViewById(R.id.btn_no);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    network.dismiss();
                    SplashActivity.this.finish();
                }
            });

            Button btnYes = (Button) network.findViewById(R.id.btn_yes);
            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    network.dismiss();
                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                }
            });

            network.show();
        }
    }

    @Override
    protected void onResume() {
        loadMainScreen();
        super.onResume();
    }
}
