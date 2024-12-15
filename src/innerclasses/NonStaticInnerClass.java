package innerclasses;

public class NonStaticInnerClass {
	
	int id=101;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		NonStaticInnerClass nc=new NonStaticInnerClass();
		TC1 tc1=nc.new TC1();
		tc1.show();
		tc1.add();
		

	}
	
//	public class TC1{
//		
//		public static void main(String[] args) {
//			
//		}
//	}
//	
//	private static class TC2{
//		
//		
//	}
//	
//	protected final class TC3{
//		
//	}
	
//	public abstract class TC4{
//		
//	}
	
	
	
//	public class TC1{
//		int id=102;
//		public void show() {
//			int id=103;
//			System.out.println(id);
//			System.out.println("inner class instance variable : "+this.id);
//			System.out.println("outer class instance variable : "+NonStaticInnerClass.this.id);
//			System.out.println();
//		}
//		
//		public void add() {
//			System.out.println("add method");
//			TC1 tc1=new TC1();
//			tc1.show();
//		}
//	}

	

	
//	public class TC1 extends CheckingInnerClassExtendsNonAbstractClass{
//		int id=102;
//		public void show() {
//			int id=103;
//			System.out.println(id);
//			System.out.println("inner class instance variable : "+this.id);
//			System.out.println("outer class instance variable : "+NonStaticInnerClass.this.id);
//			System.out.println();
//			
//			System.out.println("from student class : "+getMarks());
//			tractAttendance();
//			System.out.println("show method end >>>>>>>>>>>>>>>>");
//
//		}
//		
//		public void add() {
//			System.out.println("add method");
//			TC1 tc1=new TC1();
//			tc1.show();
//		}
//	}
//	
	
	
	
//	public class TC1 extends CheckingInnerClassExtendAbstractClass{
//		int id=102;
//		public void show() {
//			int id=103;
//			System.out.println(id);
//			System.out.println("inner class instance variable : "+this.id);
//			System.out.println("outer class instance variable : "+NonStaticInnerClass.this.id);
//			System.out.println();
//		}
//		
//		public void add() {
//			System.out.println("add method");
//			TC1 tc1=new TC1();
//			tc1.show();
//			tc1.play();
//		}
//
//		@Override
//		public void play() {
//			// TODO Auto-generated method stub
//			System.out.println("playing");
//		}
//	}
//	
//	
	
	
//	
//	public class TC1 implements CheckingInnerClassImplementingInterface{
//		int id=102;
//		public void show() {
//			int id=103;
//			System.out.println(id);
//			System.out.println("inner class instance variable : "+this.id);
//			System.out.println("outer class instance variable : "+NonStaticInnerClass.this.id);
//			System.out.println();
//		}
//		
//		public void add() {
//			System.out.println("add method");
//			TC1 tc1=new TC1();
//			tc1.show();
//			tc1.eat();
//			tc1.sleep();
//		}
//
//		@Override
//		public void eat() {
//			// TODO Auto-generated method stub
//			System.out.println("eating");
//		}
//
//		@Override
//		public void sleep() {
//			// TODO Auto-generated method stub
//			System.out.println("sleeping");
//		}
//	}
//	
	
	
	
	

	public class TC1{
		int id=102;
		public void show() {
			int id=103;
			System.out.println(id);
			System.out.println("inner class instance variable : "+this.id);
			System.out.println("outer class instance variable : "+NonStaticInnerClass.this.id);
			System.out.println();
		
		}
		
		public void add() {
			System.out.println("add method");
			TC1 tc1=new TC1();
			tc1.show();
			System.out.println("TC2");
		}
	}
	
	public class TC2 extends TC1{
		public void extending() {
			System.out.println("from TC2");
			add();
			
		}
	}
	
	
	

}
