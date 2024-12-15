package multithreading;

public class CreatingThreadByAnonymousClassesAndLambdaExpression {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/* By using anonymous class */
		Runnable r1 = new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < 10; i++) {
					// TODO Auto-generated method stub
					System.out.println("Hello   " + Thread.currentThread().getName() + "   " + Thread.currentThread().getId());
				}
			}
		};

		Thread t1 = new Thread(r1, "T1");
		System.out.println(t1.getState());

		/* by using lambda expression */
		Runnable r2 = () -> {
			for (int i = 0; i < 20; i++) {
				System.out.println("Hi   " + Thread.currentThread().getName() + "   " + Thread.currentThread().getId());

			}
		};

		Thread t2 = new Thread(r2, "T2");
		t1.start();
		System.out.println(t1.getState());
//		try {
//			t1.join();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		try {
			t1.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(t1.getState());

		t2.start();

	//	t1.start();
		//System.out.println(Thread.activeCount());

	}

}
