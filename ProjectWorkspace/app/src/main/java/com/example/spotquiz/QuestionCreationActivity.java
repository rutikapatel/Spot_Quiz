package com.example.spotquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spotquiz.pojo.Question;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class QuestionCreationActivity extends AppCompatActivity {

    private EditText question, option1, option2, option3, option4;
    private TextView questionNo;
    private Spinner answer;
    private Button create, next, previous;
    int i = 0;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_creation);

        questionNo = findViewById(R.id.questionNo);
        question = findViewById(R.id.question);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        answer = findViewById(R.id.answer);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        next = findViewById(R.id.btnNext);
        previous = findViewById(R.id.btnPrevious);
        create = findViewById(R.id.btnCreate);

        previous.setVisibility(View.INVISIBLE);
        ArrayList<Question> list = new ArrayList<>(10);


        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDatabase.child("Questions").child("quiz1").setValue(list);
                Toast.makeText(QuestionCreationActivity.this, list.get(0).getQuestion(), Toast.LENGTH_SHORT);
            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Question q = new Question();
                q.setQuestion(question.getText().toString());

                ArrayList<String> options = new ArrayList<>();
                options.add(option1.getText().toString());
                options.add(option2.getText().toString());
                options.add(option3.getText().toString());
                options.add(option4.getText().toString());
                q.setOptions(options);

                q.setCorrectAnswer(Integer.parseInt(answer.getSelectedItem().toString()));

                list.add(q);
                i++;

                questionNo.setText("Question"+ (i+1));
                question.getText().clear();
                option1.getText().clear();
                option2.getText().clear();
                option3.getText().clear();
                option4.getText().clear();
                answer.setSelection(0);

                question.requestFocus();

                previous.setVisibility(View.VISIBLE);


                Toast.makeText(QuestionCreationActivity.this, list.get(0).getQuestion(), Toast.LENGTH_SHORT);
            }
        });


        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(i==1){
                    previous.setVisibility(View.INVISIBLE);
                }
                i--;
                questionNo.setText("Question"+ (i+1));

                Question q = list.get(i);
                question.setText(q.getQuestion());
                option1.setText(q.getOptions().get(0));
                option2.setText(q.getOptions().get(1));
                option3.setText(q.getOptions().get(2));
                option4.setText(q.getOptions().get(3));
                answer.setSelection(q.getCorrectAnswer()-1);

                Toast.makeText(QuestionCreationActivity.this, list.get(0).getQuestion(), Toast.LENGTH_SHORT);
            }
        });


    }
}
