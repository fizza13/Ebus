package com.example.e_bus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
  EditText fullname,email,password,phone;
    Button registerBtn,registerBtn1;
    boolean valid = true;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    boolean isPassenger ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        fAuth =FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        fullname = findViewById(R.id.inputuser);
        email = findViewById(R.id.inputemail);
        password = findViewById(R.id.inputpassword);
        phone = findViewById(R.id.inputPhone);
        registerBtn = findViewById(R.id.button);
        registerBtn1 = findViewById(R.id.button1);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkField()) {
                    if (valid) {
                        fAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                FirebaseUser user = fAuth.getCurrentUser();
                                Toast.makeText(SignUp.this, "Account Created", Toast.LENGTH_SHORT).show();
                                DocumentReference df = fStore.collection("Users").document(user.getUid());
                                Map<String, Object> userInfo = new HashMap<>();
                                userInfo.put("FullName", fullname.getText().toString());
                                userInfo.put("UserEmail", email.getText().toString());
                                userInfo.put("PhoneNumber", phone.getText().toString());

                                //specify if user is driver
                                userInfo.put("isPassenger", "1");


                                df.set(userInfo);

                                startActivity(new Intent(getApplicationContext(), MainMenu.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(SignUp.this, "Failed to Create Account", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

            }
        });
        registerBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkField()) {
                    if (valid) {
                        fAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                FirebaseUser user = fAuth.getCurrentUser();
                                Toast.makeText(SignUp.this, "Account Created", Toast.LENGTH_SHORT).show();
                                DocumentReference df = fStore.collection("Drivers").document(user.getUid());
                                Map<String, Object> userInfo = new HashMap<>();
                                userInfo.put("FullName", fullname.getText().toString());
                                userInfo.put("UserEmail", email.getText().toString());
                                userInfo.put("PhoneNumber", phone.getText().toString());

                                //specify if user is driver
                                userInfo.put("isPassenger", "1");

                                // Add dummy latitude and longitude values
                                userInfo.put("Latitude", 0.0); // Replace with your dummy latitude value
                                userInfo.put("Longitude", 0.0); // Replace with your dummy longitude value


                                df.set(userInfo);

                                startActivity(new Intent(getApplicationContext(), MainMenu.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(SignUp.this, "Failed to Create Account", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

            }
        });

        }

    private boolean checkField() {
        String emailText = email.getText().toString().trim();
        String passwordText = password.getText().toString().trim();
        String fullnameText = fullname.getText().toString().trim();
        String phoneText = phone.getText().toString().trim();


        if (emailText.isEmpty()) {
            email.setError("Email is required");
            valid = false;
        }

        if (passwordText.isEmpty()) {
            password.setError("Password is required");
            valid = false;
        }

        if (fullnameText.isEmpty()) {
            fullname.setError("Full name is required");
            valid = false;
        }

        if (phoneText.isEmpty()) {
            phone.setError("Phone number is required");
            valid = false;
        }
        return valid;
    }
}