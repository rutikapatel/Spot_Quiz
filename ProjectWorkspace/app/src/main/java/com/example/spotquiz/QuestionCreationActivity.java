package com.example.spotquiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spotquiz.adapters.Grid;
import com.example.spotquiz.adapters.NumberGridAdapter;
import com.example.spotquiz.pojo.Question;
import com.example.spotquiz.pojo.Quiz;
import com.example.spotquiz.pojo.QuizLocation;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class QuestionCreationActivity extends AppCompatActivity {

    private EditText question, option1, option2, option3, option4;
    private TextView questionNo,quizName;
    private Spinner answer;
    private Button create, next, previous;
    int i = 0;
    private DatabaseReference mDatabase;
    private NumberGridAdapter nga;
    private GridView gridView;
    private Quiz quiz;
    int noOfQuestions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_creation);

        quizName = findViewById(R.id.quizName);
        questionNo = findViewById(R.id.questionNo);
        question = findViewById(R.id.question);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        answer = findViewById(R.id.answer);

        Intent intent = getIntent();
        quiz = (Quiz)intent.getSerializableExtra("quiz");
        if(quiz != null){
            quizName.setText(quiz.getQuizName());
        }

        noOfQuestions = Integer.parseInt(quiz.getNoOfQuestions());
        gridView = findViewById(R.id.gridview);
        ArrayList<Grid> numbers = new ArrayList<>();
        for(int j=0;j<noOfQuestions;j++){
            Grid g = new Grid();
            g.setNumber(i+1);
            g.setFinished(false);
            numbers.add(new Grid());
        }
        nga = new NumberGridAdapter(this,numbers);
        gridView.setAdapter(nga);

        Grid h = (Grid) nga.getItem(0);
        h.change(ContextCompat.getColor(this,R.color.colorYellow));
        mDatabase = FirebaseDatabase.getInstance().getReference();

        next = findViewById(R.id.btnNext);
        previous = findViewById(R.id.btnPrevious);
        create = findViewById(R.id.btnCreate);

        previous.setVisibility(View.INVISIBLE);
        ArrayList<Question> list = new ArrayList<>();


        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean valid = true;
                if( question.getText().toString().isEmpty() ){
                    question.setError("Please enter your question");
                    valid = false;
                }if( option1.getText().toString().isEmpty() ){
                    option1.setError("Please enter this option");
                    valid = false;
                }if( option2.getText().toString().isEmpty() ){
                    option2.setError("Please enter this option");
                    valid = false;
                } if( option3.getText().toString().isEmpty() ){
                    option3.setError("Please enter this option");
                    valid = false;
                } if( option4.getText().toString().isEmpty() ){
                    option4.setError("Please enter this option");
                    valid = false;
                }if(valid) {
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

                    quiz.setQuestions(list);
                    //  mDatabase.child("Questions").child("quiz1").setValue(list);
                    mDatabase.child("quizzes").child(quiz.getQuizName() + quiz.getCourseName() + quiz.getQuizLocation().getName() + quiz.getProfessorId()).setValue(quiz);
                    startActivity(new Intent(QuestionCreationActivity.this, ProfessorHomeActivity.class));
                    finish();

                  //  Toast.makeText(QuestionCreationActivity.this, list.get(0).getQuestion(), Toast.LENGTH_SHORT);
                }
            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean valid = true;
                if( question.getText().toString().isEmpty() ){
                    question.setError("Please enter your question");
                    valid = false;
                }if( option1.getText().toString().isEmpty() ){
                    option1.setError("Please enter this option");
                    valid = false;
                }if( option2.getText().toString().isEmpty() ){
                    option2.setError("Please enter this option");
                    valid = false;
                } if( option3.getText().toString().isEmpty() ){
                    option3.setError("Please enter this option");
                    valid = false;
                } if( option4.getText().toString().isEmpty() ){
                    option4.setError("Please enter this option");
                    valid = false;
                }

                if(valid) {
                    Question q = new Question();
                    q.setQuestion(question.getText().toString());

                    ArrayList<String> options = new ArrayList<>();
                    options.add(option1.getText().toString());
                    options.add(option2.getText().toString());
                    options.add(option3.getText().toString());
                    options.add(option4.getText().toString());
                    q.setOptions(options);

                    q.setCorrectAnswer(Integer.parseInt(answer.getSelectedItem().toString()));

                    try {
                        if (list.get(i) !=null) {
                            Question  temp = list.get(i);
                            temp =q;
                        }
                    }catch(IndexOutOfBoundsException e){
                        list.add(q);
                    }

                    Grid h = (Grid) nga.getItem(i);
                    h.setFinished(true);
                    h.change(ContextCompat.getColor(QuestionCreationActivity.this, R.color.colorGreen));
                    i++;
                    h = (Grid) nga.getItem(i);
                    h.change(ContextCompat.getColor(QuestionCreationActivity.this, R.color.colorYellow));

                    Grid g = (Grid) nga.getItem(0);
                    g.change(ContextCompat.getColor(QuestionCreationActivity.this, R.color.colorGreen));

                    questionNo.setText("Question" + (i + 1));

                    try{
                        if (list.get(i) !=null) {
                            Question p = list.get(i);
                            question.setText(p.getQuestion());
                            option1.setText(p.getOptions().get(0));
                            option2.setText(p.getOptions().get(1));
                            option3.setText(p.getOptions().get(2));
                            option4.setText(p.getOptions().get(3));
                            answer.setSelection(p.getCorrectAnswer()-1);
                        }
                    }catch(IndexOutOfBoundsException e){
                        question.getText().clear();
                        option1.getText().clear();
                        option2.getText().clear();
                        option3.getText().clear();
                        option4.getText().clear();
                        answer.setSelection(0);
                    }
                    /*if(list.get(i)==null) {
                        Question p = list.get(i);
                        question.setText(p.getQuestion());
                        option1.setText(p.getOptions().get(0));
                        option2.setText(p.getOptions().get(1));
                        option3.setText(p.getOptions().get(2));
                        option4.setText(p.getOptions().get(3));
                        answer.setSelection(p.getCorrectAnswer()-1);
                    }else {
                        question.getText().clear();
                        option1.getText().clear();
                        option2.getText().clear();
                        option3.getText().clear();
                        option4.getText().clear();
                        answer.setSelection(0);
                    }*/
                    question.requestFocus();
                    System.out.println(i);
                    System.out.print(noOfQuestions);
                    if (noOfQuestions == (i + 1)) {
                        System.out.println("end");
                        create.setVisibility(View.VISIBLE);
                        next.setVisibility(View.INVISIBLE);
                    }

                    previous.setVisibility(View.VISIBLE);


                    Toast.makeText(QuestionCreationActivity.this, list.get(0).getQuestion(), Toast.LENGTH_SHORT);
                }
            }
        });


        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(i==1){
                    previous.setVisibility(View.INVISIBLE);
                }
                if(noOfQuestions == (i+1)){
                    create.setVisibility(View.INVISIBLE);
                    next.setVisibility(View.VISIBLE);
                }



                Grid h = (Grid) nga.getItem(i);
                System.out.println("objchk"+h.getFinished());
                if(  h.getFinished()!=null && h.getFinished() ){
                    h.change(ContextCompat.getColor(QuestionCreationActivity.this,R.color.colorGreen));
                }else{
                    h.change(ContextCompat.getColor(QuestionCreationActivity.this,R.color.colorGrey));


                }
                i--;
                h = (Grid) nga.getItem(i);
                h.change(ContextCompat.getColor(QuestionCreationActivity.this,R.color.colorYellow));
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_logout,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        startActivity(new Intent(this,LoginActivity.class));
        finish();
        return true;
    }
}
