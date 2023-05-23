package com.assignment1.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextInput; //edit TEXT VIEW FOR USER INPUT
    private TextView textHistory; //textview to display history
    private Button historyButton; //button toggling for history mode
    private StringBuilder calculationBuilder; //calculation text
    private boolean historyMode = false; //initial setting of history mode = standard
    private List<String> operationHistory; //operation history
    private Calculation calculator; //calculation class

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //super is base basically so parent oncreate function call
        setContentView(R.layout.activity_main); // sets the content view of the activity


        editTextInput = findViewById(R.id.editTextInput); // find the EditText view with the editTextInput id and assign it to the editTextInput variable
        textHistory = findViewById(R.id.textHistory); //find the textView view by id textHistory assign it to textHistory variable

        calculationBuilder = new StringBuilder(); //create a new string builder to build the calculation text
        operationHistory = new ArrayList<>(); //create a new array list instance to store history
        calculator = new Calculation(); //creates an instance of calculation class

        //listeners for all buttons
        setButtonClickListener(R.id.button_0); // Numeric Digits
        setButtonClickListener(R.id.button_1);
        setButtonClickListener(R.id.button_2);
        setButtonClickListener(R.id.button_3);
        setButtonClickListener(R.id.button_4);
        setButtonClickListener(R.id.button_5);
        setButtonClickListener(R.id.button_6);
        setButtonClickListener(R.id.button_7);
        setButtonClickListener(R.id.button_8);
        setButtonClickListener(R.id.button_9);
        setButtonClickListener(R.id.button_plus); // +
        setButtonClickListener(R.id.button_minus); // -
        setButtonClickListener(R.id.button_multiply); // *
        setButtonClickListener(R.id.button_divide); // /
        setButtonClickListener(R.id.button_equals); // =
        setButtonClickListener(R.id.button_c); // clear
        setButtonClickListener(R.id.button_Mode); //history

        historyButton = findViewById(R.id.button_Mode); //History Button
        setHistoryButtonText(); //display for the history button
    }

    private void setHistoryButtonText() { //display for the button itself
        if (historyMode) {
            historyButton.setText("Advance - With History Mode"); //ON
        } else {
            historyButton.setText("Standard - No History Mode"); //OFF
        }
    }

    private void setButtonClickListener(int buttonId) {
        Button button = findViewById(buttonId); //find the button view based on the buttonId
        button.setOnClickListener(this); //sets the click listiner for the buttton on current activity
    }

    @Override
    public void onClick(View view) {
        Button button = (Button) view;
        String buttonText = button.getText().toString();
        if (button.getId() == R.id.button_Mode) { //changes modes if history mode is clicked
            if (!historyMode) {
                historyMode = true;
                setHistoryButtonText();
            } else {
                historyMode = false;
                setHistoryButtonText();
            }
        }
        if (button.getId() == R.id.button_equals) { //if "=" is pressed
            String input = editTextInput.getText().toString();
            calculator.evaluateExpression(input, operationHistory, historyMode, calculationBuilder, editTextInput, textHistory);
        } else if (button.getId() == R.id.button_c) { //if clear is pressed
            calculator.clearInput(calculationBuilder, editTextInput);
        } else if (button.getId() != R.id.button_Mode) { //if history mode is not clicked
            calculator.appendToInput(buttonText, calculationBuilder, editTextInput); //basically just UI element
        }
    }
}