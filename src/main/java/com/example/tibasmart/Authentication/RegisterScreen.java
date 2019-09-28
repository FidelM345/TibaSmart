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
import com.example.tibasmart.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

public class RegisterScreen extends AppCompatActivity implements View.OnClickListener {
    EditText reg_email,reg_password,reg_confirm;
    Button reg_register;
    TextView login_btn;
    ProgressBar progressBar;
    AwesomeValidation mAwesomeValidation;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_screen);

        mAuth=FirebaseAuth.getInstance();
        mAwesomeValidation = new AwesomeValidation(BASIC);//setting type and initiating
        //form validation


        mAwesomeValidation.addValidation(RegisterScreen.this, R.id.reg_password, R.id.reg_confirm, R.string.err_password_confirmation);

        reg_email=findViewById(R.id.reg_email);
        reg_password=findViewById(R.id.reg_password);
        reg_confirm=findViewById(R.id.reg_confirm);
        reg_register=findViewById(R.id.reg_btn_reg);
        login_btn =findViewById(R.id.reg_btn_exist);
        progressBar =findViewById(R.id.reg_progressBar);

        progressBar.setVisibility(View.INVISIBLE);

        reg_register.setOnClickListener(this);
        login_btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.reg_btn_reg:

                mAwesomeValidation.validate();

                methodRegister();

                break;

            case R.id.reg_btn_exist:

                methodLogin();
                break;

            default:
                return;
        }
    }

    private void methodLogin() {
        Intent intent=new Intent(RegisterScreen.this,Login.class);
        startActivity(intent);
    }

    private void methodRegister() {

        String email=reg_email.getText().toString().trim();
        String pass=reg_password.getText().toString().trim();
        String pass1=reg_confirm.getText().toString().trim();


        if (!TextUtils.isEmpty(email)&& !TextUtils.isEmpty(pass) &&!TextUtils.isEmpty(pass1)){

            if (pass.equals(pass1)){
                //checks whether the two entered password are the same

                progressBar.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){
                            progressBar.setVisibility(View.INVISIBLE);
                            gotoSetupActivity();
                            finish();
                        }
                        else {
                            progressBar.setVisibility(View.INVISIBLE);
                            String error=task.getException().getMessage();
                            Toast.makeText(RegisterScreen.this, "Error: "+error, Toast.LENGTH_LONG).show();
                        }

                    }
                });

            }else {
                Toast.makeText(this, "Please re-enter password ", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this, "Please Ensure all the three text fields have been filled before " +
                    "pressing register button ", Toast.LENGTH_LONG).show();

        }
    }

    private void gotoSetupActivity() {
        Intent intent=new Intent(RegisterScreen.this,ProfileSetupScreen.class);
        startActivity(intent);
    }
}
