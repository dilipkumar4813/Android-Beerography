
package iamdilipkumar.com.beerography.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Labels {

    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("medium")
    @Expose
    private String medium;
    @SerializedName("large")
    @Expose
    private String large;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }

}
