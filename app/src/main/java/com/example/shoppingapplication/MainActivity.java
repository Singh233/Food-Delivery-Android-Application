package com.example.shoppingapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.shoppingapplication.Model.Users;
import com.example.shoppingapplication.Prevalent.Prevalent;
import com.example.shoppingapplication.User.HomeActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;


public class MainActivity extends AppCompatActivity {
    private  Button loginButton;
    private ProgressDialog loadingBar;
    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAuth mAuth;
    TextView view, view2, view3;
    LottieAnimationView lottie, lottie2, lottie3, getLottie3, lottie4, success;
    FloatingActionButton fab;
    float v = 0;
    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 123;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final LoadingDialog loadingDialog = new LoadingDialog(MainActivity.this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mAuth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);


        createRequest();


        success = findViewById(R.id.success);
        fab = findViewById(R.id.google_login);

        Paper.init(this);

        String UserPhoneKey = Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);

        if (UserPhoneKey != null && UserPasswordKey != null) {
            if (!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey)) {
                AllowAccess(UserPhoneKey, UserPasswordKey);
                loadingBar.setTitle("Already Logged in");
                loadingBar.setMessage("Please wait!");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }
        }

        loginButton = (Button) findViewById(R.id.main_login_btn);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }

        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                success.animate().translationY(0).alpha(1).setDuration(500).setStartDelay(0).start();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        signIn();
                    }
                }, 3500);
            }
        });



        view = findViewById(R.id.top);
        view2 = findViewById(R.id.app_slogan);
        lottie = findViewById(R.id.red_bar);
        lottie2 = findViewById(R.id.blue_bar);
        lottie3 = findViewById(R.id.green_bar);
        view3 = findViewById(R.id.text_or);
        getLottie3 = findViewById(R.id.twitter);
        lottie4 = findViewById(R.id.fb);


        view.setTranslationX(1000);
        view2.setTranslationX(800);
        lottie.setTranslationX(800);
        lottie2.setTranslationX(800);
        lottie3.setTranslationX(800);
        loginButton.setTranslationY(300);
        view3.setTranslationY(300);
        getLottie3.setTranslationY(300);
        lottie4.setTranslationY(300);
        fab.setTranslationY(300);
        success.setTranslationY(300);

        view2.setAlpha(v);
        lottie.setAlpha(v);
        lottie2.setAlpha(v);
        lottie3.setAlpha(v);
        loginButton.setAlpha(v);
        view3.setAlpha(v);
        getLottie3.setAlpha(v);
        lottie4.setAlpha(v);
        fab.setAlpha(v);
        success.setAlpha(v);

        view.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        view2.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(500).start();
        lottie.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(500).start();
        lottie2.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(500).start();
        lottie3.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(500).start();
        loginButton.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(700).start();
        view3.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(800).start();
        getLottie3.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(1000).start();
        lottie4.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(1000).start();
        fab.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(1000).start();






    }


    private void createRequest() {

        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(MainActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);

                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Sorry Authentication failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void AllowAccess(String phone, String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Users").child(phone).exists()) {
                    Users usersData = dataSnapshot.child("Users").child(phone).getValue(Users.class);

                    if (usersData.getPhone().equals(phone)) {
                        if (usersData.getPassword().equals(password)) {
                            loadingBar.dismiss();


                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            Prevalent.currentOnlineUser = usersData;

                            startActivity(intent);

                        } else {
                            Toast.makeText(MainActivity.this, "Password is incorrect", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }

                    }
                } else {
                    Toast.makeText(MainActivity.this, "Account with number " + phone + " do not exist", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}