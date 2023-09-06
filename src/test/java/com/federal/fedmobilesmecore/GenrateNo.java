package com.federal.fedmobilesmecore;

import java.security.SecureRandom;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.format.datetime.joda.DateTimeFormatterFactory;

public class GenrateNo {

	public static void main(String[] args) {
		String refNo = null;
		final ZonedDateTime input = ZonedDateTime.now();
		String prefix = "SMEFB";
		DateTimeFormatter europeanDateFormatter = DateTimeFormatter.ofPattern("yy");

		String year = input.format(europeanDateFormatter);
		String yearofday = String.valueOf(input.getDayOfYear());
		int hexint = new SecureRandom().nextInt();

		refNo = prefix.concat(year).concat(yearofday).concat(Integer.toHexString(hexint));
		System.out.println(prefix + ":" + year + ":" + yearofday + ":" + Integer.toHexString(hexint) + ":" + refNo);
		String generatedString = RandomStringUtils.randomAlphanumeric(6);

		System.out.println(generatedString);

		int length = 3;
		boolean useLetters = true;
		boolean useNumbers = false;
		String generatedString2 = RandomStringUtils.random(length, useLetters, useNumbers);

		System.out.println(generatedString2);

		String zeros = "000000";
		Random rnd = new Random();
		String s = Integer.toString(rnd.nextInt(0X1000000), 16);
		s = zeros.substring(s.length()) + s;
		System.out.println("s = " + s);

	}

}
