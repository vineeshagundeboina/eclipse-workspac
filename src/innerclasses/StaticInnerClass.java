package innerclasses;

public class StaticInnerClass {
	
	public static void main(String[] args) {
		
		TestClass5 tc5=new StaticInnerClass.TestClass5();
		tc5.add();
		
	}
	
	
	public static class TestClass5{
		public void add() {
			System.out.println("add");
		}
	}

}
