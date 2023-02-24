package com.example.attandancemanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Recycleattandadapter extends RecyclerView.Adapter<Recycleattandadapter.viewholder>{
    Context context;
    ArrayList<Model> arr;
    public Recycleattandadapter(Context context, ArrayList<Model> arr) {
        this.context = context;
        this.arr = arr;
    }
    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.rec_allsubj,parent,false);
        viewholder holder=new viewholder(v);
        return holder;
    }
    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        Model m=arr.get(position);
        holder.subject.setText(m.getSubject());
        holder.attend.setText("Attandance:"+m.getPresc()+"/"+m.getTotalc());
        holder.status.setText("Status:On track");
        if(m.getTotalc()==0){
            holder.perc.setVisibility(View.GONE);
        }
        else{
            holder.perc.setVisibility(View.VISIBLE);
            int x=(int)(m.getPresc()*100/m.getTotalc());
            holder.perc.setText(x+"%");
        }

    }
    @Override
    public int getItemCount() {
        return arr.size();
    }
    public class viewholder extends RecyclerView.ViewHolder{

        TextView subject,attend,status,perc,leave;
        CircleImageView present,absent,men;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            subject=itemView.findViewById(R.id.subject);
            attend=itemView.findViewById(R.id.attend);
            status=itemView.findViewById(R.id.status);
            perc=itemView.findViewById(R.id.perc);
            leave=itemView.findViewById(R.id.leave);
            present=itemView.findViewById(R.id.present);
            absent=itemView.findViewById(R.id.absent);
            men=itemView.findViewById(R.id.men);
        }
    }
}
