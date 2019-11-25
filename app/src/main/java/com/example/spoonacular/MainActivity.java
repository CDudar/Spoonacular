package com.example.spoonacular;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;



public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, InteractionListener {

    enum Mode{
        DEV,
        USER
    }

    public Mode mode = Mode.USER;

    int currentSelectedItemId;

    private SparseArray<Fragment.SavedState> savedStates = new SparseArray<Fragment.SavedState>();

    // database variables
    DatabaseHelper myDb;
    Button btnAddIng, btnRestartDB, btnAddRecipe, btnAddStep, btnDeleteRecipe, btnUpdateRecipe, btnGetRecipe, btnAddUser, btnGetIngs, btnGetSteps, btnAddFavorite, btnRemoveFavorite, btnGetUser, btnGetFavorite;

    SearchFragment searchFrag;
    AccountFragment accountFrag;
    SpoonacularQuery q = new SpoonacularQuery(this);
    //Fragment favoritesFrag;

    private TextView mTextViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        System.out.println("running again");

        super.onCreate(savedInstanceState);

        if(savedInstanceState != null){
            searchFrag = (SearchFragment) getSupportFragmentManager().getFragment(savedInstanceState, "search");
        }

        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(this);

        navView.setSelectedItemId(R.id.navigation_search);
        swapFragments(R.id.navigation_search, "search");

        // create a DB
        myDb = new DatabaseHelper(this);
    }

    public void DeleteRecipe() {
        btnDeleteRecipe.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //boolean isDeleted = myDb.deleteRecipe(edit_recipe_id.getText().toString());
                        boolean isDeleted = false;
                        if(isDeleted == true)
                            toastMessage("Deleted.");
                        else
                            toastMessage("Not deleted");
                    }
                }
        );
    }

    public void UpdateRecipe() {
        btnUpdateRecipe.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //boolean isUpdated = myDb.updateRecipe(edit_recipe_id.getText().toString(), edit_recipe_name.getText().toString());
                        boolean isUpdated = false;
                        if(isUpdated == true)
                            toastMessage("Updated.");
                        else
                            toastMessage("Not Updated");
                    }
                }
        );
    }

    public void GetRecipes() {
        btnGetRecipe.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<String> ingredients = new ArrayList<>();
                        ingredients.add("egg");
                        ingredients.add("pasta");

                        ArrayList<String[]> result = myDb.getRecipesFromIngredients(ingredients);

                        for (int i = 0; i < result.size(); i++) {
                            String[] row = result.get(i);
                            String srow = "(" + row[0] + ", " + row[1] + ", " + row[2] + ")";
                            Log.d("GET_RECIPE_DATA", srow, null);
                        }

                        String[] result2 = myDb.getRecipeByID("10");
                        String srow = "(" + result2[0] + ", " + result2[1] + ", " + result2[2] + ", " + result2[3] + ")";
                        Log.d("GET_RECIPE_DATA", srow, null);

                        String[] result3 = myDb.getRecipeByID("1000");
                        String srow2 = "(" + result3[0] + ", " + result3[1] + ", " + result3[2]+ ", " + result2[3] + ")";
                        Log.d("GET_RECIPE_DATA", srow2, null);

                    }
                }
        );
    }

    // Listener for add ingredients button
    public void AddIngredient() {
        btnAddIng.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<String[]> ing = new ArrayList<>();
                        String[] ing1 = new String[3];
                        String[] ing2 = new String[3];
                        String[] ing3 = new String[3];
                        ing1[0] = "3";
                        ing1[1] = "eggs";
                        ing1[2] = "6";
                        ing2[0] = "1";
                        ing2[1] = "tea leaves";
                        ing2[2] = "2 tbs";
                        ing3[0] = "10";
                        ing3[1] = "cinnamon stick";
                        ing3[2] = "1 piece";
                        ing.add(ing1);
                        ing.add(ing2);
                        ing.add(ing3);
                        boolean isInserted1 = myDb.addIngredientsToRecipe("3", ing);

                        ing.clear();
                        ing1[0] = "1013";
                        ing1[1] = "egg";
                        ing1[2] = "1";
                        ing2[0] = "21";
                        ing2[1] = "pesto";
                        ing2[2] = "485 mL";
                        ing3[0] = "11123";
                        ing3[1] = "pasta";
                        ing3[2] = "50 g";
                        ing.add(ing1);
                        ing.add(ing2);
                        ing.add(ing3);
                        boolean isInserted2 = myDb.addIngredientsToRecipe("12", ing);

                        ing.clear();
                        ing2[0] = "21";
                        ing2[1] = "pesto";
                        ing2[2] = "455 mL";
                        ing3[0] = "11123";
                        ing3[1] = "penne pasta";
                        ing3[2] = "100 g";
                        ing.add(ing2);
                        ing.add(ing3);
                        boolean isInserted3 = myDb.addIngredientsToRecipe("13", ing);

                        if(isInserted1 || isInserted2 || isInserted3)
                            toastMessage("Ingre. inserted");
                        else
                            toastMessage("Ingre. NOT inserted");
                    }
                }
        );
    }

    public void GetIngredients() {
        btnGetIngs.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<String[]> result = myDb.getIngredientsFromRecipeID("3");

                        for (int i = 0; i < result.size(); i++) {
                            String[] row = result.get(i);
                            String srow = "(" + row[0] + ", " + row[1] + ", " + row[2] + ")";
                            Log.d("GET_ING_DATA", srow, null);
                        }
                    }
                }
        );
    }

    public void GetSteps() {
        btnGetSteps.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<String[]> result = myDb.getStepsFromRecipeID("3");

                        for (int i = 0; i < result.size(); i++) {
                            String[] row = result.get(i);
                            String srow = "(" + row[0] + ", " + row[1] + ")";
                            Log.d("GET_STEPS_DATA", srow, null);
                        }
                    }
                }
        );
    }


    // Listener for add Recipe button
    public void AddRecipe() {
        btnAddRecipe.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isInserted1 = myDb.addRecipe("3", "tea eggs", "20 minutes", "breakfast, asian");
                        boolean isInserted2 = myDb.addRecipe("10", "seafood linguini", "45 minutes", "pasta, dinner, seafood");
                        boolean isInserted3 = myDb.addRecipe("11", "chicken noodle soup", "120 minutes", "soup");
                        boolean isInserted4 = myDb.addRecipe("7", "tomatoe soup", "50 minutes", "soup");
                        boolean isInserted5 = myDb.addRecipe("12", "pesto linguini", "50 minutes", "pasta");
                        boolean isInserted6 = myDb.addRecipe("13", "pesto penne", "50 minutes", "pasta");

                        if(isInserted1 || isInserted2 || isInserted3 || isInserted4 || isInserted5 || isInserted6)
                            toastMessage("Recipe inserted");
                        else
                            toastMessage("Recipe NOT inserted");
                    }
                }
        );
    }

    // Listener for add Step button
    public void AddStep() {
        btnAddStep.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDb.addStep("1", "Add water and eggs to pot.", "3");
                        myDb.addStep("2", "Boil for 15 minutes.", "3");
                        myDb.addStep("3", "Turn off heat, crack egg shells.", "3");
                        myDb.addStep("4", "Add tea leaves, cinnamon stick, and salt (to taste) into pot.", "3");
                        myDb.addStep("1", "Add pesto sauce into pan.", "12");
                        myDb.addStep("3", "Heat pan to a simmer.", "12");
                        myDb.addStep("2", "Cook pasta.", "12");

                        //boolean isInserted = myDb.addStep(edit_step_no.getText().toString()

                        /*
                        if(isInserted == true)
                            toastMessage("Step inserted");
                        else
                            toastMessage("Step NOT inserted");

                         */
                    }
                }
        );
    }

    // Listener for restart / upgrading DB
    public void restartDB() {
        btnRestartDB.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDb.refreshDB();
                    }
                }
        );
    }

    public void AddUser(){
        btnAddUser.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int result_id = myDb.addUser("panda123", "secret@123");

                        toastMessage(Integer.toString(result_id));
                    }
                }
        );
    }

    public void GetUser(){
        btnGetUser.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String[] result = myDb.getUser("panda123");
                        toastMessage("Welcome user #" + result[0] + " : " + result[1] + ", PW: " + result[2]);
                    }
                }
        );
    }


    public void AddToFavourite(){
        btnAddFavorite.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //boolean isInserted1 = myDb.addFavoriteRecipe("panda123", "secret@123");
                        myDb.addFavoriteRecipe("1", "3");
                        myDb.addFavoriteRecipe("1", "7");
                    }
                }
        );
    }

    public void RemoveFavourite(){
        btnRemoveFavorite.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isInserted1 = myDb.removeFavoriteRecipe("1", "7");
                        boolean isInserted2 = myDb.removeFavoriteRecipe("1", "1000");

                        if(isInserted1)
                            Log.d("TESTING", "Removed recipe 7.");
                        if(isInserted2)
                            Log.d("TESTING", "Removed recipe 1000.");
                    }
                }
        );
    }

    public void GetFavouriteRecipes(){
        btnGetFavorite.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<String> ingredients = new ArrayList<>();
                        ingredients.add("egg");
                        ingredients.add("pasta");
                        ArrayList<String[]> result = myDb.getFavoriteRecipesFromIngredients("1", ingredients);

                        for (int i = 0; i < result.size(); i++) {
                            String[] row = result.get(i);
                            String srow = "(" + row[0] + ", " + row[1] + ", " + row[2] + ")";
                            Log.d("GET_F_RECIPE_DATA", srow, null);
                        }
                    }
                }
        );
    }


    private void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        Fragment fragment = null;


        switch(menuItem.getItemId()){
            case R.id.navigation_account:
                swapFragments(menuItem.getItemId(), "account");
                break;

            case R.id.navigation_search:
                swapFragments(menuItem.getItemId(), "search");
                break;

            case R.id.navigation_favorites:
                swapFragments(menuItem.getItemId(), "favorites");
                break;
        }

        return true;
    }


    public void swapFragments(int itemId, String tag){
        if(getSupportFragmentManager().findFragmentByTag("tag") == null){
            saveCurrentFragmentState(itemId);
            createFragment(tag, itemId);
        }
    }


    public void createFragment(String tag, int itemId){
        Fragment fragment =  null;
        if(tag == "account"){
            if(accountFrag == null)
                accountFrag = new AccountFragment();
            fragment = accountFrag;
        }
        else if(tag == "search"){
            if(searchFrag == null)
                searchFrag = new SearchFragment();
            fragment = searchFrag ;
        }
        else if(tag == "favorites"){
            fragment = new FavoritesFragment();
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment, tag)
                .commit();
    }

    public void saveCurrentFragmentState(int itemId){

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if(currentFragment != null){
            System.out.println(currentFragment.getClass().getSimpleName());
            System.out.println(currentFragment.hashCode());
           getSupportFragmentManager().saveFragmentInstanceState(currentFragment);

        }
        currentSelectedItemId = itemId;

    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        System.out.println("saving in activity");
        getSupportFragmentManager().putFragment(outState, "search", searchFrag);
    }



    public void onFragmentSubmission(String string){
        //if entered string in ingredients
        //System.out.println("user submitted " + string.toString());

        q.addIngredient(string);

        if(mode == Mode.DEV){
            q.performQuery();
            addRecipeListToDB(q.getResults());
        }

        else if(mode == Mode.USER){
         //   ArrayList<String[]> queryResults =

            TextView hv = new TextView(getApplicationContext());
            hv.setText(string);
            hv.setPadding(20, 5, 0, 0);
            hv.setGravity(Gravity.CENTER | Gravity.CENTER);
            hv.setOnClickListener(horizontalScrollHandler);
            searchFrag.getQueriedIngredientsLayout().addView(hv);

            ArrayList<String[]> queryResults = myDb.getRecipesFromIngredients(q.getIngredients());

            System.out.println(queryResults.size() + " ================");

            addRecipeViews(queryResults);

        }

    }

    public void addRecipeViews(ArrayList<String[]> queryResults){

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        TextView test = new TextView(getApplicationContext());
        test.setText("test");
        searchFrag.getRecipeResultsLayout().addView(test);
        for(int i = 0 ; i < queryResults.size(); i++){

            View recipeResult = inflater.inflate(R.layout.recipe_search_result, null);


            TextView title = recipeResult.findViewById(R.id.title);
            title.setText("recipe title");
            TextView cookTime = recipeResult.findViewById(R.id.cooktime);
            cookTime.setText("recipe cook time");

            searchFrag.getRecipeResultsLayout().addView(recipeResult);

        }


    }


