package kharkov.kp.gic.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "v_portal_advert_constraction")
@Data @NoArgsConstructor 
public class AdvConstr {

	@Id
	@Column(name="constr_id")
	private Integer constructionId;
	
	@Column(name="cust_long") 
	private String customer;
	
	@Column(name="cust_tax_number") 
	private String customerCode;
	 
	@Column(name="cust_email") 
	private String email;
	
	@Column(name="cust_phone") 
	private String phone;
	
	@Column(name="address_full")
	private String address;
	
	@Column(name="cro_address_full")
	private String crossAddress;
	
	@Column(name="ct_id") 
	private int constructionTypeId;
	
	@Column(name="sizes") 
	private String sizes;
	
	@Column(name="permit_num") 
	private Long permintNumber;
	
	@Column(name="permit_valid") 
	private Date permitUntill;
	
}
