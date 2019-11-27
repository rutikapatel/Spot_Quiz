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

import com.example.spotquiz.QuizConfirmationActivity;
import com.example.spotquiz.QuizCreationActivity;
import com.example.spotquiz.R;
import com.example.spotquiz.pojo.Quiz;

import org.w3c.dom.Text;

import java.util.ArrayList;



//Card adapter to create cards
public class QuizListCardAdapter extends BaseAdapter {
    Context context;
    ArrayList<Quiz> listOfQuizzes = new ArrayList<>();

    public QuizListCardAdapter(Context context, ArrayList<Quiz> listOfQuizzes) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.quiz_list_card_view, parent, false);
        }
        TextView name = convertView.findViewById(R.id.quizName);
        TextView time = convertView.findViewById(R.id.quizStartTime);
        Button startQuiz = convertView.findViewById(R.id.btnStartQuiz);
        Quiz q = (Quiz) this.getItem(position);
        name.setText(q.getQuizName());
        time.setText(q.getQuizStartTime());
        System.out.println("card:"+q.getQuizName());

        startQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("clicked");
                Intent intent = new Intent(context, QuizConfirmationActivity.class);
                intent.putExtra("quiz",q);
                context.startActivity(intent);
            }
        });
        return convertView;
    }



}
