package com.mobileappdev.cookinbythebook;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Map;


public class navigation_account extends Fragment {

    private Button signOutButton, changePasswordButton;
    private TextView name, emailText, numRecipesText;
    private EditText oldPassword, newPassword, confirmPassword;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final String TAG = "Account";

        mAuth = FirebaseAuth.getInstance();

        View root =  inflater.inflate(R.layout.fragment_navigation_account, container, false);
        name = (TextView) root.findViewById(R.id.name);
        emailText = (TextView) root.findViewById(R.id.email);
        numRecipesText = (TextView) root.findViewById(R.id.numRecipes);

        FirebaseUser user = mAuth.getCurrentUser();
        String authEmail = user.getEmail();

        Databaser db = new Databaser();
        db.init();
        CollectionReference userStore = db.getStore("users");

        userStore.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task0) {
                if (task0.isSuccessful()) {
                    for (QueryDocumentSnapshot document0 : task0.getResult()) {
                        Map<String, Object> dater0 = document0.getData();
                        String email = (String) dater0.get("email");
                        if (email != null && email.equals(authEmail)) {
                            String firstName = (String) dater0.get("firstName");
                            String lastName = (String) dater0.get("lastName");
                            ArrayList recipes = (ArrayList) dater0.get("recipes");
                            String numRecipes = String.valueOf(recipes.size());
                            name.setText(String.format("%s %s", firstName, lastName));
                            emailText.setText(email);
                            numRecipesText.setText(numRecipes);
                        }
                    }
                } else {
                    Log.d(TAG, "Error getting documents.", task0.getException());
                }
            }
        });

        changePasswordButton = (Button) root.findViewById(R.id.changePassword);
        oldPassword = (EditText) root.findViewById(R.id.oldPassword);
        newPassword = (EditText) root.findViewById(R.id.newPassword);
        confirmPassword = (EditText) root.findViewById(R.id.confirmPassword);

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldPassword.getText().toString());

                // Prompt the user to re-provide their sign-in credentials
                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, newPassword.getText().toString());
                                    Log.d(TAG, confirmPassword.getText().toString());
                                    if(newPassword.getText().toString().equals(confirmPassword.getText().toString())) {
                                        user.updatePassword(newPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d(TAG, "Password updated");
                                                    Toast.makeText(getActivity(), "Password changed successfully", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Log.d(TAG, "Error password not updated");
                                                    Toast.makeText(getActivity(), "Password change failed", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    } else {
                                        Toast.makeText(getActivity(), "Passwords must match", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Log.d(TAG, "Error auth failed");
                                }
                            }
                        });
            }
        });



        signOutButton = (Button) root.findViewById(R.id.signOut);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("account", "sign out");
                mAuth.signOut();
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });
        return root;
    }
}