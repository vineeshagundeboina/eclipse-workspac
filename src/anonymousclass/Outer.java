package anonymousclass;

public class Outer {

	private int id=101;
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		int salary=1000;
		
//		Class2 c2=new Class2();
//		c2.print();
		
		
		Class1 c1=new Class1() {
			public void print() {
				System.out.println("updated print method is called");
			}
		};
		
		
		Class1 c2=new Class1() {
			public void print() {
				System.out.println("updated print method is called");
			}
		};
		
		c1.print();
		c1.show();
	}

}

class Class1{
	public void show() {
		System.out.println("show method is called");
	}
	
	public void print() {
		System.out.println("print method is called");
	}
}

class Class2 extends Class1{
	public void print() {
		System.out.println("updated print method is called");
	}
}
