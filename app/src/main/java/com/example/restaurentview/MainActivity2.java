package com.example.restaurentview;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.acl.Owner;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity2 extends AppCompatActivity {
    EditText emailId, password ,nameOfOwner,phone,nameOfRestaurent;
    Button btnSignUp;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    TextView tvSignIn;
    FirebaseAuth mFirebaseAuth;
    String email,pwd,nameOfOwn,mob,nameOfRest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        firebaseDatabase = FirebaseDatabase.getInstance();

        mFirebaseAuth = FirebaseAuth.getInstance();
                emailId = findViewById(R.id.editText);
                password = findViewById(R.id.editText2);
                btnSignUp = findViewById(R.id.button2);
                tvSignIn = findViewById(R.id.textView);
        nameOfOwner = findViewById(R.id.nameOfOwner);
        nameOfRestaurent=findViewById(R.id.nameOfRestaurent);
        phone =findViewById(R.id.phone);
                btnSignUp.setOnClickListener(v -> {
                     email = emailId.getText().toString();
                    databaseReference = firebaseDatabase.getReference().child("Owner Details");

                    pwd = password.getText().toString();
                     nameOfOwn = nameOfOwner.getText().toString();
                     mob = phone.getText().toString();
                     nameOfRest = nameOfRestaurent.getText().toString();
                    if(email.isEmpty() || pwd.isEmpty() || nameOfOwn.isEmpty()||mob.isEmpty()||nameOfRest.isEmpty()){
                        Toast.makeText(MainActivity2.this,"Fields Are Empty! Please fill it",Toast.LENGTH_SHORT).show();
                    }
                    else if (mob.length()!=10){
                        Toast.makeText(MainActivity2.this, "Enter correct Mobile Number", Toast.LENGTH_SHORT).show();
                    }
                    else if (!isEmailValid(email)){
                        Toast.makeText(MainActivity2.this, "Your Email Id is Invalid", Toast.LENGTH_SHORT).show();
                    }
                    else if (!isValidPassword(pwd)){
                        Toast.makeText(MainActivity2.this, "Your Password is Incompatible \nIt must contain atleast One capital letter \nOne number and One symbol(@,$,%,&,#) \nand Minimum length should be 6 characters", Toast.LENGTH_SHORT).show();
                    }
                    else if (!isEmailValid(email)&&!isValidPassword(pwd)){
                        Toast.makeText(MainActivity2.this, "Your Email Id Invalid and Password is Incompatible \nIt must contain atleast One capital letter \nOne number and One symbol(@,$,%,&,#) \nand Minimum length should be 6 characters", Toast.LENGTH_SHORT).show();
                    }
                    else  if(!(email.isEmpty() && pwd.isEmpty())){
                        mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(MainActivity2.this, task -> {
                            if (!task.isSuccessful()) {
                                Toast.makeText(MainActivity2.this, "Email already registered, Please Sign In directly", Toast.LENGTH_SHORT).show();
                            } else {

                                ownerDet owner = new ownerDet(email, pwd, nameOfOwn, mob, nameOfRest);
                                String emailchild = email.replaceAll("\\.","");

                                Toast.makeText(MainActivity2.this, emailchild, Toast.LENGTH_SHORT).show();

                                String id =databaseReference.child(emailchild).getKey();
                                databaseReference.child(id).setValue(owner);
                                Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                                intent.putExtra("emailchild",emailchild);
                                startActivity(intent);
                            }
                        });
                    }
                    else{
                        Toast.makeText(MainActivity2.this,"Error Occurred! Please re-install the app",Toast.LENGTH_SHORT).show();

                    }
                });

                tvSignIn.setOnClickListener(v -> {

                    Intent i = new Intent(MainActivity2.this,LoginActivity.class);
                    startActivity(i);
                });
            }
    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    public boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }
        }
//mAuth.signInWithCustomToken(mCustomToken)
//        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()) {
//                    // Sign in success, update UI with the signed-in user's information
//                    Log.d(TAG, "signInWithCustomToken:success");
//                    FirebaseUser user = mAuth.getCurrentUser();
//                    updateUI(user);
//                } else {
//                    // If sign in fails, display a message to the user.
//                    Log.w(TAG, "signInWithCustomToken:failure", task.getException());
//                    Toast.makeText(CustomAuthActivity.this, "Authentication failed.",
//                            Toast.LENGTH_SHORT).show();
//                    updateUI(null);
//                }
//            }
//        });