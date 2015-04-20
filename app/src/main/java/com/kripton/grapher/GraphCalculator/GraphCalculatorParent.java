package com.kripton.grapher.GraphCalculator;

import java.util.HashMap;
import java.util.Map;

import com.kripton.grapher.exceptions.ParseErrorException;

public class GraphCalculatorParent {
	
	protected boolean isVariable(String token) {
		if((token.length() == 1 && token.charAt(0) >= 97 && token.charAt(0) <= 122) || token.equals("Pi") || token.equals("pi")) {
			return true;
		}
		else {
			return false;
		}
	}
	
	
	protected boolean isSeparator(String token) {
		return token.equals(SEPARATOR);
	}
	
	
	protected boolean isNumber(String token) {
		try {
			Double.parseDouble(token);
		} catch(Exception e) {
			return false;
		}
		return true;
	}
	
	
	protected boolean isOpenBracket(String token) {
		return token.equals("(");
	}
	
	
	protected boolean isCloseBracket(String token) {
		return token.equals(")");
	}
	
	
	protected boolean isFunction(String token) {
		for(String item : FUNCTIONS) {
			if(item.equals(token)) {
				return true;
			}
		}
		return false;
	}
	
	
	protected boolean isOperator(String token) {
		return OPERATIONS.contains(token);
	}
	
	
	/* Returns priority of the operator */
	protected int getPrecedence(String operator) throws ParseErrorException {
		if(isOperator(operator)) {
			if(operator.equals("+") || operator.equals("-")) {
				return 1;
			}
			else if(operator.equals("*") || operator.equals("/")){
				return 2;
			}
			else {
				return 3;
			}
		}
		else {
			throw new ParseErrorException("Precedence function got unknown operator");
		}
	}
	
	
	
	protected final String OPERATIONS = "+-*/^";
	protected final String SEPARATOR = ",";
	protected final String[] FUNCTIONS = {"abs", "sin", "asin", "sinh", "cos", "acos","cosh", "tan", "atan", "tanh", "cot", "acot", "coth", "exp",
			"ln", "log", "pow", "sqrt",};
	
	protected Map<String, Double> variables = new HashMap<String, Double>();
	

}
