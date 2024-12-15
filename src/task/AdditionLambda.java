package task;

public class AdditionLambda {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Addition ad=(a,b)->{
			
			return a+b;
			
		};
		System.out.println(ad.add(4, 5));
	}

}
