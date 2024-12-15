package lambdaexpressions;

//public class ClassA {
//
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//
//		Audi a=new Audi();
//		a.drive();
//		
//		//Anonymous class
//		Car c=new Car() {
//
//			@Override
//			public void drive() {
//				// TODO Auto-generated method stub
//				System.out.println("BMW is driving");
//			}
//			
//		};
//		
//		c.drive();
//		
//		//lambda expression with anonymous function
//		
////		Car c2=()->{System.out.println("driving TATA");};
//		
////		Car c2=()->System.out.println("driving TATA");
//		
//		Car c2=()->{
//			int speed=50;
//			System.out.println("driving TATA");
//			System.out.println("car is driving smoothly at speed "+speed);
//			if(speed>100) {
//				System.out.println("fast driving");
//			}
//			else {
//				System.out.println("slow driving");
//			}
//			};
//		c2.drive();
//		
//		
//	}
//
//}
//
//
//class Audi implements Car{
//
//	@Override
//	public void drive() {
//		// TODO Auto-generated method stub
//		System.out.println("audi is driving");
//	}
//	
//}
//
//@FunctionalInterface
// interface Car{
//	
//	public void drive();
//	
//}








/* lambda expression with two parameters */


//public class ClassA {
//
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//
//		Audi a=new Audi();
//		a.drive(60,"Audi");
//		
//		//Anonymous class
//		Car c=new Car() {
//
//			@Override
//			public void drive(int s,String model) {
//				// TODO Auto-generated method stub
//				System.out.println("BMW is driving : "+model);
//			}
//			
//		};
//		
//		c.drive(60,"BMW");
//		
//		//lambda expression with anonymous function
//		
////		Car c2=()->{System.out.println("driving TATA");};
//		
////		Car c2=()->System.out.println("driving TATA");
//		
//		Car c2=(speed,model)->{
//			//int speed=50;
//			System.out.println("driving TATA");
//			System.out.println("car is driving smoothly at speed "+speed+"model : "+model);
//			if(speed>100) {
//				System.out.println("fast driving");
//			}
//			else {
//				System.out.println("slow driving");
//			}
//			};
//		c2.drive(30,"Tata");
//		
//		
//	}
//
//}
//
//
//class Audi implements Car{
//
//	@Override
//	public void drive(int s,String model) {
//		// TODO Auto-generated method stub
//		System.out.println("audi is driving : "+model);
//	}
//	
//}
//
//@FunctionalInterface
// interface Car{
//	
//	public void drive(int speed,String model);
//	
//}












/* lambda expression with return type */







public class ClassA {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Audi a=new Audi();

		System.out.println(a.getSpeed());
		//Anonymous class
		Car c=new Car() {

			@Override
			public int getSpeed() {
				// TODO Auto-generated method stub
				System.out.println("driving BMW");
				return 50;
			}
			
		};
		

		System.out.println(c.getSpeed());
		//lambda expression with anonymous function
		
//		Car c2=()->{System.out.println("driving TATA");};
		
//		Car c2=()->System.out.println("driving TATA");
		
		Car c2=()->{
			int speed=100;
			System.out.println("driving TATA");
			System.out.println("car is driving smoothly at speed "+speed);
			if(speed>100) {
				System.out.println("fast driving");
			}
			else {
				System.out.println("slow driving");
			}
			
			return speed;
			};
			System.out.println(c2.getSpeed());
		
		
		
	}

}


class Audi implements Car{

	@Override
	public int getSpeed() {
		// TODO Auto-generated method stub
		System.out.println("driving audi");
		
		return 50;
	}
	
}

@FunctionalInterface
 interface Car{
	
	public int getSpeed();
	
}