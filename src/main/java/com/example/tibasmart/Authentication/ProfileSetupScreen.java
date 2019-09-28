package com.example.tibasmart.Authentication;

import android.app.DatePickerDialog;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tibasmart.Dashboard;
import com.example.tibasmart.DatePick;
import com.example.tibasmart.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.joda.time.LocalDate;
import org.joda.time.Years;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;






















import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tibasmart.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;




public class ProfileSetupScreen extends AppCompatActivity implements View.OnClickListener,DatePickerDialog.OnDateSetListener{

    StorageReference mstorageReference;
    FirebaseAuth mAuth;
    FirebaseFirestore mfirestore;
    EditText Fname, Lname,phone_no,weight,age;
    Long timestamp;

    Spinner donor_group1,donor_gender1,donor_rhesus1,Province_Name;

    Button Okbutton;
    CircleImageView birth_date;
    ImageView circleImageView;

    Uri mainImageUri=null;
    boolean isChaanged = false;
    ProgressBar progressBar;
    String user_id;
    Bitmap compressedImageBitmap;
    CheckBox checkBox;
    boolean check_box_value=false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_setup_screen);

        //initializing firebase critical objects
        mstorageReference = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mfirestore = FirebaseFirestore.getInstance();

        Fname = findViewById(R.id.profile_fname);
        Province_Name= findViewById(R.id.provine_name);
        Lname = findViewById(R.id.profile_lname);
        weight = findViewById(R.id.profile_weight);
        age = findViewById(R.id.profile_age);
        phone_no = findViewById(R.id.profile_phoneno);
        donor_group1 =findViewById(R.id.profile_blood_group);
        donor_gender1=findViewById(R.id.profile_gender);
        donor_rhesus1=findViewById(R.id.profile_rhesus);


        birth_date=findViewById(R.id.id_birth);


        checkBox=findViewById(R.id.profile_check);

        Okbutton = findViewById(R.id.profile_btn_submit);
        circleImageView = findViewById(R.id.profile_circular);
        progressBar= findViewById(R.id.profile_progressBar);
        progressBar.setVisibility(View.INVISIBLE);


        age.setFocusable(false);//makes the edit text in active

        circleImageView.setOnClickListener(this);

        birth_date.setOnClickListener(this);


        Okbutton.setOnClickListener(this);





    }


    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.profile_check:
                if (checked)
                    check_box_value = true;

                else
                    check_box_value = false;

                break;

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.profile_circular:
                //allow user to select profile pic from phone memory
                profilePicPicker();

                break;

            case R.id.id_birth:
                //allow user to select profile pic from phone memory
                dateSelector();

                break;

            case R.id.profile_btn_submit:

                if(isChaanged){ //becomes true when new image is selected
                    final String first_name=Fname.getText().toString().trim();
                    final String user_phoneno=phone_no.getText().toString().trim();
                    final String last_name=Lname.getText().toString().trim();
                    final String user_location=Province_Name.getSelectedItem().toString().trim();;
                    final String user_age=age.getText().toString().trim();
                    final String user_weight=weight.getText().toString().trim();
                    final String user_blood_group=donor_group1.getSelectedItem().toString().trim();
                    final String user_gender=donor_gender1.getSelectedItem().toString().trim();
                    final String user_blood_rhesus=donor_rhesus1.getSelectedItem().toString().trim();


                    user_id=mAuth.getCurrentUser().getUid();



                    if (!TextUtils.isEmpty(first_name)&&!TextUtils.isEmpty(last_name)&&!TextUtils.isEmpty(user_location)&&
                            !TextUtils.isEmpty(user_age)&& !TextUtils.isEmpty(user_weight) && !TextUtils.isEmpty(user_blood_group)
                            && !TextUtils.isEmpty(user_gender)&& !TextUtils.isEmpty(user_blood_rhesus)
                            &&!TextUtils.isEmpty(user_phoneno)&& timestamp!=null&& mainImageUri !=null){

                        progressBar.setVisibility(View.VISIBLE);

                        final StorageReference image_path=mstorageReference.child("Profile_image").child(user_id+".jpg");

                        image_path.putFile(mainImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {


                                image_path.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {


                                        final String main_profile_uri=uri.toString();
                                        storeProfileThumburi(first_name,last_name,user_phoneno,user_location,user_age,user_weight,user_blood_group,
                                                user_gender,user_blood_rhesus,main_profile_uri);


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        Log.e("ImageUploadError", " "+e.getLocalizedMessage());
                                        Toast.makeText(ProfileSetupScreen.this, "Image Upload Error is: "+e.getLocalizedMessage(), Toast.LENGTH_LONG).show();

                                    }
                                });


                            }
                        });


                    }else {
                        Toast.makeText(ProfileSetupScreen.this, "Ensure all fields have been filled", Toast.LENGTH_LONG).show();
                    }

                }else {
                    Toast.makeText(ProfileSetupScreen.this, "Please select image first by clicking image icon above to setup your account", Toast.LENGTH_LONG).show();


                }
                break;

        }
    }

    private void dateSelector() {

        DatePick dialogFragment=new DatePick();
        dialogFragment.show(getSupportFragmentManager(),"Date Picker");


    }

    private void storeProfileThumburi(final String fname, final String lname, final String phoneno, final String location1, final String age,
                                      final String weight, final String blood_group, final String gender, final String rhesus, final String main_profile_uri) {
        File actualImageFile=new File(mainImageUri.getPath());


        try {
            compressedImageBitmap = new Compressor(ProfileSetupScreen.this)
                    //compressing image of high quality to a thumbnail bitmap for faster loading
                    .setMaxWidth(60)
                    .setMaxHeight(60)
                    .setQuality(5)
                    .setCompressFormat(Bitmap.CompressFormat.WEBP)
                    .compressToBitmap(actualImageFile);


        } catch (IOException e) {
            e.printStackTrace();
        }

        //compressing image of high quality to a thumbnail bitmap for faster loading
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        compressedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        final UploadTask thumbfilepath=mstorageReference.child("Forum_images/forum_thumbs").child(user_id+".jpg").putBytes(data);

        thumbfilepath.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {


                task.getResult().getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        //after the image thumbnail has been uploaded successfully to the cloud storage
                        //it publishes the contents to the firestore cloud storage

                        String photoLink = uri.toString();

                        storeFirestore(photoLink,fname,lname,phoneno,location1,age,weight,blood_group,gender,rhesus,main_profile_uri);//used for storing image and the user name in firebase

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                     //   String exception=task.getException().getMessage();
                        Toast.makeText(ProfileSetupScreen.this, "Thumb Error is: "+e.getLocalizedMessage(), Toast.LENGTH_LONG).show();

                    }
                });

               /* if(task.isSuccessful()){
                    //after the image thumbnail has been uploaded successfully to the cloud storage
                    //it publishes the contents to the firestore cloud storage
                }
                else{
                    String exception=task.getException().getMessage();
                    Toast.makeText(ProfileSetupScreen.this, "Thumb Error is: "+exception, Toast.LENGTH_LONG).show();
                }*/

            }
        });





    }

    private void storeFirestore(String thumbnailUrl, String first_name,
                                String last_name, String phoneno, String user_location, String age,
                                String weight, String blood_group,
                                String gender, String rhesus, String main_profile_uri) {


       // Uri download_uri;
       // download_uri=task.getResult().getDownloadUrl();
        progressBar.setVisibility(View.INVISIBLE);
        Map<String,Object> user_details=new HashMap<>();
        user_details.put("fname",first_name);
        user_details.put("lname",last_name);
        user_details.put("phone_no",phoneno);
        user_details.put("email",mAuth.getCurrentUser().getEmail());
        user_details.put("location",user_location);
        user_details.put("age",timestamp);
        user_details.put("weight", Integer.valueOf(weight));
        user_details.put("blood_group",blood_group);
        user_details.put("gender",gender);
        user_details.put("donor",check_box_value);
        user_details.put("rhesus",rhesus);
        user_details.put("imageuri",main_profile_uri);
        user_details.put("thumburi",thumbnailUrl);




        mfirestore.collection("user_table").document(user_id).set(user_details).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    gotMainActivity();

                }else{
                    String exception=task.getException().getMessage();
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(ProfileSetupScreen.this, "Text Error is: "+exception, Toast.LENGTH_LONG).show();
                }

            }
        });





    }

    private void gotMainActivity() {
        Toast.makeText(this, "Your Account has been setup successfully", Toast.LENGTH_LONG).show();
        Intent intent=new Intent(ProfileSetupScreen.this, Dashboard.class);
        startActivity(intent);
        finish();

    }

    private void profilePicPicker() {
        // the first if statement checks whether the user is running android Mash mellow and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(ProfileSetupScreen.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // if the permission has been denied allow user to request for the permission

                ActivityCompat.requestPermissions(ProfileSetupScreen.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

            } else {

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(4, 3)
                        .setScaleType(CropImageView.ScaleType.CENTER_CROP)
                        .start(this);

            }
        } else {

            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(4, 3)
                    .setScaleType(CropImageView.ScaleType.CENTER_CROP)
                    .start(this);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mainImageUri = result.getUri();
                circleImageView.setImageURI(mainImageUri);
                isChaanged = true;

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }


        }
    }








    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){


            case 1:

                if (grantResults.length>0){

                    if (grantResults[0]==PackageManager.PERMISSION_GRANTED){



                    }else if(grantResults[0]==PackageManager.PERMISSION_DENIED){

                        Toast.makeText(getApplicationContext(),"Permission Denied",Toast.LENGTH_LONG).show();

                    }
                }
        }

    }



    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

        String currentDate= DateFormat.getDateInstance().format(calendar.getTime());

        timestamp=calendar.getTimeInMillis();

        age.setText(""+currentDate);
    }
}
