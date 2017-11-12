package test.com.dzmitryf.expression;

import static org.mockito.Mockito.*;

import java.text.ParseException;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.*;
import com.dzmitryf.expression.ExpressionCalculator;

public class ExpressionCalculatorTest {

	
	@Test
	public void calculateExpression()
	{
		ExpressionCalculator calculator = new ExpressionCalculator();
		try {
			calculator.calculateExpression("213+2-1-(3+5)/2-2+213+1-1");
		} catch (ParseException e) {			
			fail("Calculate expression test failed: " + e.getLocalizedMessage());
		}
		
		List<String> steps = calculator.getStepsCalculation(); 
		for (String step : steps) {
			System.out.println(step);
		}
	}
	
	@Test(expected=ParseException.class)
	public void invalidSyntax() throws ParseException {
		
		ExpressionCalculator calculator = new ExpressionCalculator();								
		
		checkSyntax(calculator, "213qwe");
		checkSyntax(calculator, "213(");
		
		throw new ParseException("", 0);
	}
	
	private void checkSyntax(ExpressionCalculator calculator, String expression) {
		try {
			calculator.calculateExpression(expression);
		} catch (ParseException e) {
			return;
		}				
		fail("\"Invalid syntax test\" failed: " + expression);
	}
}
