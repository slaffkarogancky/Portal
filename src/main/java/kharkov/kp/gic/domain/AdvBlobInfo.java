package kharkov.kp.gic.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "v_portal_blobs")
@Data @NoArgsConstructor 
public class AdvBlobInfo {

	@Id
	@Column(name="id")
	protected Integer id;
	
	@Column(name="doc_id")
	protected int constructionId;	

}
