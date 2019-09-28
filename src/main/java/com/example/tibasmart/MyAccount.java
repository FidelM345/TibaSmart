package com.example.tibasmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.tibasmart.Authentication.Login;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.joda.time.LocalDate;
import org.joda.time.Years;

import java.util.HashMap;
import java.util.Map;

public class MyAccount extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    Button back, update;
    TextView names, email, phoneno, bd_group, gender, weight, age, location, rhesus;
    ImageView circleImageView,delete_account;
    String user_id;
    CheckBox checkBox;
    boolean check_box_value = false;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_account);
        toolbar=findViewById(R.id.toolbar_myaccount);
        setSupportActionBar(toolbar);

        names = findViewById(R.id.account_flname);
        email = findViewById(R.id.account_mail);
        phoneno = findViewById(R.id.account_phone_no);
        bd_group = findViewById(R.id.account_bloodgroup);
        weight = findViewById(R.id.user_weight);
        age = findViewById(R.id.user_age);
        location = findViewById(R.id.user_location);
        gender = findViewById(R.id.gender);
        rhesus = findViewById(R.id.account_rhesus);
        circleImageView = findViewById(R.id.profile_circular);
        checkBox = findViewById(R.id.profile_check);

        delete_account = findViewById(R.id.id_backmain);



        //firebase objects
        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();//gets user current id
        firestore = FirebaseFirestore.getInstance();


        donor_registration();



        delete_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                delete_User_Account();

            }
        });




    }

    private void delete_User_Account() {

        delete_AlertDialog();
    }

    private void delete_AlertDialog() {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Delete Account");
        builder1.setMessage("Are you sure you want to Delete Account");
        builder1.setCancelable(true);
        builder1.setIcon(R.drawable.email_icon);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    @SuppressLint("MissingPermission")
                    public void onClick(DialogInterface dialog, int id) {

                        delete_Account_query();


                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

    }

    private void delete_Account_query() {

        firestore.collection("user_table").document(user_id).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){



                            mAuth.getCurrentUser().delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {


                                            if(task.isSuccessful()){


                                                Toast.makeText(MyAccount.this, "User account has been deleted successfully", Toast.LENGTH_LONG).show();
                                                Intent intent=new Intent(MyAccount.this, Login.class);
                                                startActivity(intent);


                                            }else{

                                                Toast.makeText(MyAccount.this, "Error: "+task.getException().toString(), Toast.LENGTH_LONG).show();


                                            }

                                        }
                                    });


                        }else{

                            Toast.makeText(MyAccount.this, "Error: "+task.getException().toString(), Toast.LENGTH_LONG).show();


                        }



                    }
                });


    }


    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.profile_check:
                if (checked) {
                    check_box_value = true;

                    Map<String, Object> map = new HashMap<>();
                    map.put("donor", check_box_value);

                    firestore.collection("user_table").document(user_id).update(map).addOnCompleteListener(
                            new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        Toast.makeText(MyAccount.this, "Your are now a donor", Toast.LENGTH_LONG).show();

                                    }
                                }
                            }
                    );


                } else if (!checked) {
                    check_box_value = false;

                    Map<String, Object> map = new HashMap<>();
                    map.put("donor", check_box_value);

                    firestore.collection("user_table").document(user_id).update(map).addOnCompleteListener(
                            new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(MyAccount.this, "Your are now not a donor", Toast.LENGTH_LONG).show();

                                }
                            }
                    );


                    break;

                }

        }
    }

    public void donor_registration () {

        firestore.collection("user_table").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {

                    String fname = task.getResult().getString("fname");
                    String lname = task.getResult().getString("lname");
                    String location1 = task.getResult().getString("location");
                    String blood_group1 = task.getResult().getString("blood_group");
                    String email1 = task.getResult().getString("email");
                    String gender1 = task.getResult().getString("gender");
                    String rhesus1 = task.getResult().getString("rhesus");
                    String image = task.getResult().getString("imageuri");
                    boolean donor = task.getResult().getBoolean("donor");
                    String PhoneNO = task.getResult().getString("phone_no");
                    String Thumb = task.getResult().getString("thumburi");
                    Long bith_timestamp = task.getResult().getLong("age");
                    int weight1 = task.getResult().getLong("weight").intValue();//converting long to integer


                    java.util.Date time1=new java.util.Date((long)bith_timestamp);

                    java.util.Date time=new java.util.Date((long)timeStamp_now());

                    LocalDate birthdate = new LocalDate(time1);

                    LocalDate now= new LocalDate(time); // test, in real world without args
                    int year_diff  = Years.yearsBetween(birthdate, now).getYears();



                    names.setText(fname + " " + lname);
                    weight.setText(""+weight1);
                    age.setText(""+year_diff);
                    bd_group.setText(blood_group1);
                    email.setText(email1);
                    gender.setText(gender1);
                    rhesus.setText(rhesus1);
                    phoneno.setText(PhoneNO);
                    location.setText(location1);

                    if (donor == true) {
                        checkBox.setChecked(true);
                    }


                    RequestOptions placeHolder = new RequestOptions();
                    placeHolder.placeholder(R.drawable.profile_placeholder);

                    Glide.with(MyAccount.this).applyDefaultRequestOptions(placeHolder).load(image).thumbnail(
                            //loads the thumbnail if the image has not loaded first
                            Glide.with(MyAccount.this).load(Thumb)
                    ).into(circleImageView);




                } else {

                    String exception = task.getException().getMessage();
                    Toast.makeText(MyAccount.this, "Error is: " + exception, Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private Object timeStamp_now() {
        Long timestamp=System.currentTimeMillis();
        return timestamp;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Glide.with(getApplicationContext()).pauseRequests();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id ==R.id.id_edit) {
            Intent intent=new Intent(this,Edit_Account.class);
            startActivity(intent);

        }

        return true;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(this,Dashboard.class);
        startActivity(intent);

    }
}
