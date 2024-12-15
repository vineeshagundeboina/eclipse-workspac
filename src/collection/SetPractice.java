package collection;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.TreeSet;

public class SetPractice {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
//		HashSet<Integer> hs=new HashSet<>();
//		hs.add(10);
//		hs.add(2);
//		hs.add(34);
//		hs.add(10);
//		hs.add(65);
//		hs.add(85);
//		hs.add(10);
//		
//		System.out.println(hs);
//		
//		for(int element:hs) {
//			System.out.println(element);
//		}
//		
//		System.out.println(hs.remove(34));
//		System.out.println(hs.contains(10));
//		System.out.println(hs);
//		hs.clear();
//		System.out.println(hs);
		
		
		
/* linked hash set*/
		
		
		
//		LinkedHashSet<Integer> lhs=new LinkedHashSet<>();
//		lhs.add(2);
//		lhs.add(5);
//		lhs.add(1);
//		lhs.add(43);
//		
//		System.out.println(lhs);
//		System.out.println(lhs.remove(1));
//		System.out.println(lhs.contains(1));
//		System.out.println(lhs);
		
		
		/* treeset*/
		
		TreeSet<Integer> ts=new TreeSet<>();
		ts.add(5);
		ts.add(3);
		ts.add(121);
		ts.add(1);
		System.out.println(ts);
		System.out.println(ts.descendingSet());
		System.out.println(ts.first());
		System.out.println(ts.last());
		System.out.println(ts.subSet(1,23));
		System.out.println(ts.pollFirst());
		System.out.println(ts.pollLast());
		System.out.println(ts);


	}

}
