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
        /*
        db.execSQL(DatabaseContract.Recipe.CREATE_TABLE);
        db.execSQL(DatabaseContract.Step.CREATE_TABLE);
        db.execSQL(DatabaseContract.Ingredient.CREATE_TABLE);
        db.execSQL(DatabaseContract.User.CREATE_TABLE);
        db.execSQL(DatabaseContract.Keyword.CREATE_TABLE);
        db.execSQL(DatabaseContract.Ingredient_Recipe.CREATE_TABLE);
        db.execSQL(DatabaseContract.User_Recipe.CREATE_TABLE);
        db.execSQL(DatabaseContract.Keyword_Recipe.CREATE_TABLE);
         */
        db.execSQL("CREATE TABLE recipe (id INTEGER PRIMARY KEY AUTOINCREMENT, recipe_id INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE step (step_no INTEGER, description TEXT, recipe_id INTEGER, FOREIGN KEY (recipe_id) REFERENCES recipe(id) ON UPDATE CASCADE ON DELETE CASCADE, PRIMARY KEY (recipe_id, step_no))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*
        db.execSQL(DatabaseContract.Recipe.DELETE_TABLE);
        db.execSQL(DatabaseContract.Step.DELETE_TABLE);
        db.execSQL(DatabaseContract.Ingredient.DELETE_TABLE);
        db.execSQL(DatabaseContract.User.DELETE_TABLE);
        db.execSQL(DatabaseContract.Keyword.DELETE_TABLE);
        db.execSQL(DatabaseContract.Ingredient_Recipe.DELETE_TABLE);
        db.execSQL(DatabaseContract.User_Recipe.DELETE_TABLE);
        db.execSQL(DatabaseContract.Keyword_Recipe.DELETE_TABLE);
         */

        db.execSQL("drop table if exists recipe");
        db.execSQL("drop table if exists step ");

        onCreate(db);
    }

    // initial function for adding data to DB -- incomplete -- do not use
    public boolean addData(String item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContract.Recipe.RECIPE_ID, item);

        Log.d(TAG, "addData: Adding " + item + " to " + DatabaseContract.Recipe.TABLE_NAME);

        long result = db.insert(DatabaseContract.Recipe.TABLE_NAME, null, contentValues);

        if (result == -1){
            return false;
        }else{
            return true;
        }
    }
}
