package collection;
import java.util.*;
public class EnumerationPractice {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Vector<Integer> v=new Vector<>();
		v.add(4);
		v.add(3);
		v.add(5);
		Enumeration<Integer> e=v.elements();
		while(e.hasMoreElements()) {
			System.out.println(e.nextElement());
		}
		
		Hashtable<Integer,String> ht=new Hashtable<>();
		ht.put(1, "vuh");
		ht.put(2, "fjkd");
		Enumeration<String> n=ht.elements();
		while(n.hasMoreElements()) {
			System.out.println(n.nextElement());
		}
	}

}