View.OnClickListener horizontalScrollHandler = new View.OnClickListener(){
        public void onClick(View v){

            q.removeIngredient(((TextView) v).getText().toString());
            searchFrag.getQueriedIngredientsLayout().removeView(v);

        }
    };


    public void addRecipeListToDB(ArrayList<Recipe> results){


        for(int i = 0 ; i < results.size(); i++){
               addRecipeToDB(results.get(i));
        }
    }


    public void addRecipeToDB(Recipe recipe){

        ArrayList<String> keywords = recipe.getKeywords();
        String keywordString = "";
        if(keywords.size() > 0) {

            for (int i = 0; i < keywords.size() - 1; i++) {
                keywordString += keywords.get(i);
                keywordString += ", ";
            }
            keywordString += keywords.get(keywords.size() - 1);
        }

        myDb.addRecipe(recipe.id, recipe.title, recipe.cookTime, keywordString);

        addStepsToDB(recipe);
        addIngredientsToDB(recipe);

    }


    public void addIngredientsToDB(Recipe recipe){


        ArrayList<RecipeIngredient> ingredients = recipe.getRecipeIngredients();
        ArrayList<String[]> ingredientsForDB = new ArrayList<>();
        for(int i = 0 ; i < recipe.getRecipeIngredients().size(); i++){
            RecipeIngredient currentIngredient = ingredients.get(i);
            String[] ing_array = new String[3];
            ing_array[0] = currentIngredient.getIngredientID();
            ing_array[1] = currentIngredient.getName();
            ing_array[2] = currentIngredient.getQuantity();
            ingredientsForDB.add(ing_array);

        }

        myDb.addIngredientsToRecipe(recipe.getId(), ingredientsForDB);

    }

    public void addStepsToDB(Recipe recipe){

        ArrayList<RecipeStep> recipeSteps = recipe.getRecipeSteps();
        for(int i = 0; i < recipe.getRecipeSteps().size(); i++){
            myDb.addStep(recipeSteps.get(i).getStepNo(), recipeSteps.get(i).getDescription(), recipe.getId());
        }

    }


    @Override
    public void onFragmentInteraction(String string) {
        //System.out.println("User entering " + string.toString());



    }




}
