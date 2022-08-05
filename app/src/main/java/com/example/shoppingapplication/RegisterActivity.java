

 package com.example.shoppingapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.shoppingapplication.User.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private ProgressDialog loadingBar;
    LottieAnimationView fab;
    FloatingActionButton google, next;
    LinearLayout layout;
    TextView view, view2;
    private Button CreateAccountButton;
    private EditText InputName, InputPhoneNUmber, InputPassword;
    float v = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        CreateAccountButton = findViewById(R.id.Register_login_btn);
        InputName = findViewById(R.id.register_username_input);
        InputPhoneNUmber = findViewById(R.id.register_phone_number_input);
        InputPassword = findViewById(R.id.register_password_input);
        loadingBar = new ProgressDialog(this);



        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateAccount();
            }

            private void CreateAccount() {
                String name = InputName.getText().toString();
                String phone = InputPhoneNUmber.getText().toString();
                String password = InputPassword.getText().toString();

                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(RegisterActivity.this, "Please write your name...", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(RegisterActivity.this, "Please write your phone number...", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(RegisterActivity.this, "Please write your password...", Toast.LENGTH_SHORT).show();
                } else {
                    loadingBar.setTitle("Create Account");
                    loadingBar.setMessage("Please wait, while we are checking the credentials.");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    ValidatePhoneNumber(name, phone, password);
                }
            }

            private void ValidatePhoneNumber(String name, String phone, String password) {
                final DatabaseReference RootRef;
                RootRef = FirebaseDatabase.getInstance().getReference();
                RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        if (!(dataSnapshot.child("Users").child(phone).exists()))
                        {
                            HashMap<String, Object> userdataMap = new HashMap<>();
                            userdataMap.put("phone", phone);
                            userdataMap.put("password", password);
                            userdataMap.put("name", name);

                            RootRef.child("Users").child(phone).updateChildren(userdataMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if (task.isSuccessful())  {
                                                Toast.makeText(RegisterActivity.this, "Congratulations, your account has been created.", Toast.LENGTH_SHORT).show();
                                                loadingBar.dismiss();

                                                Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                                                startActivity(intent);
                                            } else {
                                                loadingBar.dismiss();
                                                Toast.makeText(RegisterActivity.this, "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                        else {
                            Toast.makeText(RegisterActivity.this, "This " + phone + " already exists.", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            Toast.makeText(RegisterActivity.this, "Please try again using another phone number.", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

        });

        fab = findViewById(R.id.lottie_register);
        layout = findViewById(R.id.linear_layout_2);
        view2 = findViewById(R.id.tcr);

        fab.setTranslationY(300);
        InputName.setTranslationY(300);
        InputPhoneNUmber.setTranslationY(300);
        InputPassword.setTranslationY(300);
        layout.setTranslationY(300);
        view2.setTranslationY(300);
        CreateAccountButton.setTranslationY(300);



        fab.setAlpha(v);
        InputName.setAlpha(v);
        InputPhoneNUmber.setAlpha(v);
        InputPassword.setAlpha(v);
        layout.setAlpha(v);
        view2.setAlpha(v);
        CreateAccountButton.setAlpha(v);



        fab.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        InputName.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        InputPhoneNUmber.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        InputPassword.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(800).start();
        layout.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(1000).start();
        view2.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(1800).start();
        CreateAccountButton.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(1600).start();
    }
}