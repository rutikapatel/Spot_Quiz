package com.example.spotquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.spotquiz.pojo.QuizLocation;
import com.google.android.material.textfield.TextInputLayout;

public class QuizCreationActivity extends AppCompatActivity {
    private TextInputLayout chooseLocation ;
    EditText quizLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_creation);
        chooseLocation = findViewById(R.id.boxquizLocation);
        quizLocation =  findViewById(R.id.quizLocation);
        chooseLocation.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        QuizLocation selectedLocation = (QuizLocation)intent.getSerializableExtra("location");
        if(selectedLocation != null){
            quizLocation.setText(selectedLocation.getName());
        }
    }
}
