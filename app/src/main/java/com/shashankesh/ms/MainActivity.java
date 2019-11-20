package com.shashankesh.ms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Toolbar mToolbar;
    private Button mAddItm;
    private Button mThUpdate;
    private Button mUpdtVndr;
    private Button mStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Motor Part Management");
        mAddItm = (Button)findViewById(R.id.addBtn);
        mThUpdate = (Button)findViewById(R.id.updtThBtn);
        mUpdtVndr = (Button)findViewById(R.id.updtVendBtn);
        mStatus = (Button)findViewById(R.id.statusBtn);
        mAddItm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"Add Item",Toast.LENGTH_SHORT).show();
                Intent startInt = new Intent(MainActivity.this,ItemActivity.class);
                startActivity(startInt);
            }
        });
        mThUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"Update Threshold",Toast.LENGTH_SHORT).show();
                Intent startInt = new Intent(MainActivity.this,ThresholdActivity.class);
                startActivity(startInt);
            }
        });
        mUpdtVndr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"See vendors",Toast.LENGTH_SHORT).show();
                Intent startInt = new Intent(MainActivity.this,VendorActivity.class);
                startActivity(startInt);
            }
        });
        mStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startInt = new Intent(MainActivity.this,PrintActivity.class);
                startActivity(startInt);
                Toast.makeText(MainActivity.this,"Items to order",Toast.LENGTH_SHORT).show();
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
        if(currentUser == null){
            Intent startIntent = new Intent(MainActivity.this,StartActivity.class);
            startActivity(startIntent);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId()==R.id.setting){
            Intent startInt = new Intent(MainActivity.this,SettingsActivity.class);
            startActivity(startInt);
            Toast.makeText(MainActivity.this,"Settings",Toast.LENGTH_LONG).show();
        }else if (item.getItemId()==R.id.logout){
            mAuth.signOut();
            updateUI(null);
        } else if(item.getItemId()==R.id.home){
            Toast.makeText(MainActivity.this,"Press home to exit",Toast.LENGTH_LONG).show();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
        //Toast.makeText(MainActivity.this,"Press home to exit",Toast.LENGTH_LONG).show();
    }
}
