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

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.userViewHolder> {

    private Context mCtx;
    private String id;
    private ArrayList<ProjectDetails> userdata;



    public ProjectAdapter(Context mCtx, ArrayList<com.example.praba.ipfire.ProjectDetails> userdata) {
        this.mCtx = mCtx;
        this.userdata = userdata;


    }


    @Override
    public ProjectAdapter.userViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(mCtx);
        View view=inflater.inflate(R.layout.blog_row,parent,false);
        userViewHolder holder=new userViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(ProjectAdapter.userViewHolder holder, int position) {

        ProjectDetails users=userdata.get(position);

        holder.usersname.setText(users.getName());
        holder.usersname.setText(users.getDesc());


    }


    @Override
    public int getItemCount() {
        return userdata.size();
    }

    public void filteruserlist(ArrayList<ProjectDetails> filterduser) {

        userdata=filterduser;
        notifyDataSetChanged();

    }


    class  userViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView usersname,phone,status;


        public userViewHolder(View itemView) {
            super(itemView);
            usersname=itemView.findViewById(R.id.textname);
            status=itemView.findViewById(R.id.textstatus);
            itemView.setOnClickListener(userViewHolder.this);



        }



        @Override
        public void onClick(View v) {


            int position = getAdapterPosition();
            ProjectDetails id=userdata.get(position);
            Intent newIntent = new Intent(mCtx,ProjectDetailsActivity.class);
            String projname=id.getName();
            newIntent.putExtra("projName",projname);
            mCtx.startActivity(newIntent);

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
