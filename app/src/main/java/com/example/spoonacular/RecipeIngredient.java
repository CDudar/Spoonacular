package com.example.spoonacular;

public class RecipeIngredient {

    String ingredientID;
    String name;
    String amount;
    String units;

    //Derived fields
    String quantity;

    public RecipeIngredient(String ingredientID, String name, String amount, String units){
        this.ingredientID = ingredientID;
        this.name = name;
        this.amount = amount;
        this.units = units;
    }

    public String getIngredientID(){
        return ingredientID;
    }

    public String getName(){
        return name;
    }

    public String getQuantity(){
        return amount + " " + units;
    }


}
