package kharkov.kp.gic.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kharkov.kp.gic.domain.DocProfItem;

public interface DocProfRepository extends JpaRepository<DocProfItem, Integer>{
	
	@Query("select i from DocProfItem i where i.cartDate >= :from and i.cartDate <= :until")
	List<DocProfItem> loadDocProfRepository(@Param("from") Date from, @Param("until") Date until);

}
// "в процесі", "виконано", "закрито"