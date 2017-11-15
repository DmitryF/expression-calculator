package com.dzmitryf.expression;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class ExpressionCalculator {

	private static final String SEPARATOR = ";";
	
	private static final String SIMPLE_OPERANDS = "[[+]|[-]|[*]|[/]]{1}";
	
	private static final String LEFT_BRACKET = "[(]";
	
	private static final String RIGHT_BRACKET = "[)]";
	
	private static final String BRACKETS = LEFT_BRACKET + "|" + RIGHT_BRACKET;
	
	private static final String OPERANDS_IN_BRACKETS = "[(]{1}([0-9]{1,}([+]|[-]|[*]|[/])*){1,}[0-9]{1,}[)]{1}";
	
	private static final String GROUP_OPERANDS_WITH_DIV_MUL = "([0-9]{1,}([/]|[*])[0-9]{1,})";
	
	private static final String NOT_EXPRESSION_SYMBOLS = "[^\\+|\\-|\\*|\\/|\\(|\\)|0-9]";
	
	private static final String INVALID_SYNTAX_OPERATORS = "[\\+|\\-|\\*|\\/]{2,}|([(][)])|([0-9][(])|([)][0-9])";
	
	private List<String> stepsCalculation = new ArrayList<>();

	public ExpressionCalculator() {
		
	}

	public int calculateExpression(String expression) throws ParseException {

		if (expression == null || expression.length() == 0) {
			throw new ParseException("Expression is empty", 0);
		}

		if (!isValid(expression)) {
			throw new ParseException("Expression has syntax error", 0);
		}

		stepsCalculation.clear();
		expression = addGroupBrackets(expression.replace(" ", "").trim());
		return calculate(expression);
	}	

	private boolean isValid(String expression) {

		boolean hasNotExpressionSymbols = Pattern.compile(NOT_EXPRESSION_SYMBOLS).matcher(expression).find();
		boolean hasInvalidSyntaxOperators = Pattern.compile(INVALID_SYNTAX_OPERATORS)
				.matcher(expression).find();
		int countLeftBrackets = expression.split(LEFT_BRACKET).length;
		int countRightBrackets = expression.split(RIGHT_BRACKET).length;
		boolean countBracketsEquals = countLeftBrackets == countRightBrackets;

		return !(hasNotExpressionSymbols || hasInvalidSyntaxOperators) && countBracketsEquals;
	}

	private int calculate(String expression) {
		
		if (isDigit(expression)) {
			return Integer.valueOf(expression);
		} else {

			if (expression.length() <= 1)
				return 0;

			String[] operandsExpression = getOperandsInBrackets(expression);

			if (operandsExpression.length > 1) {
				int valueInBrackets = calculate(getOperandWithoutBrackets(operandsExpression[1]));
				operandsExpression[1] = String.valueOf(valueInBrackets);
				String newOperandsExpression = addGroupBrackets(String.join("", operandsExpression));
				return calculate(newOperandsExpression);
			} else {
				operandsExpression = getSimpleOperands(expression);
			}

			if (operandsExpression.length > 0) {
				return calculateOperandsExpression(operandsExpression);
			}
		}

		return 0;
	}

	private String[] getSimpleOperands(String expression) {
		return expression.replaceAll(SIMPLE_OPERANDS, SEPARATOR + "$0" + SEPARATOR).split(SEPARATOR);
	}

	private String[] getOperandsInBrackets(String expression) {
		return expression
				.replaceFirst(OPERANDS_IN_BRACKETS, SEPARATOR + "$0" + SEPARATOR)
				.split(SEPARATOR);
	}

	private int calculateOperandsExpression(String[] operandsExpression) {
		String lastOperand = null;
		int leftOperandValue = 0;
		int rightOperandValue = 0;
		int valueExpression = 0;
		for (int indexOperand = 0; indexOperand < operandsExpression.length; indexOperand++) {
			String expressionInBreackets = getOperandWithoutBrackets(operandsExpression[indexOperand]);
			if (!isDigit(expressionInBreackets)) {
				if (isOperand(expressionInBreackets)) {
					lastOperand = expressionInBreackets;
				} else {
					operandsExpression[indexOperand] = String.valueOf(calculate(expressionInBreackets));
					stepsCalculation.add(operandsExpression[indexOperand]);
				}
			} else {
				if (indexOperand == 0) {
					leftOperandValue = calculate(expressionInBreackets);
				} else {
					rightOperandValue = calculate(expressionInBreackets);
				}

				if (isOperand(lastOperand)) {
					valueExpression = calculateSimpleExpression(leftOperandValue, rightOperandValue, lastOperand);
					leftOperandValue = valueExpression;
					lastOperand = null;
				}
			}
		}
		stepsCalculation.add(Arrays.toString(operandsExpression) + " => " + valueExpression);
		return valueExpression;
	}

	private int calculateSimpleExpression(int leftOperandValue, int rightOperandValue, String symbolOperand) {

		if (symbolOperand.equals("+")) {
			return leftOperandValue + rightOperandValue;
		} else if (symbolOperand.equals("-")) {
			return leftOperandValue - rightOperandValue;
		} else if (symbolOperand.equals("*")) {
			return leftOperandValue * rightOperandValue;
		} else if (symbolOperand.equals("/")) {
			return leftOperandValue / rightOperandValue;
		}
		return 0;
	}

	private String getOperandWithoutBrackets(String expression) {
		return expression.replaceAll(BRACKETS, "");
	}

	private String addGroupBrackets(String expression) {		
		String expressionWithBrackets = expression.replaceAll(GROUP_OPERANDS_WITH_DIV_MUL, "(" + "$0" + ")");
		stepsCalculation.add(expressionWithBrackets);
		return expressionWithBrackets;
	}

	private boolean isOperand(String value) {
		return value != null && value.length() == 1 && Pattern.compile(SIMPLE_OPERANDS).matcher(value).find();
	}

	private boolean isDigit(String value) {
		try {
			Integer.valueOf(value);
		} catch (NumberFormatException | NullPointerException e) {
			return false;
		}
		return true;
	}

	public List<String> getStepsCalculation() {				
		return stepsCalculation;
	}
}
