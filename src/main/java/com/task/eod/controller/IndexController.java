package com.task.eod.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins="*")
public class IndexController {
	@GetMapping("/")
	private String getHomePage() {
		return "welcome to EOD application";
	}
	

}
