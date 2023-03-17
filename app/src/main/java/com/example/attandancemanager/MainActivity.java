package com.example.attandancemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerlayout;
    NavigationView navigationview;
    Toolbar toolbar;
    TextView date,addsubj,goall,presattend,totalperc;
    RecyclerView recview;
    int goal=75;
    private Mydbhandler db;
    ArrayList<Model> arr=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Dialog dialog = new Dialog(MainActivity.this);
        drawerlayout=findViewById(R.id.drawerlayout);
        goall=findViewById(R.id.goall);
        totalperc=findViewById(R.id.totalperc);
        presattend=findViewById(R.id.presattend);
        addsubj=findViewById(R.id.addsubj);
        navigationview=findViewById(R.id.navigationview);
        toolbar=findViewById(R.id.toolbar);
        recview=findViewById(R.id.recview);
        recview.setLayoutManager(new LinearLayoutManager(this));
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        db=new Mydbhandler(MainActivity.this);

        SharedPreferences pref= getSharedPreferences("Goal", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=pref.edit();
        editor.putInt("key1",goal);
        editor.apply();
        goall.setText("Goal:"+goal+"%");

        //setting date
        date=findViewById(R.id.date);
        final String currenttimestamp = String.valueOf(System.currentTimeMillis());
        Timestamp timestamp=new Timestamp(Long.parseLong(currenttimestamp));
        Date date1=new Date(timestamp.getTime());
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        date.setText(simpleDateFormat.format(date1));
        Recycleattandadapter adapter=new Recycleattandadapter(this,db);
        recview.setAdapter(adapter);

        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerlayout,toolbar,R.string.OpenDrawer,R.string.CloseDrawer);
        drawerlayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();
                if(id==R.id.Subjects){
                }
                else if(id==R.id.Editcrit){
                    dialog.setContentView(R.layout.editgoal);
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    EditText newgoal;
                    Button cancel,set;
                    newgoal=dialog.findViewById(R.id.newgoal);
                    cancel=dialog.findViewById(R.id.cancel);
                    set=dialog.findViewById(R.id.set);
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    set.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(newgoal.getText().toString().isEmpty()||Integer.parseInt(newgoal.getText().toString())>100){
                                Toast.makeText(MainActivity.this,"Enter a valid Goal",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                goal=Integer.parseInt(newgoal.getText().toString());
                                SharedPreferences pref= getSharedPreferences("Goal", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor=pref.edit();
                                editor.putInt("key1",goal);
                                editor.apply();
                                adapter.notifyDataSetChanged();
                                goall.setText("Goal:"+goal+"%");
                                dialog.dismiss();
                            }
                        }
                    });
                    dialog.show();
                }
                else if(id==R.id.Suggestion || id==R.id.Report){
//                    Intent i = new Intent(Intent.ACTION_SENDTO);
//                    i.setType("text/plain");
//                    i.setData(Uri.parse("dpadhan121@gmail.com"));
//                    i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"dpadhan121@gmail.com"});
//                    i.putExtra(Intent.EXTRA_SUBJECT, "Mail Subject");
//                    i.putExtra(Intent.EXTRA_TEXT   , "massage");
//                    i.setPackage("com.google.android.gm");
//                    try {
//                        startActivity(Intent.createChooser(i, "Send mail..."));
//                    } catch (android.content.ActivityNotFoundException ex) {
//                        Toast.makeText(MainActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
//                    }



                    String recipient = "dpadhan121@gmail.com";
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("googlegmail:///co"));
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[] { recipient });
                    PackageManager packageManager = getPackageManager();
                    List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
                    boolean isIntentSafe = activities.size() > 0;

                    if (isIntentSafe) {
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "No email app found on this device", Toast.LENGTH_SHORT).show();
                    }

                }
                else{
                }
                drawerlayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        addsubj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setContentView(R.layout.newsubj_dialog);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                EditText subject,pres,total;
                Button cancel,add;
                subject=dialog.findViewById(R.id.subject);
                pres=dialog.findViewById(R.id.pres);
                total=dialog.findViewById(R.id.total);
                cancel=dialog.findViewById(R.id.cancel);
                add=dialog.findViewById(R.id.add);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String sub=subject.getText().toString();
                        String t=total.getText().toString();
                        String p=pres.getText().toString();
                        if(sub.isEmpty()||t.isEmpty()||p.isEmpty()){
                            Toast.makeText(MainActivity.this,"All fields are required",Toast.LENGTH_SHORT).show();
                        } else if (Integer.parseInt(t)<Integer.parseInt(p)) {
                            Toast.makeText(MainActivity.this,"Total classes can't be less than classes present",Toast.LENGTH_SHORT).show();
                        } else{
                            Model m=new Model(sub,Integer.parseInt(t),Integer.parseInt(p));
                            db.insertTask(m);
                            adapter.updateData(m);
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
            }
        });
        List<Model> contacts = db.getAllTasks();
        int a=0,b=0;
        for (Model cn : contacts){
            a+=cn.getTotalc() ;
            b+=cn.getPresc();
        }
        totalperc.setText((int)(b*100/a)+"%");
        presattend.setText("Overall Attendance:"+(int)(b*100/a)+"%");
    }
    @Override
    public void onBackPressed(){
        if(drawerlayout.isDrawerOpen(GravityCompat.START)){
            drawerlayout.closeDrawer(GravityCompat.START);
        } else{
            super.onBackPressed();
        }
    }
}