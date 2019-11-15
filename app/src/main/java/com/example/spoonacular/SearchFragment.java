package com.example.spoonacular;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.spoonacular.R;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.List;

public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener, View.OnClickListener {


    View rootView;
    SearchView search;
    HorizontalScrollView queriedIngredients;
    LinearLayout queriedIngredientsLayout;
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
        search = rootView.findViewById(R.id.search);
        search.setOnQueryTextListener(this);
        queriedIngredients = rootView.findViewById(R.id.queriedIngredients);
        queriedIngredientsLayout = rootView.findViewById((R.id.queriedIngredientsLayout));
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
        mListener.onFragmentInteraction("hi");
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
}







