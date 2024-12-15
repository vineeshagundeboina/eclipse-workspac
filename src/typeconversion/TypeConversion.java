package typeconversion;

public class TypeConversion {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		byte b=10;  //-128 to 127
		short s=78;
		int i=256;
		long l=232433334;
		float f=23.45f;
		double d=2132424.56;
		
		char c='a';
		boolean bo=true;
	//	System.out.println(i);
		
		//i=s;
		b=(byte)i;
		
	//	i=(int)bo; not possible
		
	//	bo=(boolean)i;
		
		
		int b1=b+b;
		
		System.out.println(b1);
		System.out.println(b);

	}

}
