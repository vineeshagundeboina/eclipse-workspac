package anonymousobject;

public class Student {

	String name="yadagiri reddy";
	
	public void greet() {
		System.out.println("hi "+name);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Student s1=new Student(); // referenced object
		s1.greet();
		
		new Student().greet(); //anonymous object
	}

}
