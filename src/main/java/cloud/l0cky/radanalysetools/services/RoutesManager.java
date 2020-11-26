package cloud.l0cky.radanalysetools.services;

import cloud.l0cky.radanalysetools.models.Route;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class RoutesManager {
    private List<Route> allRoutes = new ArrayList<Route>();

    public List<Route> getAllRoutes(){ return allRoutes;}

    @PostConstruct
    @Scheduled(cron = "* * * 1 * *")
    public void loadAllRoutes() throws IOException, InterruptedException {

    }


    public Route getRouteByName(String routeName){
        for (Route route : allRoutes)
            if(routeName.equals(route.getName()))
                return route;
        //TODO Fehler zurückgeben wenn nicht gefunden
        return new Route();
    }
    public Route getRouteById(int id){
        return allRoutes.get(id);
    }


}
