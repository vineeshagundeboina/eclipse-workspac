package collection;
import java.util.*;

public class LinkedListPractice {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		ArrayList<String> al=new ArrayList<>();
		al.add("s1");
		al.add("s2");
		al.add("s3");
		
		LinkedList<String> ll=new LinkedList<>();
		ll.add("y1");
		ll.add("y2");
		ll.add("y3");
		ll.add(1,"yy");
		ll.addAll(al);
		ll.addAll(0,al);
		//ll.remove(0);
		//ll.removeAll(al);
		//ll.clear();
		System.out.println(ll);
		System.out.println(ll.get(4));
		System.out.println(ll.contains("ra"));
		System.out.println(ll.containsAll(al));
		ll.set(2,"vinee");
		System.out.println(ll);
		Collections.synchronizedList(ll);
	

	}

}
