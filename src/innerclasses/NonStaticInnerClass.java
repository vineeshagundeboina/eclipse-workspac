package innerclasses;

public class NonStaticInnerClass {
	
	private int id=101;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		System.out.println("In main method : inside nonstaticinnerclass");
		
	//	TestClass4 tc4=new TestClass4();
		
		TestClass1 tc1=new TestClass1();
		
		TestClass4 tc4=new TestClass1().new TestClass4();
		tc4.show();

	}
	
	
	public class TestClass4 extends Student{
		int id=102;
		public TestClass4() {
			
		}
		
		public void show() {
			int id=103;
			System.out.println(id);
			System.out.println(this.id);
			System.out.println(TestClass1.this.id);
			showAttendance();
		}
		
//		public void add() {
//			TestClass4 tc4=new TestClass4();
//			tc4.show();
//		}
	}
	
	

}
