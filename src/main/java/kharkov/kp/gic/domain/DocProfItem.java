package kharkov.kp.gic.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "v_webdocprof_cards")
@Data @NoArgsConstructor 
public class DocProfItem {

	@Id
	@Column(name="cartid") 
	private Integer id;
	
	@Column(name="cartdate") 
	@Temporal(TemporalType.DATE)
	private Date cartDate;
	
	@Column(name="status")  
	private int statusId;
		
	@Column(name="indtema_vk")  
	private String indTema;
		
	@Column(name="card_number")  
	private String cardNumber;
		
	@Column(name="theme_name")  	
	private String themeName;
		
	@Column(name="adr_x") 
	private double x;	
		
	@Column(name="adr_y") 
	private double y;
	
}
