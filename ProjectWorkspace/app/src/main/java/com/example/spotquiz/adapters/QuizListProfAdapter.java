package com.example.spotquiz.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import com.example.spotquiz.QuestionAnswerActivity;
import com.example.spotquiz.QuestionCreationActivity;
import com.example.spotquiz.QuizConfirmationActivity;
import com.example.spotquiz.QuizCreationActivity;
import com.example.spotquiz.R;
import com.example.spotquiz.pojo.Quiz;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;



    //Card adapter to create cards
    public class QuizListProfAdapter extends BaseAdapter {
        Context context;
        ArrayList<Quiz> listOfQuizzes = new ArrayList<>();
        private DatabaseReference mDatabse;
        private FirebaseUser user;

        public QuizListProfAdapter(Context context, ArrayList<Quiz> listOfQuizzes) {
            this.context = context;
            this.listOfQuizzes =  listOfQuizzes;
        }

        @Override
        public int getCount() {
            return listOfQuizzes.size();
        }

        @Override
        public Object getItem(int position) {
            return listOfQuizzes.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }



        //Loop through  the list and set value for each card
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.quiz_prof_list_card, parent, false);
            }
            TextView name = convertView.findViewById(R.id.quizName);
            TextView location = convertView.findViewById(R.id.quizLocation);
            TextView date = convertView.findViewById(R.id.quizDate);
            Button activate = convertView.findViewById(R.id.btnActivate);
            Quiz q = (Quiz) this.getItem(position);
            name.setText(q.getQuizName());
            location.setText(q.getQuizLocation().getName());
            date.setText(q.getQuizDate());
            mDatabse = FirebaseDatabase.getInstance().getReference("quizzes");
            if(q.getActive()!= null && q.getActive()){
                activate.setText("De-Activate");
            }else{
                activate.setText("Activate");
            }
            System.out.println("card:"+q.getQuizName());

            activate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("clicked");
                    q.setActive(!q.getActive());
                    mDatabse.child(q.getQuizName()+q.getCourseName()+q.getQuizLocation().getName()+q.getProfessorId()).setValue(q);
                }
            });

            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("adapter clicked");

                }
            });


            return convertView;
        }



    }

