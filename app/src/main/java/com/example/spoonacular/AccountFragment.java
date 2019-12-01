package com.example.spoonacular;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AccountFragment extends Fragment implements View.OnClickListener {

    MainActivity main;
    Button createAccount;
    Button signInOutBtn;

    EditText userName;
    EditText passWord;



    private InteractionListener mListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_account, container, false);

        createAccount = rootView.findViewById(R.id.createAccountButton);
        signInOutBtn = rootView.findViewById(R.id.signInOutButton);

        userName = rootView.findViewById(R.id.username);
        passWord = rootView.findViewById(R.id.password);

        createAccount.setOnClickListener(this);
        signInOutBtn.setOnClickListener(this);

        return rootView;
    }

    public void onViewStateRestored(Bundle savedInstanceState){
        super.onViewStateRestored(savedInstanceState);
        if(main.getSignedInAccount() == 0){
            main.setViewEffectsSignOut();
        }
        else{
            main.setViewEffectsSignIn();
        }

    }


    public void onAttach(Context context) {
        super.onAttach(context);

        main = (MainActivity) context;

        if (context instanceof InteractionListener) {
            //init the listener
            mListener = (InteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement InteractionListener");
        }
    }


    public void onClick(View v){
        mListener.accountScreenListener(v);

    }

    public EditText getUserName() {
        return userName;
    }

    public EditText getPassWord() {
        return passWord;
    }

    public Button getCreateAccountBtn(){
        return createAccount;
    }

    public Button getSignInOutBtn(){
        return signInOutBtn;
    }

}
