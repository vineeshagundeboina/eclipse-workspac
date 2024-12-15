package stringbufferandstringbuilder;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
//		String s1="Yadagiri";  //this s1 variable will hold address of the memory location,that's why no concept of capacity
//		String s2="Yadagiri";
//		System.out.println(s1==s2);
//		
//		s1=s1+"Reddy";
//		System.out.println(s1==s2);
//		
//		String s3="YadagiriReddy";
//		System.out.println(s1==s3);
//		
//		
//		StringBuffer sb1=new StringBuffer("Yadagiri");
//		StringBuffer sb2=new StringBuffer("Yadagiri");
//		System.out.println(sb1==sb2);
//		
//		sb1=sb1.append("Reddy");
//		System.out.println(sb1==sb2);
//		StringBuffer sb3=new StringBuffer("YadagiriReddy");
//		System.out.println(sb1==sb3);
		
		StringBuilder sb1=new StringBuilder("Yadagiri");
		StringBuilder sb2=new StringBuilder("Yadagiri");
		
		System.out.println(sb1==sb2);
		sb1=sb1.append("Reddy");
		System.out.println(sb1==sb2);
		StringBuilder sb3=new StringBuilder("YadagiriReddy");
		System.out.println(sb1==sb3);
		
		
		
		
		
		
		StringBuilder sb=new StringBuilder("yadagiri");//this sb variable will hold data(yadagiri) not address
		
		sb.ensureCapacity(100);
		
		System.out.println(sb.capacity());
		
		
		sb.insert(4,"reddy");
		System.out.println(sb);
		sb.reverse();
		System.out.println(sb);
		sb.deleteCharAt(0);
		sb.delete(0, 3);
		System.out.println(sb);
		
		StringBuilder s=new StringBuilder();
		System.out.println(" default capacity : "+s.capacity());
		
		
		StringBuilder s1=new StringBuilder("hello");
		StringBuilder s2=new StringBuilder("hell");
		System.out.println(s1.equals(s2));
		
		System.out.println(s1.toString().compareTo(s2.toString()));

	}

}
