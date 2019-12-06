package edu.fsu.cs.mobileproject2;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class SetNames extends Fragment {

    public SetNames(){}

    EditText aName;
    YahtzeeActivity main;
    LinearLayout setNames;
    Button playTheGame;
    String theName;
    goToGame goToGameListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.setnames, container, false);

        main = (YahtzeeActivity) getActivity();
        setNames = (LinearLayout) rootView.findViewById(R.id.theLayout);
        main.playerNames = new ArrayList<>();

        for(int i = 0; i < main.numberOfTotalPlayers; i++){
            setNames.addView(new EditText(getContext()));
            aName = (EditText) setNames.getChildAt(i);
            aName.setHint("Enter name of player " + (i+1));

            if(i == main.numberOfTotalPlayers-1){
                setNames.addView(new Button(getContext()));
                playTheGame = (Button) setNames.getChildAt(main.numberOfTotalPlayers);
                playTheGame.setText("Play the game!");
            }
        }

        playTheGame.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                for (int i = 0; i < main.numberOfTotalPlayers; i++){
                    aName = (EditText) setNames.getChildAt(i);
                    main.playerNames.add(i, aName.getText().toString());
                }
                goToGameListener.GoToGame();
            }
        });


        return rootView;
    }

    public interface goToGame{
        void GoToGame();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SetNames.goToGame){
            goToGameListener = (SetNames.goToGame) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement goToGame");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        goToGameListener = null;
    }
}
