
package iamdilipkumar.com.beerography.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("nameDisplay")
    @Expose
    private String nameDisplay;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("abv")
    @Expose
    private String abv;

    @SerializedName("ibu")
    @Expose
    private String ibu;

    @SerializedName("glasswareId")
    @Expose
    private Integer glasswareId;

    @SerializedName("availableId")
    @Expose
    private Integer availableId;

    @SerializedName("styleId")
    @Expose
    private Integer styleId;

    @SerializedName("isOrganic")
    @Expose
    private String isOrganic;

    @SerializedName("labels")
    @Expose
    private Labels labels;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("statusDisplay")
    @Expose
    private String statusDisplay;

    @SerializedName("createDate")
    @Expose
    private String createDate;

    @SerializedName("updateDate")
    @Expose
    private String updateDate;

    @SerializedName("glass")
    @Expose
    private Glass glass;

    @SerializedName("available")
    @Expose
    private Available available;

    @SerializedName("style")
    @Expose
    private Style style;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameDisplay() {
        return nameDisplay;
    }

    public void setNameDisplay(String nameDisplay) {
        this.nameDisplay = nameDisplay;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAbv() {
        return abv;
    }

    public void setAbv(String abv) {
        this.abv = abv;
    }

    public String getIbu() {
        return ibu;
    }

    public void setIbu(String ibu) {
        this.ibu = ibu;
    }

    public Integer getGlasswareId() {
        return glasswareId;
    }

    public void setGlasswareId(Integer glasswareId) {
        this.glasswareId = glasswareId;
    }

    public Integer getAvailableId() {
        return availableId;
    }

    public void setAvailableId(Integer availableId) {
        this.availableId = availableId;
    }

    public Integer getStyleId() {
        return styleId;
    }

    public void setStyleId(Integer styleId) {
        this.styleId = styleId;
    }

    public String getIsOrganic() {
        return isOrganic;
    }

    public void setIsOrganic(String isOrganic) {
        this.isOrganic = isOrganic;
    }

    public Labels getLabels() {
        return labels;
    }

    public void setLabels(Labels labels) {
        this.labels = labels;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDisplay() {
        return statusDisplay;
    }

    public void setStatusDisplay(String statusDisplay) {
        this.statusDisplay = statusDisplay;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public Glass getGlass() {
        return glass;
    }

    public void setGlass(Glass glass) {
        this.glass = glass;
    }

    public Available getAvailable() {
        return available;
    }

    public void setAvailable(Available available) {
        this.available = available;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    protected Datum(Parcel in) {
        id = in.readString();
        name = in.readString();
        nameDisplay = in.readString();
        description = in.readString();
        abv = in.readString();
        ibu = in.readString();
        isOrganic = in.readString();
        status = in.readString();
        statusDisplay = in.readString();
        createDate = in.readString();
        updateDate = in.readString();
    }

    public static final Creator<Datum> CREATOR = new Creator<Datum>() {
        @Override
        public Datum createFromParcel(Parcel in) {
            return new Datum(in);
        }

        @Override
        public Datum[] newArray(int size) {
            return new Datum[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(nameDisplay);
        dest.writeString(description);
        dest.writeString(abv);
        dest.writeString(ibu);
        dest.writeString(isOrganic);
        dest.writeString(status);
        dest.writeString(statusDisplay);
        dest.writeString(createDate);
        dest.writeString(updateDate);
    }
}
