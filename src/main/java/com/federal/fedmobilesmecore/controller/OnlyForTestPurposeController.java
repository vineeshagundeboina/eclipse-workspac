package com.federal.fedmobilesmecore.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.federal.fedmobilesmecore.dto.FundTransfer;
import com.federal.fedmobilesmecore.repository.FundTransferRepository;

@RestController
@RequestMapping(path = "/core/test321")
public class OnlyForTestPurposeController {

	@Autowired
	FundTransferRepository fundTransferRepository;

	@PostMapping
	public ResponseEntity<?> testAppicationUserRepo() {
		List<FundTransfer> fundTransfer = null;
		fundTransfer = fundTransferRepository.findDuplicateFTRecordsLessThan5Min("neft", "1", "error",
				new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()));
		return ResponseEntity.ok(fundTransfer);
	}
}