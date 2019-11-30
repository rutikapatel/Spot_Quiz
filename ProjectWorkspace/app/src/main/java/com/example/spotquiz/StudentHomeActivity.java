package com.example.spotquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import com.google.firebase.auth.FirebaseAuth;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);

        listView = (ListView) findViewById(R.id.list);
        imageView = findViewById(R.id.imageView);
        btnquiz = findViewById(R.id.btnquiz);

        database = FirebaseDatabase.getInstance().getReference("Quizes");
        final ArrayList<String> arrayList = new ArrayList<>();

        loadImage();

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String quiz = child.getValue(String.class);
                    arrayList.add(quiz);
                }
                ArrayAdapter arrayAdapter = new ArrayAdapter(StudentHomeActivity.this, android.R.layout.simple_list_item_1, arrayList);
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

        uDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot childData : dataSnapshot.getChildren()) {

                    System.out.println("keys " + childData.getKey());
                    System.out.println("value" + childData.getValue());


                }

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
