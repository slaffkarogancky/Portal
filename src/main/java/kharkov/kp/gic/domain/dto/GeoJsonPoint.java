package kharkov.kp.gic.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GeoJsonPoint<T> {

	@JsonProperty("type")
	public final String type = "Feature";
	
	@JsonProperty("geometry")
	public final GeoJsonPointGeometry geometry;
	
	@JsonProperty("properties")
	public final T properties;

	public GeoJsonPoint(double longitude, double latitude, T properties) {
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
