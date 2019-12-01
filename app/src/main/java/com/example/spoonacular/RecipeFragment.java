package com.example.spoonacular;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class RecipeFragment extends Fragment implements View.OnClickListener {

    String id;
    MainActivity main;
    DatabaseHelper myDB;
    LinearLayout recipeLayout;
    String recipeInfo;
    boolean favorited;

    InteractionListener mListener;


    public void onAttach(Context context) {
        super.onAttach(context);

        main = (MainActivity) context;

        if (context instanceof InteractionListener) {
            //init the listener
            mListener = (InteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement InteractionListener");
        }
    }


    @Nullable
    @Override
    @TargetApi(16)
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);

        Bundle args = getArguments();

        id = args.getString("id");


        myDB = main.getDBHelper();

        recipeLayout = rootView.findViewById(R.id.recipeLayout);



        String title = getTitle(id);
        String keywords = getKeywords(id);
        String ingredients = getIngredients(id);
        String steps = getSteps(id);

        //favoritesBtn.setBackground(getResources().getDrawable(R.drawable.favorites_on));

        TextView titleTextView = new TextView(getActivity());
        titleTextView.setGravity(Gravity.CENTER_HORIZONTAL);
        titleTextView.setText(title);

        TextView keywordTextView = new TextView(getActivity());
        keywordTextView.setGravity(Gravity.CENTER_HORIZONTAL);
        keywordTextView.setText(keywords);

        TextView ingredientsTextView = new TextView(getActivity());
        ingredientsTextView.setGravity(Gravity.CENTER_HORIZONTAL);
        ingredientsTextView.setText(ingredients);

        TextView stepsTextView = new TextView(getActivity());
        stepsTextView.setGravity(Gravity.CENTER_HORIZONTAL);
        stepsTextView.setText(steps);

        TextView recipeInfo = new TextView(getActivity());
//        recipeInfo.setText(title +  ingredients + steps);

        if(main.getSignedInAccount() != 0) {

            Button favoritesBtn = new Button(getActivity());
            favoritesBtn.setWidth(10);
            favoritesBtn.setHeight(10);
         //   favoritesBtn.set
            favoritesBtn.setMaxWidth(10);
            favoritesBtn.setGravity(Gravity.RIGHT);
            favoritesBtn.setBackground(getResources().getDrawable(R.drawable.favorites_on));
            favoritesBtn.setOnClickListener(this);
            recipeLayout.addView(favoritesBtn);
        }
        recipeLayout.addView(titleTextView);
        recipeLayout.addView(keywordTextView);
        recipeLayout.addView(ingredientsTextView);
        recipeLayout.addView(stepsTextView);

        return rootView;

    }


    public void onClick(View v){

        toggleFavorited();
        if(favorited) {
            myDB.addFavoriteRecipe(String.valueOf(main.getSignedInAccount()), id);
        }
        else{
            myDB.removeFavoriteRecipe(String.valueOf(main.getSignedInAccount()), id);
        }
    }

    private void toggleFavorited(){
        if(favorited){
            favorited = false;
        }
        else{
            favorited = true;
        }

    }



    private String getTitle(String id) {
        String[] recipeResults = myDB.getRecipeByID(id);
        return recipeResults[1] + "\n\n";
    }

    private String getKeywords(String id){
        String[] keywordResults = myDB.getRecipeByID(id);
        return keywordResults[3] + "\n\n";
    }

    private String getIngredients(String id) {
        ArrayList<String[]> ingredientResults = myDB.getIngredientsFromRecipeID(id);
        String ingredientsString = "";

        for(int i = 0; i < ingredientResults.size(); i++) {
            String[] currentIngredient = ingredientResults.get(i);
            ingredientsString += currentIngredient[2] + " ";
            ingredientsString += currentIngredient[1];
            ingredientsString += "\n";

        }
        return ingredientsString;
    }

    private String getSteps(String id) {
        ArrayList<String[]> stepsResults = myDB.getStepsFromRecipeID(id);
        String stepsString = "";

        for(int i = 0; i < stepsResults.size(); i++){
            String[] currentStep = stepsResults.get(i);
            stepsString += currentStep[0] + ". ";
            stepsString += currentStep[1];
            stepsString += "\n\n";
        }

        return stepsString;
    }

    //public String getKeywords(String id){
        //ArrayList<String[]> keywordResults = myDB.get
    //}

    public LinearLayout getRecipeLayout(){
        return recipeLayout;
    }

    public String getRecipeInfo(){
        return recipeInfo;
    }



}



