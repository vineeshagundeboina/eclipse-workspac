package java8;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SumNumbersStartsWithFive {

	public static void main(String[] args) {

		Integer[] arr = { 1, 1, 0, 0, 1, 0, 1, 0 };
//		
//		for(int i=0;i<arr.length;i++) {
//			if(arr[i]==1) {
//				System.out.println(i+"    ");
//			}
//		}

		System.out.println("indices before sorting :");

		IntStream.range(0, arr.length).filter(i -> arr[i] == 1).forEach(i -> System.out.print(i + " "));
		System.out.println();
		Arrays.sort(arr, Collections.reverseOrder());

		System.out.println(Arrays.toString(arr));
		
		List<Integer> abc=new ArrayList<>();
		
		List<Integer> filteredList=abc.stream().filter(num->num!=0).collect(Collectors.toList());
	
		System.out.println();

	}

}
