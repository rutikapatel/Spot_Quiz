package com.example.spotquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.spotquiz.adapters.QuizPastCardAdapter;
import com.example.spotquiz.pojo.QuizResult;
import com.example.spotquiz.pojo.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StudentHomeActivity extends AppCompatActivity {

    private ListView listView;
    private ImageView imageView;
    private Button btnquiz;
    private DatabaseReference database;
    private DatabaseReference uDatabase;
    private String studentImage;
    private static final String TAG = "StudentHomeAcitivity";
    private FirebaseUser userID;
    private ArrayList<QuizResult> arrayList;
    Users usr;
    Bitmap bmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);

        listView = (ListView) findViewById(R.id.list);
        imageView = findViewById(R.id.imageView);
        btnquiz = findViewById(R.id.btnquiz);
        userID = FirebaseAuth.getInstance().getCurrentUser();

        database = FirebaseDatabase.getInstance().getReference("QuizResults");


        loadImage();

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList = new ArrayList<>();
                System.out.println("past"+dataSnapshot.getChildrenCount());
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    System.out.println("past"+child.getKey());
                    if(child.getKey().equalsIgnoreCase(userID.getUid()))
                    for(DataSnapshot childData:child.getChildren()) {
                        System.out.println("past"+childData.getValue());
                        String img="";
                        if(childData.child("quizId").getValue()!=null){
                            img = childData.child("quizId").getValue().toString();
                        }
                        String result="";
                                if(childData.child("result").getValue()!=null){
                                    result = childData.child("result").getValue().toString();
                                }

                        QuizResult q = new QuizResult(childData.child("quizId").getValue().toString(),
                                img,
                                childData.child("quizName").getValue().toString(),
                                result);
                        arrayList.add(q);
                    }
                }
                QuizPastCardAdapter arrayAdapter = new QuizPastCardAdapter(StudentHomeActivity.this, arrayList);
                listView.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        btnquiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(StudentHomeActivity.this, QuizListActivity.class));
            }
        });
    }

    private void loadImage() {

        uDatabase = FirebaseDatabase.getInstance().getReference("users");
        System.out.println("current user" + userID.getUid());

        uDatabase.orderByChild("userId").equalTo(userID.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot childData : dataSnapshot.getChildren()) {

                    System.out.println("keys " + childData.getKey());
                    System.out.println("value" + childData.getValue());

                    usr = new Users(childData.child("email").getValue().toString(),
                            childData.child("name").getValue().toString(),
                            childData.child("dalId").getValue().toString(),
                            Boolean.parseBoolean(childData.child("professor").getValue().toString()),
                            Boolean.parseBoolean(childData.child("student").getValue().toString()),
                            childData.child("profilePhoto").getValue().toString(),
                            childData.child("userId").getValue().toString()

                    );

                }
                studentImage = usr.getProfilePhoto();
                bmap = getBitmapFromEncodedString(studentImage);
                RoundedBitmapDrawable mDrawable = RoundedBitmapDrawableFactory.create(getResources(), bmap);
                //   imageView.setImageBitmap(bmap);
                mDrawable.setCircular(true);
                imageView.setImageDrawable(mDrawable);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private Bitmap getBitmapFromEncodedString(String encodedString) {
        byte[] arr = Base64.decode(encodedString, Base64.URL_SAFE);
        Bitmap image = BitmapFactory.decodeByteArray(arr, 0, arr.length);
        return image;
    }
}
