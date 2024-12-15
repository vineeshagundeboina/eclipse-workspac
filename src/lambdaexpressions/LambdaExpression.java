package lambdaexpressions;

public class LambdaExpression  {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Sayable s1=(name1,name2)->{
			return name1+name2;
		};
		System.out.println(s1.say("vine", "nukj"));

	}

}
