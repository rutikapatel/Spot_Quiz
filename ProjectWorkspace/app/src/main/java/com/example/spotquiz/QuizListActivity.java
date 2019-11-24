package com.example.spotquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

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

import java.util.ArrayList;

public class QuizListActivity extends AppCompatActivity {
    private boolean mLocationPermissionGranted;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastKnownLocation;

    private static final String TAG = "QuizListActivity";

    private EditText location;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_list);
        location = findViewById(R.id.quizLocation);
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
                                location.setText(mLastKnownLocation.toString());
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

        ArrayList<Quiz> todayQuiz = new ArrayList<>();
        mDatabase.orderByChild("quizDate").equalTo("2019-10-21").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childData : dataSnapshot.getChildren()) {
                    System.out.println("keys" + childData.getKey());

                    QuizLocation quizLocation = new QuizLocation(childData.child("quizLocation").child("name").getValue().toString(),
                            Double.parseDouble(childData.child("quizLocation").child("longitude").getValue().toString()),
                            Double.parseDouble(childData.child("quizLocation").child("latitude").getValue().toString())
                            );

                    Quiz q = new Quiz(childData.child("quizName").getValue().toString(),
                            childData.child("courseName").getValue().toString(),
                            childData.child("noOfQuestions").getValue().toString(),
                            childData.child("quizKey").getValue().toString(),
                            childData.child("quizDate").getValue().toString(),
                            childData.child("quizStartTime").getValue().toString(),
                            childData.child("quizLength").getValue().toString(),
                            quizLocation);
                    System.out.println("pish" + q.getQuizName());
                    todayQuiz.add(q);
                }
                checkProximity(todayQuiz);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    void checkProximity(ArrayList<Quiz> todayQuiz) {

        Location cur = new Location("");
        /*double lat =44.64068109999999;
        double lon =-63.5783336;*/

        cur.setLatitude(mLastKnownLocation.getLatitude());
        cur.setLongitude(mLastKnownLocation.getLongitude());

       /* lat =44.6394053;
        lon =-63.5837703;*/


        ArrayList<Quiz> availableQuiz = new ArrayList<>();
        availableQuiz.addAll(todayQuiz);
        for (Quiz q : todayQuiz) {
            Location des = new Location("");
            des.setLatitude(q.getQuizLocation().getLatitude());
            des.setLongitude(q.getQuizLocation().getLongitude());
            float dist = cur.distanceTo(des);
            System.out.println("distance" + dist+"quiz anme"+ q.getQuizLocation().getName()+"   lat  "+des.getLatitude()+ "  lon  "+des.getLongitude());
            if (dist > 200.00) {
                availableQuiz.remove(q);
            }
        }
        System.out.println("available quiz"+availableQuiz.get(0)) ;

    }

}
