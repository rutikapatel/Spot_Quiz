package com.example.spotquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.spotquiz.adapters.QuizListProfAdapter;
import com.example.spotquiz.pojo.Question;
import com.example.spotquiz.pojo.Quiz;
import com.example.spotquiz.pojo.QuizLocation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProfessorHomeActivity extends AppCompatActivity {
    Button btnquiz;
    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private QuizListProfAdapter cardAdapter;

    private ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_home);

        btnquiz = findViewById(R.id.btnquiz);
        list = findViewById(R.id.quizList);
        mDatabase = FirebaseDatabase.getInstance().getReference("quizzes");


        user = FirebaseAuth.getInstance().getCurrentUser();

        btnquiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ProfessorHomeActivity.this, QuizCreationActivity.class));
            }
        });

        getQuiz();

    }

    private void getQuiz() {

        ArrayList<Quiz> quizList = new ArrayList<>();
        //System.out.println(todayDate);
        mDatabase.orderByChild("professorId").equalTo(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childData : dataSnapshot.getChildren()) {
                    System.out.println("keys" + childData.getKey());

                    QuizLocation quizLocation = new QuizLocation(childData.child("quizLocation").child("name").getValue().toString(),
                            Double.parseDouble(childData.child("quizLocation").child("longitude").getValue().toString()),
                            Double.parseDouble(childData.child("quizLocation").child("latitude").getValue().toString())
                    );


                    ArrayList<Question> questions = new ArrayList<>();

                    for(int i = 0;i<childData.child("questions").getChildrenCount();i++){
                        ArrayList<String> options = new ArrayList<>();
                        Question question = new Question();
                        for(int j=0;j<4;j++)
                        options.add(childData.child("questions").child(Integer.toString(i)).child("options").child(Integer.toString(j)).getValue().toString());
                        question.setQuestion(childData.child("questions").child(Integer.toString(i)).child("question").getValue().toString());
                        question.setCorrectAnswer(Integer.parseInt(childData.child("questions").child(Integer.toString(i)).child("correctAnswer").getValue().toString()));
                        question.setOptions(options);
                        questions.add(question);
                    }
                    Quiz q = new Quiz(childData.child("quizName").getValue().toString(),
                            childData.child("courseName").getValue().toString(),
                            childData.child("noOfQuestions").getValue().toString(),
                            childData.child("quizKey").getValue().toString(),
                            childData.child("quizDate").getValue().toString(),
                            childData.child("quizStartTime").getValue().toString(),
                            childData.child("quizLength").getValue().toString(),
                            quizLocation,
                            Boolean.parseBoolean(childData.child("active").getValue().toString()),
                            questions);
                    System.out.println("pish" + q.getQuizName());
                    quizList.add(q);
                }
                createQuizList(quizList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    void createQuizList(ArrayList<Quiz> quizList){
        cardAdapter = new QuizListProfAdapter(this,quizList);
        list.setAdapter(cardAdapter);
    }
}
