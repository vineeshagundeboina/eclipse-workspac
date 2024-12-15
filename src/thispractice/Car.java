package thispractice;

import java.util.Arrays;

public interface Car {
	
	String companyName="Jaguar";
	
	public void drive();
	
	default void headLight() {
		System.out.println(Arrays.toString(this.getClass().getDeclaredMethods()));
		System.out.println(this.getClass().getName());

	}

}
