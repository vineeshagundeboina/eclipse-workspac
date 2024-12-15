package com.sra.designpatterns.structural.adapter;

import com.sra.designpatterns.structural.adapter.classes.MicroUSB;

public class ConnectUSB {

	public static void main(String args[]) {

		USBAdapter usbAdapter = new USBAdapter(new MicroUSB());
		System.out.println("i need big pin to charge my mobile but i have small pin so with adapter needs to connect");
		System.out.print("connected mobile with microusb >>>>");
		usbAdapter.connectedWithUSB();
	}
}
