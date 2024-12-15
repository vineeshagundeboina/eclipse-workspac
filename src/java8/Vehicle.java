package java8;


@FunctionalInterface
public interface Vehicle {
	
	public void drive();
	
	public default void speedUp() {
		System.out.println("speedup  method is called from interface");
	}
	
	static void speedDown() {
		System.out.println("static method is called from interface");
	}
	

}
