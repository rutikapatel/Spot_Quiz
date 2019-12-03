package com.example.spotquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.spotquiz.pojo.Quiz;
import com.example.spotquiz.pojo.QuizResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StudentResultsActivity extends AppCompatActivity {
private DatabaseReference mDatabase;
private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_results);

        mDatabase = FirebaseDatabase.getInstance().getReference("QuizResults");
        user = FirebaseAuth.getInstance().getCurrentUser();

        Quiz q =new Quiz();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //arrayList = new ArrayList<>();
                System.out.println("past"+dataSnapshot.getChildrenCount());
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    System.out.println("past"+child.getKey());
                        for(DataSnapshot childData:child.getChildren()) {
                            String key =q.getQuizName()+q.getCourseName();
                            if(child.getKey().equalsIgnoreCase(q.getQuizName()));
                            System.out.println("past"+childData.getValue());

                        }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
