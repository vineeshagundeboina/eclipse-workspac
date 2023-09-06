package com.federal.fedmobilesmecore.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.federal.fedmobilesmecore.config.RecordLog;
import com.federal.fedmobilesmecore.model.BlockModeReq;
import com.federal.fedmobilesmecore.model.BlockModeResponse;
import com.federal.fedmobilesmecore.service.BlockModeService;

@RestController
@RequestMapping(path = "/core/api")
public class BlockModeController {
	private static final Logger log4j = LogManager.getLogger(BlockModeController.class);
	@Autowired
	BlockModeService blockModeService;

	@PostMapping(path = "/blockmode", consumes = "Application/json", produces = "Application/json")
	public ResponseEntity<?> blockMode(@RequestBody BlockModeReq request) {
		RecordLog.writeLogFile("BlockModeController /blockmode api is calling. API request: "+request);
		BlockModeResponse response = null;
		log4j.info("Block Mode API INPUT: " + request.toString());
		response = blockModeService.blockMode(request);
		log4j.info("Block Mode API OUTPUT: " + response.toString());
		RecordLog.writeLogFile("BlockModeController /blockmode api is completed. API response: "+response);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
