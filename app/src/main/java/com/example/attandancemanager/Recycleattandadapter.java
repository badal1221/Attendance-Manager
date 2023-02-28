package com.example.attandancemanager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
public class Recycleattandadapter extends RecyclerView.Adapter<Recycleattandadapter.viewholder>{
    Context context;
    List<Model> arr;
    private Mydbhandler db;
    public Recycleattandadapter(Context context,Mydbhandler db) {
        this.context = context;
        this.db=db;
        this.arr=this.db.getAllTasks();
    }
    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.rec_allsubj,parent,false);
        viewholder holder=new viewholder(v);
        return holder;
    }
    @Override
    public void onBindViewHolder(@NonNull viewholder holder,int position) {
        Model m=arr.get(position);
        holder.subject.setText(m.getSubject());
        holder.attend.setText("Attandance: "+m.getPresc()+"/"+m.getTotalc());
        if(m.getTotalc()==0){
            holder.perc.setText("0%");
            holder.leave.setText("You can't leave any class now");
        }
        else{
            int x=(int)(m.getPresc()*100/m.getTotalc());
            holder.perc.setText(x+"%");
            SharedPreferences pref= context.getSharedPreferences("Goal",Context.MODE_PRIVATE);
            int goal=pref.getInt("key1",100);
            if(x>=goal){
                holder.status.setText("Status:On Track");
                int a=0;
                while (a >= 0) {
                    if(m.getPresc()*100/(m.getTotalc()+a)<goal){
                        break;
                    }
                    a+=1;
                }
                a-=1;
                holder.leave.setText("You can leave "+ a +" classes now");
            }
            else{
                holder.status.setText("Status:Not On Track");
                int a=0;
                while(a>=0) {
                    if((m.getPresc()+a)*100/(m.getTotalc()+a)>=goal){
                        break;
                    }
                    a+=1;
                }
                holder.leave.setText("You need to attend "+a+" classes to get on track");
            }
        }
        holder.present.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.performHapticFeedback(HapticFeedbackConstants.REJECT);
                m.totalc+=1;
                m.presc+=1;
                db.updateattend(m.getSubject(),m.getTotalc(),m.getPresc());
                notifyDataSetChanged();
            }
        });
        holder.absent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.performHapticFeedback(HapticFeedbackConstants.REJECT);
                m.totalc+=1;
                db.updateattend(m.getSubject(),m.getTotalc(),m.getPresc());
                notifyDataSetChanged();
            }
        });
        holder.ll1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                view.performHapticFeedback(HapticFeedbackConstants.REJECT);
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Do you want to delete?");
                builder.setTitle("Delete !");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                    db.deletetask(m.getSubject());
                    arr.remove(position);
                    view.performHapticFeedback(HapticFeedbackConstants.REJECT);
                    notifyDataSetChanged();
                    dialog.cancel();
                });
                builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                    dialog.cancel();
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return false;
            }
        });
    }
    public void updateData(Model m){
        this.arr.add(m);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return arr.size();
    }
    public class viewholder extends RecyclerView.ViewHolder{
        LinearLayout ll1;
        TextView subject,attend,status,perc,leave;
        CircleImageView present,absent,men;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            ll1=itemView.findViewById(R.id.ll1);
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
