package edu.fsu.cs.mobileproject2;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

import androidx.fragment.app.Fragment;

public class PickNumberOfNames extends Fragment {

    public PickNumberOfNames(){}


    NumberPicker theNumber;
    Button nextPage;
    YahtzeeActivity theActivity;
    goToSetNames goToSetNamesListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.firstpage, container, false);

        theNumber = rootView.findViewById(R.id.numPlayers);
        theNumber.setMinValue(1);
        theNumber.setMaxValue(4);

        nextPage = rootView.findViewById(R.id.nextPage);

        theActivity = (YahtzeeActivity) getActivity();


        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                theActivity.numberOfTotalPlayers = theNumber.getValue();
                goToSetNamesListener.GoToSetNames();
            }
        });

        return rootView;

    }

    public interface goToSetNames{
        void GoToSetNames();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PickNumberOfNames.goToSetNames){
            goToSetNamesListener = (PickNumberOfNames.goToSetNames) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement goToSetNames");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        goToSetNamesListener = null;
    }



}
