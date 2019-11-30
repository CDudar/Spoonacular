package com.example.spoonacular;

public interface InteractionListener {
    void onFragmentInteraction(String string);
    void onFragmentSubmission(String string);
    void onFavoritesToggle();
}


