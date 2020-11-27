package cloud.l0cky.radanalysetools.services;

import cloud.l0cky.radanalysetools.models.*;

import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

@EnableScheduling
@Service
public class QueryOverpassDataService {
	private static final String OVERPASS_BASE_URL = "https://lz4.overpass-api.de/api/interpreter";
	private static boolean developMode = false;
	private final String CITY = "Bochum";

	private List<BicycleParking> allBicycleParkings = new ArrayList<>();
	private List<Bicyclepath> allBicyclePaths = new ArrayList<>();
	private List<Bicyclepath> sidewalks = new ArrayList<>();
	private List<Poi> pois = new ArrayList<>();
	private List<Barrier> barriers = new ArrayList<>();

	public List<Poi> getAllPois() {
		return pois;
	}

	public List<BicycleParking> getAllBicycleParkings() {
		return allBicycleParkings;
	}

	public List<Bicyclepath> getAllBicyclePaths() { return allBicyclePaths; }

	public List<Bicyclepath> getSidewalks() { return sidewalks; }

	public List<Barrier> getBarriers() { return barriers; }

	@PostConstruct
	@Scheduled(cron = "* * * 1 * *")
	private void updateAllData()throws IOException, InterruptedException{
		this.fetchBicycleParking();
		this.fetchBicyclePaths();
		this.fetchPois();
		this.fetchBarriers();
		this.fetchSidewalks();

	}

	public void fetchBicycleParking() throws IOException, InterruptedException {
		List<BicycleParking> actualParking= new ArrayList<BicycleParking>();
		String queryString = "data=" +
						"[out:json];" +
						"(area[name=" + CITY + "];)->.searchArea;" +
						"(" +
						"node[amenity=bicycle_parking](area.searchArea);" +
						"way[amenity=bicycle_parking](area.searchArea);" +
						");" +
						"(._;>;);" +
						"out geom;";

		// create object mapper instance
		List<LinkedHashMap> elements = this.overpassQuery(queryString);
		if (elements == null)
			return;
		int i = 0;
		for (LinkedHashMap element : elements){
			String type = (String)element.get("type");
			if(element.getOrDefault("tags", null) != null) {

				LinkedHashMap tags = (LinkedHashMap) element.get("tags");
				BicycleParking bicycleParking = new BicycleParking();
				int capacity = Integer.parseInt((String) tags.getOrDefault("capacity", "0"));
				String bicycle_parking = (String) tags.getOrDefault("bicycle_parking", "0");
				String covered = (String) tags.getOrDefault("covered", "0");
				String access = (String) tags.getOrDefault("access", "0");
				bicycleParking.setBicycle_parking(bicycle_parking);
				bicycleParking.setCapacity(capacity);
				bicycleParking.setCovered(covered.equals("yes") ? true : false);
				bicycleParking.setAccess(access);

				if (type.equals("node")) {
					double lat = (double) element.getOrDefault("lat", 0.);
					double lon = (double) element.getOrDefault("lon", 0.);
					bicycleParking.setLatitude(lat);
					bicycleParking.setLongitude(lon);
				} else if (type.equals("way") && element.getOrDefault("tags", null) != null) {
					LinkedHashMap bounds = (LinkedHashMap) element.get("bounds");
					double media_lat = ((Double) bounds.get("maxlat") + (Double)bounds.get("minlat"))/2;
					double media_lon = ((Double) bounds.get("maxlon") + (Double)bounds.get("minlon"))/2;
					bicycleParking.setLatitude(media_lat);
					bicycleParking.setLongitude(media_lon);
				} else
					continue;
				actualParking.add(bicycleParking);
			}
			else // Node or: Way has no Tags
				continue;
		}
		this.allBicycleParkings = actualParking;
	}

