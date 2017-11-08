package kharkov.kp.gic.service;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


import kharkov.kp.gic.domain.DugInfo;
import kharkov.kp.gic.domain.dto.DugGeoJsonProperties;
import kharkov.kp.gic.domain.geojson.GeoJsonFeatureCollection;
import kharkov.kp.gic.domain.geojson.GeoJsonFeaturePolygon;
import kharkov.kp.gic.repository.DugInfoRepository;

@Service
public class KharkovDugService {

	private final String DUG_URL = "http://arcgis-nlb2.citynet.kharkov.ua:6080/arcgis/rest/services/dug_multi_web/MapServer/0/query?where=1%3D1&text=&objectIds=&time=&geometry=&geometryType=esriGeometryEnvelope&inSR=&spatialRel=esriSpatialRelIntersects&relationParam=&outFields=*&returnGeometry=true&maxAllowableOffset=&geometryPrecision=&outSR=&returnIdsOnly=false&returnCountOnly=false&orderByFields=&groupByFieldsForStatistics=&outStatistics=&returnZ=false&returnM=false&gdbVersion=&returnDistinctValues=false&f=pjson";

	private final String CONVERTER_URL = "http://arcgis-nlb2/arcgis/rest/services/Utilities/Geometry/GeometryServer/project";
	
	@Autowired
	private DugInfoRepository dugInfoRepository;
	
	private String dugCache;
	
	public String getDugs(boolean updateCache) {
		if ((updateCache) || dugCache == null) {
			List<DugSourceInfo> src = updateDugsCache();
			if (src == null) {
				return dugCache != null ? dugCache : "";
			}
			List<GeoJsonFeaturePolygon> features = new ArrayList<>(src.size());
			for(DugSourceInfo dsi : src) {
				DugGeoJsonProperties props = DugGeoJsonProperties.builder()
						.typko(dsi.typko)
						.id(dsi.dugInfo.getId())					
						.work(dsi.dugInfo.getWork())				
						.startDate(dsi.dugInfo.getStartDate())
						.owner(dsi.dugInfo.getOwner())	
						.address(dsi.dugInfo.getAddress())	
						.addressDesc(dsi.dugInfo.getAddressDesc())
						.volume(dsi.dugInfo.getVolume())	
						.stopDate(dsi.dugInfo.getStopDate())	
						.name(dsi.dugInfo.getName())	
						.resolutionNumber(dsi.dugInfo.getResolutionNumber())
						.resolutionDate(dsi.dugInfo.getResolutionDate())
						.build();
				double[][][] coords = new double[1][][];
				coords[0] = new double[dsi.points.size()][];
		        int i = 0;
				for(Pointko p : dsi.points) {					
					coords[0][i++] = new double[] { p.x, p.y };
				}
				features.add(new GeoJsonFeaturePolygon(coords, props));
			}			
			// формируем коллекцию в формате GeoJson
			GeoJsonFeatureCollection geoJsonCollection = new GeoJsonFeatureCollection("EPSG:3857", features);
			// и трансформируем ее в строку
			dugCache = _serializeToJson(geoJsonCollection);	
		}
		return dugCache;
	}
	
	@Transactional(readOnly = true)
	public List<DugSourceInfo> updateDugsCache() {	
		try
		{
			// load attributes from AR_AUDIT and group by Id
			List<DugInfo> attributes = dugInfoRepository.findAll(); 
			Map<Integer, List<DugInfo>> attrMap = attributes.stream()
					.collect(Collectors.groupingBy(DugInfo::getId));
			// load geometry and link attributes
			List<DugSourceInfo> allGeometries = _loadDugGeometry();
			List<DugSourceInfo> validDugs = allGeometries.stream()
				 .peek(g -> { if (attrMap.containsKey(g.Id)) g.dugInfo = attrMap.get(g.Id).get(0);})
				 .filter(g -> g.dugInfo != null)
				 .collect(Collectors.toList());
			// convert coordinates
			List<Pointko> unique = _getUniquePoints(validDugs);		
			Map<String, Pointko> converted = _convertCoordinates(unique);
			validDugs.stream().parallel().forEach(d -> { d.points.stream().forEach(p -> p.relocate(converted.get(p.toString())));});
			return validDugs;
		}
		catch(Exception exc) {
			return null;
		}
	}		
	
