package com.kripton.grapher.GraphCalculator;

import java.util.Collections;
import java.util.Stack;
import java.util.StringTokenizer;

import com.kripton.grapher.exceptions.ParseErrorException;


public class NotationChanger extends GraphCalculatorParent {
	
	
	public NotationChanger() {
		// TODO Auto-generated constructor stub
	}

	public void infixToPostNotation(String expression) throws ParseErrorException {
		stackOperation.clear();
		stackRPN.clear();
		stackAnswer.clear();
		
		expression = expression.replace(" ", "").replace("(-", "(0-").replace(",-", ",0-");
		if(expression.charAt(0) == '-') {
			expression = "0" + expression;
		}
		
		StringTokenizer tokens = new StringTokenizer(expression, OPERATIONS + SEPARATOR + "()", true);
		
		while(tokens.hasMoreTokens()) {
			String token = tokens.nextToken();
			
			if(isNumber(token)) {
				stackRPN.push(token);
			}
			
			else if(isFunction(token)) {
				stackOperation.push(token);
			}
			

			else if(isSeparator(token)) {
				
				while(!stackOperation.isEmpty() && !isOpenBracket(stackOperation.lastElement())) {
					stackRPN.push(stackOperation.pop());
				}
				
				if(stackOperation.isEmpty()) {									//if stack is empty it means that there is no  
					throw new ParseErrorException("Open bracket is missed");	//open bracket because another way there will be 
				}																//at least one elements(open bracket)
			
			}
			
			
			else if(isOperator(token)) {
			
				while(!stackOperation.isEmpty() 
						&& isOperator(stackOperation.lastElement())
						&& getPrecedence(token) <= getPrecedence(stackOperation.lastElement())) {
					stackRPN.push(stackOperation.pop());
				}
				
				stackOperation.push(token);
			}
			
			
			else if(isOpenBracket(token)) {
				stackOperation.push(token);
			}
			
			
			else if(isCloseBracket(token)) {
				
				//Replace elements while don't meet open bracket
				while(!stackOperation.isEmpty() && !isOpenBracket(stackOperation.lastElement())) {
					stackRPN.push(stackOperation.pop());					
				}
				
				//if stack is not empty remove open bracket and if next token 
				//is function then move it to stackRPN
				if(!stackOperation.isEmpty()) {		
					stackOperation.pop();
					if(!stackOperation.isEmpty() && isFunction(stackOperation.lastElement())) {
						stackRPN.push(stackOperation.pop());   
					}
				}
				//if stack is empty it means that there is no open bracket
				else {			
					throw new ParseErrorException("Open bracket is missed");
				}
			
			}
			
			//Custom modification for using variables
			else if(isVariable(token)) {
				
				if(token.charAt(0) == 'x' && token.length() == 1) {
					stackRPN.push(token);
					continue;
				}
				else if(token.equals("Pi") || token.equals("pi") || token.equals("PI")) {
					stackRPN.push(Double.toString(Math.PI));
					continue;
				}
				else if(token.equals("E") || token.equals("e")) {
					stackRPN.push(Double.toString(Math.E));
					continue;
				}		
				
				if(variables.containsKey(token)) {
					continue;
				}		
				variables.put(token, 1.0);
				//containsNewVariables = true;	
			}

		}
		
		
		//move remaining elements
		while(!stackOperation.isEmpty()) {
			
			//if we have bracket in operators stack it means that one bracket is missed
			if(isOpenBracket(stackOperation.lastElement()) || isCloseBracket(stackOperation.lastElement())) {
				throw new ParseErrorException("Bracket is missed");
			}
			
			stackRPN.push(stackOperation.pop());
		}	
		
		Collections.reverse(stackRPN);
		
	}
	
	
	public Stack<String> getStackRPN() {
		return stackRPN;
	}
	

	private Stack<String> stackOperation = new Stack<String>();
	private Stack<String> stackRPN = new Stack<String>();
	private Stack<String> stackAnswer = new Stack<String>();

}
	