	private void fetchSidewalks() throws IOException, InterruptedException{
		List<Bicyclepath> actualSidewalks= new ArrayList<Bicyclepath>();
		String queryString = "data=[out:json];(area[name=\"Bochum\"];)->.searchArea;" +
				"(" +
				"way[~\"traffic_sign$\"~\"DE:1022-10\"][~\"traffic_sign$\"~\"DE:239\"](area.searchArea);" +
				");" +
				"out geom;";
		List<LinkedHashMap> elements = this.overpassQuery(queryString);
		if (elements == null)
			return;
		for (LinkedHashMap element : elements) {
			String type = (String) element.get("type");
			if (type.equals("node"))
				continue;
			ArrayList<LinkedHashMap> geometry = (ArrayList) element.getOrDefault("geometry", 0.);
			LinkedHashMap tags = (LinkedHashMap) element.get("tags");

			String surface = (String) tags.getOrDefault("surface", "0");
			String traffic_signs = (String) tags.getOrDefault("traffic_sign", "0");
			String traffic_signs_right = (String) tags.getOrDefault("sidewalk:right:traffic_sign", "0");
			String traffic_signs_left = (String) tags.getOrDefault("sidewalk:left:traffic_sign", "0");
			int width_left = 0;
			int width_right = 0;
			try {
				width_left = Integer.parseInt((String) tags.getOrDefault("cycleway:left:width", "0"));
				width_right = Integer.parseInt((String) tags.getOrDefault("cycleway:right:width", "0"));
			} catch (NumberFormatException e) {
				continue;
			}
			Bicyclepath bicyclepath = new Bicyclepath();
			bicyclepath.setSurface(surface);
			bicyclepath.setGeometry(geometry);

			if (traffic_signs.contains("DE:1022-10")) { //muss bleiben da auch left und right gesetzt sein können. Sonst wird überschrieben
				bicyclepath.setSidewalk("both");
			}
			if (traffic_signs_right.contains("DE:1022-10")) {
				bicyclepath.setSidewalk("right");
			}
			if (traffic_signs_left.contains("DE:1022-10")) {
				bicyclepath.setSidewalk("left");
			}
			actualSidewalks.add(bicyclepath);

		}
		this.sidewalks = actualSidewalks;
	}

	private void fetchBicyclePaths() throws IOException, InterruptedException{
		List<Bicyclepath> actualBicyclepaths= new ArrayList<Bicyclepath>();

		String queryString = "data=[out:json];(area[name=\"Bochum\"];)->.searchArea;" +
				"(" +
				"way[~\"^cycleway*\"~\"^(lane|opposite_lane|opposite_track|shared_lane$)$\"](area.searchArea);" +
				");" +
				"out geom;";
		List<LinkedHashMap> elements = this.overpassQuery(queryString);
		if (elements == null)
			return;
		for (LinkedHashMap element : elements){
			String type = (String)element.get("type");
			if(type.equals("node"))
				continue;
			ArrayList<LinkedHashMap> geometry = (ArrayList)element.getOrDefault("geometry", 0.);
			LinkedHashMap tags = (LinkedHashMap)element.get("tags");
			int maxspeed = 0;
			try {
				maxspeed = Integer.parseInt((String) tags.getOrDefault("maxspeed", "0"));
			}
			catch (NumberFormatException e ){
				continue;
			}
			String surface = (String)tags.getOrDefault("surface","0");
			String cycleway_left = (String)tags.getOrDefault("cycleway:left","0");
			String cycleway_right = (String)tags.getOrDefault("cycleway:right","0");
			String cycleway_both = (String)tags.getOrDefault("cycleway:both","0");
			if (!cycleway_both.equals("0") || cycleway_right.equals("0") && cycleway_left.equals("0")){
				cycleway_left = (String)tags.getOrDefault("cycleway:both","0");
				cycleway_right = (String)tags.getOrDefault("cycleway:both","0");
			}
			String traffic_signs_right = (String) tags.getOrDefault("cycleway:right:traffic_sign", "0");
			String traffic_signs_left = (String) tags.getOrDefault("cycleway:left:traffic_sign", "0");
			String traffic_signs = (String) tags.getOrDefault("cycleway:traffic_sign", "0");
			if (!traffic_signs.equals("0")){
				traffic_signs_right = traffic_signs;
				traffic_signs_left = traffic_signs;
			}
			int width_left = 0;
			int width_right = 0;
			try {
				width_left = Integer.parseInt((String) tags.getOrDefault("cycleway:left:width", "0"));
				width_right = Integer.parseInt((String) tags.getOrDefault("cycleway:right:width", "0"));
			}
			catch (NumberFormatException e ){
				continue;
			}
			Bicyclepath bicyclepath = new Bicyclepath();
			bicyclepath.setSurface(surface);
			bicyclepath.setGeometry(geometry);
			bicyclepath.setCycleway_left(cycleway_left);
			bicyclepath.setCycleway_right(cycleway_right);
			bicyclepath.setWidth_left(width_left);
			bicyclepath.setWidth_right(width_right);
			bicyclepath.setMaxspeed(maxspeed);
			bicyclepath.setTrafficSign_left(traffic_signs_left);
			bicyclepath.setTrafficSign_right(traffic_signs_right);

			actualBicyclepaths.add(bicyclepath);
		}
		this.allBicyclePaths = actualBicyclepaths;
	}

