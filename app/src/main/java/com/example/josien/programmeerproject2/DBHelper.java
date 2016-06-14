package com.example.josien.programmeerproject2;

/* Josien Jansen
*  11162295
*  Universiteit van Amsterdam
*/

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;

import java.util.ArrayList;

/*
*  Create a SQLdatabase
*/
public class DBHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "historie.db";
    public static final String TABLE_History = "historie";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_VERTREKTIJD = "vertrektijd";
    public static final String COLUMN_EINDBESTEMMING = "eindbestemming";


    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_History + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EINDBESTEMMING + " TEXT, " + COLUMN_VERTREKTIJD + " TEXT" +
                ")";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_History);
        onCreate(db);
    }

    /*
    * Add a new row to the database
    */
    public void addHistory(String eindbestemming, String vertrektijd){
        ContentValues values = new ContentValues();
        values.put(COLUMN_EINDBESTEMMING, eindbestemming);
        values.put(COLUMN_VERTREKTIJD, vertrektijd);
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_History, null,values);
        db.close();

    }


    /*
    * Delete an item from the database
    */
    public void deleteItem (int id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_History, "_id = ?", new String[] {String.valueOf(id)});
        db.close();
    }

    /*
    * Get all history items from the database
    */
    public ArrayList<History> retrieveHistorie(){
        ArrayList<History> historieArray = new ArrayList<>();

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_History + " WHERE 1";

        Cursor c = db.rawQuery(query, null);

        // Move to the first row in your results
        if (c.moveToFirst()) {
            // Loop through all results
            do {
                // Create a new History object and set the correct data
                History todo = new History();
                todo.set_id(c.getInt(0));
                todo.set_eindbestemming(c.getString(1) + "                  ");
                todo.set_vertrektijd(c.getString(2));

                historieArray.add(todo);
            } while (c.moveToNext()) ;
        }

        db.close();
        c.close();
        return historieArray;
    }
}