package cloud.l0cky.radanalysetools.models;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Highway{
    private boolean hasCycleway;
    private int maxspeed;
    private int length;
    private long id;
    private ArrayList<LinkedHashMap> geometry;

    public int getMaxspeed() {
        return maxspeed;
    }

    public void setMaxspeed(int maxspeed) {
        this.maxspeed = maxspeed;
    }

    public boolean isHasCycleway() {
        return hasCycleway;
    }

    public void setHasCycleway(boolean hasCycleway) {
        this.hasCycleway = hasCycleway;
    }

    public ArrayList<LinkedHashMap> getGeometry() {
        return geometry;
    }

    public void setGeometry(ArrayList<LinkedHashMap> geometry) {
        this.geometry = geometry;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