	private List<LinkedHashMap> overpassQuery (String queryString) throws IOException, InterruptedException{
		if (developMode){
			List<LinkedHashMap> tempList = new ArrayList<>();
			return tempList;
		}
		else {
			HttpClient client = HttpClient.newBuilder().build();
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create(OVERPASS_BASE_URL))
					.POST(HttpRequest.BodyPublishers.ofString(queryString))
					.build();
			HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
			String temp = (String) response.body();
			// create object mapper instance
			ObjectMapper mapper = new ObjectMapper();
			Map<?, ?> map = mapper.readValue(temp, Map.class);
			List<LinkedHashMap> elements = (ArrayList) map.getOrDefault("elements", null);
			return elements;
		}
	}

	private void fetchPois() throws IOException, InterruptedException {
		int minParkingPlaceDistance = 50;
		String queryString = "data=" +
				"[out:json];" +
				"(area[name=\""+ CITY + "\"];)->.searchArea;" +
				"(" +
				"  node[shop=supermarket](area.searchArea);" +
				"  way[shop=supermarket](area.searchArea);" +
				");" +
				"out geom;";
		String parkingSupermarketString = "data=" +
				"[out:json];" +
				"(area[name=\""+ CITY + "\"];)->.searchArea;" +
				"(" +
				"	node[shop=supermarket](area.searchArea);" +
				"	way[shop=supermarket](area.searchArea);" +
				")->.a;" +
				"(" +
				"	way(around.a:" + minParkingPlaceDistance + ")[amenity=parking];" +
				")->.b;" +
				".b out geom;";
		List<LinkedHashMap> elements = this.overpassQuery(queryString);
		List<LinkedHashMap> parkingElements = this.overpassQuery(parkingSupermarketString);
		List<Parking> parkings = new ArrayList<>();
		for(LinkedHashMap parking : parkingElements){
			String type = (String)parking.get("type");
			LinkedHashMap tags = (LinkedHashMap) parking.get("tags");
			String name = (String)tags.getOrDefault("name","0");
			String operator = (String)tags.getOrDefault("operator","0");
			LinkedHashMap bounds = (LinkedHashMap) parking.get("bounds");
			double lon_medium = ((Double) bounds.get("maxlon") + (Double)bounds.get("minlon"))/2;
			double lat_medium = ((Double) bounds.get("maxlat") + (Double)bounds.get("minlat"))/2;
			ArrayList<LinkedHashMap> geometry = (ArrayList) parking.getOrDefault("geometry", 0.);
			Parking tempParking = new Parking();
			tempParking.setGeometry(geometry);
			tempParking.setType(type);
			tempParking.setOperator(operator);
			tempParking.setLatitude(lat_medium);
			tempParking.setLongitude(lon_medium);
			parkings.add(tempParking);
		}
		if (elements == null)
			return;
		for (LinkedHashMap element : elements) {
			LinkedHashMap tags = (LinkedHashMap) element.get("tags");
			String name = (String)tags.getOrDefault("name","0");
			ArrayList<LinkedHashMap> geometry = new ArrayList<>();
			String type = (String) element.get("type");
			double center_x, center_y;

			if (type.equals("node")) {
				double lat = (double)element.getOrDefault("lat", 0.);
				double lon = (double)element.getOrDefault("lon",0.);
				LinkedHashMap tempHashmap = new LinkedHashMap();
				tempHashmap.put("lat", lat);
				tempHashmap.put("lon", lon);
				geometry.add(tempHashmap);
				center_y = lon;
				center_x = lat;
			}
			else { // type == way
				geometry = (ArrayList) element.getOrDefault("geometry", 0.);
				LinkedHashMap bounds = (LinkedHashMap) element.get("bounds");
				center_y = ((Double) bounds.get("maxlon") + (Double)bounds.get("minlon"))/2;
				center_x = ((Double) bounds.get("maxlat") + (Double)bounds.get("minlat"))/2;
			}

			Poi tempPoi = new Poi();
			Tuple<Integer, Coordinate> distance = this.calculateMinDistance(center_x, center_y, allBicycleParkings);
			Tuple<Integer, Coordinate> parkingPlaceDistance = this.calculateMinDistance(center_x, center_y, parkings);
			tempPoi.setMinDistance(distance.getFirst());
			tempPoi.setLat_closestParking(distance.getSecond().getLatitude());
			tempPoi.setLon_closestParking(distance.getSecond().getLongitude());
			tempPoi.setType(type);
			tempPoi.setName(name);
			tempPoi.setGeometry(geometry);
			if(parkingPlaceDistance.getFirst() < minParkingPlaceDistance)
				tempPoi.setParking((Parking)parkingPlaceDistance.getSecond());
			pois.add(tempPoi);
		}
	}

	Tuple<Integer, Coordinate> calculateMinDistance(double center_lat, double center_lon, List<? extends Coordinate> referenceGeoList){
		double first_lat = referenceGeoList.get(0).getLatitude();
		double first_lon = referenceGeoList.get(0).getLongitude();
		double lat_closestParking = first_lat;
		double lon_closestParking = first_lon;
		Coordinate closestParking = referenceGeoList.get(0);
		int minDistance = this.distance(first_lat, first_lon, center_lat, center_lon);
		for (Coordinate parking : referenceGeoList){
			int actualDistance = this.distance(parking.getLongitude(), parking.getLatitude(), center_lon, center_lat);
			if (actualDistance < minDistance) {
				minDistance = actualDistance;
				closestParking = parking;
				lat_closestParking = parking.getLatitude();
				lon_closestParking = parking.getLongitude();
			}
		}

		return new Tuple<>(minDistance, closestParking);
	}

	private int distance(double lat1, double lon1, double lat2, double lon2) {
		final int R = 6371;
		Double latDistance = Math.toRadians(lat2-lat1);
		Double lonDistance = Math.toRadians(lon2-lon1);
		Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
				Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
						Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
		Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		Double distance = R * c * 1000;

		return distance.intValue();
	}

	private void fetchBarriers()  throws IOException, InterruptedException {
		List<Barrier> actualBarriers= new ArrayList<Barrier>();
		HttpClient client = HttpClient.newBuilder().build();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(OVERPASS_BASE_URL))
				.POST(HttpRequest.BodyPublishers.ofString("data=" +
						"[out:json];" +
						"(area[name=" + CITY + "];)->.searchArea;" +
						"node[barrier=\"cycle_barrier\"](area.searchArea);" +
						"(._;>;);" +
						"out geom;"))
				.build();

		HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
		String temp = (String) response.body();

		ObjectMapper mapper = new ObjectMapper();
		Map<?, ?> map = mapper.readValue(temp, Map.class);
		List<LinkedHashMap> elements = (ArrayList)map.getOrDefault("elements",null);
		if (elements == null)
			return;
		for (LinkedHashMap element : elements){
			String type = (String)element.get("type");
			if(element.getOrDefault("tags", null) != null) {
				LinkedHashMap tags = (LinkedHashMap) element.get("tags");
				Barrier barrier = new Barrier();
				if (type.equals("node")) {
					String bicycle = (String) tags.getOrDefault("bicycle", "0");
					double lat = (double) element.getOrDefault("lat", 0.);
					double lon = (double) element.getOrDefault("lon", 0.);
					double maxwidth = 0.;
					try {
						maxwidth = (double)tags.getOrDefault("maxwidth", 0.);
					}
					catch (ClassCastException e) {
						continue;
					}
					barrier.setLatitude(lat);
					barrier.setLongitude(lon);
					barrier.setMaxwidth(maxwidth);
					barrier.setBicycle(bicycle);
				} else
					continue;
				actualBarriers.add(barrier);
			}
			else // Node / Way has no Tags
				continue;
		}
		this.barriers = actualBarriers;
	}


}
