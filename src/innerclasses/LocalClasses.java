package innerclasses;

public class LocalClasses {

	private int id=100;
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		int salary=566;
		
		class A{
			
			public void show() {
				System.out.println("salary : "+salary);
			}
			
		}
		
		A a=new A();
		a.show();
	}
	
	public LocalClasses(){
		class B{
			
		}
	}
	
	{
		class C{
			
		}
	}
	
	static {
		class A{
			
		}
	}

}
