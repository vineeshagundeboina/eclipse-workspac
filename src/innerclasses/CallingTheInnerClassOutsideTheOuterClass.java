package innerclasses;

import innerclasses.NonStaticInnerClass.TC1;

public class CallingTheInnerClassOutsideTheOuterClass {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		TC1 tc1=new NonStaticInnerClass().new TC1();
		tc1.add();
	}

}
