package localclasses;

import java.util.ArrayList;
import java.util.List;

public class SumUsingTwoForLoops {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		
//		int[] arr  = { 1, 5, 3, 2, 4,7 };
//		int sum=6;
//		
//		List<int[]> result = findSum(arr,sum);
//		
//		if(!result.isEmpty()) {
//			for(int[] pair:result) {
//				System.out.println("index1   "+pair[0]+" >>>>>> "+"inex2   "+pair[1]);
//
//				
//			}
//		}
//		else {
//			System.out.println("no such  pair exists");
//		}
//		
//		
//	}
//	
//	
//	public static List<int[]> findSum(int[] arr,int sum) {
//		
//		List<int[]> pairs=new ArrayList<>();
//				
//		
//		
//		for(int i=0;i<arr.length;i++) {
//			for(int j=i+1;j<arr.length;j++) {
//				if(arr[i]+arr[j]==sum) {
//					//System.out.println("index1 >>>>>>>>  "+i+"           "+"index2 >>>>>>>> "+j);
//					pairs.add(new int[]{i,j});
//					
//				}
//			}
//		}
//		return pairs;
//
//	}
//
//}
		
		
		
		
		
		
		
		
		
		
		
		int[] arr= {1,3,4,6,7};
		int sum=6;
		
		List<int[]> result=findSum(arr, sum);
		if(!result.isEmpty()) {
			for(int[] pair:result) {
				System.out.println("index1   "+pair[0]+" >>>> "+"index2    "+pair[1]);
			}
		}
		else {
			System.out.println("no such pair found");
		}
		
	}
	
	public static  List<int[]> findSum(int[] arr,int sum){
		
		List<int[]> pairs=new ArrayList<>();
		for(int i=0;i<arr.length;i++) {
			for(int j=i+1;j<arr.length;j++) {
				if(arr[i]+arr[j]==sum) {
					pairs.add(new int[] {i,j});
				}
			}
		}
		return pairs;
	}
}
		
