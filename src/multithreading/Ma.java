package multithreading;

public class Ma {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		A a=new A();
		
		Thread t1=new Thread(a,"T1");
		t1.start();
		
		B b=new B();
		Thread t2=new Thread(b,"T2");
		t2.start();
		
		System.out.println(Thread.activeCount());
				

	}

}


class A implements Runnable{

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("i am from class a");
	}
	
	
}


class B implements Runnable{

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("i am from class b");
	}
	
}