package com.hblackcat.sttpro.DataBase;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

import java.io.File;

public class MySQLiteHelper extends SQLiteOpenHelper {

    private String DB_PATH = "/data/data/com.hblackcat.sttpro/databases/"; // path of db file ..
    private Context context;
    private SQLiteDatabase myDataBase;

    public MySQLiteHelper(Context contexts) {
        super(contexts, "stt.db", null, 1);
        this.context = contexts;
    }

    //implementation method ..
    @Override
    public void onCreate(SQLiteDatabase db) { }

    //implementation method ..
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }

    // Creates a empty database on the system if not exist ..
    public void createDataBase(){
        //check database exist ..
        boolean dbExist = checkDataBase();
        if(dbExist){
            //do nothing - database already exist
        }else{
            // if not exist ..
            try {
                //create database with three column ..
                myDataBase =getWritableDatabase();
                // make name unique to avoid repeat it ..
                String create_table = "CREATE TABLE reserved_words_tbl(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL UNIQUE, value TEXT NOT NULL)";
                myDataBase.execSQL(create_table);

                // insert all fixed values in column name ..
                fixedValues();
            } catch (Exception e) {e.printStackTrace();}
        }
    }

    // Check if the database already exist ..@return true if it exists, false if it doesn't
    private boolean checkDataBase(){
        File dbFile = context.getDatabasePath("stt.db");
        return dbFile.exists();
    }

    //open database ..
    public void openDataBase(){
        try {
            //Open the database
            String myPath = DB_PATH + "stt.db";
            myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        }catch (Exception e){e.printStackTrace();}
    }

    // insert all fixed values in columns name and value ..
    public void fixedValues()
    {
        try
        {
            insertIntoDBNames("first_time","true");
            insertIntoDBNames("auto_stt","3");
            insertIntoDBNames("tts_speed","1.0");
            insertIntoDBNames("reserved_words","true");
        } catch (Exception e) {e.printStackTrace();}
    }

    //insert into database fixed unique names and value ..
    public void insertIntoDBNames(String word_before,String word_after)
    {
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues content_values = new ContentValues();
            content_values.put("name", word_before);
            content_values.put("value", word_after);
            db.insert("reserved_words_tbl", null, content_values);
            db.close();
        }catch (Exception e){e.printStackTrace();}
    }

    //get all data ..
    public Cursor getAllData() {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor res = db.rawQuery("select * from reserved_words_tbl",null);
            return res;
        }catch (Exception e){e.printStackTrace();}
        return null;
    }

    //replace value method ..
    public boolean replaceValue(String name ,String new_name ,String new_value) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String query = "UPDATE reserved_words_tbl SET name='"+new_name+"' , value='"+new_value+"' WHERE name='"+name+"'";
            db.execSQL(query);
            //return true if done ..
            return true;
        }catch (Exception e){e.printStackTrace();}
        //return false if crash or name from DialogNewItem is existed before in DB (it will crash also because names In DB are UNIQUE so it will crash and return false) ..
        return false;
    }

    //getValue Search in DataBase ..
    public String getValue(String name) {
        try
        {
            SQLiteDatabase db = this.getWritableDatabase();
            SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder ();

            //table name ..
            queryBuilder.setTables("reserved_words_tbl");

            // Search in column 1 where name like ..
            String selectQuery = " select * from reserved_words_tbl where name like  '%"+ name +"%'";

            // Initialize Cursor ..
            Cursor mCursor = db.rawQuery(selectQuery, null);

            //Search in all table start from the top of column ..
            if(mCursor.moveToFirst())
            {
                do{
                    return mCursor.getString(mCursor.getColumnIndex("value"));
                }while (mCursor.moveToNext());
            }
        }catch (Exception e){e.printStackTrace();}
        return null;
    }

    //get rows all data name and value ..
    public String[] getRowsAllData(String which_value) {
        try
        {
            SQLiteDatabase db = this.getWritableDatabase();
            SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder ();

            //table name ..
            queryBuilder.setTables("reserved_words_tbl");

            // Initialize Cursor ..
            Cursor res = db.rawQuery("select * from reserved_words_tbl",null);

            //Search in all table start from the top of column ..
            if(res.moveToFirst())
            {
                int length=(int)getCountOfRows();
                String[] before_array = new String[length];
                String[] after_array = new String[length];

                for(int i = 0; i < length; i++)
                {
                    before_array[i] = res.getString(res.getColumnIndex("name"));
                    after_array[i] = res.getString(res.getColumnIndex("value"));

                    if(res.moveToNext() == false)
                        break;
                }
                if(which_value.equals("before"))
                    return before_array;
                else
                    return after_array;
            }
        }catch (Exception e){e.printStackTrace();}
        return null;
    }

    //get count of rows ..
    public long getCountOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, "reserved_words_tbl");
        return count;
    }

    //delete specific row ..
    public void deleteRow(String the_name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM reserved_words_tbl WHERE name='"+the_name+"';";
        db.execSQL(query);
    }

    //Search in DataBase ..
    public boolean searchInDB(String befores) {
        try
        {
            SQLiteDatabase db = this.getWritableDatabase();
            SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder ();

            //table name ..
            queryBuilder.setTables("reserved_words_tbl");

            // Search in column 1 where name like ..
            String selectQuery = "select * from reserved_words_tbl where name like  '%" + befores +"%'";

            // Initialize Cursor ..
            Cursor mCursor = db.rawQuery(selectQuery, null);

            //Search in all table start from the top of column ..
            if(mCursor.moveToFirst())
            {
                do{
                    return true;
                }while (mCursor.moveToNext());
            }
        }catch (Exception e){e.printStackTrace();}
        return false;
    }

    //Drop database if exist ..
    public void dropDataBase()
    {
        //check database exist ..
        boolean dbExist = checkDataBase();

        if(dbExist) {
            //database already exist .. delete it
            context.deleteDatabase("stt.db");
        }
    }

    //close database ..
    @Override
    public synchronized void close() {

        if(myDataBase != null)
            myDataBase.close();
        super.close();
    }
}