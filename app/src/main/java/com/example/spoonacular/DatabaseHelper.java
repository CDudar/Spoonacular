package com.example.spoonacular;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DatabaseContract.DATABASE_NAME, null, DatabaseContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys = ON");

        // create tables in DB
        db.execSQL(DatabaseContract.Recipe.CREATE_TABLE);
        db.execSQL(DatabaseContract.Step.CREATE_TABLE);
        db.execSQL(DatabaseContract.Ingredient.CREATE_TABLE);
        db.execSQL(DatabaseContract.User.CREATE_TABLE);
        db.execSQL(DatabaseContract.Keyword.CREATE_TABLE);
        db.execSQL(DatabaseContract.Ingredient_Recipe.CREATE_TABLE);
        db.execSQL(DatabaseContract.User_Recipe.CREATE_TABLE);
        db.execSQL(DatabaseContract.Keyword_Recipe.CREATE_TABLE);
     }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "Upgrading SQLite DB");

        switch (oldVersion) {
            case 2:
                // drop tables
                db.execSQL(DatabaseContract.Recipe.DELETE_TABLE);
                db.execSQL(DatabaseContract.Step.DELETE_TABLE);
                db.execSQL(DatabaseContract.Ingredient.DELETE_TABLE);
                db.execSQL(DatabaseContract.User.DELETE_TABLE);
                db.execSQL(DatabaseContract.Keyword.DELETE_TABLE);
                db.execSQL(DatabaseContract.Ingredient_Recipe.DELETE_TABLE);
                db.execSQL(DatabaseContract.User_Recipe.DELETE_TABLE);
                db.execSQL(DatabaseContract.Keyword_Recipe.DELETE_TABLE);
                break;
        }


        // create DB
        onCreate(db);
    }

    public void refreshDB() {
        SQLiteDatabase db = this.getWritableDatabase();
        onUpgrade(db, 2, 3);
    }

    // -------- RECIPE
    // done
    public boolean addRecipe(String id, String name) {
        if(id.trim().equals("") || id.isEmpty()){
            return false;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseContract.Recipe.RECIPE_ID, id);
        contentValues.put(DatabaseContract.Recipe.RECIPE_NAME, name);

        long result = db.insert(DatabaseContract.Recipe.TABLE_NAME, null, contentValues);

        if (result == -1){
            return false;
        }else{
            return true;
        }
    }

    // done
    public boolean updateRecipe(String id, String name) {
        if(id.trim().equals("") || id.isEmpty()){
            return false;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseContract.Recipe.RECIPE_ID, id);
        contentValues.put(DatabaseContract.Recipe.RECIPE_NAME, name);

        long result = db.update(DatabaseContract.Recipe.TABLE_NAME, contentValues, DatabaseContract.Recipe.RECIPE_ID + "=" + id, null);

        if (result == -1){
            return false;
        }else{
            return true;
        }
    }

    // done
    public boolean deleteRecipe(String id) {
        if(id.trim().equals("") || id.isEmpty()){
            return false;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseContract.Recipe.RECIPE_ID, id);

        long result = db.delete(DatabaseContract.Recipe.TABLE_NAME, DatabaseContract.Recipe.RECIPE_ID + "=" + id, null);

        if (result == -1){
            return false;
        }else{
            return true;
        }
    }


    // -------- INGREDIENT
    // done
    public boolean addIngredient(String ing_name) {
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

    // -------- STEP
    // done
    public boolean addStep(String no, String description, String recipe_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseContract.Step.STEP_NO, no);
        contentValues.put(DatabaseContract.Step.DESCRIPTION, description);
        contentValues.put(DatabaseContract.Step.RECIPE_ID, recipe_id);

        long result = db.insert(DatabaseContract.Step.TABLE_NAME, null, contentValues);

        if (result == -1){
            return false;
        }else{
            return true;
        }
    }

    /*
    public ArrayList<String[]> getStepsFromRecipeID(String id) {
        String selectQuery = "SELECT * FROM " + DatabaseContract.Step.TABLE_NAME +
                " WHERE " + DatabaseContract.Step.RECIPE_ID + " = " + id;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        ArrayList<String[]> result = new ArrayList<String[]>();

        //cursor = SQLiteDatabaseInstance_.rawQuery("SELECT EmployeeName FROM Employee WHERE EmpNo=?", new String[] {empNo + ""});
        // String[] columns = new String[]{DatabaseContract.Recipe.RECIPE_ID};
        // String where =  DatabaseContract.Recipe.RECIPE_ID + " LIKE ?";
        // String where =  DatabaseContract.Recipe.RECIPE_ID + " = ?";
        // searchString = "%" + searchString + "%";
        // String[] whereArgs = new String[]{searchString};
        // cursor = mReadableDB.query(WORD_LIST_TABLE, columns, where, whereArgs, null, null, null);


        try {
            cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            while (cursor.moveToNext()) {
                String[] aRow = new String[2];
                aRow[0] = cursor.getString(cursor.getColumnIndex(DatabaseContract.Step.STEP_NO));
                aRow[1] = cursor.getString(cursor.getColumnIndex(DatabaseContract.Step.DESCRIPTION));
                result.add(aRow);
            }

        }finally {
            cursor.close();
        }

        return result;
    }
     */

    // -------- USER
    //
    public boolean addUser() {
        return false;
    }

    // -------- KEYWORD
    //
    public boolean addKeyword() {
        return false;
    }

    //
    public boolean removeKeyword() { return false; }

    // public boolean getKeywordFromKeywordID() { return false; }

    // -------- INGREDIENT - RECIPE
    //
    public boolean addIngredientToRecipe() {
        return false;
    }

    //
    public boolean removeIngredientFromRecipe() { return false; }

    // public boolean getIngredientsFromRecipeID() { return false; }

    // -------- USER - RECIPE
    //
    public boolean addFavoriteRecipe() {
        return false;
    }

    //
    public boolean removeFavoriteRecipe() {
        return false;
    }

    // -------- KEYWORD - RECIPE
    //
    public boolean addKeywordRecipe() {
        return false;
    }

    //
    public boolean removeKeywordRecipe() { return false; }

    //public boolean getKeywordFromRecipeID() { return false; }

    // -------- GET QUERIES
    //
    public ArrayList<String[]> getCompleteRecipesData(String[] id) {
        ArrayList<String[]> result = new ArrayList<>();

        return result;
    }

    //
    public ArrayList<String[]> getCompleteFavoriteRecipesData(String[] id) {
        ArrayList<String[]> result = new ArrayList<>();

        return result;
    }

    // change to get favorite recipe names and keywords??
    public boolean getFavoriteRecipeNames(){
        return false;
    }

    // do we need separate functions for get ALL recipe and get

    // done - need testing
    // change to get recipe names and keywords??
    public ArrayList<String> getRecipeNamesFromIngredients(String[] ingredients) {
        ArrayList<String> result = new ArrayList<String>();
        //String selectQuery = "SELECT DISTINCT r.recipe_name FROM recipe r INNER JOIN ingredient i ON r.recipe_id = i.recipe_id WHERE i." +
        //        DatabaseContract.Ingredient.NAME + " IN (";

        String selectQuery = "SELECT DISTINCT * FROM recipe";

        /*
        for (int i = 0; i < ingredients.length; i++) {
            if (ingredients[i].trim().equals("") || ingredients[i].isEmpty()) {
                // skip
            }
            else if (i == ingredients.length-1) {
                selectQuery += ingredients[i] + ")";
            }else {
                selectQuery += ingredients[i] + ",";
            }
        }
        */

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;

        //cursor = SQLiteDatabaseInstance_.rawQuery("SELECT EmployeeName FROM Employee WHERE EmpNo=?", new String[] {empNo + ""});
        // String[] columns = new String[]{DatabaseContract.Recipe.RECIPE_ID};
        // String where =  DatabaseContract.Recipe.RECIPE_ID + " LIKE ?";
        // String where =  DatabaseContract.Recipe.RECIPE_ID + " = ?";
        // searchString = "%" + searchString + "%";
        // String[] whereArgs = new String[]{searchString};
        // cursor = mReadableDB.query(WORD_LIST_TABLE, columns, where, whereArgs, null, null, null);

        cursor = db.rawQuery(selectQuery, null);
        if (cursor != null && cursor.moveToFirst()){
            do {
                result.add(cursor.getString(cursor.getColumnIndex(DatabaseContract.Recipe.RECIPE_NAME)));
            } while (cursor.moveToNext());

            cursor.close();
        }

        /*
        try {
            cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            while (cursor.moveToNext()) {
                result.add(cursor.getString(cursor.getColumnIndex(DatabaseContract.Recipe.RECIPE_NAME)));
            }

        }finally {
            cursor.close();
        }
        */

        return result;
    }

}
