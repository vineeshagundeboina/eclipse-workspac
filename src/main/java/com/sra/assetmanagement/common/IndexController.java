package com.sra.assetmanagement.common;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("")
@CrossOrigin(origins ="*")
@RestController
public class IndexController {
	
	
	@GetMapping("/")
	public String index() {
		return "welcome to assetmanagement";
	}

}
