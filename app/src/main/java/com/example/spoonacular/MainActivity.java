package com.example.spoonacular;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.textclassifier.TextLinks;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spoonacular.ui.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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
import java.util.Iterator;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, InteractionListener {

    int currentSelectedItemId;
    private SparseArray<Fragment.SavedState> savedStates = new SparseArray<Fragment.SavedState>();

    // database variables
    DatabaseHelper myDb;
    EditText edit_ing_name, edit_step_no, edit_step_desc, edit_step_recipe, edit_recipe_name, edit_recipe_id;
    Button btnAddIng, btnRestartDB, btnAddRecipe, btnAddStep;




    SearchFragment searchFrag;
    AccountFragment accountFrag;
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
        edit_ing_name = (EditText)findViewById(R.id.edit_ing_name);
        edit_step_no = (EditText)findViewById(R.id.edit_step_no);
        edit_step_desc = (EditText)findViewById(R.id.edit_step_description);
        edit_step_recipe = (EditText)findViewById(R.id.edit_step_recipe_id);
        edit_recipe_name = (EditText)findViewById(R.id.edit_recipe_name);
        edit_recipe_id = (EditText)findViewById(R.id.edit_recipe_id);

        // link up buttons
        btnAddIng = (Button)findViewById(R.id.button_add);
        btnAddRecipe = (Button)findViewById(R.id.button_add_recipe);
        btnAddStep = (Button)findViewById(R.id.button_add_step);
        btnRestartDB = (Button)findViewById(R.id.button_restart_db);

        // event listeners
        AddIngredient();
        AddRecipe();
        AddStep();
        restartDB();

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

    }

    @Override
    public void onFragmentInteraction(String string) {
        System.out.println("User entering " + string.toString());


    }




}
