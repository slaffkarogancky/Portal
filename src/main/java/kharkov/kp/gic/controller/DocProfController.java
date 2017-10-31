package kharkov.kp.gic.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kharkov.kp.gic.domain.geojson.GeoJsonFeatureCollection;
import kharkov.kp.gic.service.DocProfService;

@RestController
@CrossOrigin
@RequestMapping("/portal/api/v1/docprof")
public class DocProfController {

	@Autowired
	private DocProfService docProfService;
	
	// http://localhost:2018/portal/api/v1/docprof
	// http://localhost:2018/portal/api/v1/docprof?from=05-10-2017&to=05-10-2017
	@GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> getDocProfItems(
			@RequestParam(value="from", required = false) @DateTimeFormat(pattern="dd-MM-yyyy") Date from,
			@RequestParam(value="to", required = false) @DateTimeFormat(pattern="dd-MM-yyyy") Date to) {
		
		if (from == null) {
			// по умолчанию извлекаем данные за последнюю неделю
			to = new Date();
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_YEAR, -7);
			from = cal.getTime();			
		}
		GeoJsonFeatureCollection result = docProfService.loadDocProf(from, to);
		return new ResponseEntity<GeoJsonFeatureCollection>(result, HttpStatus.OK);
	}
	
}
