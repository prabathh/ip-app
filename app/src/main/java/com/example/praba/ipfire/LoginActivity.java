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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private Button buttonSignIn;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignUp;

    private FirebaseAuth firebaseAuth;

    private ProgressDialog progressDialog;

    @Override
    protected void onStart() {
        super.onStart();

        if(!isConnected(LoginActivity.this)) buildDialog(LoginActivity.this).show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



            progressDialog = new ProgressDialog(this);

            firebaseAuth = FirebaseAuth.getInstance();

            if (firebaseAuth.getCurrentUser() != null) {
                finish();
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            }

            buttonSignIn = (Button) findViewById(R.id.buttonSignIn);
            editTextEmail = (EditText) findViewById(R.id.editTextEmail);
            editTextPassword = (EditText) findViewById(R.id.editTextPassword);
            textViewSignUp = (TextView) findViewById(R.id.textViewSignUp);

            buttonSignIn.setOnClickListener(this);
            textViewSignUp.setOnClickListener(this);




    }

    private void userLogin(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)){
            //email is empty
            Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)){
            //password is empty
            Toast.makeText(this, "Please enter password", Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.setMessage("Loggin Please wait..");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if (task.isSuccessful()){
                            finish();
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        }
                    }
                });

    }

    @Override
    public void onClick(View v) {
        if (v == buttonSignIn){
            userLogin();
        }

        if (v == textViewSignUp){
            finish();
            startActivity(new Intent(this, MainActivity.class));
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
