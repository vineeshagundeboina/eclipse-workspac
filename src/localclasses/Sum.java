package localclasses;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Sum {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		int[] arr = { 1, 5, 3, 2, 4,7 };
		int sum = 6;

		int[] result = findSum(arr, sum);

//		if (result != null) {
//			System.out.println("output : " + result[0] + "," + result[1]);
//		} else {
//			System.out.println("no such pair");
//		}

	}

	public static int[] findSum(int[] arr, int sum) {
		// code
		//Arrays.sort(arr);

//		{ 1, 5, 3, 2, 7 }
		
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		int currentValue = 0;
		for (int i = 0; i < arr.length; i++) {

			currentValue = arr[i];
			int remValue = sum - currentValue;

			if (map.containsKey(remValue)) {

				System.out.println("index1 " + (map.get(remValue)) + "    index2 " + (i));
			}

			if(!map.containsKey(currentValue)) {
				map.put(currentValue,i);
			}
			
			//System.out.println(map);
		}
		return null;

	}

}
