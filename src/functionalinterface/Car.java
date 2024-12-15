package functionalinterface;

/* this is the functional interface,so don't add new abstract method*/ 

@FunctionalInterface
public interface Car {
	void drive();
//	void honk();
	
	default void honk() {
		
	}
	
	static void honk2() {
		
	}
	
	default void light() {
		
	}
	
	
	
	//java.util.function

}
