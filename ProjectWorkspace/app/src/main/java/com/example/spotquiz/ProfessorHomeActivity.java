package com.example.spotquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ProfessorHomeActivity extends AppCompatActivity {
Button btnquiz;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_home);

        btnquiz = findViewById(R.id.btnquiz);

        btnquiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ProfessorHomeActivity.this, QuizCreationActivity.class));
            }
        });
    }
}
