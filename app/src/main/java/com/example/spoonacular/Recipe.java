package com.example.spoonacular;

import java.util.ArrayList;

public class Recipe {


    String id;
    String title;
    ArrayList<RecipeStep>  recipeSteps = new ArrayList<RecipeStep>();
    ArrayList<RecipeIngredient> recipeIngredients;
    ArrayList<String> keywords;
    String cookTime;


    public Recipe(String id){
        this.id = id;
    }

    public String getId(){
        return id;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public ArrayList<RecipeStep> getRecipeSteps(){
        return recipeSteps;
    }

    public ArrayList<RecipeIngredient> getRecipeIngredients(){
        return recipeIngredients;
    }

    public ArrayList<String> getKeywords(){
        return keywords;
    }

    public String getCookTime(){
        return cookTime + " minutes";
    }

    public void setCookTime(String cookTime){
        this.cookTime = cookTime;
    }

    public void stepsPrinter(){
        if(recipeSteps.size() == 0) {
            System.out.println("no recipe steps");
            return;
        }
        for(int i = 0 ; i < recipeSteps.size(); i++){
            System.out.println(recipeSteps.get(i).getStepNo());
            System.out.println(recipeSteps.get(i).getDescription());
        }
    }

}
