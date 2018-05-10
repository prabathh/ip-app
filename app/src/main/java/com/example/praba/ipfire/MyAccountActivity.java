package com.example.praba.ipfire;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyAccountActivity extends AppCompatActivity {

    private String uid;
    private FirebaseDatabase mDb;
    private DatabaseReference mRef;
    private EditText email,firstname,lastname,address,status,contactno,pass;
    private Button updtBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        uid=getIntent().getStringExtra("username");
        mDb=FirebaseDatabase.getInstance();
        mRef=mDb.getReference("Users/"+uid);

        email = (EditText) findViewById(R.id.email);
        firstname=(EditText)findViewById(R.id.firstName);
        lastname=(EditText)findViewById(R.id.secName);
        address=(EditText)findViewById(R.id.address);
        status=(EditText)findViewById(R.id.status);
        contactno=(EditText)findViewById(R.id.contNo);
        pass=(EditText)findViewById(R.id.pass);
        updtBtn=(Button)findViewById(R.id.updateBtn);



        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                email.setText(dataSnapshot.child("email").getValue().toString());
                firstname.setText(dataSnapshot.child("name").getValue().toString());
                lastname.setText(dataSnapshot.child("name2").getValue().toString());
                address.setText(dataSnapshot.child("address").getValue().toString());
                status.setText(dataSnapshot.child("status").getValue().toString());
                contactno.setText(dataSnapshot.child("tell").getValue().toString());
                pass.setText(dataSnapshot.child("password").getValue().toString());


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {




            }
        });

        updtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newEmail = email.getText().toString();
                String newFName = firstname.getText().toString();
                String newSecName = lastname.getText().toString();
                String newAddress = address.getText().toString();
                String newStatus = status.getText().toString();
                String newTelNo = contactno.getText().toString();
                String newPass = pass.getText().toString();

                mRef.child("email").setValue(newEmail);
                mRef.child("name").setValue(newFName);
                mRef.child("name2").setValue(newSecName);
                mRef.child("address").setValue(newAddress);
                mRef.child("status").setValue(newStatus);
                mRef.child("tell").setValue(newTelNo);

                Toast.makeText(MyAccountActivity.this, "Your Details Updated", Toast.LENGTH_SHORT).show();
            }


        });



    }
}
