package java8;

public class Audi implements Vehicle{

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Audi a=new Audi();
		
		a.drive();
		a.speedUp();
		Vehicle.speedDown();
		a.speedDown();
	}

	@Override
	public void drive() {
		// TODO Auto-generated method stub
		System.out.println("drive");
	}
	
	 public void speedUp() {
		System.out.println("speedup from class");
	}

	 static void speedDown() {
		System.out.println("static method from class");
	}
}
