package multithreading;

public class SynchronizedUseInMultithreading {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		BrickDairy bd = new BrickDairy();
		Runnable r1 = () -> {
			for (int i = 0; i < 10000; i += 50) {
				bd.increamentBrickCount();
			}
		};

		Runnable r2 = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				for (int i = 0; i < 15000; i += 50) {
					bd.increamentBrickCount();
				}

			}
		};

		Runnable r3 = () -> {
			for (int i = 0; i < 5000; i += 50) {
				bd.increamentBrickCount();
			}

		};

		Thread t1 = new Thread(r1);
		Thread t2 = new Thread(r2);
		Thread t3 = new Thread(r3);

		t1.start();
		t2.start();
		t3.start();

		try {
			t1.join();
			t2.join();
			t3.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("brickcount : " + bd.brickCount);
		System.out.println("brickcount2 : " + bd.brickCount2);

	}

}

class BrickDairy {
	
	
	volatile int brickCount = 0;
	volatile int brickCount2 = 0;
	
	
//	int brickCount = 0;
//	int brickCount2 = 0;

//	public synchronized void increamentBrickCount() {
//		brickCount+=50;
//		brickCount2+=50;
//	}

//	public void increamentBrickCount() {
//		brickCount += 50;
//		synchronized (this) {
//			brickCount2 += 50;
//		}
//	}
	
	public void increamentBrickCount() {
		brickCount+=50;
		brickCount2+=50;
		
	}
}
