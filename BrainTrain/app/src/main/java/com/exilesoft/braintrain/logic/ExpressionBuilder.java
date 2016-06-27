package com.exilesoft.braintrain.logic;

import static java.lang.Integer.parseInt;
import static java.lang.Math.round;
import static java.lang.String.valueOf;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class ExpressionBuilder {

	private final static Random RANDOM_GENERATOR = new Random();
	private final static String OPERATORS = "+-*/";

	private List<String> equation = new ArrayList<>();
	private int expTermWeight;

	public ExpressionBuilder(int expTermWeight){
		this.expTermWeight = expTermWeight;
	}

	public String buildExpression(){
		createEquation();
		final StringBuffer buffEq = new StringBuffer();
		for(String t : equation){
			buffEq.append(t);
		}
		return buffEq.toString();
	}
	
	public int getExpressionResult(){
		return eval();
	}
	
	private void createEquation() {
		equation.clear();
        for (int i = 0; i < getRandomTerms(); i++) {
        	equation.add(Integer.toString(getRandomNumber()));
        	equation.add(Character.toString(getRandomOperator()));
        }
        equation.remove(equation.size() -1);
    }
	
	private int eval(){
    	final List<String> tempEq = new ArrayList<>();
    	final Iterator<String> itP1 = equation.iterator();
    	int finalVal = 0;
    	
    	while(itP1.hasNext()){
    		final String term = itP1.next();
    		
    		if(term.equals("/")){
    			float operand1 = parseInt(tempEq.get(tempEq.size() -1));
    			float operand2 = parseInt(itP1.next());
    			tempEq.set(tempEq.size() -1, valueOf(round(operand1/operand2)));
    		}else if (term.equals("*")){
    			float operand1 = parseInt(tempEq.get(tempEq.size() -1));
    			float operand2 = parseInt(itP1.next());
    			tempEq.set(tempEq.size() -1, valueOf(round(operand1*operand2)));
    		}else{
    			tempEq.add(term);
    		}
    		
    	}
    	
    	
    	final Iterator<String> itP2 = tempEq.iterator();
    	while(itP2.hasNext()){
    		final String term = itP2.next();
    		if(term.equals("+")){
    			int operand2 = parseInt(itP2.next());
    			finalVal = finalVal + operand2;
    		}else if (term.equals("-")){
    			int operand2 = parseInt(itP2.next());
    			finalVal = finalVal - operand2;
    		}else{
    			finalVal = parseInt(term);
    		}
    	}
    	return finalVal;
    }
	
	private char getRandomOperator() {
        return OPERATORS.charAt(RANDOM_GENERATOR.nextInt(OPERATORS.length()));
    }

	private int getRandomNumber() {
        return RANDOM_GENERATOR.nextInt(100)+1;
    }

	private int getRandomTerms(){
		if(expTermWeight == 4){
			return RANDOM_GENERATOR.nextInt(3)+4;
		}else{
			return RANDOM_GENERATOR.nextInt(expTermWeight)+2;
		}
	}
}
