package com.example.praba.ipfire;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth firebaseAuth;

    private Button buttonLogout;
    private Button buttonMyProject;
    private Button buttonNewProject;
    private Button buttonMyProfile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();

        buttonLogout = (Button)findViewById(R.id.buttonLogout);
        buttonMyProject = (Button)findViewById(R.id.buttonMyProject);
        buttonNewProject = (Button)findViewById(R.id.buttonNewProject);
        buttonMyProfile = (Button)findViewById(R.id.buttonMyProfile);

        buttonLogout.setOnClickListener(this);
        buttonMyProject.setOnClickListener(this);
        buttonNewProject.setOnClickListener(this);
        buttonMyProfile.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        if (v == buttonLogout){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class ));
        }

        if (v == buttonMyProject){

            Intent intent = new Intent(HomeActivity.this,MyProject.class);
            startActivity(intent);
        }

        if (v == buttonNewProject){
            startActivity(new Intent(this, NewProject.class));
        }
        if (v == buttonMyProfile){
            startActivity(new Intent(HomeActivity.this, MyProfile.class));
        }
    }


}
