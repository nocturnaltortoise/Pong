package exercises;

import java.util.Scanner;

public class ConsoleCalculator {
	
	static Scanner userInput = new Scanner(System.in);
	
	public static double evaluateAnswer(double numOne, double numTwo, String operator){
		
		if(operator.equals("+")){
			return numOne + numTwo;
		}else if(operator.equals("-")){
			return numOne - numTwo;
		}else if(operator.equals("*")){
			return numOne * numTwo;
		}else if(operator.equals("/")){
			return numOne / numTwo;
		}else{
			System.out.println("invalid operator: " + operator);
			return 0;
		}
		
	}
	
	public static void main(String[] args){
		
		System.out.println("Enter your first number, followed by your operator (+, -, * or /) , followed by your second number. Make sure to press enter after every entry.");
		
		double numOne = userInput.nextDouble();
		
		String operator = userInput.next();
		
		double numTwo = userInput.nextDouble();
	
		System.out.println("The answer is: " + evaluateAnswer(numOne, numTwo, operator));
		
	}
	
	
	
	
	
}
