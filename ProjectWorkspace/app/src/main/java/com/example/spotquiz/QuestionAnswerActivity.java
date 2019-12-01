package com.example.spotquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class QuestionAnswerActivity extends AppCompatActivity {

    private TextView q_number, q_description, option_1, option_2, option_3, option_4, q_showing;
    private String answer;
    private Button start, next, previous;
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
    private long minutes, seconds;

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
        start = findViewById(R.id.btnStartQuiz);

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
        mDatabase = FirebaseDatabase.getInstance().getReference("Questions");

        previous.setVisibility(View.INVISIBLE);



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



                q_number.setText(quesnum.get(i));
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

                q_number.setText(quesnum.get(i));
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

                if(i==0)
                {
                    previous.setVisibility(View.INVISIBLE);
                }
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

                q_number.setText(quesnum.get(i));
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