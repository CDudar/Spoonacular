package com.example.spoonacular;

import android.provider.BaseColumns;

public final class DatabaseContract {
    public static final  int    DATABASE_VERSION   = 4;
    public static final  String DATABASE_NAME      = "spoonacular.db";

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private DatabaseContract() {}

    /* Inner class that defines the table contents */
    public static class Recipe implements BaseColumns {
        public static final String TABLE_NAME = "recipe";
        public static final String RECIPE_ID = "recipe_id";
        public static final String RECIPE_NAME = "recipe_name";
        public static final String COOK_TIME = "cook_time";
        public static final String KEYWORD = "keywords";

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
                RECIPE_ID + " INTEGER PRIMARY KEY NOT NULL, " +
                RECIPE_NAME + " TEXT, " +
                COOK_TIME + " TEXT, " +
                KEYWORD + " TEXT)";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class Step implements BaseColumns {
        public static final String TABLE_NAME = "step";
        public static final String STEP_NO = "step_no";
        public static final String DESCRIPTION = "description";
        public static final String RECIPE_ID = "recipe_id";

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
                STEP_NO + " INTEGER NOT NULL, " +
                DESCRIPTION + " TEXT, " +
                RECIPE_ID + " INTEGER NOT NULL, " +
                "FOREIGN KEY (" + RECIPE_ID + ") REFERENCES " + Recipe.TABLE_NAME + "(" + Recipe.RECIPE_ID + ") " +
                    "ON UPDATE CASCADE " +
                    "ON DELETE CASCADE, " +
                "PRIMARY KEY (" + RECIPE_ID + ", " + STEP_NO + "))";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class Ingredient implements BaseColumns {
        public static final String TABLE_NAME = "ingredient";
        public static final String ID = "ingredient_id";
        public static final String NAME = "ingredient_name";

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
                ID + " INTEGER PRIMARY KEY NOT NULL, " +
                NAME + " TEXT NOT NULL)";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class Ingredient_Recipe implements BaseColumns {
        public static final String TABLE_NAME = "ingredient_recipe";
        public static final String INGREDIENT_ID = "ingredient_id";
        public static final String RECIPE_ID = "recipe_id";
        public static final String QUANTITY = "quantity";

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
                INGREDIENT_ID + " INTEGER NOT NULL, " +
                RECIPE_ID + " INTEGER NOT NULL, " +
                QUANTITY + " TEXT, " +
                "FOREIGN KEY (" + RECIPE_ID + ") REFERENCES " + Recipe.TABLE_NAME + "(" + Recipe.RECIPE_ID + ") " +
                    "ON UPDATE CASCADE " +
                    "ON DELETE CASCADE, " +
                "FOREIGN KEY (" + INGREDIENT_ID + ") REFERENCES " + Ingredient.TABLE_NAME + "(" + Ingredient.ID + ") " +
                    "ON UPDATE CASCADE " +
                    "ON DELETE CASCADE, " +
                "PRIMARY KEY (" + INGREDIENT_ID + ", " + RECIPE_ID + "))";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class User implements BaseColumns {
        public static final String TABLE_NAME = "user";
        public static final String ID = "user_id";
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USERNAME + " TEXT NOT NULL UNIQUE, " +
                PASSWORD + " TEXT NOT NULL)" ;

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class User_Recipe implements BaseColumns {
        public static final String TABLE_NAME = "user_recipe";
        public static final String USER_ID = "user_id";
        public static final String RECIPE_ID = "recipe_id";

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
                USER_ID + " INTEGER NOT NULL, " +
                RECIPE_ID + " INTEGER NOT NULL, " +
                "FOREIGN KEY (" + RECIPE_ID + ") REFERENCES " + Recipe.TABLE_NAME + "(" + Recipe.RECIPE_ID + ") " +
                    "ON UPDATE CASCADE " +
                    "ON DELETE CASCADE, " +
                "FOREIGN KEY (" + USER_ID + ") REFERENCES " + User.TABLE_NAME + "(" + User.ID + ") " +
                    "ON UPDATE CASCADE " +
                    "ON DELETE CASCADE, " +
                "PRIMARY KEY (" + USER_ID + ", " + RECIPE_ID + "))";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

}
