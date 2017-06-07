package iamdilipkumar.com.beerography.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created on 07/06/17.
 *
 * @author dilipkumar4813
 * @version 1.0
 *
 */
public class BeerDetail {

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private Datum data;

    @SerializedName("status")
    @Expose
    private String status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Datum getData() {
        return data;
    }

    public void setData(Datum data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
