package com.example.restaurentview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    EditText emailId, password;
    Button btnSignIn;
    TextView tvSignUp;
    FirebaseAuth mFirebaseAuth;

    private FirebaseAuth.AuthStateListener mAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


                mFirebaseAuth = FirebaseAuth.getInstance();
                emailId = findViewById(R.id.editText);
                password = findViewById(R.id.editText2);
                btnSignIn = findViewById(R.id.button2);
                tvSignUp = findViewById(R.id.textView);
                mAuthStateListener = firebaseAuth -> {
                    FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                    if( mFirebaseUser != null ){
                        Toast.makeText(LoginActivity.this,"You are logged in",Toast.LENGTH_SHORT).show();
                        String email = mFirebaseUser.getEmail().toString();
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        String emailchild = email.replaceAll("\\.","");
                        i.putExtra("emailchild",emailchild);
                        startActivity(i);
                    }
                    else{
                        Toast.makeText(LoginActivity.this,"Please Login",Toast.LENGTH_SHORT).show();
                    }
                };

                btnSignIn.setOnClickListener(v -> {
                    String email = emailId.getText().toString();
                    String pwd = password.getText().toString();
                    if(email.isEmpty() && pwd.isEmpty()){
                        Toast.makeText(LoginActivity.this,"Fields Are Empty!",Toast.LENGTH_SHORT).show();
                    }
                    else if(email.isEmpty()){
                        emailId.setError("Please enter email id");
                        emailId.requestFocus();
                    }

                    else  if(pwd.isEmpty()){
                        password.setError("Please enter your password");
                        password.requestFocus();
                    }

                    else if (!isEmailValid(email)){
                        Toast.makeText(LoginActivity.this, "Your Email Id is Invalid", Toast.LENGTH_SHORT).show();
                    }

                    else  if(email.isEmpty() && pwd.isEmpty()){
                        Toast.makeText(LoginActivity.this,"Fields Are Empty!",Toast.LENGTH_SHORT).show();
                    }
                    else  if(!(email.isEmpty() && pwd.isEmpty())){
                        mFirebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(LoginActivity.this, task -> {
                            if(!task.isSuccessful()){
                                Toast.makeText(LoginActivity.this,"Login Error, Please Login Again",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Intent intToMain = new Intent(LoginActivity.this,MainActivity.class);
                                String emailchild = email.replaceAll("\\.","");
                                intToMain.putExtra("emailchild",emailchild);
                                startActivity(intToMain);
                            }
                        });
                    }
                    else{
                        Toast.makeText(LoginActivity.this,"Error Occurred!",Toast.LENGTH_SHORT).show();

                    }

                });

                tvSignUp.setOnClickListener(v -> {
                    Intent intSignUp = new Intent(LoginActivity.this, MainActivity2.class);
                    startActivity(intSignUp);
                });
            }

            @Override
            protected void onStart() {
                super.onStart();
                mFirebaseAuth.addAuthStateListener(mAuthStateListener);
            }
    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
        }
