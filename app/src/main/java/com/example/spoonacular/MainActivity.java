package com.example.spoonacular;

import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.w3c.dom.Text;

import java.io.IOException;
import java.lang.reflect.Array;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;



public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, InteractionListener {

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

        TextView tv = new TextView(getApplicationContext());
        tv.setText(string);
        tv.setPadding(20, 5, 0, 0);
        tv.setGravity(Gravity.CENTER | Gravity.CENTER);

        searchFrag.getQueriedIngredientsLayout().addView(tv);
        q.addIngredient(string);
        q.performQuery();

    }

    @Override
    public void onFragmentInteraction(String string) {
        //System.out.println("User entering " + string.toString());



    }




}
