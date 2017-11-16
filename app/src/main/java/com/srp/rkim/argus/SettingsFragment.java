package com.srp.rkim.argus;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SettingsFragment extends Fragment {
    Context context;
    private Button mLogoutButton;
    private Button mChangeEmailButton;
    private Button getmChangePasswordButton;
    private EditText mEmailEntry;
    private Button mChangePasswordButton;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mEmailEntry = view.findViewById(R.id.email_input);
        mEmailEntry.setText(user.getEmail());
        mChangePasswordButton = view.findViewById(R.id.change_password);
        mChangePasswordButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(context);
                View changePasswordView = li.inflate(R.layout.dialog_changepassword, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(changePasswordView);

                final EditText oldpass = changePasswordView
                        .findViewById(R.id.old_password_input);
                final EditText newpass1 = changePasswordView
                        .findViewById(R.id.new_password_input);
                final EditText newpass2 = changePasswordView
                        .findViewById(R.id.confirm_password_input);
                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // get user input and set it to result
                                        // edit text
                                        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldpass.getText().toString());
                                        user.reauthenticate(credential);
                                        if (newpass1.getText().toString().equals(newpass2.getText().toString())) {
                                            user.updatePassword(newpass1.getText().toString()).addOnCompleteListener(new OnCompleteListener() {
                                                @Override
                                                public void onComplete(@NonNull Task task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(getActivity(), "Password is updated!", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(getActivity(), "Failed to update password!", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        } else {
                                            Toast.makeText(getActivity(), "Passwords do not match!", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

            }
        });
        mChangeEmailButton = view.findViewById(R.id.change_email);
        mChangeEmailButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (!(mEmailEntry.getText().toString().equals(user.getEmail()))) {
                    // mAuth.signInWithCredential(new EmailAuthCredential())
                    //user.reauthenticate(cre)
                    user.updateEmail(mEmailEntry.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getActivity(), "Email address is updated.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getActivity(), "Failed to update email!", Toast.LENGTH_SHORT).show();
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
