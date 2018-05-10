package com.example.praba.ipfire;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.userViewHolder> {

    private Context mCtx;
    private String id;
    private ArrayList<UserDetails> userdata;



    public UserAdapter(Context mCtx, ArrayList<com.example.praba.ipfire.UserDetails> userdata) {
        this.mCtx = mCtx;
        this.userdata = userdata;


    }


    @Override
    public UserAdapter.userViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(mCtx);
        View view=inflater.inflate(R.layout.blog_row,parent,false);
        userViewHolder holder=new userViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(userViewHolder holder, int position) {

        UserDetails users=userdata.get(position);

        holder.usersname.setText(users.getUsername());
        holder.status.setText(users.getStatus());

    }


    @Override
    public int getItemCount() {
        return userdata.size();
    }

    public void filteruserlist(ArrayList<UserDetails> filterduser) {

        userdata=filterduser;
        notifyDataSetChanged();

    }


    class  userViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView usersname,phone,status;


        public userViewHolder(View itemView) {
            super(itemView);
            usersname=itemView.findViewById(R.id.textname);
            status=itemView.findViewById(R.id.textstatus);
           // itemView.setOnClickListener(userViewHolder.this);



        }


        @Override
        public void onClick(View v) {

            int position=getAdapterPosition();
            com.example.praba.ipfire.UserDetails id = userdata.get(position);

            String workerName = id.getUsername();
            NewProject newProject = new NewProject();

            Intent selectUsers = new Intent(mCtx,NewProject.class);
            selectUsers.putExtra("name",workerName);
            mCtx.startActivity(selectUsers);


           /* int position=getAdapterPosition();
            ithub.mobileposadmin.userdata id=userdata.get(position);
            String name=id.getName();
            String tell=id.getTell();
            String address=id.getAddress();
            String uid=id.getUid();

            Intent userdata=new Intent(mCtx,userDisplay.class);
            //Toast.makeText(mCtx,String.valueOf(name),Toast.LENGTH_SHORT).show();
             /*       userdata.putExtra("name",name);
            userdata.putExtra("tell",tell);
            userdata.putExtra("address",address);
            userdata.putExtra("uid",uid);
            mCtx.startActivity(userdata); */


        }
    }
}
