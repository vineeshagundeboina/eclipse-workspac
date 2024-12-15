package lambdaexpressions;

public class Lam {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		UserInterface u=(a,b)->
		{
			return a+b;
		};
		
		
		
		System.out.println(u.getUsername(4,5));
	}

}




@FunctionalInterface
interface UserInterface{
	
	public int getUsername(int a,int b);
	
}