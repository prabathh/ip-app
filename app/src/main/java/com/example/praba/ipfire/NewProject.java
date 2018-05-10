package com.example.praba.ipfire;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewProject extends AppCompatActivity {

    RecyclerView myRecycleview;
    //MyAdapter adapter;
    List<UserDetails> listData;
    com.example.praba.ipfire.UserAdapter adapter;

    FirebaseDatabase FDB;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private ProgressDialog mRegProg;
    private String date;
    private DatabaseReference mRef;

    private String selectWorker;



    private TextView textProName , textProDes , selectedUsers;

    private Button buttonCreate;
    private CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project);



        listData = new ArrayList<com.example.praba.ipfire.UserDetails>();
        listData.clear();

       // adapter = new MyAdapter(listData);

        FDB = FirebaseDatabase.getInstance();
       // GetDataFirebase();

        myRecycleview = (RecyclerView)findViewById(R.id.myRecycler);
        myRecycleview.setHasFixedSize(true);

        myRecycleview.setLayoutManager(new LinearLayoutManager(this));



        mRegProg = new ProgressDialog(this  );
        mAuth = FirebaseAuth.getInstance();
        mRef= FDB.getReference("Users");


        selectWorker = getIntent().getStringExtra("name");

//        selectedUsers.setText(selectWorker);


        //UserDetails=new ArrayList<com.example.praba.ipfire.UserDetails>();

        textProName = (EditText)findViewById(R.id.textProName);
        textProDes = (EditText)findViewById(R.id.textProDes);
        selectedUsers = (EditText)findViewById(R.id.textSelectedUsers);
        calendarView = (CalendarView)findViewById(R.id.calendarView);
        buttonCreate = (Button)findViewById(R.id.buttonCreate);




        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                date= year + "/" +(month+1) + "/" + dayOfMonth;

            }
        });


        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pname =textProName.getText().toString();
                String des = textProDes.getText().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String selectedDate = sdf.format(new Date(calendarView.getDate()));

                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                String uID = currentUser.getUid();

                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Project").child(pname);

                if(!TextUtils.isEmpty(pname)||!TextUtils.isEmpty(des)||!TextUtils.isEmpty(date)){
                    mRegProg.setTitle("Adding Project");
                    mRegProg.setMessage("Please wait while add project details");
                    mRegProg.setCanceledOnTouchOutside(false);
                    mRegProg.show();
                    ToDatabase(pname,des,selectedDate);


                }else{
                    Toast.makeText(NewProject.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }

            }
        });

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String uid = ds.getKey().toString();
                        String name = ds.child("name").getValue().toString();
                        String status = ds.child("status").getValue().toString();
                        String address = ds.child("address").getValue().toString();
                        String email = ds.child("email").getValue().toString();

                        listData.add(new UserDetails(uid, name, status));


                    }
                    adapter = new UserAdapter(NewProject.this, (ArrayList<UserDetails>) listData);
                    myRecycleview.setAdapter(adapter);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    /* void GetDataFirebase(){
        mDatabase = FDB.getReference("Users");

        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                MyDataSetGet data = new MyDataSetGet();
                data = dataSnapshot.getValue(MyDataSetGet.class);

                listData.add(data);

                myRecycleview.setAdapter(adapter);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

   /* public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

        List<MyDataSetGet> ListArray;

        public MyAdapter(List<MyDataSetGet> List){
            this.ListArray = List;
        }

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_row, parent, false);

            return  new  MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            MyDataSetGet data = ListArray.get(position);

            holder.MyText.setText(data.getName());
            holder.MyStatus.setText(data.getStatus());
        }

        public class MyViewHolder extends RecyclerView.ViewHolder{

            TextView MyText;
            TextView MyStatus;

            public MyViewHolder(View itemView) {
                super(itemView);

                TextView MyText = (TextView)itemView.findViewById(R.id.textname);
                TextView MyStatus = (TextView)itemView.findViewById(R.id.textstatus);
            }
        }

        @Override
        public int getItemCount() {
            return ListArray.size();
        }
    } */

    private void ToDatabase(String pname ,String des ,String date) {

        final DatabaseReference newRef = mDatabase.push();

        final Map project = new HashMap();
        project.put("Project_name", pname);
        project.put("description", des);
        project.put("due_date", date );


        Thread mainThread = new Thread(new Runnable() {
            @Override
            public void run() {
                mDatabase.setValue(project).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mRegProg.dismiss();

                        if (task.isSuccessful()){
                            Toast.makeText(NewProject.this, "Project Added !", Toast.LENGTH_SHORT).show();
                            Intent showHomePanel = new Intent(NewProject.this,HomeActivity.class);
                            startActivity(showHomePanel);
                    }else {
                            Toast.makeText(NewProject.this, "Error ! Try Again", Toast.LENGTH_SHORT).show();
                        }
                }

            });
        }

        });

        mainThread.start();

    }


}








/*private RecyclerView mblogList;
private DatabaseReference mDatabase; */

      /*  mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        mDatabase.keepSynced(true);

        mblogList = (RecyclerView)findViewById(R.id.myrecycleview);
        mblogList.setHasFixedSize(true);
        mblogList.setLayoutManager(new LinearLayoutManager(this)); */



/*
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Blog, BlogViewHolder>firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, BlogViewHolder>
                (Blog.class,R.layout.blog_row,BlogViewHolder.class,mDatabase) {
            @Override
            protected void onBindViewHolder(BlogViewHolder holder, int position, Blog model) {
                holder.setName(model.getName());
                holder.setStatus(model.getStatus());
            }

            }

            @Override
            public BlogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return null;
            }
        };


//
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public BlogViewHolder(View itemView){
            super(itemView);
            mView = itemView;
        }
        public  void setName(String name){
            TextView textname = (TextView)mView.findViewById(R.id.textname);
            textname.setText(name);

        }
        public  void setStatus(String status){
            TextView textstatus = (TextView)mView.findViewById(R.id.textstatus);
            textstatus.setText(status);
    }  */



