package com.shashankesh.ms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout mPasswordAgain;
    private TextInputLayout mEmail;
    private TextInputLayout mPassword;
    private TextInputLayout mEmpId;
    private Button mCreateBtn;
    private FirebaseAuth mAuth;
    private ProgressBar mProgressBar;
    private RelativeLayout mRelativeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mPasswordAgain = (TextInputLayout) findViewById(R.id.password_again);
        mEmail = (TextInputLayout) findViewById(R.id.email);
        mPassword = (TextInputLayout) findViewById(R.id.password);
        mEmpId = (TextInputLayout) findViewById(R.id.emp_id_edit_text);
        mCreateBtn = (Button) findViewById(R.id.create_btn);
        mProgressBar = (ProgressBar) findViewById(R.id.reg_progress_bar);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.reg_rel);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passAgain = mPasswordAgain.getEditText().getText().toString().trim();
                String email = mEmail.getEditText().getText().toString().trim();
                String password = mPassword.getEditText().getText().toString().trim();
                String empId = mEmpId.getEditText().getText().toString().trim();
                if(!passAgain.equals(password)) Toast.makeText(RegisterActivity.this,"Password Not Matched",Toast.LENGTH_LONG).show();
                else createAccount(passAgain, email, password,empId);
            }
        });
    }

    private void createAccount( String passAgain, String email, String password,String empID) {
        if(email.equals("")||email==null||password.equals("")||password==null||passAgain.equals("")||passAgain==null||empID.equals("")||empID==null){
            Toast.makeText(this,"Please fill register details",Toast.LENGTH_SHORT).show();
            return;
        }
        mProgressBar.setVisibility(View.VISIBLE);
        mRelativeLayout.setVisibility(View.INVISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                } else {
                    Toast.makeText(RegisterActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });
    }

    private void updateUI(FirebaseUser user) {
        mProgressBar.setVisibility(View.INVISIBLE);
        if (user != null) {
            Intent startInt = new Intent(RegisterActivity.this, MainActivity.class);
            Toast.makeText(RegisterActivity.this,"Welcome",Toast.LENGTH_LONG).show();;
            startActivity(startInt);
            finish();
        }else mRelativeLayout.setVisibility(View.VISIBLE);
    }
}
