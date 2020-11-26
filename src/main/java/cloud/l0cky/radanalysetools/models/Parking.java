package cloud.l0cky.radanalysetools.models;

import cloud.l0cky.radanalysetools.services.Coordinate;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Parking implements Coordinate {

    private double latitude;
    private double longitude;
    private ArrayList<LinkedHashMap> geometry;
    private String type;
    private String operator;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public ArrayList<LinkedHashMap> getGeometry() {
        return geometry;
    }

    public void setGeometry(ArrayList<LinkedHashMap> geometry) {
        this.geometry = geometry;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

}
