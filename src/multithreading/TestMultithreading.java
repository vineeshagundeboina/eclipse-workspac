package multithreading;

public class TestMultithreading {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
//		System.out.println("hey,welcome to multithreading concept");
//		System.out.println(Thread.activeCount());
//		System.out.println(Thread.currentThread().getName());
//		System.out.println(Thread.currentThread().getId());
//		System.out.println(Thread.currentThread().getPriority());
		
		
		
		
//		Thread t1=new Thread();
//		
//		Thread t2=new Thread();
//		
//		Thread t3=new Thread();
//		Thread t4=new Thread();
//		
//		t1.start();
//		t2.start();
//		t3.start();
//		t4.start();
//		
//		
//		System.out.println(Thread.activeCount());
		
		
		
/* first way of creating thread(by extending the Thread class)*/		
		
		
		Employee e1=new Employee("T1");
	//	System.out.println("Before thread");
		e1.start();
		
		Manager m1=new Manager("T2");
		m1.start();
		
	//	System.out.println("After thread");
		System.out.println(Thread.activeCount());

	}

}


class Employee extends Thread{
	
	public Employee(String tName) {
		super(tName);
	}
	
	@Override
	public void run() {
		System.out.println("Hello "+Thread.currentThread().getName()+"  "+Thread.currentThread().getId());
	}
}

class Manager extends Thread{
	
	public Manager(String tName) {
		super(tName);
	}
	
	@Override
	
	public void run() {
		System.out.println("Hi "+Thread.currentThread().getName()+"  "+Thread.currentThread().getId());
	}
}

