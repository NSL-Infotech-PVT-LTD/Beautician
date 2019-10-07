package com.wellgel.london.Customer.Fragment;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wellgel.london.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class C_BookStatusFrag extends Fragment {


    private ImageView c_dash_navi;


    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;

    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    public C_BookStatusFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_c__book_status, container, false);
        c_dash_navi = view.findViewById(R.id.c_dash_navi);
        fragmentManager = getActivity().getSupportFragmentManager();


        c_dash_navi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transaction = fragmentManager.beginTransaction().remove(C_BookStatusFrag.this);
                transaction.commit();
            }
        });
        return view;
    }



}
