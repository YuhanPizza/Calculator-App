package com.assignment1.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.webkit.ValueCallback;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextInput;
    private TextView textHistory;
    private Button historyButton;
    private StringBuilder calculationBuilder;
    private boolean historyMode = false;
    private List<String> operationHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextInput = findViewById(R.id.editTextInput);
        textHistory = findViewById(R.id.textHistory);

        calculationBuilder = new StringBuilder();
        operationHistory = new ArrayList<>();

        // Set click listeners for all buttons
        setButtonClickListener(R.id.button_0);
        setButtonClickListener(R.id.button_1);
        setButtonClickListener(R.id.button_2);
        setButtonClickListener(R.id.button_3);
        setButtonClickListener(R.id.button_4);
        setButtonClickListener(R.id.button_5);
        setButtonClickListener(R.id.button_6);
        setButtonClickListener(R.id.button_7);
        setButtonClickListener(R.id.button_8);
        setButtonClickListener(R.id.button_9);
        setButtonClickListener(R.id.button_plus);
        setButtonClickListener(R.id.button_minus);
        setButtonClickListener(R.id.button_multiply);
        setButtonClickListener(R.id.button_divide);
        setButtonClickListener(R.id.button_equals);
        setButtonClickListener(R.id.button_c);
        setButtonClickListener(R.id.button_Mode);

        historyButton = findViewById(R.id.button_Mode);
        setHistoryButtonText();
    }
    private void setHistoryButtonText() {
        if (historyMode) {
            historyButton.setText("Advance - With History Mode");
        } else {
            historyButton.setText("Standard - No History Mode");
        }
    }

    private void setButtonClickListener(int buttonId) {
        Button button = findViewById(buttonId);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Button button = (Button) view;
        String buttonText = button.getText().toString();
        if(button.getId() == R.id.button_Mode){
            if (!historyMode){
                historyMode = true;
                setHistoryButtonText();
            }else {
                historyMode = false;
                setHistoryButtonText();
            }
        }
        if (button.getId() == R.id.button_equals) {
            calculateResult();
        } else if (button.getId() == R.id.button_c) {
            clearInput();
        } else if(button.getId()!= R.id.button_Mode) {
            appendToInput(buttonText);
        }
    }

    private void appendToInput(String text) {
        calculationBuilder.append(text);
        editTextInput.setText(calculationBuilder.toString());
    }

    private void calculateResult() {
        String input = editTextInput.getText().toString();

        // Check if the input is not empty
        if (!input.isEmpty()) {
            if(hasInvalidOperators(input)){
                editTextInput.setText("Invalid Operator");
                return;
            }
            try {
                // Evaluate the expression using built-in JavaScript engine
                String result = performOperation(input);

                // Clear the calculation builder and display the result
                calculationBuilder.setLength(0);
                calculationBuilder.append(result);
                editTextInput.setText(calculationBuilder.toString());
                if(historyMode) {
                    operationHistory.add(input + '=' + result);
                }
                displayHistory();
            } catch (Exception e) {
                // Handle any error that occurs during the evaluation
                editTextInput.setText("Invalid Operation");
            }
        }
    }

    private String performOperation(String expression) {
        // Remove any spaces from the expression
        expression = expression.replaceAll("\\s", "");

        // Initialize the result to the first operand
        int result = 0;
        int currentOperand = 0;
        char lastOperator = '+';

        // Process each character in the expression
        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);

            if (Character.isDigit(ch)) {
                // Build the current operand by appending digits
                currentOperand = (currentOperand * 10) + (ch - '0');
            } else {
                // Apply the last operator to the result and current operand
                result = applyOperator(result, currentOperand, lastOperator);

                // Reset the current operand and update the last operator
                currentOperand = 0;
                lastOperator = ch;
            }
        }

        // Apply the last operator to the result and final operand
        result = applyOperator(result, currentOperand, lastOperator);

        // Convert the result to a string
        String answer = String.valueOf(result);
        return answer;
    }

    private int applyOperator(int operand1, int operand2, char operator) {
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
    private boolean hasInvalidOperators(String input) {
        char lastChar = input.charAt(input.length() - 1);
        for (int i = 1; i < input.length(); i++) {
            char currentChar = input.charAt(i);
            char previousChar = input.charAt(i - 1);
            if ((isOperator(lastChar) && !Character.isDigit(previousChar)) || (isOperator(currentChar) && isOperator(previousChar))) {
                return true;
            }
        }
        return false;
    }
    private boolean isOperator(char ch) {
        return ch == '+' || ch == '-' || ch == '*' || ch == '/';
    }
    private void displayHistory() {
        if (historyMode) {
            StringBuilder stringBuilder = new StringBuilder();

            for (String operation : operationHistory) {
                stringBuilder.append(operation).append("\n");
            }

            textHistory.setText(stringBuilder.toString());
        }
    }
    private void clearInput() {
        calculationBuilder.setLength(0);
        editTextInput.setText("");
    }
}