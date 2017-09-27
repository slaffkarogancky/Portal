package kharkov.kp.gic.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "v_portal_coordinates")
@Data @NoArgsConstructor 
public class AdvCoord {

	@Id
	@Column(name="advert_id")
	private Integer constrId;
	
	@Column(name="x")
	private double longitude;
	
	@Column(name="y")
	private double latitude;
}
