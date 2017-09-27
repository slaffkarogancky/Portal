package kharkov.kp.gic.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kharkov.kp.gic.domain.AdvConstr;
import kharkov.kp.gic.domain.AdvBlob;
import kharkov.kp.gic.domain.AdvBlobInfo;
import kharkov.kp.gic.domain.AdvCoord;
import kharkov.kp.gic.domain.dto.AdvGeoJsonProperties;
import kharkov.kp.gic.domain.dto.AdvConverter;
import kharkov.kp.gic.domain.dto.GeoJsonPoint;
import kharkov.kp.gic.repository.AdvBlobInfoRepository;
import kharkov.kp.gic.repository.AdvBlobRepository;
import kharkov.kp.gic.repository.AdvCoordRepository;
import kharkov.kp.gic.repository.AdvConstrRepository;

@Service
public class AdvService {

	@Autowired
	private AdvConstrRepository advConstrRepository;
	
	@Autowired
	private AdvCoordRepository advCoordRepository;
	
	@Autowired
	private AdvBlobRepository advBlobRepository;	
	
	@Autowired
	private AdvBlobInfoRepository advBlobInfoRepository;
	
	private List<GeoJsonPoint<AdvGeoJsonProperties>> cache;
	
	@Transactional(readOnly = true)
	public List<GeoJsonPoint<AdvGeoJsonProperties>> getAdvertConstructionTree() {		
		if (cache == null) {
			List<AdvCoord> coords = advCoordRepository.findAll();
			List<AdvBlobInfo> blobs = advBlobInfoRepository.findAll();
			List<AdvConstr> constrs = advConstrRepository.findAll(); 
			cache = _getAdvTree(coords, blobs, constrs);
		}
		return cache;
	}
	
	@Transactional(readOnly = true)
	public byte[] getBlobById(int id) {
		AdvBlob blob = advBlobRepository.findOne(id);
		return blob == null ? null : blob.getJpegScan();
	}	
	
	private List<GeoJsonPoint<AdvGeoJsonProperties>> _getAdvTree(List<AdvCoord> coords, List<AdvBlobInfo> blobs, List<AdvConstr> constrs){
		// @formatter:off
		// Извлекаем координаты для рекламных конструкций
		Map<Integer, AdvCoord> _coords = coords.stream()
											   .collect(Collectors.toMap(AdvCoord::getConstrId, 
												   					     (c) -> c, 
												   					     (c1, c2) -> c1));		
		// Группируем блобы по конструкциям
		Map<Integer, List<Integer>> _blobs =blobs.stream()
												 .collect(Collectors.groupingBy(AdvBlobInfo::getConstructionId,
														  						Collectors.mapping(AdvBlobInfo::getId, Collectors.toList())));
		// формируем список рекламных конструкци в формате GeoJson
		return constrs.stream()
					  .filter(c -> _coords.containsKey(c.getConstructionId()))
					  .map(c -> {
						  AdvGeoJsonProperties props = AdvConverter.convert(c);
						  props.setBlobs(_blobs.get(props.getId()));
						  AdvCoord coord = _coords.get(props.getId());
						  return new GeoJsonPoint<AdvGeoJsonProperties>(coord.getLongitude(), coord.getLatitude(), props);
					  })
					  .collect(Collectors.toList());
		// @formatter:on
	}
}
