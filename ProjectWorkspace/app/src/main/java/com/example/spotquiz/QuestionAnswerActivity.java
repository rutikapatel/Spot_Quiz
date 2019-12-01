package com.example.spotquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spotquiz.adapters.Grid;
import com.example.spotquiz.adapters.NumberGridAdapter;
import com.example.spotquiz.pojo.Question;
import com.example.spotquiz.pojo.Quiz;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class QuestionAnswerActivity extends AppCompatActivity {

    private TextView q_number, q_description, option_1, option_2, option_3, option_4;
    private Button next, previous, submit;
    int i = 0;
    private DatabaseReference mDatabase;
    private NumberGridAdapter nga;
    private GridView gridView;
    private TextView txtTimer;

    private ArrayList<String> quesnum = new ArrayList<>();
    private ArrayList<String> quesdesc = new ArrayList<>();
    private ArrayList<ArrayList<String>> quesopt = new ArrayList<>();
    private ArrayList<String> correctans = new ArrayList<>();
    private String givenans[] = new String[15];

    private RadioButton radioButton;
    private RadioGroup radioGroup;

    private CountDownTimer timer;
    private long minutes, seconds = 0;

    private Quiz sq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_page);

        q_number = findViewById(R.id.q_number);
        q_description = findViewById(R.id.q_description);
        option_1 = findViewById(R.id.option_1);
        option_2 = findViewById(R.id.option_2);
        option_3 = findViewById(R.id.option_3);
        option_4 = findViewById(R.id.option_4);
        radioGroup = findViewById(R.id.radio);
        next = findViewById(R.id.btnNext);
        previous = findViewById(R.id.btnPrevious);
        submit = findViewById(R.id.btnSubmit);

        txtTimer = findViewById(R.id.timer);

        gridView = findViewById(R.id.gridview);
        ArrayList<Grid> numbers = new ArrayList<>();

        for(int j=0;j<10;j++)
        {
            Grid g = new Grid();
            g.setNumber(i+1);
            g.setFinished(false);
            numbers.add(new Grid());
        }

        nga = new NumberGridAdapter(this,numbers);
        gridView.setAdapter(nga);

        Grid h = (Grid) nga.getItem(0);
        h.change(ContextCompat.getColor(this,R.color.colorYellow));
        mDatabase = FirebaseDatabase.getInstance().getReference("quizzes");

        previous.setVisibility(View.INVISIBLE);

        startTimer();

       /* mDatabase.equalTo("Quiz1").addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        for(DataSnapshot childData : dataSnapshot.getChildren())
                        {

                                for(int j = 0; j < childData.getChildrenCount(); j++)
                                {
                                    String index = Integer.toString(j);
                                    quesnum.add(index);
                                    quesdesc.add(childData.child(index).child("question").getValue().toString());
//                                    if(childData.child(index).child("type").getValue().toString() == "MCQ")
//                                    {
                                        for(int k=0; k<4; k++)
                                        {
                                            ArrayList<String> option = new ArrayList<>();
                                            String optindex = Integer.toString(k);
                                            option.add(childData.child(index).child("options").child(optindex).getValue().toString());
                                            quesopt.add(option);
                                        }
//                                    }
                                    correctans.add(childData.child(index).child("correctAnswer").getValue().toString());
                                }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });*/
                Intent intent = getIntent();
                sq  = (Quiz)intent.getSerializableExtra("quiz");



                ArrayList<Question> questions = sq.getQuestions();

                for(int j = 0;j<questions.size();j++){
                    quesnum.add(Integer.toString(j));
                    quesdesc.add(questions.get(j).getQuestion());
                    correctans.add(Integer.toString(questions.get(j).getCorrectAnswer()));
                    quesopt.add(questions.get(j).getOptions());

                }



                q_number.setText(Integer.toString(Integer.parseInt(quesnum.get(i)) + 1));
                q_description.setText(quesdesc.get(i));
                option_1.setText(quesopt.get(i).get(0));
                option_2.setText(quesopt.get(i).get(1));
                option_3.setText(quesopt.get(i).get(2));
                option_4.setText(quesopt.get(i).get(3));

                //int selectedId = radioGroup.getCheckedRadioButtonId();
                //radioButton = findViewById(selectedId);
                //givenans[i] = radioButton.getText().toString();




        next.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Grid h = (Grid) nga.getItem(i);
                h.setFinished(true);
                h.change(ContextCompat.getColor(QuestionAnswerActivity.this, R.color.colorGreen));
                i++;
                h = (Grid) nga.getItem(i);
                h.change(ContextCompat.getColor(QuestionAnswerActivity.this, R.color.colorYellow));

                if (i>1) radioButton.setChecked(false);

                previous.setVisibility(View.VISIBLE);
                if(i==4) next.setVisibility(View.INVISIBLE);

                q_number.setText((Integer.toString(Integer.parseInt(quesnum.get(i)) + 1)));
                q_description.setText(quesdesc.get(i));
                option_1.setText(quesopt.get(i).get(0));
                option_2.setText(quesopt.get(i).get(1));
                option_3.setText(quesopt.get(i).get(2));
                option_4.setText(quesopt.get(i).get(3));

                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(selectedId);
                givenans[i] = radioButton.getText().toString();

