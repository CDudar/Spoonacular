package com.example.spoonacular;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DatabaseContract.DATABASE_NAME, null, DatabaseContract.DATABASE_VERSION);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create tables in DB
        db.execSQL(DatabaseContract.Recipe.CREATE_TABLE);
        //db.execSQL(DatabaseContract.Step.CREATE_TABLE);
        db.execSQL(DatabaseContract.Ingredient.CREATE_TABLE);
        db.execSQL(DatabaseContract.User.CREATE_TABLE);
        //db.execSQL(DatabaseContract.Keyword.CREATE_TABLE);
        db.execSQL(DatabaseContract.Ingredient_Recipe.CREATE_TABLE);
        //db.execSQL(DatabaseContract.User_Recipe.CREATE_TABLE);
        //db.execSQL(DatabaseContract.Keyword_Recipe.CREATE_TABLE);

     }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // drop tables
        db.execSQL(DatabaseContract.Recipe.DELETE_TABLE);
        //db.execSQL(DatabaseContract.Step.DELETE_TABLE);
        db.execSQL(DatabaseContract.Ingredient.DELETE_TABLE);
        db.execSQL(DatabaseContract.User.DELETE_TABLE);
        //db.execSQL(DatabaseContract.Keyword.DELETE_TABLE);
        db.execSQL(DatabaseContract.Ingredient_Recipe.DELETE_TABLE);
        //db.execSQL(DatabaseContract.User_Recipe.DELETE_TABLE);
        //db.execSQL(DatabaseContract.Keyword_Recipe.DELETE_TABLE);

        // create DB
        onCreate(db);
    }

    // initial function for adding data to DB -- incomplete -- do not use
    public boolean addIngredient(String ing_name) {
        Log.d(TAG, "addData: Adding " + ing_name + " to " + DatabaseContract.Ingredient.TABLE_NAME);

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseContract.Ingredient.NAME, ing_name);
        long result = db.insert(DatabaseContract.Ingredient.TABLE_NAME, null, contentValues);

        if (result == -1){
            return false;
        }else{
            return true;
        }
    }

}
