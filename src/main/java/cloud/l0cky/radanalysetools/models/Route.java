package cloud.l0cky.radanalysetools.models;

public class Route {
    private String name;
    private String fahrradTrack;
    private String autoTrack;
    private int duration_car;
    private int duration_bike;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFahrradTrack() {
        return fahrradTrack;
    }

    public void setFahrradTrack(String fahrradTrack) {
        this.fahrradTrack = fahrradTrack;
    }

    public String getAutoTrack() {
        return autoTrack;
    }

    public void setAutoTrack(String autoTrack) {
        this.autoTrack = autoTrack;
    }

    public int getDuration_car() {
        return duration_car;
    }

    public void setDuration_car(int duration_car) {
        this.duration_car = duration_car;
    }

    public int getDuration_bike() {
        return duration_bike;
    }

    public void setDuration_bike(int duration_bike) {
        this.duration_bike = duration_bike;
    }

    @Override
    public String toString(){
        return "Route{" +
                "name='" + name + '\'' +
                ", duration_car=" + duration_car +
                ", duration_bike=" + duration_bike +
                ", fahrradTrack='" + fahrradTrack + '\'' +
                ", autoTrack='" + autoTrack + + '\'' +
                "}";
    }

}
