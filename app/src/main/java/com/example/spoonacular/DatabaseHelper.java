package com.example.spoonacular;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.lang.reflect.Array;
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
    // done -
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

    // done  - need testing
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

    // done - need testing
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
    // done -
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

    // -------- STEP
    // done -
    public boolean addStep(String no, String description, String recipe_id) {
        Log.d(TAG, "addData: Adding " + no + "(" + recipe_id + ")" + " to " + DatabaseContract.Step.TABLE_NAME);

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
    public boolean addUser(String no, String description, String recipe_id) {
        Log.d(TAG, "addData: Adding " + no + "(" + recipe_id + ")" + " to " + DatabaseContract.Step.TABLE_NAME);

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

    // -------- KEYWORD
    //
    public boolean addKeyword(String no, String description, String recipe_id) {
        Log.d(TAG, "addData: Adding " + no + "(" + recipe_id + ")" + " to " + DatabaseContract.Step.TABLE_NAME);

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

    public boolean removeKeyword() { return false; }

    // public boolean getKeywordFromKeywordID() { return false; }

    // -------- INGREDIENT - RECIPE
    //
    public boolean addIngredientToRecipe(String no, String description, String recipe_id) {
        Log.d(TAG, "addData: Adding " + no + "(" + recipe_id + ")" + " to " + DatabaseContract.Step.TABLE_NAME);

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

    public boolean removeIngredientFromRecipe() { return false; }

    // public boolean getIngredientsFromRecipeID() { return false; }

    // -------- USER - RECIPE
    //
    public boolean addFavoriteRecipe(String no, String description, String recipe_id) {
        Log.d(TAG, "addData: Adding " + no + "(" + recipe_id + ")" + " to " + DatabaseContract.Step.TABLE_NAME);

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

    public boolean removeFavoriteRecipe() {
        return false;
    }

    // -------- KEYWORD - RECIPE
    //
    public boolean addKeywordRecipe(String no, String description, String recipe_id) {
        Log.d(TAG, "addData: Adding " + no + "(" + recipe_id + ")" + " to " + DatabaseContract.Step.TABLE_NAME);

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

    public boolean removeKeywordRecipe() { return false; }

    //public boolean getKeywordFromRecipeID() { return false; }

    // -------- GET QUERIES
    public ArrayList<String[]> getCompleteRecipesData(String[] id) {
        ArrayList<String[]> result = new ArrayList<>();

        return result;
    }

    public ArrayList<String[]> getCompleteFavoriteRecipesData(String[] id) {
        ArrayList<String[]> result = new ArrayList<>();

        return result;
    }

    // change to get favorite recipe names and keywords??
    public boolean getFavoriteRecipeNames(){
        return false;
    }

    // done - need testing
    // change to get recipe names and keywords??
    public ArrayList<String> getRecipeNames(String[] ids) {
        ArrayList<String> result = new ArrayList<String>();
        String selectQuery = "SELECT * FROM " + DatabaseContract.Recipe.TABLE_NAME + " WHERE " + DatabaseContract.Recipe.RECIPE_ID
                + " IN (";

        for (int i = 0; i < ids.length; i++) {
            if (ids[i].trim().equals("") || ids[i].isEmpty()) {
                return result;
            }

            if (i == ids.length-1) {
                selectQuery += ids[i] + ")";
            }else {
                selectQuery += ids[i] + ",";
            }
        }

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;

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
                result.add(cursor.getString(cursor.getColumnIndex(DatabaseContract.Recipe.RECIPE_NAME)));
            }

        }finally {
            cursor.close();
        }

        return result;
    }

}
