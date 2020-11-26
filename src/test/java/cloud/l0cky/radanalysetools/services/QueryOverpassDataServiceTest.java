package cloud.l0cky.radanalysetools.services;

import cloud.l0cky.radanalysetools.models.BicycleParking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class QueryOverpassDataServiceTest {

    @Test
    public void testJsonParsing() throws Exception{
        File file = new File("src/test/resources/test_osm_parkplaetze_bochum.json");
        assertTrue(file.exists());
        FileReader fr = new FileReader(file);

        JsonObject deserialize = (JsonObject) Jsoner.deserialize(fr);
        fr = new FileReader(file);
        JsonArray deserialize2 = (JsonArray) Jsoner.deserialize(fr);
        //assertEquals(0.6, deserialize.getDouble("version");
    }
    @Test
    public void testJacksonParsing() throws Exception{
        File file = new File("src/test/resources/test_osm_parkplaetze_bochum.json");
        assertTrue(file.exists());
        try {
            // create object mapper instance
            ObjectMapper mapper = new ObjectMapper();

            Map<?, ?> map = mapper.readValue(file, Map.class);

            double version = (double)map.get("version");
            assertEquals(0.6, version);
            ArrayList<LinkedHashMap> elements = (ArrayList)map.get("elements");
            for (LinkedHashMap element : elements){
                double lat = (double)element.get("lat");
                double lon = (double)element.get("lon");
                LinkedHashMap tags = (LinkedHashMap)element.get("tags");
                int capacity = Integer.parseInt((String) tags.getOrDefault("capacity", "0"));
                String bicycle_parking = (String)tags.getOrDefault("bicycle_parking","");
                String covered = (String)tags.getOrDefault("covered","");
                System.out.println(capacity);
                String type = (String)element.get("type");
                if(type.equals("node")){
                    BicycleParking bicycleParking = new BicycleParking();
                    bicycleParking.setLatitude(lat);
                    bicycleParking.setLongitude(lon);
                    bicycleParking.setBicycle_parking(bicycle_parking);
                    bicycleParking.setCapacity(capacity);
                    bicycleParking.setCovered(covered.equals("yes") ? true : false);

                }
                else if(type.equals("way")){
                    continue;
                }
                else
                    continue;
            }
            // print map entries
//            for (Map.Entry<?, ?> entry : map.entrySet()) {
//                System.out.println(entry.getKey() + "=" + entry.getValue());
//            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}