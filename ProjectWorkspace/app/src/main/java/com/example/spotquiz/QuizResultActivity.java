package com.example.spotquiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spotquiz.pojo.QuizResult;



public class QuizResultActivity extends AppCompatActivity {
private TextView quiz,score;
private QuizResult result;
private Button finish;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);


        quiz = findViewById(R.id.txtQuiz);
        score = findViewById(R.id.txtScore);
        finish = findViewById(R.id.button);
        Intent intent = getIntent();

        result = (QuizResult) intent.getSerializableExtra("result");

        quiz.setText(result.getQuizName());
        score.setText(result.getResult());

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(QuizResultActivity.this, StudentHomeActivity.class);
                startActivity(i);
            }
        });



    }
}
