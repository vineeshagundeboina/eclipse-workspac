package collections;
import java.util.*;
import java.util.Map.Entry;

public class TablePractice {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
        //creation of map
		Hashtable<Integer,String> ht=new Hashtable<Integer,String>();	
		
		
		//addition of elements into map
		ht.put(102,"hyderabad");
		ht.put(103, "chennai");
		ht.put(104,"banglore");
		
		//retrieval of keys from map
        Set<Integer> keys=ht.keySet();	
        for(Integer key:keys) {
        	System.out.println(key);
        }
        System.out.println();
        
        
        //retrieval of values from map
        Collection<String> values=ht.values();
        for(String value:values) {
        	System.out.println(value);
        }
        System.out.println();
        
        
        //retrieval of value from the map based on key
        System.out.println(ht.get(104));
        //retrieeval of all values bassed on keys
        for(Integer key:keys) {
        	System.out.println(key+"="+ht.get(key));
        }
        
        System.out.println(ht);
        
        //deletion of element from map
       // ht.remove(103);
        //System.out.println(ht);
        
        
        //verification of keys in the map
        System.out.println(ht.containsKey(102));
        System.out.println(ht.containsKey(343));
        
        //verification of values in the map
        System.out.println(ht.containsValue("hyderabad"));
        System.out.println(ht.containsValue("dfffffdg"));
        
        //updation of elements in tha map
        System.out.println(ht); 
        ht.put(102, "mumbai");
        System.out.println(ht); 

        ht.putIfAbsent(102, "hyderabad");
        System.out.println(ht); 
        ht.replace(102,"hyderabad");
        System.out.println(ht); 
       
        //to know no.of elements in map
        System.out.println(ht.size());
        
        //to delete all elements from map
        //ht.clear();
        System.out.println(ht);
        
        
        //iteration of map using entryset method,it is other way
              Set<Entry<Integer,String>> entries=ht.entrySet();
              for(Entry<Integer,String> entry:entries) {
            	  Integer key=entry.getKey();
            	  String value=entry.getValue();
            	  System.out.println(key+"="+value);
              }
              
              
              
	}
}
