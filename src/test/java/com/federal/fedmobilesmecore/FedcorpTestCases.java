package com.federal.fedmobilesmecore;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

public class FedcorpTestCases {

	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddhhmmssSS");

	@Test
	public void testrandomnumber() {
		String currentDate = simpleDateFormat.format(new Date()).toString();
		System.out.println(currentDate);
		System.out.println(currentDate.length());
	}

}
