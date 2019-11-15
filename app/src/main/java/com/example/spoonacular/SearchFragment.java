package com.example.spoonacular;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.spoonacular.R;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.List;

public class SearchFragment extends Fragment implements View.OnClickListener{


    View rootView;
    Button button;
    TextView txt;

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

        button = (Button) (rootView.findViewById(R.id.button));
        button.setOnClickListener(this);

        System.out.println("new txt created");
        txt = (TextView) (rootView.findViewById(R.id.num));
        txt.setText(txt.getText());
        System.out.println("oncreateview");
        if(savedInstanceState != null){
       //     button = (Button) savedInstanceState.getSerializable("button");
            System.out.println("restoring");
            txt = (TextView) (rootView.findViewById(R.id.num));
            txt.setText(savedInstanceState.getString("txt"));
        }



        return rootView;
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


    public void onClick(View v){
        mListener.onFragmentInteraction("hi");

     //   switch(v.getId()){

        //    case R.id.button:
        //        System.out.println("is txt null22222?");
       //         System.out.println(txt == null);
       //         txt.append("y");


      //  }
    }

    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        System.out.println("savingg");
        //outState.put("button",(Serializable) button);
        outState.putString( "txt", txt.getText().toString());

    }

    public TextView getTextV(){
        return txt;
    }


}



