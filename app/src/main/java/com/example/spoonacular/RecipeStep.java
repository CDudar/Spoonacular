package com.example.spoonacular;

public class RecipeStep {

    String stepNo;
    String description;


    public RecipeStep(String stepNo, String description){
        this.stepNo = stepNo;
        this.description = description;
    }

    public String getStepNo(){
        return stepNo;
    }

    public String getDescription(){
        return description;
    }

}
