package com.example.spotquiz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.spotquiz.pojo.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class RegistrationActivity extends AppCompatActivity {

    // Initializing UI components

    private EditText name, email, password, dalId;
    RadioButton student, professor;
    Button register;
    ImageView imgView;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    boolean studnt, prof;
    static int PreqCode = 1;
    //  static int REQCODE = 1;
    static int camCode = 0;
    static int galCode = 1;
    String photo;
    Uri selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        dalId = findViewById(R.id.dalId);
        student = findViewById(R.id.student);
        professor = findViewById(R.id.professor);
        register = findViewById(R.id.btnregister);
        imgView = findViewById(R.id.imageView);
        mDatabase = FirebaseDatabase.getInstance().getReference();


        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= 22) {

                    checkForPermission();
                } else {

                    imageSelect(RegistrationActivity.this);
                }
            }
        });

        //OnClick listener method for register button
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Edittext validation
                if (name.getText().toString().isEmpty() || email.getText().toString().isEmpty()
                        || password.getText().toString().isEmpty() || dalId.getText().toString().isEmpty()
                        || !(student.isChecked() || professor.isChecked())) {


                    name.setError("Please enter your name");
                    email.setError("Please enter your E-Mail ID");
                    password.setError("Please enter the password");
                    dalId.setError("Please enter your dalhousie ID");
                    student.setError("Please select student if applicable");
                    professor.setError("Please select professor if applicable");
                    register.setVisibility(View.INVISIBLE);
                } else {
                    //Registering as student
                    if (student.isChecked() == true) {
                        studnt = student.isChecked();
                        prof = professor.isChecked();
                        System.out.println("student button" + studnt);
                        System.out.println("prof button" + prof);
                        firebaseAuthInsertion(email.getText().toString(), password.getText().toString(), dalId.getText().toString(), name.getText().toString(), studnt, prof);
                        startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                        finish();

                        //Registering as professor
                    } else if (professor.isChecked() == true) {

                        studnt = student.isChecked();
                        prof = professor.isChecked();
                        System.out.println("student button" + studnt);
                        System.out.println("prof button" + prof);
                        firebaseAuthInsertion(email.getText().toString(), password.getText().toString(), dalId.getText().toString(), name.getText().toString(), studnt, prof);
                        startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                        finish();

                    }

                }
            }
        });


    }


    public void imageSelect(Context context) {
        final CharSequence[] opt = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose image for profile");

        builder.setItems(opt, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (opt[which].equals("Take Photo")) {
                    Intent takePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePic, camCode);
                } else if (opt[which].equals("Choose from Gallery")) {
                    Intent galIntent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(galIntent, galCode);
                } else if (opt[which].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

        //Allow user to pick image from gallery

  /*      Intent galIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galIntent.setType("image/*");
        startActivityForResult(galIntent,REQCODE);*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_CANCELED) {

            switch (requestCode) {

                case 0:
                    if (resultCode == RESULT_OK && data != null) {

                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        imgView.setImageBitmap(selectedImage);
                        photo = convertToBase64(selectedImage);
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selImage = data.getData();
                        Bitmap bmap = null;
                        try {
                            bmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selImage);
                            imgView.setImageBitmap(bmap);
                            photo = convertToBase64(bmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;

            }

        }

    }

    private String convertToBase64(Bitmap selectedImage) {
        ByteArrayOutputStream outstream = new ByteArrayOutputStream();

        selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, outstream);

        byte[] picArray = outstream.toByteArray();

        System.out.println("base64 string is" + Base64.encodeToString(picArray, Base64.URL_SAFE));
        return Base64.encodeToString(picArray, Base64.URL_SAFE);


    }

    //Method to grant permission to camera access
    private void checkForPermission() {
        if (ContextCompat.checkSelfPermission(RegistrationActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(RegistrationActivity.this
                    , Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(RegistrationActivity.this,
                        "Please provide the required permission", Toast.LENGTH_SHORT).show();

            } else {

                ActivityCompat.requestPermissions(RegistrationActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},

                        PreqCode);
            }
        } else {
            imageSelect(RegistrationActivity.this);
        }


    }

    //Firebase authentication method
    protected void firebaseAuthInsertion(final String mail, String password, final String dalId, final String name, final boolean studnt, boolean prof) {
        mAuth.createUserWithEmailAndPassword(mail, password)
                .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            System.out.println("success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            userProfileCreation(name, mail, dalId, studnt, prof);
                            Toast.makeText(RegistrationActivity.this, "Registration Success",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(RegistrationActivity.this, "There is an error" + task.getException(), Toast.LENGTH_SHORT).show();
                            System.out.println(task.getException());
                        }

                        // ...
                    }
                });
    }

    //Setting values based on user type
    protected void userProfileCreation(String name, String email, String dalId,
                                       final boolean studnt, final boolean prof) {
        Users user = new Users();
        user.setEmail(email);
        user.setName(name);
        user.setStudent(studnt);
        user.setProfessor(prof);
        user.setDalId(dalId);
        mDatabase.child("users").child(dalId).setValue(user);
    }
}
