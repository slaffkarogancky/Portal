package kharkov.kp.gic.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "V_DUGS")
@Data @NoArgsConstructor 
public class DugInfo {	
	
	@Id
	@Column(name="dug_plan_id")	
	private Integer id;	
	
	@Column(name="name_work") 
	private String work;		
	
	@Column(name="date_start_work") 
	private String startDate;
	
	@Column(name="owner") 
	private String owner;	
	
	@Column(name="address") 
	private String address;		
	
	@Column(name="address_description") 
	private String addressDesc;	
	
	@Column(name="volume_dug") 
	private String volume;		
	
	@Column(name="is_carriageway") 
	private boolean  is_carriageway;	
	
	@Column(name="is_otmostka") 
	private boolean  isOtmostka;	
	
	@Column(name="is_sidewalk") 
	private boolean  isSidewalk;		
	
	@Column(name="is_internal_road") 
	private boolean  isInternalRoad;	
	
	@Column(name="is_green_zone") 
	private boolean  isGreenZone;	
	
	@Column(name="is_house_zone") 
	private boolean  isHouseZone;	
	
	@Column(name="is_adjacent_house_zone") 
	private boolean isAdjacentHouseZone;		
	
	@Column(name="is_industrialzone") 
	private boolean  isIndustrialzone;	
	
	@Column(name="is_manhole") 
	private boolean isManhole;		
	
	@Column(name="is_dirt_road") 
	private boolean isDirtRoad;	
	
	@Column(name="date_stop_work") 
	private String stopDate;	
	
	@Column(name="name") 
	private String name;
	
	@Column(name="number_resolution") 
	private String resolutionNumber;
	
	@Column(name="date_resolution") 
	private String resolutionDate;	

}
