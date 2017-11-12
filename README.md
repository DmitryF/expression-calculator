# Expression calculator
Class for calculating simple arithmetic expressions getting from string.

Example
```java
ExpressionCalculator calculator = new ExpressionCalculator();
try {
  System.out.println(calculator.calculateExpression("213+2-1-(3+5)/2-2+213+1-1"));
} catch (ParseException e) {			
  // TODO
}
```
Console:
421
