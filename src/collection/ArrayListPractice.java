package collection;
 import java.util.*;
public class ArrayListPractice {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	//creation of arraylist	
     ArrayList<Integer> al=new ArrayList<>();
     al.add(3);
     System.out.println(al);
     System.out.println(al.size());
     
     //converting array to arraylist
     Integer[] arr=new Integer[] {2,4,6,7,9,3,5};
     ArrayList<Integer> al1=new ArrayList<>(Arrays.asList(arr));
     System.out.println(al1);
     
     //remaining operations
     System.out.println(al1.get(3));
     System.out.println(al1.remove(3));
     System.out.println(al1);
     System.out.println(al1.contains(7));
     al1.set(0,10);
     System.out.println(al1);
     
     //iteration
     for(Integer a:al1) {
    	 System.out.print(a+" ");
    	 
     }
     System.out.println();
     for(int i=0;i<al1.size();i++) {
    	 System.out.println(al1.get(i));
     }

     //conversion of arraylist to synchronized arraylist
     Collections.synchronizedList(al1);
     
     
      
		
	}

}
