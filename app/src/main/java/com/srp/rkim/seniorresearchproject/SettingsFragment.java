package com.srp.rkim.seniorresearchproject;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SettingsFragment extends Fragment {
    private Button mLogoutButton;
    private Button mChangeEmailButton;
    private EditText mEmailEntry;
    private Button mChangePasswordButton;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mEmailEntry = view.findViewById(R.id.email_input);
        mEmailEntry.setText(user.getEmail());
        mChangeEmailButton = view.findViewById(R.id.change_email);
        mChangeEmailButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!(mEmailEntry.getText().toString().equals(user.getEmail()))) {
                    user.updateEmail(mEmailEntry.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getActivity(), "Email address is updated.", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getActivity(), "Failed to update email!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
//                    mEmailEntry.setText(user.getEmail());
                } else {
                    Toast.makeText(getActivity(), "Email is the same!", Toast.LENGTH_SHORT).show();
                }

            }
        });
//        user.updatePassword(newPassword.getText().toString().trim())
//                .addOnCompleteListener(new OnCompleteListener() {
//                    @Override
//                    public void onComplete(@NonNull Task task) {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(MainActivity.this, "Password is updated!", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(MainActivity.this, "Failed to update password!", Toast.LENGTH_SHORT).show();
//                            progressBar.setVisibility(View.GONE);
//                        }
//                    }
//                });
        mLogoutButton = view.findViewById(R.id.log_out);
        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                mAuth.signOut();
                Intent intent = new Intent(getActivity(), EmailPasswordActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

}
