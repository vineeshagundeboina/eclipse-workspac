package innerclasses;

import innerclasses.StaticInnerClass.TestClass5;

public class TestClass100 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
 
		TestClass5 tc5=new StaticInnerClass.TestClass5();
		
		tc5.add();
				
	}
	
	public class TC101{
		public void show() {
			TestClass5 tc5=new StaticInnerClass.TestClass5();
			tc5.add();
		}
	}

}
