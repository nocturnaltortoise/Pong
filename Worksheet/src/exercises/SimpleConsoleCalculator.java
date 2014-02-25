package exercises;

import java.util.Scanner;

public class SimpleConsoleCalculator {
	
	static Scanner userInput = new Scanner(System.in);
	
	public static void main(String[] args){
		
		System.out.println("Choose your operator (+, -, * or /): ");
		
		String operator = userInput.next();
		
		System.out.println("Enter first number, followed by second number");
		
		double numOne = userInput.nextInt();
		double numTwo = userInput.nextInt();
		double answer;
		
		
		//checks for what operator the user entered, performs the required calculation
		if(operator.equals("+")){
			answer = numOne + numTwo;
		}else if(operator.equals("-")){
			answer = numOne - numTwo;
		}else if(operator.equals("*")){
			answer = numOne * numTwo;
		}else if(operator.equals("/")){
			answer = numOne / numTwo;
		}else{
			answer = 0;
			System.out.print("an error occurred");
			//prints out a message if something goes wrong, and assigns a value to answer
		}
		
		
		System.out.println("The answer is " + answer);
		
	}
	
}
