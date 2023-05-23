package com.assignment1.calculator;

import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Calculation {
    public void appendToInput(String text, StringBuilder calculationBuilder, EditText editTextInput) {
        calculationBuilder.append(text);
        editTextInput.setText(calculationBuilder.toString()); //editTextInput view display
    }

    public void evaluateExpression(String input, List<String> operationHistory, boolean historyMode, StringBuilder calculationBuilder, EditText editTextInput, TextView textHistory) {
        List<String> expressionList = prepareExpressionList(input); //call the prepareExpressionList function, which converts the inpu string into a list of operands and operators
        if (isValidExpression(expressionList)) { //checks if its a validExpression
            String result = evaluate(expressionList); //calls evaluate method you know what it does.
            if (result != null) {  //if result is not null from the evaluate function
                calculationBuilder.setLength(0); //clears the calculationbuilder
                calculationBuilder.append(result); //appends the result to calculation builder
                editTextInput.setText(calculationBuilder.toString()); //sets the editTextInput to display the result
                if (historyMode) { //if history mode is enabled
                    operationHistory.add(input + " = " + result); //adds the input + = + result to the history
                }
                displayHistory(operationHistory, historyMode, textHistory); //displayHistory function
            } else { //if null
                editTextInput.setText("Invalid Operation");
            }
        } else { //if failed validexpression check.
            editTextInput.setText("Invalid Expression");
        }
    }


    private List<String> prepareExpressionList(String input) {
        List<String> expressionList = new ArrayList<>(); // create new  ArrayList
        StringBuilder operandBuilder = new StringBuilder(); //c create stringbuilder

        for (char ch : input.toCharArray()) { //iterates over each char in the input string
            if (Character.isDigit(ch)) { //chceks if the char is a digit
                operandBuilder.append(ch); //append the digit to the operandbuilder
            } else { //if not a digit
                if (operandBuilder.length() > 0) { //check operanbuilder has any chars
                    expressionList.add(operandBuilder.toString()); //add the operand to the expressionlist
                    operandBuilder.setLength(0); //clear operandbuilder
                }
                expressionList.add(String.valueOf(ch));// add the current character as a separate element to the expressionList
            }
        }

        if (operandBuilder.length() > 0) { // checks if there is an operand remaining in the operandBuilder
            expressionList.add(operandBuilder.toString()); //add the remaining operand to the expression list
        }

        return expressionList; //returns the expressionlist
    }

    private boolean isValidExpression(List<String> expressionList) {
        if (expressionList.isEmpty())
            return false;

        // Check consecutive operators and operators at the beginning or end
        String operators = "+-*/";
        String firstElement = expressionList.get(0);
        String lastElement = expressionList.get(expressionList.size() - 1);

        if (operators.contains(firstElement) || operators.contains(lastElement))
            return false;

        for (int i = 0; i < expressionList.size() - 1; i++) {
            String current = expressionList.get(i);
            String next = expressionList.get(i + 1);

            if (operators.contains(current) && operators.contains(next))
                return false;
        }

        return true;
    }

    private String evaluate(List<String> expressionList) {
        Stack<Integer> operandStack = new Stack<>();
        Stack<Character> operatorStack = new Stack<>();

        for (String element : expressionList) {
            if (isNumeric(element)) { //checks if the element is a numeric value
                operandStack.push(Integer.parseInt(element)); // pushes the parsed numeric value to the operand stack
            } else if (isOperator(element)) { //checks if the element is an operator
                char operator = element.charAt(0); // gets the operator charcter
                // checks for  higher precedence
                while (!operatorStack.isEmpty() && hasPrecedence(operator, operatorStack.peek())) { //loops if there are still operators and if the
                    int operand2 = operandStack.pop(); //retrieve the 2nd operand                  //current operator has highier presidence than the one at the top of the stack
                    int operand1 = operandStack.pop();//retrieve the 1st operand
                    char op = operatorStack.pop(); //retrieve operator
                    int result = applyOperator(operand1, operand2, op); //calls for applyOperator Function
                    operandStack.push(result); //pushes the calculated result back into the operand stack
                }

                operatorStack.push(operator); //pushes the operator to the stack
            }
        }
        //evaluates remaining operators in the stack
        while (!operatorStack.isEmpty()) {
            int operand2 = operandStack.pop();
            int operand1 = operandStack.pop();
            char operator = operatorStack.pop();
            int result = applyOperator(operand1, operand2, operator);
            operandStack.push(result);
        }

        if (operandStack.isEmpty()) {
            return null; // incase the operand stack is empty, return null (invalid operation)
        } else {
            return String.valueOf(operandStack.pop()); //converts to string and returns it
        }
    }

    private boolean isNumeric(String str) { //checks for number
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isOperator(String str) { //operator checker
        String operators = "+-*/"; //accepted operators
        return str.length() == 1 && operators.contains(str);
    }

    private boolean hasPrecedence(char op1, char op2) { //basically checks if the first operator has a highier presidence than the 2nd operator
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-')) {
            return false; //if op1 has highier presidence
        }
        return true; //if op2 has highier presidence or equal presidence
    }

    private int applyOperator(int operand1, int operand2, char operator) { //operations
        switch (operator) {
            case '+':
                return operand1 + operand2;
            case '-':
                return operand1 - operand2;
            case '*':
                return operand1 * operand2;
            case '/':
                return operand1 / operand2;
            default:
                return 0;
        }
    }
    //display for history
    private void displayHistory(List<String> operationHistory, boolean historyMode, TextView textHistory) {
        if (historyMode) { //if history mode is enabled
            StringBuilder stringBuilder = new StringBuilder();

            for (String operation : operationHistory) {
                stringBuilder.append(operation).append("\n");
            }

            textHistory.setText(stringBuilder.toString());
        }
    }
    //clearInput
    public void clearInput(StringBuilder calculationBuilder, EditText editTextInput) {
        calculationBuilder.setLength(0);
        editTextInput.setText("");
    }
}