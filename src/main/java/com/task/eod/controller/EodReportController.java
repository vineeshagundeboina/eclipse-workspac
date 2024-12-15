package com.task.eod.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.task.eod.repository.TaskRepo;

@RestController
@CrossOrigin(origins="*")
@RequestMapping("/api/eodreport")
public class EodReportController {
	
	@Autowired
	private TaskRepo taskRepo;
	
	//send report daily basis
	@GetMapping("/senddailyeod")
	private List<?> sendDailyEod(@RequestParam("eodDate") String date){
		System.out.println("date:"+date);
		return taskRepo.findByEodDate(date);
	}
	
	//send report weekly basis
	@GetMapping("/sendweekeod")
	private List<?> sendWeeklyEod(@PathVariable String date){
		
		return taskRepo.findByEodDate(date);
	}

}
