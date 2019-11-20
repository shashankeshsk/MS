package com.shashankesh.ms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartActivity extends AppCompatActivity {
    Button mNeedAcc;
    Button mLogin;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        mLogin = (Button)findViewById(R.id.login);
        mNeedAcc = (Button)findViewById(R.id.need_acc);
        mAuth = FirebaseAuth.getInstance();

        mNeedAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startInt = new Intent(StartActivity.this,RegisterActivity.class);
                startActivity(startInt);
            }
        });
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(StartActivity.this,"Login inProgress",Toast.LENGTH_SHORT).show();
                Intent startInt = new Intent(StartActivity.this,LoginActivity.class);
                startActivity(startInt);
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        if(currentUser != null){
            Intent startIntent = new Intent(StartActivity.this,MainActivity.class);
            startActivity(startIntent);
            finish();
        }
    }
}
