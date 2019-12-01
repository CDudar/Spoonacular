package com.example.spoonacular;

import android.view.View;

public interface InteractionListener {
    void onFragmentInteraction(String string);
    void onFragmentSubmission(String string);
    void onFavoritesToggle();
    void accountScreenListener(View v);
}


