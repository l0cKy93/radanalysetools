package cloud.l0cky.radanalysetools.models;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Bicyclepath {
    private String surface;
    private int maxspeed;
    private String cycleway_left;
    private String cycleway_right;
    private String trafficSign_left;
    private String trafficSign_right;
    private int width_left;
    private int width_right;
    private String sidewalk;
    private ArrayList<LinkedHashMap> geometry;

    public String getCycleway_right() {
        return cycleway_right;
    }

    public void setCycleway_right(String cycleway_right) { this.cycleway_right = cycleway_right; }

    public int getWidth_left() { return width_left; }

    public void setWidth_left(int width_left) { this.width_left = width_left; }

    public int getWidth_right() { return width_right; }

    public void setWidth_right(int width_right) { this.width_right = width_right; }

    public ArrayList<LinkedHashMap> getGeometry() {
        return geometry;
    }

    public void setGeometry(ArrayList<LinkedHashMap> geometry) {
        this.geometry = geometry;
    }

    public String getSurface() {
        return surface;
    }

    public void setSurface(String surface) {
        this.surface = surface;
    }

    public int getMaxspeed() {
        return maxspeed;
    }

    public void setMaxspeed(int maxspeed) {
        this.maxspeed = maxspeed;
    }

    public String getCycleway_left() {
        return cycleway_left;
    }

    public void setCycleway_left(String cycleway_left) {
        this.cycleway_left = cycleway_left;
    }



    public String getSidewalk() {
        return sidewalk;
    }

    public void setSidewalk(String sidewalk) {
        this.sidewalk = sidewalk;
    }

    @Override
    public String toString() {
        return "Bicyclepath{" +
                "surface=" + surface +
                "maxspeed=" + maxspeed +
                "sidwalk=" + sidewalk +
                "width_left=" + width_left +
                "width_right=" + width_right +
                "geometry=" + geometry +
                (cycleway_left.equals("0") ? "" : "cycleway_left=" + cycleway_left) +
                (cycleway_right.equals("0") ? "" : "cycleway_right=" + cycleway_right) +
                "}";
    }

    public String getTrafficSign_left() {
        return trafficSign_left;
    }

    public void setTrafficSign_left(String trafficSign_left) {
        this.trafficSign_left = trafficSign_left;
    }

    public String getTrafficSign_right() {
        return trafficSign_right;
    }

    public void setTrafficSign_right(String trafficSign_right) {
        this.trafficSign_right = trafficSign_right;
    }
}
