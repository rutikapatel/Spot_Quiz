package com.example.spotquiz.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.spotquiz.QuestionCreationActivity;
import com.example.spotquiz.QuizConfirmationActivity;
import com.example.spotquiz.R;
import com.example.spotquiz.listeners.QuestionChangeListener;


import java.util.ArrayList;

public class NumberGridAdapter extends BaseAdapter {

    Context context;
    ArrayList<Grid> listNumber = new ArrayList<>();

    public NumberGridAdapter(Context context, ArrayList<Grid> listNumber) {
        this.context = context;
        this.listNumber =  listNumber;
    }

    @Override
    public int getCount() {
        return listNumber.size();
    }

    @Override
    public Object getItem(int position) {
        return listNumber.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    //Loop through  the list and set value for each card
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView name = new TextView(context);
        name.setText(String.valueOf(position));
        name.setBackgroundColor(ContextCompat.getColor(context,R.color.colorGrey));

        listNumber.get(position).setOnQuestionChange(new QuestionChangeListener() {
            @Override
            public void onQuestionChange(int color) {
                System.out.println("colorChange"+color+"posotio"+position);
                name.setBackgroundColor(color);
                if(color == ContextCompat.getColor(context,R.color.colorYellow)){
                    if(position!=0)
                    listNumber.get(position-1).setFinished(true);
                }

            }
        });
        return name;

    }
}
