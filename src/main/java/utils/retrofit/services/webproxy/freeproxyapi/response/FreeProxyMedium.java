package utils.retrofit.services.webproxy.freeproxyapi.response;

import com.google.gson.annotations.SerializedName;

public class FreeProxyMedium extends FreeProxyMini {

    @SerializedName("isAlive")
    private Boolean isAlive;

    @SerializedName("miliseconds")
    private int miliseconds;

    @SerializedName("averageTime")
    private int averageTime;

    @SerializedName("countryName")
    private String countryName;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longitude")
    private String longitude;

    public Boolean getAlive() {
        return isAlive;
    }

    public void setAlive(Boolean alive) {
        isAlive = alive;
    }

    public int getMiliseconds() {
        return miliseconds;
    }

    public void setMiliseconds(int miliseconds) {
        this.miliseconds = miliseconds;
    }

    public int getAverageTime() {
        return averageTime;
    }

    public void setAverageTime(int averageTime) {
        this.averageTime = averageTime;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "FreeProxyMedium{" +
                "isAlive=" + isAlive +
                ", miliseconds=" + miliseconds +
                ", averageTime=" + averageTime +
                ", countryName='" + countryName + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                '}';
    }
}
