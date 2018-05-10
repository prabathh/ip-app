package com.example.praba.ipfire;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProjectDetailsActivity extends AppCompatActivity {

    private EditText userName,userFullName,userEmail,userPassword,userContactNo;
    private String uid;
    private FirebaseDatabase mDb;
    private DatabaseReference mRef;
    private Button updateUserInfo;
    private Button mContact;
    private Button email;
    private Button chat;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details);

        userName=(EditText)findViewById(R.id.supName);
        userFullName=(EditText)findViewById(R.id.supEmail);
        userEmail=(EditText)findViewById(R.id.supAddress);
        chat = (Button)findViewById(R.id.chatWithUsers);



        uid=getIntent().getStringExtra("projName");
        mDb=FirebaseDatabase.getInstance();
        mRef=mDb.getReference("Project/"+uid);

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userFullName.setText(dataSnapshot.child("Project_name").getValue().toString());
                userName.setText(dataSnapshot.child("description").getValue().toString());
                userEmail.setText(dataSnapshot.child("due_date").getValue().toString());




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chatUser = new Intent(ProjectDetailsActivity.this,ChatActivity.class);
                startActivity(chatUser);
            }
        });

    }
}
