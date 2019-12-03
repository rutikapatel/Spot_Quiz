package com.example.spotquiz;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spotquiz.pojo.Quiz;
import com.example.spotquiz.pojo.QuizResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class QuizConfirmationActivity extends AppCompatActivity {
    private Quiz quiz;
    private TextView quizName;
    private TextView eula;
    private ImageView imageView;
    private Button confirm;
    static int PreqCode = 1;
    //  static int REQCODE = 1;
    static int camCode = 0;
    static int galCode = 1;
    String photo;
    Boolean setImage = false;
    private FirebaseUser user;
    private DatabaseReference mDatabse;
    private EditText quizpin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_confirmation);
        quizName = findViewById(R.id.quizName);
        eula = findViewById(R.id.agreement);
        imageView = findViewById(R.id.imageView);
        confirm = findViewById(R.id.btnConfirm);
        quizpin = findViewById(R.id.quizId);

        Intent intent = getIntent();
        quiz = (Quiz) intent.getSerializableExtra("quiz");


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= 22) {

                    checkForPermission();
                } else {

                    imageSelect(QuizConfirmationActivity.this);
                }

            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean valid = true;
                if (quizpin.getText().toString().isEmpty()) {
                    quizpin.setError("Please enter quiz pin shared by the professor to proceed further");
                    System.out.println("get text val" + quizpin.getText().toString());
                    valid = false;
                }
                if ((!setImage)) {
                    Toast.makeText(QuizConfirmationActivity.this, "Please take a photo or select an image in gallery by clicking the photo icon", Toast.LENGTH_LONG).show();
                    valid = false;
                }
                if ( valid && !(quizpin.getText().toString().equalsIgnoreCase(quiz.getQuizKey()))){
                    Toast.makeText(QuizConfirmationActivity.this, "Wrong pin", Toast.LENGTH_LONG).show();
                    valid = false;
                }

                if(valid) {
                    System.out.println("Image set" + setImage);
                    QuizResult result = new QuizResult();
                    result.setQuizName(quiz.getQuizName());
                    result.setQuizId(quiz.getQuizName() + quiz.getCourseName() + quiz.getQuizLocation().getName() + quiz.getProfessorId());
                    result.setImg(photo);
                    user = FirebaseAuth.getInstance().getCurrentUser();
                    mDatabse = FirebaseDatabase.getInstance().getReference();

                    mDatabse.child("QuizResults").child(user.getUid()).child(result.getQuizId()).setValue(result);
                    Intent i = new Intent(QuizConfirmationActivity.this, QuestionAnswerActivity.class);
                    i.putExtra("quiz", quiz);
                    i.putExtra("result",result);
                    startActivity(i);


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

    //Method to grant permission to camera access
    private void checkForPermission() {
        if (ContextCompat.checkSelfPermission(QuizConfirmationActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(QuizConfirmationActivity.this
                    , Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(QuizConfirmationActivity.this,
                        "Please provide the required storage permission in the app setting", Toast.LENGTH_SHORT).show();

            } else {

                ActivityCompat.requestPermissions(QuizConfirmationActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},

                        PreqCode);
            }
        } else {
            imageSelect(QuizConfirmationActivity.this);
        }


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

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_CANCELED) {

            switch (requestCode) {

                case 0:
                    if (resultCode == RESULT_OK && data != null) {

                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        //  imageView.setImageBitmap(selectedImage);
                        photo = convertToBase64(selectedImage);

                        RoundedBitmapDrawable mDrawable = RoundedBitmapDrawableFactory.create(getResources(), selectedImage);
                        //   imageView.setImageBitmap(bmap);
                        mDrawable.setCircular(true);
                        imageView.setImageDrawable(mDrawable);

                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selImage = data.getData();
                        Bitmap bmap = null;
                        try {
                            bmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selImage);
                            imageView.setImageBitmap(bmap);
                            photo = convertToBase64(bmap);
                            RoundedBitmapDrawable mDrawable = RoundedBitmapDrawableFactory.create(getResources(), bmap);
                            //   imageView.setImageBitmap(bmap);
                            mDrawable.setCircular(true);
                            imageView.setImageDrawable(mDrawable);

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

        System.out.println("setimage value in base 64" + setImage);
        selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, outstream);

        byte[] picArray = outstream.toByteArray();
        setImage = true;

        // System.out.println("base64 string is" + Base64.encodeToString(picArray, Base64.URL_SAFE));
        return Base64.encodeToString(picArray, Base64.URL_SAFE);


    }
}
