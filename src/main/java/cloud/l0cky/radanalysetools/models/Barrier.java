package cloud.l0cky.radanalysetools.models;

import cloud.l0cky.radanalysetools.services.Coordinate;

public class Barrier implements Coordinate {
    private double latitude;
    private double longitude;
    private String bicycle;
    private double maxwidth;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double lat) {
        this.latitude = lat;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double lon) {
        this.longitude = lon;
    }

    public String getBicycle() {
        return bicycle;
    }

    public void setBicycle(String bicycle) {
        this.bicycle = bicycle;
    }


    public double getMaxwidth() {
        return maxwidth;
    }

    public void setMaxwidth(double maxwidth) {
        this.maxwidth = maxwidth;
    }
}
