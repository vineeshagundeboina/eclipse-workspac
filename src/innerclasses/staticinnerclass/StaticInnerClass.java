package innerclasses.staticinnerclass;

public class StaticInnerClass {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
	//	TC1 tc1=new StaticInnerClass().TC1();
        StaticInnerClass.TC1 inner = new StaticInnerClass.TC1();
        inner.breathe();

	}
	
	public static class TC1{
		public void breathe() {
			System.out.println("breathing");
		}
	}

}
