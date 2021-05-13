package com.mobileappdev.cookinbythebook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "Register";
    private EditText editTextEmail, editTextPassword, editTextFirstName, editTextLastName;
    private Button loginButton, registerButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Databaser db2;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();

        loginButton = (Button) findViewById(R.id.login);
        registerButton = (Button) findViewById(R.id.register);
        editTextEmail = (EditText) findViewById(R.id.emailLogin);
        editTextPassword = (EditText) findViewById(R.id.passwordLogin);
        editTextFirstName = (EditText) findViewById(R.id.firstName);
        editTextLastName = (EditText) findViewById(R.id.lastName);

        db2 = new Databaser();
        db2.init();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                String fName = editTextFirstName.getText().toString();
                String lName = editTextLastName.getText().toString();

                if(!email.isEmpty() && !password.isEmpty()) {
                    if(password.length() < 6) {
                        Toast.makeText(RegisterActivity.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Log.d(TAG, "email and password");
                        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()) {
                                    Log.d(TAG, "user registered");
                                    user = new User(fName, lName, email, new ArrayList<>());
                                    db = FirebaseFirestore.getInstance();
                                    db.collection("users").add(user);
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    // copy yank that code boi
                                    SharedPreferences globalSettingsReader = (((App) getApplication()).preferences);
                                    SharedPreferences.Editor globalSettingsEditor = (((App) getApplication()).preferences.edit());
                                    db2.getStore("users")
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                            Map<String, Object> incomingUser = document.getData();
                                                            String incomingEmail = (String) incomingUser.get("email");
                                                            if (incomingEmail.equals(user.getEmail())) {
                                                                globalSettingsEditor.putString("uuid", document.getId());
                                                                globalSettingsEditor.commit();
                                                                break;
                                                            }
                                                        }
                                                        Log.d(TAG, globalSettingsReader.getString("uuid", "0"));

                                                    } else {
                                                        Toast.makeText(RegisterActivity.this, "This should never appear", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });



                                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                } else {
                                    Log.d(TAG, "registration failed");
                                    Toast.makeText(RegisterActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "Please enter values", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
