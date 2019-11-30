package com.example.spoonacular;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener, View.OnClickListener {


    View rootView;
    SearchView search;
    HorizontalScrollView queriedIngredients;
    Button btnFavorites;

    LinearLayout queriedIngredientsLayout;
    LinearLayout recipeResultsLayout;

    private InteractionListener mListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_search, container, false);


        System.out.println("Created search Fragment");
        btnFavorites = rootView.findViewById(R.id.button_favorites);
        btnFavorites.setOnClickListener(this);

        search = rootView.findViewById(R.id.search);
        search.setOnQueryTextListener(this);
        queriedIngredients = rootView.findViewById(R.id.queriedIngredients);
        queriedIngredientsLayout = rootView.findViewById((R.id.queriedIngredientsLayout));
        recipeResultsLayout = rootView.findViewById(R.id.recipeResultsLayout);


        return rootView;
    }

    public boolean onQueryTextSubmit(String newText) {
        mListener.onFragmentSubmission(newText);
        return false;
    }

    public boolean onQueryTextChange(String newText) {
        mListener.onFragmentInteraction(newText);
        return false;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof InteractionListener) {
            //init the listener
            mListener = (InteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement InteractionListener");
        }
    }


    public void onClick(View v) {
        mListener.onFavoritesToggle();
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        System.out.println("savingg");
    }

    public View getQueriedIngredients() {
        return queriedIngredients;
    }

    public LinearLayout getQueriedIngredientsLayout(){
        return queriedIngredientsLayout;
    }

    public LinearLayout getRecipeResultsLayout(){
        return recipeResultsLayout;
    }

    public SearchView getSearch(){
        return search;
    }

    public Button getBtnFavorites(){
        return btnFavorites;
    }
}







