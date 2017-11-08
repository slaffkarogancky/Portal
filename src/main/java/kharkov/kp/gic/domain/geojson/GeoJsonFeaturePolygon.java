package kharkov.kp.gic.domain.geojson;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GeoJsonFeaturePolygon extends AbstractGeoJsonFeature {

	@JsonProperty("geometry")
	public final GeoJsonPolygonGeometry geometry;
	
	public GeoJsonFeaturePolygon(double[][][] coordinates, Object properties) {
		super(properties);
		this.geometry = new GeoJsonPolygonGeometry(coordinates);
	}
	
	public static class GeoJsonPolygonGeometry {

		@JsonProperty("type")
		public final String type = "Polygon";
		
		@JsonProperty("coordinates")
		public final double[][][] coordinates;
		
		public GeoJsonPolygonGeometry(double[][][] coordinates) {
			this.coordinates = coordinates;
		}
	}
	
}
