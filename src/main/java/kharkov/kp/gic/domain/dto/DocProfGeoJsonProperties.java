package kharkov.kp.gic.domain.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import kharkov.kp.gic.domain.geojson.LongLatCoordHolder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class DocProfGeoJsonProperties implements LongLatCoordHolder{

	@JsonProperty("id")
	private Integer id;
	
	@JsonProperty("cd") 
	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone="Europe/Kiev")
	//private Date cartDate;
	private String carDate;
	
	@JsonProperty("sd") 
	private int statusId;
		
	@JsonProperty("it") 
	private String indTema;
		
	@JsonProperty("cn")  
	private String cardNumber;
		
	@JsonProperty("tn") 
	private String themeName;		
	
    // Необходимо для имплементации LongLatCoordHolder
	private double longitude;
	
	private double latitude;

	@Override
	@JsonIgnore
	public double getLongitude() {
		return longitude;
	}

	@Override
	@JsonIgnore
	public double getLatitude() {
		return latitude;
	}
}
