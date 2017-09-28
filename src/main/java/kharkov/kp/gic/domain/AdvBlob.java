package kharkov.kp.gic.domain;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;


import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "v_portal_blobs")
@Data @NoArgsConstructor 
public class AdvBlob {
	
	@Id
	@Column(name="id")
	protected Integer id;
	
	@Lob
	@Basic(fetch=FetchType.EAGER)	
	@Column(name="screen")
	private byte[] jpegScan;
}
