package com.example.spoonacular;

import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.textclassifier.TextLinks;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import com.google.android.material.bottomnavigation.BottomNavigationView;


import java.util.ArrayList;



public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, InteractionListener {

    int currentSelectedItemId;
    private SparseArray<Fragment.SavedState> savedStates = new SparseArray<Fragment.SavedState>();

    // database variables
    DatabaseHelper myDb;
    EditText edit_ing_name, edit_step_no, edit_step_desc, edit_step_recipe, edit_recipe_name, edit_recipe_id;
    Button btnAddIng, btnRestartDB, btnAddRecipe, btnAddStep, btnDeleteRecipe, btnUpdateRecipe, btnGetRecipe;




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

        // link up input text boxes
        // edit_ing_name = (EditText)findViewById(R.id.edit_ing_name);
        edit_step_no = (EditText)findViewById(R.id.edit_step_no);
        edit_step_desc = (EditText)findViewById(R.id.edit_step_description);
        edit_step_recipe = (EditText)findViewById(R.id.edit_step_recipe_id);
        edit_recipe_name = (EditText)findViewById(R.id.edit_recipe_name);
        edit_recipe_id = (EditText)findViewById(R.id.edit_recipe_id);

        // link up buttons
        // btnAddIng = (Button)findViewById(R.id.button_add);
        btnAddRecipe = (Button)findViewById(R.id.button_add_recipe);
        btnAddStep = (Button)findViewById(R.id.button_add_step);
        btnRestartDB = (Button)findViewById(R.id.button_restart_db);
        btnDeleteRecipe = (Button)findViewById(R.id.button_delete_recipe);
        btnUpdateRecipe = (Button)findViewById(R.id.button_update_recipe);
        btnGetRecipe = (Button)findViewById(R.id.button_get_recipe);

        // event listeners
        //AddIngredient();
        AddRecipe();
        AddStep();
        restartDB();
        DeleteRecipe();
        UpdateRecipe();
        GetRecipes();

    }

    public void DeleteRecipe() {
        btnDeleteRecipe.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isDeleted = myDb.deleteRecipe(edit_recipe_id.getText().toString());

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
                        boolean isUpdated = myDb.updateRecipe(edit_recipe_id.getText().toString(), edit_recipe_name.getText().toString());

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
                        String[] ingredients = new String[2];
                        ingredients[0] = "egg";
                        ingredients[1] = "pasta";
                        ArrayList<String> result = myDb.getRecipeNamesFromIngredients(ingredients);

                        for (int i = 0; i < result.size(); i++) {
                            Log.d("Get_Recipe_Data", result.get(i), null);
                        }
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
                        boolean isInserted = myDb.addIngredient(edit_ing_name.getText().toString());

                        if(isInserted == true)
                            toastMessage("Ingre. inserted");
                        else
                            toastMessage("Ingre. NOT inserted");
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
                        boolean isInserted = myDb.addRecipe(edit_recipe_id.getText().toString(),
                                                            edit_recipe_name.getText().toString());

                        if(isInserted == true)
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
                        boolean isInserted = myDb.addStep(edit_step_no.getText().toString(),
                                                          edit_step_desc.getText().toString(),
                                                          edit_step_recipe.getText().toString());

                        if(isInserted == true)
                            toastMessage("Step inserted");
                        else
                            toastMessage("Step NOT inserted");
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
        System.out.println("user submitted " + string.toString());

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
        System.out.println("User entering " + string.toString());



    }




}
