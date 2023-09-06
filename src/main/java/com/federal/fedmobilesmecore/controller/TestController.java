package com.federal.fedmobilesmecore.controller;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.federal.fedmobilesmecore.config.GlobalProperties;
import com.federal.fedmobilesmecore.config.RecordLog;
import com.federal.fedmobilesmecore.security.EncryptDecrypt;

@RestController
@RequestMapping(path = "/core")
public class TestController {

	private static final Logger log4j = LogManager.getLogger(TestController.class);

	@Autowired
	GlobalProperties messages;

	@Autowired
	EncryptDecrypt util;

	@GetMapping("/test")
	public ResponseEntity<?> get() {
		log4j.info("test:");
		TestVO test = new TestVO();
		test.setKey("Name");
		test.setValue("Vikas");
		return ResponseEntity.ok(test);
	}

	@PostMapping(path = "/encrypt", consumes = "text/plain")
	public String get(@RequestBody String data) throws InvalidKeyException, JSONException, SecurityException,
			NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException, IOException {
		try {
			String result = util.Encryption(data);
			RecordLog.writeLogFile(result);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@PostMapping(path = "/decrypt", consumes = "text/plain")
	public String decrypt(@RequestBody String data) throws InvalidKeyException, JSONException, SecurityException,
			NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException, IOException {
		try {
			String decrypresult = util.Decryption(data);
			return decrypresult;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private class TestVO {
		private String key;
		private String value;

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getEnclosingInstance().hashCode();
			result = prime * result + ((key == null) ? 0 : key.hashCode());
			result = prime * result + ((value == null) ? 0 : value.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			TestVO other = (TestVO) obj;
			if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
				return false;
			if (key == null) {
				if (other.key != null)
					return false;
			} else if (!key.equals(other.key))
				return false;
			if (value == null) {
				if (other.value != null)
					return false;
			} else if (!value.equals(other.value))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "TestVO [key=" + key + ", value=" + value + "]";
		}

		private TestController getEnclosingInstance() {
			return TestController.this;
		}

	}

}
