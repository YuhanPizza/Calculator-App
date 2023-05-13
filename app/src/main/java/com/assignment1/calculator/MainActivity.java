package com.assignment1.calculator;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /*
             Initialize your UI component
         */

          /*
             Attach action listener for button
            */


    }

    @Override
    public void onClick(View v) {
        // Handle button clicks here
        int id = v.getId();

        /*
        Perform operations based on your button ID.
         */
    }

}