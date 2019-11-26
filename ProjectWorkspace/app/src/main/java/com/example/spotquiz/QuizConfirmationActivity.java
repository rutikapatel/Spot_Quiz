package com.example.spotquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.spotquiz.pojo.Quiz;

public class QuizConfirmationActivity extends AppCompatActivity {
    private Quiz quiz;
    private TextView quizName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_confirmation);
        quizName = findViewById(R.id.quizName);

        Intent intent = getIntent();
        quiz  = (Quiz)intent.getSerializableExtra("quiz");
        if(quiz != null){
            quizName.setText(quiz.getQuizName());
        }
    }
}
