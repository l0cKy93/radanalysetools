package cloud.l0cky.radanalysetools.controllers;

import cloud.l0cky.radanalysetools.models.*;
import cloud.l0cky.radanalysetools.services.QueryOverpassDataService;
import com.example.demo.models.*;

import cloud.l0cky.radanalysetools.services.RoutesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Controller
public class HelloControler {


	@Autowired
    QueryOverpassDataService queryOverpassDataService;

	@Autowired
	RoutesManager routesManager;
	
	@GetMapping("/")
	public String index() {
		return "index";
	}
	@GetMapping("/impressum")
		public String Impressum() {
			return "impressum";
		}


	@GetMapping("/radinfrastruktur")
	public String Radinfra(Model model) {
		List<BicycleParking> bicycleParkings = queryOverpassDataService.getAllBicycleParkings();
		model.addAttribute("bicycleParkings", bicycleParkings);
		List<Bicyclepath> bicyclepaths = queryOverpassDataService.getAllBicyclePaths();
		List<Bicyclepath> sidewalks = queryOverpassDataService.getSidewalks();
		List<Poi> pois = queryOverpassDataService.getAllPois();
		List<Barrier> barriers = queryOverpassDataService.getBarriers();
		model.addAttribute("bicyclepaths", bicyclepaths);
		model.addAttribute("sidewalks", sidewalks);
		model.addAttribute("pois", pois);
		model.addAttribute("barriers", barriers);
		return "radinfrastruktur";
	}
	@GetMapping("/unfallatlas-analyse")
	public String UnfallAnalyse() {
		return "unfallatlas-analyse";
	}


	@GetMapping("/fahrzeiten")
	public String Fahrzeiten() {
		return "fahrradvsauto";
	}
	// Get Route REST
	@GetMapping("/get/route/{id}")
	public Route getRoute(@PathVariable("id") int id){
		return routesManager.getRouteById(id);
	}

	@GetMapping("get/route/")
	public Route getRouteById(@RequestParam(value = "name", defaultValue = "none") String name){
		return routesManager.getRouteByName(name);
	}
	@GetMapping("get/routes")
	public List<Route> getRoutes(){
		return routesManager.getAllRoutes();
	}


	/////////////////////////////////////
	//Testing Zone
	//
	@GetMapping(
			value = "caricon",
			produces = MediaType.IMAGE_JPEG_VALUE
	)
	public @ResponseBody byte[] getImageWithMediaType() throws IOException {
		InputStream in = getClass()
				.getResourceAsStream("/static/data-for-leaflet/images/car.png");
		return in.readAllBytes();
	}


}
