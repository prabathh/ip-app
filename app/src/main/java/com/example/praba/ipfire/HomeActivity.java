package com.example.praba.ipfire;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth firebaseAuth;

    private Button buttonLogout;
    private Button buttonMyProject;
    private Button buttonNewProject;
    private Button buttonMyProfile;
    private Button chat;
    private TextView userName;


    String loggedUserName;
    private FirebaseUser mCurrentUser;
    private DatabaseReference usersReference;




    @Override
    public void onStart() {
        super.onStart();

        if(!isConnected(HomeActivity.this)) buildDialog(HomeActivity.this).show();
        else {

            FirebaseUser user = firebaseAuth.getCurrentUser();

            final String userID = user.getUid();

            usersReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
            usersReference.keepSynced(true);

            userName = (TextView) findViewById(R.id.loggedUserName);

            usersReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    loggedUserName = dataSnapshot.child("name").getValue().toString();
                    // mUserName.setText(loggedUserName);
                    userName.setText(loggedUserName);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        firebaseAuth = FirebaseAuth.getInstance();

        mCurrentUser = firebaseAuth.getCurrentUser();






        if (firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }




        buttonMyProject = (Button)findViewById(R.id.buttonMyProject);
        buttonNewProject = (Button)findViewById(R.id.buttonNewProject);
        chat = (Button)findViewById(R.id.buttonChat);


        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(HomeActivity.this,ChatActivity.class);
                newIntent.putExtra("currentUserName",loggedUserName);
                startActivity(newIntent);
            }
        });


        buttonMyProject.setOnClickListener(this);
        buttonNewProject.setOnClickListener(this);




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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);


        getMenuInflater().inflate(R.menu.main_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId() == R.id.logOut){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class ));


        }else if(item.getItemId() == R.id.myAccount){

                final String loggedUserID = mCurrentUser.getUid();
                Intent profile = new Intent(HomeActivity.this,MyAccountActivity.class);
                profile.putExtra("username",loggedUserID);
                startActivity(profile);
        }


        return true;
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
