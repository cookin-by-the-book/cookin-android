package com.mobileappdev.cookinbythebook;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


public class navigation_account extends Fragment {

    private Button signOutButton, changePasswordButton;
    private ImageButton addPhotoButton;
    private static final int RESULT_LOAD_IMAGE = 1;
    private TextView name, emailText, numRecipesText;
    private EditText oldPassword, newPassword, confirmPassword;
    private FirebaseAuth mAuth;
    private View i;
    final String TAG = "Account";

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
                            String image = (String) dater0.get("image");
                            if(image != null) {
                                byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
                                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                ImageView imageView = (ImageView) root.findViewById(R.id.account_avatar);
                                imageView.setImageBitmap(decodedByte);
                            }
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
                                    if(newPassword.getText().toString() != null && confirmPassword.getText().toString() != null && newPassword.getText().toString().equals(confirmPassword.getText().toString())) {
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
                Log.d(TAG, "sign out");
                mAuth.signOut();
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });

        addPhotoButton = (ImageButton) root.findViewById(R.id.addPhoto);
        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, RESULT_LOAD_IMAGE);
                } else {
                    startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), RESULT_LOAD_IMAGE);
                }
            }
        });

        i = root;

        return root;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SharedPreferences globalSettingsReader = (((App) getActivity().getApplication()).preferences);

        Databaser db = new Databaser();
        db.init();

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            Log.d(TAG, picturePath);
            ImageView imageView = (ImageView) i.findViewById(R.id.account_avatar);
            Bitmap bm = BitmapFactory.decodeFile(picturePath);
            imageView.setImageBitmap(bm);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
            byte[] b = baos.toByteArray();
            String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
            Log.d(TAG, encodedImage);
            db.getStore("users").document(globalSettingsReader.getString("uuid", "0'")).update("image", encodedImage);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults)
    {
        switch (requestCode) {
            case RESULT_LOAD_IMAGE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                } else {
                    //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
                }
                break;
        }
    }

}