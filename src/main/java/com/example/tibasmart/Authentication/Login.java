package com.example.tibasmart.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.example.tibasmart.Dashboard;
import com.example.tibasmart.MainActivity;
import com.example.tibasmart.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;


public class Login extends AppCompatActivity implements View.OnClickListener {
    EditText Login_email, Login_password;
    Button login,register;
    TextView Forget_password,Quick_access;
    ProgressBar progressBar;
    AwesomeValidation mAwesomeValidation;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        mAuth = FirebaseAuth.getInstance();
        Login_email = findViewById(R.id.login_email);
        Login_password = findViewById(R.id.login_password);
        login= findViewById(R.id.btn_login);
        register= findViewById(R.id.btn_register);
        Forget_password= findViewById(R.id.forget_passwords);
        progressBar= findViewById(R.id.login_progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        Quick_access= findViewById(R.id.quick_access);

       mAwesomeValidation = new AwesomeValidation(BASIC);

       mAwesomeValidation.addValidation(Login.this, R.id.login_email, android.util.Patterns.EMAIL_ADDRESS, R.string.err_email);



        login.setOnClickListener(this);
        Forget_password.setOnClickListener(this);
        register.setOnClickListener(this);
        Quick_access.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_login:

              //  mAwesomeValidation.validate();
                methodLogin();

                break;



            case R.id.btn_register:

                methodRegister();
                break;
//
//            case R.id.forget_passwords:
//                forgetPassword();
//
//                break;
//
//            case R.id.quick_access:
//                quick_Access();
//                break;
            default:
                return;
        }

    }


//    private void quick_Access() {
//        Intent intent=new Intent(Login.this,Quick_access.class);
//        startActivity(intent);
//    }
//
//    private void forgetPassword() {
//
//        Intent intent=new Intent(Login.this,Password_reset.class);
//        startActivity(intent);
//    }
//
    private void methodRegister() {
        Intent intent=new Intent(Login.this,RegisterScreen.class);
        startActivity(intent);

    }

    private void methodLogin() {

        String email = Login_email.getText().toString().trim();
        String pass = Login_password.getText().toString().trim();




        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)) {

            progressBar.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){

                        progressBar.setVisibility(View.INVISIBLE);
                        gotoMain();

                    }else {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(Login.this, "Login error: "+task.getException().getMessage(), Toast.LENGTH_LONG).show();

                    }


                }
            });
        }else{
            Toast.makeText(Login.this, "Please ensure all text fields have" +
                    " been filled ", Toast.LENGTH_LONG).show();


        }


    }

    private void gotoMain() {
        Intent intent=new Intent(Login.this, Dashboard.class);
        startActivity(intent);
        finish();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
