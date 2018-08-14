package demo.recycle.com.salforceapp.SqliteHelper;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import demo.recycle.com.salforceapp.Activity.TaskActivity;
import demo.recycle.com.salforceapp.pojo.Taskpojo;

/**
 * Created by User on 07-08-2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "tasks_db";
  static   DatabaseHelper  sInstance;
 static Context contextofActivity;

    public static synchronized DatabaseHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx

        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
            contextofActivity=context;
        }
        return sInstance;
    }


    public DatabaseHelper(Context context)
    {

        super(context, DATABASE_NAME,null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(Taskpojo.CREATE_TABLE);
        db.execSQL(Taskpojo.CREATE_TABLE1);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Taskpojo.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Taskpojo.TABLE_NAME1);

        // Create tables again
        onCreate(db);

    }

    //  WhatId,OwnerId,Description,Status,ActivityDate,Priority,Subject,WhoId

    public long insertTask(String WhatId,String OwnerId,String Description,String Status,String ActivityDate,String Priority,String Subject,String WhoId) {
        // get writable database as we want to write data
        SQLiteDatabase db = sInstance.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Taskpojo.TASK_WhatId, WhatId);
        values.put(Taskpojo.TASK_OwnerId, OwnerId);
        values.put(Taskpojo.TASK_Description, Description);
        values.put(Taskpojo.TASK_Status, Status);
        values.put(Taskpojo.TASK_ActivityDate, ActivityDate);
        values.put(Taskpojo.TASK_Priority, Priority);
        values.put(Taskpojo.TASK_Subject, Subject);
        values.put(Taskpojo.TASK_WhoId, WhoId);



        // insert row
        long id = db.insert(Taskpojo.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }


    public List<Taskpojo> getAllTask(String DATE) {



        List<Taskpojo> tasks = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Taskpojo.TABLE_NAME+ " where  ActivityDate='" +
                DATE+"'";

        SQLiteDatabase db = sInstance.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                //  WhatId,OwnerId,Description,Status,ActivityDate,Priority,Subject,WhoId

                Taskpojo task = new Taskpojo();
                task.setId(String.valueOf(cursor.getInt(cursor.getColumnIndex(Taskpojo.TASK_ID))));
                task.setDescription(cursor.getString(cursor.getColumnIndex(Taskpojo.TASK_Description)));
                task.setEventDate(cursor.getString(cursor.getColumnIndex(Taskpojo.TASK_ActivityDate)));
                task.setStatus(cursor.getString(cursor.getColumnIndex(Taskpojo.TASK_Status)));
                task.setAccountid(cursor.getString(cursor.getColumnIndex(Taskpojo.TASK_WhatId)));
                task.setContactid(cursor.getString(cursor.getColumnIndex(Taskpojo.TASK_WhoId)));
                task.setPriority(cursor.getString(cursor.getColumnIndex(Taskpojo.TASK_Priority)));
                task.setTaskName(cursor.getString(cursor.getColumnIndex(Taskpojo.TASK_Subject)));




                tasks.add(task);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();


        // return notes list
        return tasks;
    }

    public long insertDraft(String WhatId,String OwnerId,String Description,String Status,String ActivityDate,String Priority,String Subject,String WhoId) {
        // get writable database as we want to write data
        SQLiteDatabase db = sInstance.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Taskpojo.DRAFT_WhatId, WhatId);
        values.put(Taskpojo.DRAFT_OwnerId, OwnerId);
        values.put(Taskpojo.DRAFT_Description, Description);
        values.put(Taskpojo.DRAFT_Status, Status);
        values.put(Taskpojo.DRAFT_ActivityDate, ActivityDate);
        values.put(Taskpojo.DRAFT_Priority, Priority);
        values.put(Taskpojo.DRAFT_Subject, Subject);
        values.put(Taskpojo.DRAFT_WhoId, WhoId);



        // insert row
        long id = db.insert(Taskpojo.TABLE_NAME1, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }


    public List<Taskpojo> getAllDraft() {
        List<Taskpojo> tasks = new ArrayList<>();

        // Select All Query
        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(contextofActivity);
        String userid = shared.getString("UserId", "");
        String selectQuery = "SELECT  * FROM " + Taskpojo.TABLE_NAME1+ " where  OwnerId='" +
                userid  + "' ORDER BY " +
                Taskpojo.TASK_ActivityDate + " DESC";

        SQLiteDatabase db = sInstance.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                //  WhatId,OwnerId,Description,Status,ActivityDate,Priority,Subject,WhoId

                Taskpojo task = new Taskpojo();
                task.setId(String.valueOf(cursor.getInt(cursor.getColumnIndex(Taskpojo.DRAFT_ID))));
                task.setDescription(cursor.getString(cursor.getColumnIndex(Taskpojo.DRAFT_Description)));
                task.setEventDate(cursor.getString(cursor.getColumnIndex(Taskpojo.DRAFT_ActivityDate)));
                task.setStatus(cursor.getString(cursor.getColumnIndex(Taskpojo.DRAFT_Status)));
                task.setAccountid(cursor.getString(cursor.getColumnIndex(Taskpojo.DRAFT_WhatId)));
                task.setContactid(cursor.getString(cursor.getColumnIndex(Taskpojo.DRAFT_WhoId)));
                task.setPriority(cursor.getString(cursor.getColumnIndex(Taskpojo.DRAFT_Priority)));
                task.setTaskName(cursor.getString(cursor.getColumnIndex(Taskpojo.DRAFT_Subject)));




                tasks.add(task);
            } while (cursor.moveToNext());
        }




        // close db connection
        db.close();


        // return notes list
        return tasks;
    }
    public void clearTaskTable()   {
        SQLiteDatabase db = sInstance.getWritableDatabase();
        db.delete(Taskpojo.TABLE_NAME, null,null);
        db.close();
    }




    public void DeleteDraft(String id) {
        List<Taskpojo> tasks = new ArrayList<>();

        // Select All Query

        String selectQuery = "DELETE FROM " + Taskpojo.TABLE_NAME1+ " where  id='" +
                id +"'";



        SQLiteDatabase db = sInstance.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list


      //  SQLiteDatabase db = getWritableDatabase();
        String whereClause = "id=?";
        String whereArgs[] = {id};
    int result   = db.delete(Taskpojo.TABLE_NAME1, whereClause, whereArgs);
        Log.d("TAG", "DeleteDraft: "+result);


        // close db connection
        db.close();


        // return notes list

    }









}
