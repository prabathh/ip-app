package com.example.praba.ipfire;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyProject extends AppCompatActivity {

    private RecyclerView projects;
    List<ProjectDetails> listData;
    com.example.praba.ipfire.ProjectAdapter adapter;
    FirebaseDatabase FDB;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private ProgressDialog mRegProg;
    private String date;
    private DatabaseReference mRef;

    private String selectWorker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_project);

        listData = new ArrayList<ProjectDetails>();
        listData.clear();

        // adapter = new MyAdapter(listData);

        FDB = FirebaseDatabase.getInstance();
        // GetDataFirebase();

        projects = (RecyclerView) findViewById(R.id.allProjects);
        projects.setHasFixedSize(true);

        projects.setLayoutManager(new LinearLayoutManager(this));


        mRegProg = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mRef = FDB.getReference("Project");

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String uid = ds.getKey().toString();
                        String name = ds.child("Project_name").getValue().toString();
                        String status = ds.child("description").getValue().toString();


                        listData.add(new ProjectDetails(uid, name, status));


                    }
                    adapter = new ProjectAdapter(MyProject.this, (ArrayList<ProjectDetails>) listData);
                    projects.setAdapter(adapter);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });
    }

}
