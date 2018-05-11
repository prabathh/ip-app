package com.example.praba.ipfire;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonRegister;
    private EditText textEmail;
    private EditText textName;
    private EditText textName2;
    private EditText textAddress;
    private EditText textStatus;
    private EditText textPassword;
    private TextView textViewSignin;
    private EditText textTellnum;

    private DatabaseReference mDatabase;
    private ProgressDialog mRegProg;
    private FirebaseAuth mAuth;


    @Override
    protected void onStart() {
        super.onStart();
        if(!isConnected(MainActivity.this)) buildDialog(MainActivity.this).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

        mRegProg = new ProgressDialog(this  );
        mAuth = FirebaseAuth.getInstance();

            buttonRegister = (Button) findViewById(R.id.buttonRegister);
            textEmail = (EditText)findViewById(R.id.textEmail);
            textName = (EditText)findViewById(R.id.textName);
            textName2 = (EditText)findViewById(R.id.textName2);
            textAddress = (EditText)findViewById(R.id.textAddress);
            textStatus = (EditText)findViewById(R.id.textStatus);
            textPassword = (EditText)findViewById(R.id.textPassword);
            textViewSignin = (TextView)findViewById(R.id.textViewSignin);
            textTellnum = (EditText)findViewById(R.id.textTellnum);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = textEmail.getText().toString();
                String name = textName.getText().toString();
                String name2 = textName2.getText().toString();
                String address = textAddress.getText().toString();
                String status = textStatus.getText().toString();
                String password = textPassword.getText().toString();
                String tell = textTellnum.getText().toString();

                if(!TextUtils.isEmpty(tell)||!TextUtils.isEmpty(email)||!TextUtils.isEmpty(name)||!TextUtils.isEmpty(name2)||!TextUtils.isEmpty(address)||!TextUtils.isEmpty(status)||!TextUtils.isEmpty(password)){

                    mRegProg.setTitle("Registering User");
                    mRegProg.setMessage("Please wait while we create user account");
                    mRegProg.setCanceledOnTouchOutside(false);
                    mRegProg.show();
                    registerUser(tell,email,name,name2,address,status,password);

                }else{
                    Toast.makeText(MainActivity.this, "Please fill all values", Toast.LENGTH_SHORT).show();
                }
            }
        });


            textViewSignin = (TextView)findViewById(R.id.textViewSignin);


            textViewSignin.setOnClickListener(this);

    }

    private void registerUser(final String tell,final String email,final  String name, final String name2,final String address,final String status,final String password) {
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    String uID = currentUser.getUid();

                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uID);
                    HashMap<String , String> userMap = new HashMap<>();

                    userMap.put("tell",tell);
                    userMap.put("email",email);
                    userMap.put("name",name);
                    userMap.put("name2",name2);
                    userMap.put("address",address);
                    userMap.put("status",status);
                    userMap.put("password",password);


                    mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                mRegProg.dismiss();

                                Toast.makeText(MainActivity.this, "User Added ! ", Toast.LENGTH_SHORT).show();
                                Intent newIn = new Intent(MainActivity.this, HomeActivity.class);
                                startActivity(newIn);
                            }
                        }

                    });

                }else{
                    mRegProg.hide();
                    Toast.makeText(MainActivity.this, "Try Again Later !!! ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == textViewSignin){
            startActivity(new Intent(this, LoginActivity.class));
        }
    }


    // Check connection status


    public boolean isConnected(Context context){

        ConnectivityManager cn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cn.getActiveNetworkInfo();

        if(netinfo != null && netinfo.isConnectedOrConnecting()){

            android.net.NetworkInfo wifi = cn.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cn.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if(mobile != null && mobile.isConnectedOrConnecting() || (wifi != null && wifi.isConnectedOrConnecting())){
                return true;
            }else{
                return false;

            }

        }else{
            return false;
        }

    }

    public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("Press Close to Exit");
        builder.setCancelable(false);


        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent dialogIntent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(dialogIntent);

                finish();
            }
        });

        return builder;
    }



}



