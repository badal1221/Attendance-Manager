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
import android.os.Bundle;
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
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerlayout;
    NavigationView navigationview;
    Toolbar toolbar;
    TextView date,addsubj;
    RecyclerView recview;
    private Mydbhandler db;
    ArrayList<Model> arr=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Dialog dialog = new Dialog(MainActivity.this);
        drawerlayout=findViewById(R.id.drawerlayout);
        addsubj=findViewById(R.id.addsubj);
        navigationview=findViewById(R.id.navigationview);
        toolbar=findViewById(R.id.toolbar);
        recview=findViewById(R.id.recview);
        recview.setLayoutManager(new LinearLayoutManager(this));
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        db=new Mydbhandler(MainActivity.this);
//        db.openDatabase();
        //setting date
        date=findViewById(R.id.date);
        final String currenttimestamp = String.valueOf(System.currentTimeMillis());
        Timestamp timestamp=new Timestamp(Long.parseLong(currenttimestamp));
        Date date1=new Date(timestamp.getTime());
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        date.setText(simpleDateFormat.format(date1));

        arr.add(new Model("Maths",28,14));
        Recycleattandadapter adapter=new Recycleattandadapter(this,arr);
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
                else if(id==R.id.home){
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
                        }
                        else{
//                            Model m=new Model(sub,Integer.parseInt(t),Integer.parseInt(p));
//                            db.insertTask(m);
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
            }
        });
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