//                Toast.makeText(QuestionAnswerActivity.this, list.get(0).getQuestion(), Toast.LENGTH_SHORT);
            }
        });


        previous.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Grid h = (Grid) nga.getItem(i);

                System.out.println("objchk"+h.getFinished());

                if(  h.getFinished()!=null && h.getFinished() ){
                    h.change(ContextCompat.getColor(QuestionAnswerActivity.this,R.color.colorGreen));
                }else{
                    h.change(ContextCompat.getColor(QuestionAnswerActivity.this,R.color.colorGrey));
                }
                i--;
                h = (Grid) nga.getItem(i);
                h.change(ContextCompat.getColor(QuestionAnswerActivity.this,R.color.colorYellow));

                next.setVisibility(View.VISIBLE);
                if(i==0) previous.setVisibility(View.INVISIBLE);
                else previous.setVisibility(View.VISIBLE);

                q_number.setText((Integer.toString(Integer.parseInt(quesnum.get(i)) + 1)));
                q_description.setText(quesdesc.get(i));
                option_1.setText(quesopt.get(i).get(0));
                option_2.setText(quesopt.get(i).get(1));
                option_3.setText(quesopt.get(i).get(2));
                option_4.setText(quesopt.get(i).get(3));

                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(selectedId);
                givenans[i] = radioButton.getText().toString();

//                Toast.makeText(QuestionAnswerActivity.this, list.get(0).getQuestion(), Toast.LENGTH_SHORT);
            }
        });

//        submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int markcount = 0;
//                for(int j=0; j<5; j++)
//                {
//                    if(givenans[j].equalsIgnoreCase(correctans.get(j))) markcount++;
//                }
//
//            }
//        });
    }

    private void startTimer()
    {
        long startFrom = 600000;

        timer = new CountDownTimer(startFrom, 1000)
        {
            long startfrom = 600000;

            @Override
            public void onTick(long millisLeft)
            {
                setTxtTimerValue(millisLeft);
            }

            @Override
            public void onFinish()
            {
                setTxtTimerValue(0);
                Toast.makeText(getApplicationContext(), "Finished", Toast.LENGTH_SHORT).show();
            }
        }.start();
    }

    private void setTxtTimerValue(long millis)
    {
        minutes = millis / 60000;
        seconds = (millis / 1000) % 60;

        String minutesVal = String.valueOf(minutes);
        String secondsVal = String.valueOf(seconds);

        secondsVal = secondsVal.replaceAll("(?<!\\d)\\d(?!\\d)", "0$0");

        txtTimer.setText(getString(R.string.txtTimer, minutesVal, secondsVal));
    }
}