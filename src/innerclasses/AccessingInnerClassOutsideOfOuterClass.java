package innerclasses;

import innerclasses.NonStaticInnerClass.TC1;

public class AccessingInnerClassOutsideOfOuterClass {
	public static void main(String[] args) {
		TC1 tc1=new NonStaticInnerClass().new TC1();
		tc1.add();
	}
	
	
	public class AnotherInnerClass{
		public void otherInnerClassMethod() {
			System.out.println(">>> otherInnerClassMethod >>>>");
		}
	}

}
