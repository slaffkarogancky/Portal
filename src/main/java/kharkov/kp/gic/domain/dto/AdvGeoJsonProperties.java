package kharkov.kp.gic.domain.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class AdvGeoJsonProperties {

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
	
	public void setBlobs(List<Integer> _blobs) {
		if ((_blobs != null) && (_blobs.size() > 0)) {
			this.blobs = _blobs;
		}
	}
}
