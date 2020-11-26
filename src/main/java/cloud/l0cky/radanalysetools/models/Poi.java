package cloud.l0cky.radanalysetools.models;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Poi {

    private String name;
    private String type;
    private int minDistance;
    private ArrayList<LinkedHashMap> geometry;
    private double lat_closestParking;
    private double lon_closestParking;
    private Parking parking;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public int getMinDistance() {
        return minDistance;
    }

    public void setMinDistance(int minDistance) {
        this.minDistance = minDistance;
    }

    public ArrayList<LinkedHashMap> getGeometry() {
        return geometry;
    }

    public void setGeometry(ArrayList<LinkedHashMap> geometry) {
        this.geometry = geometry;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLon_closestParking() {
        return lon_closestParking;
    }

    public void setLon_closestParking(double lon_closestParking) {
        this.lon_closestParking = lon_closestParking;
    }

    public double getLat_closestParking() {
        return lat_closestParking;
    }

    public void setLat_closestParking(double lat_closestParking) {
        this.lat_closestParking = lat_closestParking;
    }

    public Parking getParking() {
        return parking;
    }

    public void setParking(Parking parking) {
        this.parking = parking;
    }
}
