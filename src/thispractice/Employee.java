package thispractice;

public class Employee {

	int age=20;
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//
//		Employee e=new Employee();
//		e.setAge(132,e);
//		System.out.println(e.getAge());
//	}
//
//	
////	public void setAge(int age) {
////		age=age;
////	}
//	
//	
////	public void setAge(int age) {
////		new Employee().age=age;
////	}
//	
//	public void setAge(int age,Employee e) {
//		e.age=age;
//	}
//	
//	public int getAge() {
//		return age;
//	}
	
	
	
	
//	public static void main(String[] args) {
//		
//		Employee e=new Employee();
//		e.setAge(40);
//		e.setAge(50);
//		System.out.println(e.getAge());
//		
//	}
//	
//	public void setAge(int age) {
//		this.age=age;
//	}
//	
//	public int getAge() {
//		return age;
//	}
	
	
	
	
	public Employee() {
		System.out.println(this.getAge());
	}
	
	
	public static void main(String[] args) {
		
		Employee e=new Employee();
		e.setAge(40);
		System.out.println(e.getAge());
		
		Employee e1=new Employee();
		e1.setAge(50);
		System.out.println(e1.getAge());
		
		Employee e2=new Employee();
		e2.setAge(60);
		System.out.println(e2.getAge());

	}
	
	public void setAge(int age) {
		this.age=age;
	}
	
	public int getAge() {
		return age;
	}
	
	{
		System.out.println(this.getAge());
	}
	
	
	
}
