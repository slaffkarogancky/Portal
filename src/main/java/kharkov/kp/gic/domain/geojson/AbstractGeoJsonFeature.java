package kharkov.kp.gic.domain.geojson;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class AbstractGeoJsonFeature {

	@JsonProperty("type")
	public final String type = "Feature";
	
	@JsonProperty("properties")
	public final Object properties;
	
	public AbstractGeoJsonFeature(Object properties) {
		this.properties = properties;
	}
}
