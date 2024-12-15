package innerclasses;

public class AnonymousClass {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		class1 c=new class1() {
			public void print() {
				System.out.println("updated print method is called");
			}
		};
		
		
		class1 c2=new class1() {
			public void print() {
				System.out.println("updated");
			}
		};
		
		
		AbstractClass ab=new AbstractClass() {
			
			@Override
			public void print() {
				// TODO Auto-generated method stub
				System.out.println("print");
				
			}
		};
		
		
		
		c.show();
		
		c.print();
		c2.print();
		
	}

}

class class1{
	public void show() {
		System.out.println("show method is called");
	}
	
	public void print() {
		System.out.println("print method is called");
	}
}

//class class2 extends class1{
//	public void print() {
//		System.out.println("updated print method is called");
//	}

