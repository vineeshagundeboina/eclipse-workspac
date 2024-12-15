package functionalinterface;

public class User {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Car c=new Car() {
			public void drive() {
				System.out.println("BMW drive is called");
			}
		};
		c.drive();
	}

}
