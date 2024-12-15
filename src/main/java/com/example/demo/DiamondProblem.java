package com.example.demo;

public class DiamondProblem implements A,B{

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		DiamondProblem d=new DiamondProblem();
		d.show();
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		B.super.show();
		A.super.show();
		System.out.println("i am from class");
	}

}

interface A{
	default void show() {
		System.out.println("interface a");
	}
}

interface B{
	default void show() {
		System.out.println("interface a");
	}
}
