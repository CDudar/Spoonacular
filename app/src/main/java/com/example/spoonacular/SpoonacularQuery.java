package com.example.spoonacular;

import android.app.Activity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SpoonacularQuery {


    OkHttpClient client = new OkHttpClient();

    Activity mainActivity;

    String key = "db9a577a695640528bb7a67487f8a907";
    String searchRecipesURL;// = "https://api.spoonacular.com/recipes/findByIngredients?ingredients=apples,+flour,+sugar&number=5&apiKey=db9a577a695640528bb7a67487f8a907";
    String specificRecipeURL;

    //String search

    ArrayList<String> ingredients = new ArrayList<>();
    public ArrayList<Recipe> results = new ArrayList<>();


//    int numOfIngredients = 0;

//"https://api.spoonacular.com/recipes/findByIngredients?ingredients=apples,+flour,+sugar&number=5&apiKey=db9a577a695640528bb7a67487f8a907";

    public SpoonacularQuery(Activity main){
        mainActivity = main;
    }


    public void addIngredient(String ingredient){
        ingredients.add(ingredient);
    }


    public String buildSearchRecipesURLString(){
        String url;

        url = "https://api.spoonacular.com/recipes/findByIngredients?ingredients=";

        url = url + ingredients.get(0) + ",";
        if(ingredients.size() > 1) {
            for (int i = 1; i < ingredients.size() - 1; i++) {
                url = url + "+" + ingredients.get(i) + ",";
            }
            url = url + "+" + ingredients.get(ingredients.size() - 1);
        }

        url = url + "&number=10";
        url = url + "&apiKey=db9a577a695640528bb7a67487f8a907";

        return url;
    }


    public void SpoonacularIngredientArrayParser(){

    }


    public synchronized void getRecipeObjects (String url){

        Request request = new Request.Builder()
                .url(url)
                .build();
        System.out.println("before req");

       // final Semaphore mutex = new Semaphore(0);
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        client.newCall(request).enqueue(new Callback() {
           // Handler mainHandler = new Handler(context.getMainLooper());

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                System.out.println("fail");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()){
                    System.out.println("successful");
                    final String myResponse = response.body().string();


                            System.out.print("got here");
                            try {

                                JSONArray jsonArr = new JSONArray(myResponse);

                                for(int i = 0; i < jsonArr.length(); i++){
                                    JSONObject currObj = jsonArr.getJSONObject(i);
                                    Iterator<String> iter = currObj.keys();
                                    while(iter.hasNext()){
                                        String key = iter.next();
                                        System.out.println(key.toString());
                                        try{
                                            if(key.toString().equals("id")){
                                                System.out.println("added id");
                                                results.add(i, new Recipe(currObj.get(key).toString()));
                                                System.out.println("so size is inc? " +  results.size());
                                            }
                                        }
                                        catch(JSONException e){
                                            e.printStackTrace();
                                        }

                                    }

                                }
                                //  JSONObject id = mainObject.getJSONObject("id");
                                // System.out.println(id.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                }
                System.out.println("done");
                countDownLatch.countDown();

            }
        });

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



    }


    public String buildSpecificRecipeURLString(String id){
        String url;

        url = "https://api.spoonacular.com/recipes/";
        url = url + id + "/information?includeNutrition=false&apiKey=db9a577a695640528bb7a67487f8a907";

        return url;
    }

    public synchronized void populateRecipeObject(final int recipeIdx, String url){

        Request request = new Request.Builder()
                .url(url)
                .build();

        final CountDownLatch countDownLatch = new CountDownLatch(1);

        client.newCall(request).enqueue(new Callback() {
            // Handler mainHandler = new Handler(context.getMainLooper());


            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                System.out.println("fail");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()){
                    System.out.println("successful");
                    final String myResponse = response.body().string();

                    System.out.print("got here");
                    JSONObject currObj = null;

                    try {
                        currObj = new JSONObject(myResponse);
                        Iterator<String> iter = currObj.keys();

                        while (iter.hasNext()) {
                            String key = iter.next();
                         //   System.out.println(key.toString());

                            if (key.toString().equals("id")) {
                                System.out.println("added id recipe meaningless");
                                // results.add(i, new Recipe(currObj.get(key).toString()));
                                //  System.out.println("so size is inc? " +  results.size());
                            } else if (key.toString().equals("title")) {
                                results.get(recipeIdx).setTitle(currObj.get(key).toString());
                            }
                              else if(key.toString().equals("readyInMinutes")){
                                results.get(recipeIdx).setCookTime(currObj.get(key).toString());
                            }
                              else if(key.toString().equals("analyzedInstructions")){

                                  JSONArray instrArray = new JSONArray(currObj.get(key).toString());
                                    for(int i = 0; i < instrArray.length(); i++){
                                        JSONObject instrObj = instrArray.getJSONObject(i);
                                        Iterator<String> instrIter = instrObj.keys();
                                        while(instrIter.hasNext()){
                                            String instrKey = instrIter.next();
                                            if(instrKey.equals("steps")){
                                                JSONArray stepsArray = new JSONArray(instrObj.get(instrKey).toString());
                                                System.out.println("at steps");
                                                for(int j = 0; j < stepsArray.length(); j++){
                                                    JSONObject stepsObject = stepsArray.getJSONObject(j);
                                                    Iterator<String> stepsIter  = stepsObject.keys();
                                                    String stepDescription ="";
                                                    String stepNo ="";
                                                    while(stepsIter.hasNext()){
                                                        String stepsKey = stepsIter.next();
                                                        if(stepsKey.toString().equals("step")){
                                                            stepDescription =  stepsObject.get(stepsKey).toString();
                                                        }
                                                        else if(stepsKey.toString().equals("number")){
                                                            stepNo = stepsObject.get(stepsKey).toString();
                                                        }
                                                    }
                                                    results.get(recipeIdx).getRecipeSteps().add(new RecipeStep(stepNo, stepDescription));

                                                }
                                            }
                                        }
                                    }
                              }


                        }

                    }
                     catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                System.out.println("done");
                countDownLatch.countDown();

            }
        });


        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    public void performQuery(){

        if(ingredients.size() == 0)
            return;

        searchRecipesURL = buildSearchRecipesURLString();
        getRecipeObjects(searchRecipesURL);

        for(int i = 0; i < results.size(); i++){
            specificRecipeURL = buildSpecificRecipeURLString(results.get(i).getId());
            System.out.println(specificRecipeURL);
            populateRecipeObject(i, specificRecipeURL);
            System.out.println(results.get(i).getTitle());
            System.out.println(results.get(i).getCookTime());
            results.get(i).stepsPrinter();

        }

    }




}