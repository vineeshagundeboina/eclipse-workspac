package garbagecollection;

public class GarbageCollectionPractice {
	
	public void finalize() {
		System.out.println("finalize method is called");
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		GarbageCollectionPractice gcp1=new GarbageCollectionPractice();
		GarbageCollectionPractice gcp2=new GarbageCollectionPractice();
		GarbageCollectionPractice gcp3=new GarbageCollectionPractice();
		
		
		new GarbageCollectionPractice();
		gcp1=null;
		gcp2=gcp3;
		
		System.gc();


	}
	
	

}
