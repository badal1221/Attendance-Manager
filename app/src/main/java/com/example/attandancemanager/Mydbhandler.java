package com.example.attandancemanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class Mydbhandler extends SQLiteOpenHelper {
    private static final int VERSION =1;
    private static final String NAME="AttendDatabase";
    private static final String ATTEND_TABLE="Attendance";
    private static final String SUBJECT="subject";
    private static final String TOTALC="totalc";
    private static final String PRESC="presc";
    private static final String GOAL="goal";
    private static final String CREATE_ATTEND_TABLE = "CREATE TABLE " + ATTEND_TABLE + "(" + SUBJECT+ "  TEXT PRIMARY KEY , " + TOTALC + " INTEGER, "
            + PRESC + " INTEGER)";
    //private SQLiteDatabase db;
    public Mydbhandler(Context context){
        super(context,NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ATTEND_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {
        //DROP older table
        db.execSQL("DROP TABLE IF EXISTS "+ATTEND_TABLE);
        //Create table again
        onCreate(db);
    }
    public void insertTask(Model m){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(SUBJECT,m.getSubject());
        cv.put(TOTALC,m.getTotalc());
        cv.put(PRESC,m.getPresc());
        //cv.put(GOAL,m.getGoal());
        db.insert(ATTEND_TABLE,null,cv);
    }
    public List<Model> getAllTasks(){
        List<Model> m1=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cur=null;
        db.beginTransaction();
        try{
            cur=db.query(ATTEND_TABLE,null,null,null,null,null,null);
            if(cur!=null){
                if(cur.moveToFirst()){
                    do{
                        Model task = new Model();
                        task.setSubject(cur.getString(cur.getColumnIndexOrThrow(SUBJECT)));
                        task.setTotalc(cur.getInt(cur.getColumnIndexOrThrow(TOTALC)));
                        task.setPresc(cur.getInt(cur.getColumnIndexOrThrow(PRESC)));
                        //task.setGoal(cur.getInt(cur.getColumnIndexOrThrow(GOAL)));
                        m1.add(task);
                    }while(cur.moveToNext());
                }
            }
        }finally {
            db.endTransaction();
            assert cur!=null;
            cur.close();
        }
        return m1;
    }
    public void updateattend(String subject,int totalc,int presc){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(TOTALC,totalc);
        cv.put(PRESC,presc);
        db.update(ATTEND_TABLE,cv,SUBJECT+"= ?",new String[]{String.valueOf(subject)});
    }
//    public void updatetask(int id,String task){
//        ContentValues cv=new ContentValues();
//        cv.put(TASK,task);
//        db.update(TODO_TABLE,cv,ID+"= ?",new String[]{String.valueOf(id)});
//    }
    public void deletetask(String subject){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(ATTEND_TABLE,SUBJECT+"= ?",new String[]{String.valueOf(subject)});
    }
}
