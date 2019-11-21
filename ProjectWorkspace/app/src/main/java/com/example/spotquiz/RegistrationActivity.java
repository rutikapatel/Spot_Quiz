package com.example.spotquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {

    private EditText name,email, password,dalId;
RadioButton student,professor;
    Button register;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        name = findViewById(R.id.personName);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        dalId = findViewById(R.id.dalId);
        student = findViewById(R.id.student);
        professor = findViewById(R.id.professor);
        register = findViewById(R.id.btnregister);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                firebaseAuthInsertion(email.getText().toString(), password.getText().toString(),dalId.getText().toString(),name.getText().toString());

            }
        });


    }

    protected  void firebaseAuthInsertion(final String mail,String password,final String dalId,final String name){
        mAuth.createUserWithEmailAndPassword(mail, password)
                .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            System.out.println("success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            userProfileCreation(name,mail,dalId);
                            Toast.makeText(RegistrationActivity.this,"Registration Success",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            // If sign in fails, display a message to the user.
                            System.out.println( task.getException());

                            // updateUI(null);
                        }

                        // ...
                    }
                });
    }

    protected void userProfileCreation(String name,String email,String dalId){
        Users user = new Users();
        user.setEmail(email);
        user.setName(name);
        user.setProfessor(true);
        user.setDalId(dalId);
        mDatabase.child("users").child(dalId).setValue(user);
    }
}
