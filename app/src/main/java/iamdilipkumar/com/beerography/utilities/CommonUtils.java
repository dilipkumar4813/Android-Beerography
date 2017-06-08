package iamdilipkumar.com.beerography.utilities;

import android.content.Context;

import iamdilipkumar.com.beerography.R;
import iamdilipkumar.com.beerography.models.Datum;

/**
 * Created on 08/06/17.
 *
 * @author dilipkumar4813
 * @version 1.0
 */

public class CommonUtils {

    public static String buildExtraInfo(Context context, Datum data) {
        String extraInfo = "";

        if (data.getAbv() != null) {
            if (!data.getAbv().isEmpty()) {
                extraInfo += context.getString(R.string.abv) + " " + data.getAbv() + "\n";
            }
        }

        if (data.getIbu() != null) {
            if (!data.getIbu().isEmpty()) {
                extraInfo += context.getString(R.string.ibu) + " " + data.getIbu() + "\n";
            }
        }

        if (data.getIsOrganic() != null) {
            if (!data.getIsOrganic().isEmpty()) {
                extraInfo += context.getString(R.string.organic) + " " + data.getIsOrganic() + "\n\n";
            }
        }

        if (data.getAvailable() != null) {
            if (data.getAvailable().getName() != null) {
                if (!data.getAvailable().getName().isEmpty()) {
                    extraInfo += context.getString(R.string.available) + " " + data.getAvailable().getName() + "\n";
                }
            }

            if (data.getAvailable().getDescription() != null) {
                if (!data.getAvailable().getDescription().isEmpty()) {
                    extraInfo += data.getAvailable().getDescription() + "\n";
                }
            }
        }

        return extraInfo;
    }
}
