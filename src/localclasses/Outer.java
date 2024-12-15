package localclasses;

public class Outer {

	private int id = 101;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		int salary = 5000;

		final class A {
			
			public void print() {
				System.out.println("salary : "+salary);
			}

		}
		
		A a=new A();
		a.print();

//		class B{
//			
//		}

	}

	public void show() {
		System.out.println("show method is called");
		
	}

	public Outer() {

		class B {

		}
	}

	{
		class B {

		}
	}
	
	static {
		class B{
			
		}
	}

}
