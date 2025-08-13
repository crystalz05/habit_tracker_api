package com.tyro.habit_tracker;
import java.util.Scanner;

public class AddNumbers {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		Scanner myobj = new Scanner(System.in);
		System.out.println("enter name, age and salary");
		String name = myobj.nextLine();
		int age = myobj.nextInt();
		double salary = myobj.nextDouble();
		
		System.out.println("My name is "+name);
		System.out.println("My age is "+age);
		System.out.println("my slary is "+salary);
	}

}
