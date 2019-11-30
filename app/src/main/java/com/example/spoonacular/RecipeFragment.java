package com.example.spoonacular;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class RecipeFragment extends Fragment {


    MainActivity main;
    DatabaseHelper myDB;

    TextView title;
    TextView ingredients;
    TextView steps;


    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof MainActivity){
            main = (MainActivity)context;
        }

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);

        Bundle args = getArguments();

        String id = args.getString("id");

        myDB = main.getDBHelper();

        title = rootView.findViewById(R.id.recipe_title);
        ingredients = rootView.findViewById(R.id.ingredients);
        steps = rootView.findViewById(R.id.steps);

        populateTitle(id);
        populateIngredients(id);
        populateSteps(id);


        return rootView;

    }

    private void populateTitle(String id) {
        String[] recipeResults = myDB.getRecipeByID(id);

        title.setText(recipeResults[1]);
    }

    private void populateIngredients(String id) {
        ArrayList<String[]> ingredientResults = myDB.getIngredientsFromRecipeID(id);
        String ingredientsString = "";

        for(int i = 0; i < ingredientResults.size(); i++) {
            String[] currentIngredient = ingredientResults.get(i);
            ingredientsString += currentIngredient[2] + " ";
            ingredientsString += currentIngredient[1];
            ingredientsString += "\n";

        }
        ingredients.setText(ingredientsString);
    }

    private void populateSteps(String id) {
        ArrayList<String[]> stepsResults = myDB.getStepsFromRecipeID(id);
        String stepsString = "";

        for(int i = 0; i < stepsResults.size(); i++){
            String[] currentStep = stepsResults.get(i);
            stepsString += currentStep[0] + ". ";
            stepsString += currentStep[1];
            stepsString += "\n";
        }

        steps.setText(stepsString);


    }


}



