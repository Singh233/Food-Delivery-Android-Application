package com.example.shoppingapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.shoppingapplication.Admin.AdminCategoryActivity;
import com.example.shoppingapplication.Model.Users;
import com.example.shoppingapplication.Prevalent.Prevalent;
import com.example.shoppingapplication.User.HomeActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    LottieAnimationView fab;
    private FloatingActionButton google;
    private EditText InputPhoneNumber, InputPassword;
    LinearLayout layout;
    TextView view2;
    private Button RegisterButton, LoginButton;
    private ProgressDialog loadingBar;
    private String parentDbName = "Users";
    private CheckBox checkBoxRememberMe;
    private TextView AdminLink, NotAdminLink, ForgetPasswordLink;

    float v = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fab = findViewById(R.id.lottie_user);
        InputPhoneNumber = findViewById(R.id.login_phone_number_input);
        InputPassword = findViewById(R.id.login_password_input);
        layout = findViewById(R.id.linear_layout_1);
        LoginButton = findViewById(R.id.login_btn);
        view2 = findViewById(R.id.tc);
        RegisterButton = findViewById(R.id.register_btn);
        AdminLink = findViewById(R.id.admin_panel_link);
        NotAdminLink = findViewById(R.id.not_admin_panel_link);
        ForgetPasswordLink = findViewById(R.id.forget_password_link);
        loadingBar = new ProgressDialog(this);


        checkBoxRememberMe = findViewById(R.id.remember_me_chkb);
        Paper.init(this);


        ForgetPasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                intent.putExtra("check", "login");
                startActivity(intent);
            }
        });

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginUser();
            }
        });

        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                LoginButton.setText("Login Admin");
                AdminLink.setVisibility(View.INVISIBLE);
                NotAdminLink.setVisibility(View.VISIBLE);
                RegisterButton.setVisibility(View.INVISIBLE);
                parentDbName = "Admins";
            }
        });

        NotAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                LoginButton.setText("Login");
                AdminLink.setVisibility(View.VISIBLE);
                NotAdminLink.setVisibility(View.INVISIBLE);
                RegisterButton.setVisibility(View.VISIBLE);
                parentDbName = "Users";
            }
        });




        fab.setTranslationY(300);
        InputPhoneNumber.setTranslationX(1000);
        InputPassword.setTranslationX(1000);
        layout.setTranslationX(1000);
        ForgetPasswordLink.setTranslationX(1000);
        LoginButton.setTranslationY(300);
        view2.setTranslationY(300);
        RegisterButton.setTranslationY(300);

        fab.setAlpha(v);
        InputPhoneNumber.setAlpha(v);
        InputPassword.setAlpha(v);
        layout.setAlpha(v);
        ForgetPasswordLink.setAlpha(v);
        LoginButton.setAlpha(v);
        view2.setAlpha(v);
        RegisterButton.setAlpha(v);

        fab.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        InputPhoneNumber.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        InputPassword.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(800).start();
        layout.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(1000).start();
        ForgetPasswordLink.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(1200).start();
        LoginButton.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(1400).start();
        view2.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(1600).start();
        RegisterButton.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(1800).start();

    }

    private void LoginUser() {
        String phone = InputPhoneNumber.getText().toString();
        String password = InputPassword.getText().toString();

        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Please write your phone number...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccessToAccount(phone, password);
        }
    }

    private void AllowAccessToAccount(final String phone, final String password) {
        if (checkBoxRememberMe.isChecked()) {
            Paper.book().write(Prevalent.UserPhoneKey, phone);
            Paper.book().write(Prevalent.UserPasswordKey, password);
        }

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();


        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(parentDbName).child(phone).exists()) {

                    Users usersData = dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);

                    if (usersData.getPhone().equals(phone)) {
                        if (usersData.getPassword().equals(password)) {
                            if (parentDbName.equals("Admins")) {
                                Toast.makeText(LoginActivity.this, "Welcome Admin, you are Logged in Successfully", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(LoginActivity.this, AdminCategoryActivity.class);
                                startActivity(intent);
                            } else if (parentDbName.equals("Users")){
                                Toast.makeText(LoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                Prevalent.currentOnlineUser = usersData;
                                startActivity(intent);
                            }
                        } else {
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "Password is incorrect.", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Account with this " + phone + " number do not exists.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}