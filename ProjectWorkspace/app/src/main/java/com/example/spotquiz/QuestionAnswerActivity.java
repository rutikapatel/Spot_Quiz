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

import com.example.spotquiz.pojo.QuizResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class QuestionAnswerActivity extends AppCompatActivity {

    private TextView q_number, q_description, option_1, option_2, option_3, option_4;
    private Button next, previous, submit;
    int i = 0;
    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private NumberGridAdapter nga;
    private GridView gridView;
    private TextView txtTimer;

    private ArrayList<String> quesnum = new ArrayList<>();
    private ArrayList<String> quesdesc = new ArrayList<>();
    private ArrayList<ArrayList<String>> quesopt = new ArrayList<>();
    private ArrayList<Integer> correctans = new ArrayList<>();
    private Integer givenans[] = new Integer[15];
    private int noOfQuestions;

    private RadioButton radioButton;
    private RadioGroup radioGroup;

    private CountDownTimer timer;
    private long minutes, seconds = 0;

    private Quiz sq;
    private QuizResult result;

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

        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        sq  = (Quiz)intent.getSerializableExtra("quiz");
        result = (QuizResult) intent.getSerializableExtra("result");


        ArrayList<Question> questions = sq.getQuestions();

        for(int j = 0;j<questions.size();j++){
            quesnum.add(Integer.toString(j));
            quesdesc.add(questions.get(j).getQuestion());
            correctans.add(questions.get(j).getCorrectAnswer());
            quesopt.add(questions.get(j).getOptions());
            givenans[j]= -1 ;
        }
        noOfQuestions = Integer.parseInt(sq.getNoOfQuestions());
        ArrayList<Grid> numbers = new ArrayList<>();

        for(int j=0;j<noOfQuestions;j++)
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


        previous.setVisibility(View.INVISIBLE);

        startTimer(Integer.parseInt(sq.getQuizLength()));

        q_number.setText(Integer.toString(Integer.parseInt(quesnum.get(i)) + 1));
        q_description.setText(quesdesc.get(i));
        option_1.setText(quesopt.get(i).get(0));
        option_2.setText(quesopt.get(i).get(1));
        option_3.setText(quesopt.get(i).get(2));
        option_4.setText(quesopt.get(i).get(3));
        radioButton = findViewById(0);


        next.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Grid h = (Grid) nga.getItem(i);



                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(selectedId);
                System.out.println("id"+radioGroup.indexOfChild(radioButton));

                if(selectedId != -1) {
                    h.setFinished(true);
                    h.change(ContextCompat.getColor(QuestionAnswerActivity.this, R.color.colorGreen));
                    givenans[i] = radioGroup.indexOfChild(radioButton);
                }else{
                    h.change(ContextCompat.getColor(QuestionAnswerActivity.this, R.color.colorGrey));
                }

                i++;
                h = (Grid) nga.getItem(i);
                h.change(ContextCompat.getColor(QuestionAnswerActivity.this, R.color.colorYellow));

                radioGroup.clearCheck();

                previous.setVisibility(View.VISIBLE);
                if(i==noOfQuestions-1) next.setVisibility(View.INVISIBLE);

                q_number.setText((Integer.toString(Integer.parseInt(quesnum.get(i)) + 1)));
                q_description.setText(quesdesc.get(i));
                option_1.setText(quesopt.get(i).get(0));
                option_2.setText(quesopt.get(i).get(1));
                option_3.setText(quesopt.get(i).get(2));
                option_4.setText(quesopt.get(i).get(3));

                if(givenans[i] != -1){
                    radioButton = (RadioButton) radioGroup.getChildAt(givenans[i]);
                    radioButton.setChecked(true);
                }


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

                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(selectedId);



                if(selectedId != -1){
                    h.setFinished(true);
                    h.change(ContextCompat.getColor(QuestionAnswerActivity.this, R.color.colorGreen));
                    givenans[i] = radioGroup.indexOfChild(radioButton);
                }else{
                    h.change(ContextCompat.getColor(QuestionAnswerActivity.this,R.color.colorGrey));
                }
                if(  h.getFinished()!=null && h.getFinished() ){
                   // h.change(ContextCompat.getColor(QuestionAnswerActivity.this,R.color.colorGreen));
                }else{
                    h.change(ContextCompat.getColor(QuestionAnswerActivity.this,R.color.colorGrey));
                }
                radioGroup.clearCheck();
                i--;
                h = (Grid) nga.getItem(i);
                h.change(ContextCompat.getColor(QuestionAnswerActivity.this,R.color.colorYellow));

                next.setVisibility(View.VISIBLE);
                if(i==0) previous.setVisibility(View.INVISIBLE);


                q_number.setText((Integer.toString(Integer.parseInt(quesnum.get(i)) + 1)));
                q_description.setText(quesdesc.get(i));
                option_1.setText(quesopt.get(i).get(0));
                option_2.setText(quesopt.get(i).get(1));
                option_3.setText(quesopt.get(i).get(2));
                option_4.setText(quesopt.get(i).get(3));

                if(givenans[i] != -1){
                    radioButton = (RadioButton) radioGroup.getChildAt(givenans[i]);
                    radioButton.setChecked(true);
                }




//                Toast.makeText(QuestionAnswerActivity.this, list.get(0).getQuestion(), Toast.LENGTH_SHORT);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(selectedId);
                int markcount = 0;
                for(int j=0; j<5; j++)
                {
                    if(givenans[j]==correctans.get(j)) markcount++;
                    System.out.println(givenans[0]);
                    System.out.println(correctans.get(0));
                }
                result.setResult(String.valueOf(markcount));
                mDatabase.child("QuizResults").child(user.getUid()).child(result.getQuizId()).setValue(result);
                Intent i =  new Intent(QuestionAnswerActivity.this, QuizResultActivity.class);
                i.putExtra("result",result);
                startActivity(i);
                finish();
                System.out.println(markcount);
            }
        });
    }

    private void startTimer(int quizLength)
    {
        long startFrom = 1 * 60 * 1000;

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
                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(selectedId);
                int markcount = 0;
                for(int j=0; j<5; j++)
                {
                    if(givenans[j]==correctans.get(j)) markcount++;
                    System.out.println(givenans[0]);
                    System.out.println(correctans.get(0));
                }
                result.setResult(String.valueOf(markcount));
                mDatabase.child("QuizResults").child(user.getUid()).child(result.getQuizId()).setValue(result);
                Intent i =  new Intent(QuestionAnswerActivity.this, QuizResultActivity.class);
                i.putExtra("result",result);
                startActivity(i);
                finish();

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