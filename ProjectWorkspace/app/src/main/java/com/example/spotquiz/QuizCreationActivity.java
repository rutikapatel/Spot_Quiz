package com.example.spotquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.example.spotquiz.pojo.Quiz;
import com.example.spotquiz.pojo.QuizLocation;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.nio.BufferUnderflowException;
import java.util.Calendar;

public class QuizCreationActivity extends AppCompatActivity {
    private TextInputLayout chooseLocation, datePicker, timePicker;
    private EditText quizName, course, quizKey, quizLocation, quizDate, quizTime;
    private Spinner noOfQuestions, quizLength;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseUser user;

    private QuizLocation selectedLocation;
    private Button create;
    int valid = 0;

    private Calendar myCalendar = Calendar.getInstance();
    private int day = myCalendar.get(Calendar.DAY_OF_MONTH);
    private int month = myCalendar.get(Calendar.MONTH);
    private int year = myCalendar.get(Calendar.YEAR);

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        String qname = savedInstanceState.getString("quizName");
        System.out.println("restored name restore instance" + qname);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {

        super.onSaveInstanceState(outState);
        outState.putString("quizName", quizName.toString());
        System.out.println("restored name" + outState.get("quizName"));
    }


    protected void onPause(Bundle outstate1) {
        super.onPause();

        String qname = outstate1.getString("quizName");
        System.out.println("inside onPause restored name restore instance" + qname);
    }


    protected void onResume(Bundle outstate2) {

        String qname = outstate2.getString("quizName");
        System.out.println("inside onResume restored name restore instance" + qname);
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_creation);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        user = mAuth.getCurrentUser();
        if (user != null) {
            System.out.println("user" + user.getUid());
        } else {
            // No user is signed in.
        }

        quizName = findViewById(R.id.quizName);
        course = findViewById(R.id.courseName);
        quizKey = findViewById(R.id.quizPassword);

        noOfQuestions = findViewById(R.id.noOfQuestions);
        quizLength = findViewById(R.id.quizLength);

        chooseLocation = findViewById(R.id.boxquizLocation);
        quizLocation = findViewById(R.id.quizLocation);

        datePicker = findViewById(R.id.boxquizdate);
        quizDate = findViewById(R.id.quizDate);

        timePicker = findViewById(R.id.boxquizstarttime);
        quizTime = findViewById(R.id.quizStartTime);

        create = findViewById(R.id.btnCreate);
        chooseLocation.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Quiz quiz = new Quiz();
                quiz.setQuizName(quizName.getText().toString());
                quiz.setCourseName(course.getText().toString());
                quiz.setQuizKey(quizKey.getText().toString());
                quiz.setNoOfQuestions(noOfQuestions.getSelectedItem().toString());
                quiz.setQuizLocation(selectedLocation);
                quiz.setQuizDate(quizDate.getText().toString());
                quiz.setQuizStartTime(quizTime.getText().toString());
                quiz.setQuizLength(quizLength.getSelectedItem().toString());
                quiz.setActive(Boolean.FALSE);
                quiz.setProfessorId(user.getUid());


                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("quiz",quiz);
                startActivity(intent);
            }
        });

        datePicker.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateView();
            }
        });

        timePicker.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeView();
            }
        });

        Intent intent = getIntent();
        selectedLocation = (QuizLocation) intent.getSerializableExtra("location");
        Quiz q = (Quiz) intent.getSerializableExtra("quiz");
        if (selectedLocation != null) {
            quizLocation.setText(selectedLocation.getName());
        }
        if(q != null){
            quizName.setText(q.getQuizName());
            course.setText(q.getCourseName());
            quizKey.setText(q.getQuizKey());
            quizDate.setText(q.getQuizDate());
            quizTime.setText(q.getQuizStartTime());


        }


        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valid = 0;

                if (quizName.getText().toString().isEmpty()) {
                    quizName.setError("Name should not be empty");
                    valid = 1;
                }
                if (course.getText().toString().isEmpty()) {
                    course.setError("Course should not be empty");
                    valid = 1;
                }
                if (quizKey.getText().toString().isEmpty()) {
                    quizKey.setError("Key should not be empty");
                    valid = 1;
                }
                if (quizLocation.getText().toString().isEmpty()) {
                    quizLocation.setError("Location should not be empty");
                    valid = 1;
                }
                if (quizDate.getText().toString().isEmpty()) {
                    quizDate.setError("Date should not be empty");
                    valid = 1;
                }
                if (quizTime.getText().toString().isEmpty()) {
                    quizTime.setError("Time should not be empty");
                    valid = 1;
                }
                if (valid == 0) {
                    createQuiz();
                }
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


    void createQuiz() {
        Quiz quiz = new Quiz();
        quiz.setQuizName(quizName.getText().toString());
        quiz.setCourseName(course.getText().toString());
        quiz.setQuizKey(quizKey.getText().toString());
        quiz.setNoOfQuestions(noOfQuestions.getSelectedItem().toString());
        quiz.setQuizLocation(selectedLocation);
        quiz.setQuizDate(quizDate.getText().toString());
        quiz.setQuizStartTime(quizTime.getText().toString());
        quiz.setQuizLength(quizLength.getSelectedItem().toString());
        quiz.setActive(Boolean.FALSE);
        quiz.setProfessorId(user.getUid());

        Intent intent = new Intent(getApplicationContext(), QuestionCreationActivity.class);
        intent.putExtra("quiz", quiz);
        startActivity(intent);
        finish();
        // mDatabase.child("quizzes").child(quiz.getQuizName()+quiz.getCourseName()+quiz.getQuizLocation().getName()).setValue(quiz);
    }

    public void DateView() {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                quizDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                quizTime.setError(null);
            }
        };
        DatePickerDialog dpDialog = new DatePickerDialog(this, listener, year, month, day);

        dpDialog.show();
    }

    public void TimeView() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(QuizCreationActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                int hour = hourOfDay;
                int minute = minutes;
                String timeSet = "";
                if (hour > 12) {
                    hour -= 12;
                    timeSet = "PM";
                } else if (hour == 0) {
                    hour += 12;
                    timeSet = "AM";
                } else if (hour == 12) {
                    timeSet = "PM";
                } else {
                    timeSet = "AM";
                }

                String min = "";
                if (minute < 10)
                    min = "0" + minute;
                else
                    min = String.valueOf(minute);

                // Append in a StringBuilder
                String aTime = new StringBuilder().append(hour).append(':')
                        .append(min).append(" ").append(timeSet).toString();
                System.out.println("Time:" + aTime);
                quizTime.setText(aTime);
                quizTime.setError(null);

            }
        }, 0, 0, false);

        timePickerDialog.show();

    }

}
