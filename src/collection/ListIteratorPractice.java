package collection;

import java.util.*;

public class ListIteratorPractice {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		ArrayList<Integer> al=new ArrayList<>();
		al.add(3);
		al.add(67);
		al.add(43);
		al.add(2);
		ListIterator<Integer> itr=al.listIterator();
		while(itr.hasNext()) {
			System.out.println(itr.next());
		}
		
		System.out.println();
		itr.add(23);
		while(itr.hasPrevious()) {
			System.out.println(itr.previous());
		}
		itr.remove();
		System.out.println(al);
	}

}
