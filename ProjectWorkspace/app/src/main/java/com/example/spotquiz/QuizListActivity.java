package com.example.spotquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.spotquiz.adapters.QuizListCardAdapter;
import com.example.spotquiz.pojo.Question;
import com.example.spotquiz.pojo.Quiz;
import com.example.spotquiz.pojo.QuizLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QuizListActivity extends AppCompatActivity {
    private boolean mLocationPermissionGranted;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastKnownLocation;

    private static final String TAG = "QuizListActivity";

    private TextView location;

    private DatabaseReference mDatabase;
    private ListView quizList;

    private QuizListCardAdapter quizCard;
    ArrayList<Quiz> todayQuiz;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_list);
        //location = findViewById(R.id.quizLocation);
        quizList = findViewById(R.id.quizList);
        quizList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLocationPermission();
        mDatabase = FirebaseDatabase.getInstance().getReference("quizzes");


    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        mLocationPermissionGranted = false;
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            getDeviceLocation();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                    getDeviceLocation();
                }
            }
        }
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();

                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            if (mLastKnownLocation != null) {
                                Log.d(TAG, "Latitude: " + mLastKnownLocation.getLatitude());
                                Log.d(TAG, "Longitude: " + mLastKnownLocation.getLongitude());
                                //location.setText(mLastKnownLocation.toString());
                                getQuiz();
                            }
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                        }

                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    private void getQuiz() {
        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-d");
        String todayDate = sdf.format(today);

        System.out.println(todayDate);
        mDatabase.orderByChild("quizDate").equalTo(todayDate).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                todayQuiz = new ArrayList<>();
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
                            questions,
                            childData.child("professorId").getValue().toString());
                    System.out.println("pish" + q.getQuizName());
                    todayQuiz.add(q);
                }
                checkProximity();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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


    void checkProximity() {

        Location cur = new Location("");

        cur.setLatitude(mLastKnownLocation.getLatitude());
        cur.setLongitude(mLastKnownLocation.getLongitude());

        ArrayList<Quiz> availableQuiz = new ArrayList<>();
        availableQuiz.addAll(todayQuiz);
        for (Quiz q : todayQuiz) {
            Location des = new Location("");
            des.setLatitude(q.getQuizLocation().getLatitude());
            des.setLongitude(q.getQuizLocation().getLongitude());
            float dist = cur.distanceTo(des);
            System.out.println("distance" + dist+"quiz anme"+ q.getQuizLocation().getName()+"   lat  "+des.getLatitude()+ "  lon  "+des.getLongitude());
            if (dist > 200.00 || !q.getActive()) {
                availableQuiz.remove(q);
            }
        }
 //        System.out.println("available quiz"+availableQuiz.get(0)) ;

        ArrayList<String> dummy = new ArrayList<>();

        dummy.add("hari");

        quizCard = new QuizListCardAdapter(this,availableQuiz);

       //1 ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.activity_quiz_list,dummy);
        quizList.setAdapter(quizCard);

    }

}
