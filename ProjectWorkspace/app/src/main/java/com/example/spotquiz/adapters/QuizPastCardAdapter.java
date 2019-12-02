package com.example.spotquiz.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.spotquiz.R;
import com.example.spotquiz.pojo.QuizResult;

import java.util.ArrayList;

public class QuizPastCardAdapter extends BaseAdapter {
    Context context;
    ArrayList<QuizResult> listOfQuizzes = new ArrayList<>();

    public QuizPastCardAdapter(Context context, ArrayList<QuizResult> listOfQuizzes) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.quiz_past_list_adapter, parent, false);
        }
        TextView name = convertView.findViewById(R.id.quizName);
        TextView score = convertView.findViewById(R.id.quizScore);

        QuizResult q = (QuizResult) this.getItem(position);
        name.setText(q.getQuizName());
        score.setText(q.getResult());
        System.out.println("card:"+q.getQuizName());


        return convertView;
    }



}
