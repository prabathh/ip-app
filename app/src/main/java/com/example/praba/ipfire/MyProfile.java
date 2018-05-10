package com.example.praba.ipfire;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MyProfile extends AppCompatActivity implements View.OnClickListener{

    private Button buttonEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        buttonEdit = (Button)findViewById(R.id.buttonEdit);

        buttonEdit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonEdit){

            Intent intent = new Intent(MyProfile.this,MainActivity.class);
            startActivity(intent);
        }
    }
}
