package com.mobileappdev.cookinbythebook;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;


public class navigation_account extends Fragment {

    private Button signOutButton;
    private TextView greeting;
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
        mAuth = FirebaseAuth.getInstance();
        View root =  inflater.inflate(R.layout.fragment_navigation_account, container, false);
        greeting = (TextView) root.findViewById(R.id.greeting);
        FirebaseUser user = mAuth.getCurrentUser();

        if(user != null) {
            greeting.setText(user.getEmail());
        }

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