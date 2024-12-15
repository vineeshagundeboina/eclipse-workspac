package com.example.demo.entity;

import java.util.*;

public class Swap {
	
	public static void main(String[] args) {
		
		System.out.println("enter the number");
		Scanner input = new Scanner(System.in);
		
		int length=input.nextInt();
		
		System.out.println("size  : "+length);
		
		System.out.println("enter the array");
		int[] arr=new int[length];
		for(int i=0;i<length;i++) {
			arr[i]=input.nextInt();
		}
		
		for(int num:arr) {
			System.out.print(num+" , ");
		}
		
		danceForm(length,arr);
	}
	
	public static void danceForm(int n,int[] arr) {
		for(int i=0;i<n-1;i+=2) {
			int temp=arr[i];
			arr[i]=arr[i+1];
			arr[i+1]=temp;
		}
		System.out.println("ouput : "+Arrays.toString(arr));
	}

	

}


