package com.sra.designpatterns.structural.adapter;

import com.sra.designpatterns.structural.adapter.classes.MicroUSB;
import com.sra.designpatterns.structural.adapter.interfaces.USB;

public class USBAdapter implements USB {

	private MicroUSB microUSB;

	public USBAdapter(MicroUSB microUSB) {
		this.microUSB = microUSB;
	}

	@Override
	public void connectedWithUSB() {
		this.microUSB.connectedWithMicroUSB();
	}

}
