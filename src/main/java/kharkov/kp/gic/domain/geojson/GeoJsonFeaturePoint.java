package kharkov.kp.gic.domain.geojson;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GeoJsonFeaturePoint {

	@JsonProperty("type")
	public final String type = "Feature";
	
	@JsonProperty("geometry")
	public final GeoJsonPointGeometry geometry;
	
	@JsonProperty("properties")
	public final Object properties;

	public GeoJsonFeaturePoint(double longitude, double latitude, Object properties) {
		this.geometry = new GeoJsonPointGeometry(longitude, latitude);
		this.properties = properties;
	}
	
	public static class GeoJsonPointGeometry {

		@JsonProperty("type")
		public final String type = "Point";
		
		@JsonProperty("coordinates")
		public final double[] coordinates;
		
		public GeoJsonPointGeometry(double longitude, double latitude) {
			coordinates = new double[] {longitude, latitude};
		}
	}
	
}
