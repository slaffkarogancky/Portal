package kharkov.kp.gic.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class DugGeoJsonProperties {

	@JsonProperty("id")	
	private int id;	
	
	@JsonProperty("tt")	
	private int typko;
	
	@JsonProperty("w") 
	private String work;		
	
	@JsonProperty("std") 
	private String startDate;
	
	@JsonProperty("o") 
	private String owner;	
	
	@JsonProperty("a") 
	private String address;		
	
	@JsonProperty("ad") 
	private String addressDesc;	
	
	@JsonProperty("v") 
	private String volume;		

	@JsonProperty("spd") 
	private String stopDate;	
	
	@JsonProperty("n") 
	private String name;
	
	@JsonProperty("nr") 
	private String resolutionNumber;
	
	@JsonProperty("rd") 
	private String resolutionDate;	

}
