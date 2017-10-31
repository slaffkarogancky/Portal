package kharkov.kp.gic.service;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kharkov.kp.gic.domain.DocProfItem;
import kharkov.kp.gic.domain.dto.DocProfConverter;
import kharkov.kp.gic.domain.geojson.GeoJsonFeatureCollection;
import kharkov.kp.gic.domain.geojson.GeoJsonFeaturePoint;
import kharkov.kp.gic.repository.DocProfRepository;

@Service
public class DocProfService {

	@Autowired
	private DocProfRepository docProfRepository;	
	
	private Date _getDay(int daysAgo) {
		Calendar cal = Calendar.getInstance();
		if (daysAgo != 0) {
			cal.add(Calendar.DAY_OF_YEAR, daysAgo);
		}
		cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
	}
	
	private List<DocProfItem> last35DaysCache;
	private Date lastCached;

	// Позволяет извлекать только за последние 35 дней!!!
	@Transactional(readOnly = true)
	private List<DocProfItem> _loadFromCacheExt(Date from, Date to)
	{
		// проверяем входные параметры
		if ((from == null) || (to == null) || from.after(to)) {
			return new LinkedList<>() ;
		}
		Date today = _getDay(0);	
		// обновляем кэш за последние 35 дней, если нужно
		if (last35DaysCache == null || (lastCached.before(today))){ 		
			List<DocProfItem> buffer = docProfRepository.loadDocProfRepository(_getDay(-35), _getDay(-1));
			lastCached = today;
			last35DaysCache = new LinkedList<>(buffer);
		}
		// Формрируем результат
		List<DocProfItem> result = new LinkedList<>(); 
		for(DocProfItem item : last35DaysCache) {
			Date itemDate = item.getCartDate();
			if ((itemDate.equals(from)|| itemDate.after(from)) 	&&  (itemDate.equals(to)|| itemDate.before(to))) {
				result.add(item);
			}
		}
		// извлекаем, если надо, результаты за сегодня
		if (to.equals(today) || to.after(today)) {
			List<DocProfItem> todays = docProfRepository.loadDocProfRepository(today, today);
			result.addAll(todays);
		}		
		return result;
	}

	@Transactional(readOnly = true)
	public GeoJsonFeatureCollection loadDocProf(Date from, Date to) {	
		// получаем список из кэша, согласно заданному временному диапазону
		List<DocProfItem> list = _loadFromCacheExt(from, to);
		// преобразовываем его в GeoJson Features point
		List<GeoJsonFeaturePoint> features = list.stream()
											 	  .map(DocProfConverter::convert)
											 	  .map(c -> new GeoJsonFeaturePoint(c.getLongitude(), c.getLatitude(), c))
											 	  .collect(Collectors.toList());
		// формируем коллекцию в формате GeoJson
		return new GeoJsonFeatureCollection("EPSG:3857", features);
	}
}
