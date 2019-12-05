package com.example.spoonacular;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DatabaseContract.DATABASE_NAME, null, DatabaseContract.DATABASE_VERSION);
        //SQLiteDatabase db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("onCreate()");
        db.execSQL("PRAGMA foreign_keys = ON");

        // create tables in DB
        db.execSQL(DatabaseContract.Recipe.CREATE_TABLE);
        db.execSQL(DatabaseContract.Step.CREATE_TABLE);
        db.execSQL(DatabaseContract.Ingredient.CREATE_TABLE);
        db.execSQL(DatabaseContract.User.CREATE_TABLE);
        //db.execSQL(DatabaseContract.Keyword.CREATE_TABLE);
        db.execSQL(DatabaseContract.Ingredient_Recipe.CREATE_TABLE);
        db.execSQL(DatabaseContract.User_Recipe.CREATE_TABLE);
        //db.execSQL(DatabaseContract.Keyword_Recipe.CREATE_TABLE);
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
            case 3:
                // drop tables
                db.execSQL(DatabaseContract.Recipe.DELETE_TABLE);
                db.execSQL(DatabaseContract.Step.DELETE_TABLE);
                db.execSQL(DatabaseContract.Ingredient.DELETE_TABLE);
                db.execSQL(DatabaseContract.User.DELETE_TABLE);
                //db.execSQL(DatabaseContract.Keyword.DELETE_TABLE);
                db.execSQL("DROP TABLE IF EXISTS keyword");
                db.execSQL(DatabaseContract.Ingredient_Recipe.DELETE_TABLE);
                db.execSQL(DatabaseContract.User_Recipe.DELETE_TABLE);
                db.execSQL("DROP TABLE IF EXISTS keyword_recipe");
                break;

            case 4:
                db.execSQL(DatabaseContract.Recipe.DELETE_TABLE);
                db.execSQL(DatabaseContract.Step.DELETE_TABLE);
                db.execSQL(DatabaseContract.Ingredient.DELETE_TABLE);
                db.execSQL(DatabaseContract.User.DELETE_TABLE);
                db.execSQL(DatabaseContract.Ingredient_Recipe.DELETE_TABLE);
                db.execSQL(DatabaseContract.User_Recipe.DELETE_TABLE);
        }

        // create DB
        onCreate(db);
    }

    public void refreshDB() {
        SQLiteDatabase db = this.getWritableDatabase();
        onUpgrade(db, 4, 4);
    }

    // -------- RECIPE
    // done
    public boolean addRecipe(String id, String name, String cooktime, String keywords) {
        if(id.trim().equals("") || id.isEmpty()){
            return false;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseContract.Recipe.RECIPE_ID, id);
        contentValues.put(DatabaseContract.Recipe.RECIPE_NAME, name);
        contentValues.put(DatabaseContract.Recipe.COOK_TIME, cooktime);
        contentValues.put(DatabaseContract.Recipe.KEYWORD, keywords);

        long result = -1;
        try {
            result = db.insert(DatabaseContract.Recipe.TABLE_NAME, null, contentValues);
        }catch (SQLiteException exception) {
            Log.d("SQLite", "addRecipe - Failed");
        }

        if (result == -1){
            return false;
        }else{
            return true;
        }
    }

    // done
    public boolean updateRecipe(String id, String name, String cooktime, String keywords) {
        if(id.trim().equals("") || id.isEmpty()){
            return false;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseContract.Recipe.RECIPE_ID, id);
        contentValues.put(DatabaseContract.Recipe.RECIPE_NAME, name);
        contentValues.put(DatabaseContract.Recipe.COOK_TIME, cooktime);
        contentValues.put(DatabaseContract.Recipe.KEYWORD, keywords);

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
        //ContentValues contentValues = new ContentValues();

        //contentValues.put(DatabaseContract.Recipe.RECIPE_ID, id);

        long result = db.delete(DatabaseContract.Recipe.TABLE_NAME, DatabaseContract.Recipe.RECIPE_ID + "=" + id, null);

        if (result == -1){
            return false;
        }else{
            return true;
        }
    }

    // -------- INGREDIENT
    // done
    public boolean addIngredientsToRecipe(String recipe_id, ArrayList<String[]> ingredients) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues ing_contentValues = new ContentValues();
        ContentValues ing_rec_contentValues = new ContentValues();

        ing_rec_contentValues.put(DatabaseContract.Ingredient_Recipe.RECIPE_ID, recipe_id);

        String[] ing;
        long result1 = -1;
        long result2 = -1;
        try {
            for (int i = 0; i < ingredients.size(); i++) {
                ing = ingredients.get(i);
                ing_contentValues.put(DatabaseContract.Ingredient.ID, ing[0]);
                ing_contentValues.put(DatabaseContract.Ingredient.NAME, ing[1]);
                // add to ingredient table first
                result1 = db.insert(DatabaseContract.Ingredient.TABLE_NAME, null, ing_contentValues);
                ing_rec_contentValues.put(DatabaseContract.Ingredient_Recipe.INGREDIENT_ID, ing[0]);
                ing_rec_contentValues.put(DatabaseContract.Ingredient_Recipe.QUANTITY, ing[2]);
                // add to ingredient_recipe table
                result2 = db.insert(DatabaseContract.Ingredient_Recipe.TABLE_NAME, null, ing_rec_contentValues);
            }
        }catch (SQLiteException exception) {
            Log.d("SQLite", "addIngredientsToRecipe - Failed");
        }


        if (result1 == -1 && result2 == -1){
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

    // -------- USER
    // done
    public int addUser(String un, String pw) {
        if(un.trim().equals("") || un.isEmpty() || pw.trim().equals("") || pw.isEmpty()){
            return -1;
        }
        un = un.trim();
        pw = pw.trim();

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseContract.User.USERNAME, un);
        contentValues.put(DatabaseContract.User.PASSWORD, pw);

        long result = db.insert(DatabaseContract.User.TABLE_NAME, null, contentValues);

        int result_id = -1;
        if (result != -1){
            String selectQuery = "SELECT user_id FROM user WHERE username = '" + un + "'";
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor != null && cursor.moveToFirst()){
                result_id = cursor.getInt(cursor.getColumnIndex(DatabaseContract.User.ID));
            }

            cursor.close();
        }

        return result_id;
    }

    // done
    public String[] getUser(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        id = id.trim();

        String selectQuery = "SELECT * FROM " + DatabaseContract.User.TABLE_NAME
                + " WHERE " + DatabaseContract.User.USERNAME + " = '" + id + "'";

        Log.d("GET_RECIPE_QUERY_3", selectQuery);

        Cursor cursor = null;
        cursor = db.rawQuery(selectQuery, null);

        String[] result = new String[3];
        if (cursor != null && cursor.moveToFirst()){
            result[0] = cursor.getString(cursor.getColumnIndex(DatabaseContract.User.ID));
            result[1] = cursor.getString(cursor.getColumnIndex(DatabaseContract.User.USERNAME));
            result[2] = cursor.getString(cursor.getColumnIndex(DatabaseContract.User.PASSWORD));

            cursor.close();
        }

        return result;
    }

    // -------- USER - RECIPE
    // done
    public boolean addFavoriteRecipe(String user_id, String recipe_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseContract.User_Recipe.USER_ID, user_id);
        contentValues.put(DatabaseContract.User_Recipe.RECIPE_ID, recipe_id);

        long result = -1;
        try {
            result = db.insert(DatabaseContract.User_Recipe.TABLE_NAME, null, contentValues);
        }catch (SQLiteException exception) {
            Log.d("SQLite", "addIngredientsToRecipe - Failed");
        }

        if (result == -1){
            return false;
        }else{
            return true;
        }
    }

    // done
    public boolean removeFavoriteRecipe(String user_id, String recipe_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        //ContentValues contentValues = new ContentValues();

        String whereClause = DatabaseContract.User_Recipe.RECIPE_ID + " = '" + recipe_id + "'"
                + " AND " + DatabaseContract.User_Recipe.USER_ID + " = '" + user_id + "'";

        long result = db.delete(DatabaseContract.User_Recipe.TABLE_NAME,  whereClause, null);


        if (result == -1){
            return false;
        }else{
            return true;
        }
    }

    public boolean inFavorites(String user_id, String recipe_id) {
        String query = "SELECT DISTINCT "+ DatabaseContract.User_Recipe.RECIPE_ID
                + " FROM " + DatabaseContract.User_Recipe.TABLE_NAME
                + " WHERE " + DatabaseContract.User_Recipe.USER_ID + " = " + user_id
                + " AND " + DatabaseContract.User_Recipe.RECIPE_ID + " = " + recipe_id;

        Log.d("GET_RECIPE_QUERY", query);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        cursor = db.rawQuery(query, null);

        boolean result = false;
        if (cursor != null && cursor.moveToFirst()){
            do {
                result = true;
                break;
            } while (cursor.moveToNext());

            cursor.close();
        }

        return result;
    }
    // -------- GET QUERIES
    // done
    public ArrayList<String[]> getFavoriteRecipesFromIngredients(String user_id, ArrayList<String> ingredients){
        ArrayList<String[]> result = new ArrayList<>();
        if (ingredients.size() == 0) {
            return result;
        }

        String ingQuery = "SELECT DISTINCT " + DatabaseContract.Ingredient.ID +
                " FROM " + DatabaseContract.Ingredient.TABLE_NAME + " WHERE " ;

        for (int i = 0; i < ingredients.size(); i++) {
            if (ingredients.get(i).trim().equals("") || ingredients.get(i).isEmpty()) {
                // skip
            }
            else if (i == ingredients.size()-1) {
                ingQuery += DatabaseContract.Ingredient.NAME + " LIKE '%" + ingredients.get(i) + "%'";
            }else {
                ingQuery += DatabaseContract.Ingredient.NAME + " LIKE '%" + ingredients.get(i) + "%' OR ";
            }
        }

        //Log.d("GET_RECIPE_QUERY_1", ingQuery);

        String recQuery = "SELECT DISTINCT r." + DatabaseContract.Ingredient_Recipe.RECIPE_ID +
                " FROM " + DatabaseContract.Ingredient_Recipe.TABLE_NAME + " r " +
                " INNER JOIN (" + ingQuery +
                ") i ON i." + DatabaseContract.Ingredient.ID + " = r." + DatabaseContract.Ingredient_Recipe.INGREDIENT_ID;

        //Log.d("GET_RECIPE_QUERY_2", recQuery);

        String interQuery = "SELECT DISTINCT r.recipe_id FROM "
                + DatabaseContract.User_Recipe.TABLE_NAME + " r "
                + " INNER JOIN (" + recQuery + ") t ON t.recipe_id = r.recipe_id "
                + " WHERE " + DatabaseContract.User_Recipe.USER_ID + " = " + user_id;

        String selectQuery = "SELECT DISTINCT R.recipe_id, R.recipe_name, R." + DatabaseContract.Recipe.COOK_TIME +
                " FROM " + DatabaseContract.Recipe.TABLE_NAME + " R" +
                " INNER JOIN (" + interQuery + ") T " +
                " ON T.recipe_id = R.recipe_id " +
                " ORDER BY r.recipe_name ASC";

        Log.d("GET_RECIPE_QUERY_3", selectQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        cursor = db.rawQuery(selectQuery, null);

        if (cursor != null && cursor.moveToFirst()){
            do {
                String[] aRow = new String[3];
                aRow[0] = cursor.getString(cursor.getColumnIndex(DatabaseContract.Recipe.RECIPE_ID));
                aRow[1] = cursor.getString(cursor.getColumnIndex(DatabaseContract.Recipe.RECIPE_NAME));
                aRow[2] = cursor.getString(cursor.getColumnIndex(DatabaseContract.Recipe.COOK_TIME));
                result.add(aRow);
            } while (cursor.moveToNext());

            cursor.close();
        }

        return result;
    }

    // done
    public ArrayList<String[]> getRecipesFromIngredients(ArrayList<String> ingredients) {
        ArrayList<String[]> result = new ArrayList<>();
        if (ingredients.size() == 0) {
            return result;
        }

        String ingQuery = "SELECT DISTINCT " + DatabaseContract.Ingredient.ID +
                " FROM " + DatabaseContract.Ingredient.TABLE_NAME + " WHERE " ;

        for (int i = 0; i < ingredients.size(); i++) {
            if (ingredients.get(i).trim().equals("") || ingredients.get(i).isEmpty()) {
                // skip
            }
            else if (i == ingredients.size()-1) {
                ingQuery += DatabaseContract.Ingredient.NAME + " LIKE '%" + ingredients.get(i) + "%'";
            }else {
                ingQuery += DatabaseContract.Ingredient.NAME + " LIKE '%" + ingredients.get(i) + "%' OR ";
            }
        }

        //Log.d("GET_RECIPE_QUERY_1", ingQuery);

        String recQuery = "SELECT DISTINCT r." + DatabaseContract.Ingredient_Recipe.RECIPE_ID +
                " FROM " + DatabaseContract.Ingredient_Recipe.TABLE_NAME + " r " +
                " INNER JOIN (" + ingQuery +
                ") i ON i." + DatabaseContract.Ingredient.ID + " = r." + DatabaseContract.Ingredient_Recipe.INGREDIENT_ID;

        //Log.d("GET_RECIPE_QUERY_2", recQuery);

        String selectQuery = "SELECT DISTINCT r.recipe_id, r.recipe_name, r.cook_time FROM recipe r " +
                " INNER JOIN (" + recQuery + ") t ON t.recipe_id = r.recipe_id ORDER BY r.recipe_name ASC";

        //Log.d("GET_RECIPE_QUERY_3", selectQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        cursor = db.rawQuery(selectQuery, null);

        if (cursor != null && cursor.moveToFirst()){
            do {
                String[] aRow = new String[3];
                aRow[0] = cursor.getString(cursor.getColumnIndex(DatabaseContract.Recipe.RECIPE_ID));
                aRow[1] = cursor.getString(cursor.getColumnIndex(DatabaseContract.Recipe.RECIPE_NAME));
                aRow[2] = cursor.getString(cursor.getColumnIndex(DatabaseContract.Recipe.COOK_TIME));
                result.add(aRow);
            } while (cursor.moveToNext());

            cursor.close();
        }

        return result;
    }

    // done
    public ArrayList<String[]> getIngredientsFromRecipeID(String id) {
        ArrayList<String[]> result = new ArrayList<>();
        String ingQuery = "SELECT DISTINCT " + DatabaseContract.Ingredient_Recipe.INGREDIENT_ID + ", " + DatabaseContract.Ingredient_Recipe.QUANTITY
                + " FROM " + DatabaseContract.Ingredient_Recipe.TABLE_NAME
                + " WHERE " + DatabaseContract.Ingredient_Recipe.RECIPE_ID + " = " + id;

        String selectQuery = "SELECT DISTINCT T.ingredient_id, I.ingredient_name, T.quantity"
                + " FROM " + DatabaseContract.Ingredient.TABLE_NAME + " I"
                + " INNER JOIN (" + ingQuery + ") T"
                + " ON I." + DatabaseContract.Ingredient.ID + " = T." + DatabaseContract.Ingredient_Recipe.INGREDIENT_ID
                + " ORDER BY I.ingredient_name, T.quantity ASC";

        Log.d("GET_RECIPE_QUERY_3", selectQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        cursor = db.rawQuery(selectQuery, null);

        if (cursor != null && cursor.moveToFirst()){
            do {
                String[] aRow = new String[3];
                aRow[0] = cursor.getString(cursor.getColumnIndex(DatabaseContract.Ingredient_Recipe.INGREDIENT_ID));
                aRow[1] = cursor.getString(cursor.getColumnIndex(DatabaseContract.Ingredient.NAME));
                aRow[2] = cursor.getString(cursor.getColumnIndex(DatabaseContract.Ingredient_Recipe.QUANTITY));
                result.add(aRow);
            } while (cursor.moveToNext());

            cursor.close();
        }

        return result;
    }

    // done
    public ArrayList<String[]> getStepsFromRecipeID(String id) {
        String selectQuery = "SELECT * FROM " + DatabaseContract.Step.TABLE_NAME +
                " WHERE " + DatabaseContract.Step.RECIPE_ID + " = " + id
                + " ORDER BY " + DatabaseContract.Step.STEP_NO + " ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        ArrayList<String[]> result = new ArrayList<>();

        cursor = db.rawQuery(selectQuery, null);

        if (cursor != null && cursor.moveToFirst()){
            do {
                String[] aRow = new String[2];
                aRow[0] = cursor.getString(cursor.getColumnIndex(DatabaseContract.Step.STEP_NO));
                aRow[1] = cursor.getString(cursor.getColumnIndex(DatabaseContract.Step.DESCRIPTION));
                result.add(aRow);
            }while (cursor.moveToNext());

            cursor.close();
        }

        return result;
    }

    // done
    public String[] getRecipeByID(String id) {
        String[] result = new String[4];
        String selectQuery = "SELECT DISTINCT *"
                + " FROM " + DatabaseContract.Recipe.TABLE_NAME
                + " WHERE " + DatabaseContract.Recipe.RECIPE_ID + " = " + id;

        Log.d("GET_RECIPE_QUERY_3", selectQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        cursor = db.rawQuery(selectQuery, null);

        if (cursor != null && cursor.moveToFirst()){
            result[0] = cursor.getString(cursor.getColumnIndex(DatabaseContract.Recipe.RECIPE_ID));
            result[1] = cursor.getString(cursor.getColumnIndex(DatabaseContract.Recipe.RECIPE_NAME));
            result[2] = cursor.getString(cursor.getColumnIndex(DatabaseContract.Recipe.COOK_TIME));
            result[3] = cursor.getString(cursor.getColumnIndex(DatabaseContract.Recipe.KEYWORD));

            cursor.close();
        }

        return result;
    }


}