	private List<Pointko> _getUniquePoints(List<DugSourceInfo> dugs){
		Set<String> set = new HashSet<>();
		List<Pointko> result = new ArrayList<>();
		for(DugSourceInfo dsi : dugs) {
			for(Pointko p: dsi.points) {
				if (!set.contains(p.toString())) {
					set.add(p.toString());
					result.add(p);
				}
			}
		}
		return result;
	}
	
	private Map<String, Pointko> _convertCoordinates(List<Pointko> source) throws JsonProcessingException, IOException {
		// make up coordinates list
		StringBuilder geometries = new StringBuilder();
		for(Pointko p : source) {
			geometries.append(p.toString()).append(",\n");
		}			
		String coords = geometries.toString();
		int postfixLength = ",\n".length();
		int fullLength = coords.length();
		coords = coords.substring(0, fullLength - postfixLength);			
		// make up and send POST HTTP request
		String url = CONVERTER_URL;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
		map.add("inSR", "{\"wkt\" : \"PROJCS[\\\"khar_test3\\\",GEOGCS[\\\"GCS_Pulkovo_1942\\\",DATUM[\\\"D_Pulkovo_1942\\\",SPHEROID[\\\"Krasovsky_1940\\\",6378245.0,298.3]],PRIMEM[\\\"Greenwich\\\",0.0],UNIT[\\\"Degree\\\",0.0174532925199433]],PROJECTION[\\\"Gauss_Kruger\\\"],PARAMETER[\\\"False_Easting\\\",-1009.35],PARAMETER[\\\"False_Northing\\\",-5526802.42],PARAMETER[\\\"Central_Meridian\\\",36.0],PARAMETER[\\\"Scale_Factor\\\",1.0],PARAMETER[\\\"Latitude_Of_Origin\\\",0.0],UNIT[\\\"Meter\\\",1.0]]\"}");
		map.add("outSR", "3857");
		map.add("transformation", "5823");
		map.add("transformForward", "true");
		map.add("f", "pjson");	
		map.add("geometries", coords);		
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.postForEntity( url, request , String.class );
		String content = response.getBody();		
		// parse response			
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode root = objectMapper.readTree(content);
		JsonNode geometryArray = root.get("geometries");
		Map<String, Pointko> result = new HashMap<>();
		int index = 0;
		for(final JsonNode point : geometryArray) {
			double x = point.get("x").asDouble();
			double y = point.get("y").asDouble();
			String hash = source.get(index++).toString();
			result.put(hash, new Pointko(x, y));
		}
		return result;
	}
	
	private List<DugSourceInfo> _loadDugGeometry() throws JsonParseException, JsonMappingException, IOException{
		List<DugSourceInfo> result = new ArrayList<>();
		ObjectMapper objectMapper = new ObjectMapper();
		URL url = new URL(DUG_URL);
		JsonNode root = objectMapper.readValue(url, JsonNode.class);
		JsonNode features = root.get("features");
		for(final JsonNode feature : features) {
			DugSourceInfo item = new DugSourceInfo();
			JsonNode attrubutes = feature.get("attributes");
			item.typko = attrubutes.get("TYPE").asInt();
			item.Id = attrubutes.get("ID").asInt();
			JsonNode rings = feature.get("geometry").get("rings").get(0);
			for(JsonNode ring : rings ) {
				double x = ring.get(0).asDouble();
				double y = ring.get(1).asDouble();					
				item.points.add(new Pointko(x, y));
			}
			result.add(item);
		}			
		return result;
	}
	
	private String _serializeToJson(Object source) {
		try {
			return new ObjectMapper().writeValueAsString(source);
		} catch (Exception e) {
			throw new RuntimeException("Cannot serialize to Json format!");
		}
	}
	
	private static class DugSourceInfo {		
		public int typko;
		public Integer Id;
		public List<Pointko> points = new ArrayList<>();
		public DugInfo dugInfo;
	}
	
	private static class Pointko {		
		
		public double x;
		public double y;
		
		public Pointko(double x, double y) {
			super();
			this.x = x;
			this.y = y;
		}
		
		public void relocate(Pointko pointko) {
			this.x = pointko.x;
			this.y = pointko.y;
			stringHash = null;
		}

		@Override
		public String toString() {
			if (stringHash == null) {				
				stringHash = String.format(Locale.ROOT, "%1$.12f, %2$.12f", this.x, this.y);
			}
			return stringHash;
		}		

		private String stringHash;
	}
}
