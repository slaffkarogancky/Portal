package kharkov.kp.gic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kharkov.kp.gic.service.KharkovDugService;

@RestController
@CrossOrigin
@RequestMapping("/portal/api/v1/dug")
public class KharkovDugController {

	@Autowired
	private KharkovDugService kharkovDugService;
	
	// http://localhost:2018/portal/api/v1/dug?updatecache=true
	@GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> getKharkovDug(@RequestParam(value="updatecache", defaultValue = "false", required = false) boolean updatecache) {
		String content = kharkovDugService.getDugs(updatecache);
		return new ResponseEntity<String>(content, HttpStatus.OK);
	}
	
}
