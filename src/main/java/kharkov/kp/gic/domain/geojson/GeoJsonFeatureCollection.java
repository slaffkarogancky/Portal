package kharkov.kp.gic.domain.geojson;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GeoJsonFeatureCollection {
	
	@JsonProperty("type")
	public final String type = "FeatureCollection";
	
	@JsonProperty("crs")
	public final Crs crs ;
	
	@JsonProperty("features")
	public final List<?> features;
	
	public GeoJsonFeatureCollection(String coordinatesSystem, List<?> features) {
		this.crs = new Crs(coordinatesSystem);
		this.features = features;
	}
	
	public static class Crs {

		@JsonProperty("type")
		public final String type = "name";
		
		@JsonProperty("properties")
		public final CrsProperties properties;
		
		public Crs(String name) {
			this.properties = new CrsProperties(name);
		}
	}
	
	public static class CrsProperties {
		
		@JsonProperty("name")
		public final String type;

		public CrsProperties(String coordinatesSystem) {
			super();
			this.type = coordinatesSystem;
		}		
	}	
}
