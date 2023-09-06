package com.federal.fedmobilesmecore.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.federal.fedmobilesmecore.dto.BankIfsc;
import com.federal.fedmobilesmecore.model.IfscRespModel;
import com.federal.fedmobilesmecore.repository.IfscRepository;

@Service
public class IfscService {
	@Autowired
	IfscRepository ifscRepository;

	public IfscRespModel searchByIfscCode(String getIfscCode) {
		Optional<BankIfsc> bankIfsc = null;
		bankIfsc = ifscRepository.findByIfscCode(getIfscCode);
		IfscRespModel setIfscRespModel = new IfscRespModel();
		
		if(bankIfsc.isPresent()) {
			setIfscRespModel.setDescription("success");
			setIfscRespModel.setStatus(true);
			setIfscRespModel.setResult(null);
			setIfscRespModel.setBankIfsc(bankIfsc.get());
		}else {
			setIfscRespModel.setDescription("failed - no data found");
			setIfscRespModel.setStatus(false);
			setIfscRespModel.setResult(null);
			setIfscRespModel.setBankIfsc(null);
		}
		
		return setIfscRespModel;
	}

	public IfscRespModel getAllBankName() {
		List<String> result = ifscRepository.getAllBankName();
		IfscRespModel setIfscRespModel = new IfscRespModel();
		if (result.size() > 0) {
			setIfscRespModel.setDescription("success");
			setIfscRespModel.setStatus(true);
			setIfscRespModel.setResult(result);
		} else {
			setIfscRespModel.setDescription("failed - no data found");
			setIfscRespModel.setStatus(false);
			setIfscRespModel.setResult(null);
		}
		return setIfscRespModel;
	}

	public IfscRespModel getStateByBankName(String bankName) {
		List<String> result = ifscRepository.findStateByBankName(bankName);
		IfscRespModel setIfscRespModel = new IfscRespModel();
		if (result.size() > 0) {
			setIfscRespModel.setDescription("success");
			setIfscRespModel.setStatus(true);
			setIfscRespModel.setResult(result);
		} else {
			setIfscRespModel.setDescription("failed - no data found");
			setIfscRespModel.setStatus(false);
			setIfscRespModel.setResult(null);
		}
		return setIfscRespModel;
	}

	public IfscRespModel getDistrictByBankNameAndState(String bankName, String state) {
		List<String> result = ifscRepository.findDistrictByBankNameAndState(bankName, state);
		IfscRespModel setIfscRespModel = new IfscRespModel();
		if (result.size() > 0) {
			setIfscRespModel.setDescription("success");
			setIfscRespModel.setStatus(true);
			setIfscRespModel.setResult(result);
		} else {
			setIfscRespModel.setDescription("failed - no data found");
			setIfscRespModel.setStatus(false);
			setIfscRespModel.setResult(null);
		}
		return setIfscRespModel;
	}

	public IfscRespModel getCityByBankNameAndStateAndDistrict(String bankName, String state, String district) {
		List<String> result = ifscRepository.findCityByBankNameAndStateAndDistrict(bankName, state, district);
		IfscRespModel setIfscRespModel = new IfscRespModel();
		if (result.size() > 0) {
			setIfscRespModel.setDescription("success");
			setIfscRespModel.setStatus(true);
			setIfscRespModel.setResult(result);
		} else {
			setIfscRespModel.setDescription("failed - no data found");
			setIfscRespModel.setStatus(false);
			setIfscRespModel.setResult(null);
		}
		return setIfscRespModel;
	}

	public IfscRespModel getBranchByBankNameAndStateAndDistrictAndCity(String bankName, String state,
			String district, String cityName) {
		List<String> result = ifscRepository.findBranchByBankNameAndStateAndDistrictAndCity(bankName, state,
				district, cityName);
		IfscRespModel setIfscRespModel = new IfscRespModel();
		if (result.size() > 0) {
			setIfscRespModel.setDescription("success");
			setIfscRespModel.setStatus(true);
			setIfscRespModel.setResult(result);
		} else {
			setIfscRespModel.setDescription("failed - no data found");
			setIfscRespModel.setStatus(false);
			setIfscRespModel.setResult(null);
		}
		return setIfscRespModel;
	}

	public IfscRespModel getIfscCodeByBranchName(String bankName,String state, String district, String city,String branchName) {
		List<String> getBankName = ifscRepository.findIfscCodeByBranch(bankName, state, district, city, branchName);
		IfscRespModel setIfscRespModel = new IfscRespModel();
		if (getBankName.size() > 0) {
			setIfscRespModel.setDescription("success");
			setIfscRespModel.setStatus(true);
			setIfscRespModel.setResult(getBankName);
		} else {
			setIfscRespModel.setDescription("failed - no data found");
			setIfscRespModel.setStatus(false);
			setIfscRespModel.setResult(null);
		}

		return setIfscRespModel;
	}
}