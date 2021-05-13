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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "Login";
    private EditText editTextEmail, editTextPassword;
    private Button loginButton, registerButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        loginButton = (Button) findViewById(R.id.login);
        registerButton = (Button) findViewById(R.id.register);
        editTextEmail = (EditText) findViewById(R.id.emailLogin);
        editTextPassword = (EditText) findViewById(R.id.passwordLogin);
        Databaser db = new Databaser();
        db.init();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                if(!email.isEmpty() && !password.isEmpty()) {
                    Log.d(TAG, "email and password");
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                Log.d(TAG, "login success");
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                FirebaseUser user = mAuth.getCurrentUser();

                                SharedPreferences globalSettingsReader = (((App) getApplication()).preferences);
                                SharedPreferences.Editor globalSettingsEditor = (((App) getApplication()).preferences.edit());

//                                SharedPreferences.Editor globalSettingsEditor = ((App)getActivity().getApplication()).preferences.edit();
//                                SharedPreferences globalSettingsReader = ((App)getActivity().getApplication()).preferences;

                                db.getStore("users")
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
                                                    Toast.makeText(LoginActivity.this, "This should never appear", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            } else {
                                Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, "Please enter values", Toast.LENGTH_SHORT).show();
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }


}
