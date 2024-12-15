package collection;
import java.util.*;
import java.util.Map.Entry;
public class IteratorPractice {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
				
				ArrayList<Integer> al=new ArrayList<>();
				al.add(3);
				al.add(5);
				al.add(7);
				al.add(8);
				
				
				Iterator<Integer> itr=al.iterator();
				while(itr.hasNext()) {
					
					System.out.println(itr.next());
					//itr.remove();
				}
		
			System.out.println(al);

			HashMap<Integer,String> hm=new HashMap<>();
			hm.put(1, "dse");
			hm.put(2, "srf");
			hm.put(3, "sfer");
			
			Set<Integer> keys=hm.keySet();
			Iterator<Integer> i=keys.iterator();
			while(i.hasNext()) {
				System.out.println(i.next());
			}
			
			Collection<String> values=hm.values();
			Iterator<String> it=values.iterator();
			
			while(it.hasNext()) {
				System.out.println(it.next());
			}
System.out.println();
			Set<Entry<Integer,String>> en=hm.entrySet();
			Iterator<Entry<Integer, String>> im=en.iterator();
			while(im.hasNext()) {
				System.out.println(im.next());
			}
	}
	

}
