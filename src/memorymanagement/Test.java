package memorymanagement;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// new Test().add(1);

		int size = 10;
		for (int i = 1; i < Integer.MAX_VALUE; i++) {

			int[] arr = new int[size];
			size = size * 2;
			System.out.println(size);
		}

		//671088640

	}

	public void add(int i) {
		System.out.println(i);
		i++;

		add(i);
	}

//	20064
}
