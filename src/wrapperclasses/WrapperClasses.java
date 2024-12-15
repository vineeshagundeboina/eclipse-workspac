package wrapperclasses;

import java.util.ArrayList;

public class WrapperClasses {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int i=10;
		
		WrapperClasses wc=new WrapperClasses();
//		Integer i2=new Integer(i); //boxing
//		
//		Integer i3=new Integer(45);

		Integer i2=Integer.valueOf(i);
		
		Integer i3=Integer.valueOf(44);
		
		Integer i4=i;  //Auto-boxing
		System.out.println(i2);
		
		int a=i3.intValue(); //unboxing
		System.out.println(a);
		
		int b=i3; //auto-unboxing
		
		System.out.println(b);
		
		
		
		
		ArrayList<Integer> al=new ArrayList<>();
		al.add(20); //auto-boxing
		al.add(30);
		al.add(Integer.valueOf(40)); //boxing
		
		int age=al.get(0);  //auto-unboxing
		System.out.println(age);
		
		String s="14";
	//	int age1=Integer.valueOf(s);
		
		int age1=Integer.parseInt(s);
		
		
		Long l=Long.valueOf(s);
		int iage=l.intValue();
		
		System.out.println(iage);
		System.out.println(age1);
				

	}

}
