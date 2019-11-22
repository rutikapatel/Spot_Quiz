package com.example.spotquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.spotquiz.pojo.Quiz;
import com.example.spotquiz.pojo.QuizLocation;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class QuizCreationActivity extends AppCompatActivity {
    private TextInputLayout chooseLocation ;
    private EditText quizName,course,quizKey,quizLocation;
    private Spinner noOfQuestions;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private QuizLocation selectedLocation;
    private Button create;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_creation);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        quizName =  findViewById(R.id.quizName);
        course =  findViewById(R.id.courseName);
        quizKey =  findViewById(R.id.quizPassword);

        noOfQuestions =  findViewById(R.id.noOfQuestions);

        chooseLocation = findViewById(R.id.boxquizLocation);
        quizLocation =  findViewById(R.id.quizLocation);

        create = findViewById(R.id.btnCreate);
        chooseLocation.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        selectedLocation = (QuizLocation)intent.getSerializableExtra("location");
        if(selectedLocation != null){
            quizLocation.setText(selectedLocation.getName());
        }


        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createQuiz();
            }
        });
    }

    void createQuiz(){
        Quiz quiz = new Quiz();
        quiz.setQuizName(quizName.getText().toString());
        quiz.setCourseName(course.getText().toString());
        quiz.setQuizKey(quizKey.getText().toString());
        quiz.setNoOfQuestions(noOfQuestions.getSelectedItem().toString());
        quiz.setQuizLocation(selectedLocation);
        mDatabase.child("quizzes").child(quiz.getQuizName()+quiz.getCourseName()+quiz.getQuizLocation().getName()).setValue(quiz);
    }
}
