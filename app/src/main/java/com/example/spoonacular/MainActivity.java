package com.example.spoonacular;

import android.os.Bundle;
import android.view.textclassifier.TextLinks;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.Iterator;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper myDb;

    private TextView mTextViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // create a DB
        myDb = new DatabaseHelper(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);


        System.out.println("about to call");

        mTextViewResult = findViewById(R.id.text_view_result);

        OkHttpClient client = new OkHttpClient();

        //String url = "https://api.spoonacular.com/recipes/findByIngredients?ingredients=apples,+flour,+sugar&number=2";
          String url = "https://api.spoonacular.com/recipes/findByIngredients?ingredients=apples,+flour,+sugar&number=2&apiKey=db9a577a695640528bb7a67487f8a907";

      //  String url = "https://www.spoonacular.com";
      //  RequestBody formBody = new FormBody.Builder()
       //         .add("apiKey", "db9a577a695640528bb7a67487f8a907")
       //         .build();


        Request request = new Request.Builder()
                .url(url)
                .build();

        System.out.println(request.toString());

        System.out.println("before req");
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                System.out.println("failxx");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()){
                    final String myResponse = response.body().string();

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.print("got here");
                            try {

                                JSONArray jsonArr = new JSONArray(myResponse);

                                for(int i = 0; i < jsonArr.length(); i++){
                                    JSONObject currObj = jsonArr.getJSONObject(i);
                                    Iterator<String> iter = currObj.keys();
                                    while(iter.hasNext()){
                                        String key = iter.next();
                                        try{
                                            Object value = currObj.get(key);
                                            String currPair = "key: " + key.toString() + "value: " + value.toString();
                                            System.out.println(currPair);
                                            mTextViewResult.setText(currPair);
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
                    });
                }
            }
        });


    }

}
