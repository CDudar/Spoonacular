package com.example.spoonacular;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, InteractionListener {
    DatabaseHelper myDb;


    int currentSelectedItemId;
    private SparseArray<Fragment.SavedState> savedStates = new SparseArray<Fragment.SavedState>();

    SearchFragment searchFrag;
    AccountFragment accountFrag;
    SpoonacularQuery q = new SpoonacularQuery(this);
    //Fragment favoritesFrag;



    private TextView mTextViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // create a DB
      //  myDb = new DatabaseHelper(this);

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

    }

    public void AddData(String newEntry) {
        boolean insertData = myDb.addData(newEntry);

        if(insertData){
            toastMessage("Data successfully inserted");
        }else{
            toastMessage("Data failed to be inserted");
        }
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
