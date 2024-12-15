package anonymousclass;

public class AnonymousClassForInterface {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		InterfaceStudent s1=new InterfaceStudent() {
			
			@Override
			public int getMarks() {
				// TODO Auto-generated method stub
				System.out.println("getmarks is called");
				return 0;
			}
			
			@Override
			public void attend() {
				System.out.println("attend is called");
				// TODO Auto-generated method stub
				
			}
		};
		
		s1.getMarks();
		s1.attend();

	}

}
