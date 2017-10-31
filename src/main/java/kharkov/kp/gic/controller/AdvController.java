package kharkov.kp.gic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//import kharkov.kp.gic.domain.geojson.GeoJsonFeatureCollection;
import kharkov.kp.gic.service.AdvService;

@RestController
@CrossOrigin
@RequestMapping("/portal/api/v1/advertize")
public class AdvController {

	@Autowired
	private AdvService advertConstructionService;

	// http://localhost:2018/portal/api/v1/advertize/blob/173552
	@GetMapping(value = "/blob/{blobid}")
	public ResponseEntity<?> getAdvertConstructionBlob(@PathVariable int blobid) {
		byte[] blob = advertConstructionService.getBlobById(blobid);
		if (blob == null) {
			new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);
		headers.set("Content-Disposition", "inline; filename=scan");
		headers.setContentLength(blob.length);
		return new ResponseEntity<byte[]>(blob, headers, HttpStatus.OK);
	}

	// http://localhost:2018/portal/api/v1/advertize
	@GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> getAllDepartments() {
		String content = advertConstructionService.getAdvertiseGeoJsonCollection();
		return new ResponseEntity<String>(content, HttpStatus.OK);
	}
}
