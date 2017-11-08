package kharkov.kp.gic.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import kharkov.kp.gic.domain.AdvConstr;
import kharkov.kp.gic.domain.AdvBlob;
import kharkov.kp.gic.domain.AdvBlobInfo;
import kharkov.kp.gic.domain.AdvCoord;
import kharkov.kp.gic.domain.dto.AdvGeoJsonProperties;
import kharkov.kp.gic.domain.geojson.GeoJsonFeatureCollection;
import kharkov.kp.gic.domain.geojson.GeoJsonFeaturePoint;
import kharkov.kp.gic.domain.dto.AdvConverter;
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
	
	//@Autowired
	//private StringRedisTemplate _template;
	
	//private final String ADVERTIZE_TREE_REDIS_KEY = "advertize:tree";
	
	private String advertCache; // временное решение, чтобы не трахаться каждый раз с Redis
	
	@Transactional(readOnly = true)
	public String getAdvertiseGeoJsonCollection() {				
		//String resultTree = _template.opsForValue().get(ADVERTIZE_TREE_REDIS_KEY);	//_template.boundValueOps(ADVERTIZE_TREE_REDIS_KEY).get();
		//if (resultTree == null) {
		if (advertCache == null) {
			// Извлекаем из базы данных список рекламных конструкций, их координаты и блобы
			List<AdvConstr> constrs = advConstrRepository.findAll(); 
			List<AdvCoord> coords = advCoordRepository.findAll();
			List<AdvBlobInfo> blobs = advBlobInfoRepository.findAll();
			// формируем дерево конструкций
			List<AdvGeoJsonProperties> tree = _createTree(coords, blobs, constrs);
			// преобразовываем его в GeoJson Features point
			List<GeoJsonFeaturePoint> features = tree.stream()
													 .map(c -> new GeoJsonFeaturePoint(c.getLongitude(), c.getLatitude(), c))
													 .collect(Collectors.toList());
			// формируем коллекцию в формате GeoJson
			GeoJsonFeatureCollection geoJsonCollection = new GeoJsonFeatureCollection("EPSG:3857", features);
			// и трансформируем ее в строку
			
			advertCache = _serializeToJson(geoJsonCollection);
			
			//resultTree = _serializeToJson(geoJsonCollection);
			// сохраняем в Redis
			//_template.opsForValue().set(ADVERTIZE_TREE_REDIS_KEY, resultTree);
		}
		//return resultTree;
		return advertCache;
	}
	
	@Transactional(readOnly = true)
	public byte[] getBlobById(int id) {
		AdvBlob blob = advBlobRepository.findOne(id);
		return blob == null ? null : blob.getJpegScan();
	}	
	
	private String _serializeToJson(Object source) {
		try {
			return new ObjectMapper().writeValueAsString(source);
		} catch (Exception e) {
			throw new RuntimeException("Cannot serialize to Json format!");
		}
	}
	
	private List<AdvGeoJsonProperties> _createTree(List<AdvCoord> coords, List<AdvBlobInfo> blobs, List<AdvConstr> constrs){
		// @formatter:off
		// Извлекаем координаты для для каждой рекламной конструкции 
		Map<Integer, AdvCoord> _coords = coords.stream()
											   .collect(Collectors.toMap(AdvCoord::getConstrId, 
												   					     (c) -> c, 
												   					     (c1, c2) -> c1));		
		// Группируем блобы по рекламным конструкциям
		Map<Integer, List<Integer>> _blobs = blobs.stream()
												  .collect(Collectors.groupingBy(AdvBlobInfo::getConstructionId,
														  						Collectors.mapping(AdvBlobInfo::getId, Collectors.toList())));
		// формируем список рекламных конструкций 
		return constrs.stream()
					  .filter(c -> _coords.containsKey(c.getConstructionId()))
					  .map(c -> {
						  AdvCoord coord = _coords.get(c.getConstructionId());
						  List<Integer> currBlobs = _blobs.get(c.getConstructionId());
						  return AdvConverter.convert(c, coord.getLongitude(), coord.getLatitude(), currBlobs);
					  })
					  .collect(Collectors.toList());
		// @formatter:on
	}

}
