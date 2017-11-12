# Expression calculator
Class for calculating simple arithmetic expressions getting from string.

Example:

```java
ExpressionCalculator calculator = new ExpressionCalculator();
try {
  calculator.calculateExpression("213+2-1-(3+5)/2-2+213+1-1");
  List<String> steps = calculator.getStepsCalculation(); 
  for (String step : steps) {
    System.out.println(step);
  }
} catch (ParseException e) {			
  // TODO
}
```
Console:
```batchfile
213+2-1-(3+5)/2-2+213+1-1
[3, +, 5] => 8
213+2-1-(8/2)-2+213+1-1
[8, /, 2] => 4
213+2-1-4-2+213+1-1
[213, +, 2, -, 1, -, 4, -, 2, +, 213, +, 1, -, 1] => 421
```
