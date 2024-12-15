package com.sra.designpatterns.creational.builder;

import com.sra.designpatterns.creational.builder.classes.Phone;

public class MobileShop {

	public static void main(String[] args) {
		PhoneBuilder phoneBuilder = new PhoneBuilder();

		// 1st customer asked us to build/make iphone12 with ram-4gb and battery 5000mh
		Phone iphone12 = phoneBuilder.setBattery("5000mh").setRam("4b").setName("iphone12").buildPhone();
		System.out.println("iphone12 has made as per customer request>>> " + iphone12.toString());

		// 2st customer asked us to build/make iphone14 with storage-1TB and model
		// IP1200

		Phone iphone14 = phoneBuilder.setStorage("1TB").setModel("IP1200").setName("iphone14").buildPhone();
		System.out.println("iphone14 has made as per customer request>>> " + iphone14.toString());

	}

}
