package thispractice;

public class ThisInAbstractClass extends AbstractJaguar{

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		AbstractJaguar a=new ThisInAbstractClass();
		
		//{
//			
//			@Override
//			public void drive() {
//				// TODO Auto-generated method stub
//				
//			}
//		};
		a.setCarName("Toyoto");
		System.out.println(a.getCarName());
	}

	@Override
	public void drive() {
		// TODO Auto-generated method stub
		
	}

	

}
