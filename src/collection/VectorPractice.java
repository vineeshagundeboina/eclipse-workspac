package collection;
import java.util.*;
public class VectorPractice {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//size-no.of elements in list
		//capacity-array capacity
		Vector v1=new Vector();
		v1.add("vineesha");
		v1.add("ramesh");
		v1.add(0,"yadagiri");
	/*	v1.add("y1");
		v1.add("y2");
		v1.add("y3");
		v1.add("y4");
		v1.add("y5");
		v1.add("y6");
		v1.add("y7");
		v1.add("y8");
		v1.add("y9");
		v1.add("y10");
		v1.add("y11");
		v1.add("y12");
		v1.add("y13");*/
		
		Vector v2=new Vector();
		v2.add("tanu");
		v2.add("anu");
		v2.add("anu");
		System.out.println(v1);
		/*for(int i=0;i<v2.size();i++) {
			v1.add(v2.get(i));
		}*/
		//v1.addAll(v2);
		v1.addAll(0,v2);
		System.out.println(v1);


        System.out.println(v1.size());
        System.out.println(v1.capacity());

        
        //retrieval of elements from list
        System.out.println(v1.get(4));

        //deleting the element from list
       // v1.remove(1);
        //v1.remove("vineesha");
        //v1.removeAll(v2);
        //to remove entire list
       // v1.clear();
        System.out.println(v1);
        
        //verification of elements in the list
        System.out.println(v1.contains("anu"));
        System.out.println(v1.containsAll(v2));
        
        //updating elements in the list
        //v1.set(1, "shiva");
        //System.out.println(v1.get(1));
        
        //finding index of element
        System.out.println(v1.indexOf("anu"));
        System.out.println(v1.lastIndexOf("anu"));
        
        //System.out.println(v1.firstElement());
        //System.out.println(v1.lastElement());
        
        //converting vector into array
        Object[] arr=v1.toArray();
        System.out.println(Arrays.toString(arr));
        
        //giving the initial size
        Vector v3=new Vector(20);
        v3.add("sd");
        v3.add("gyu");
        v3.add(null);
        v3.add(1,"grd");
        System.out.println(v3.size());
        System.out.println(v3.capacity());
        
        //creating vector from array
        Object[] arr1=new Object[] {2,5,4,2,6,7};
        Vector v4=new Vector(Arrays.asList(arr1));
        System.out.println(v4.size());
        System.out.println(v4.capacity());
        System.out.println(v4);

        
        //Generics
        Vector<Integer> v5=new Vector<>();
        v5.add(12);
        v5.add(null);
        v5.add(null);
        v5.add(12);
        System.out.println(v5);
        System.out.println(v3);

        
        

        ArrayList<Integer> al=new ArrayList<>();
        al.add(10);
        al.add(20);
        al.add(30);
        al.add(10);
        al.add(20);
       Set<Integer> set=new LinkedHashSet<>();
       set.addAll(al);
       System.out.println(al);
       al.clear();
       al.addAll(set);
       System.out.println(al);
        
      



	}

}
