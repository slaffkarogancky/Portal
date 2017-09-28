package kharkov.kp.gic.domain.dto;

import java.util.Date;
import java.util.List;

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
public class AdvGeoJsonProperties implements LongLatCoordHolder{

	@JsonProperty("id")
	private Integer id;
	
	// наименование заказчика
	@JsonProperty("ctm") 
	private String customer;
	
	// код заказчика
	@JsonProperty("tx") 
	private String customerCode;
	 
	// E-mail заказчика
	@JsonProperty("em") 
	private String email;
	
	// телефон заказчика
	@JsonProperty("ph") 
	private String phone;
	
	// адрес конструкции
	@JsonProperty("adr")
	private String address;
	
	// адрес пересечения конструкции
	@JsonProperty("cadr")
	private String crossAddress;
	
	// тип конструкции
	@JsonProperty("ct") 
	private int constructionTypeId;
	
	// глобальный тип конструкции
	@JsonProperty("gt") 
	private int globalTypeId;
	
	// размер конструкции
	@JsonProperty("sz") 
	private String sizes;
	
	// номер разрешения
	@JsonProperty("pm") 
	private Long permintNumber;
	
	// дата разрешения
	@JsonProperty("pmd") 
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date permitUntill;
	
	@JsonProperty("bs") 
	private List<Integer> blobs;
	
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
