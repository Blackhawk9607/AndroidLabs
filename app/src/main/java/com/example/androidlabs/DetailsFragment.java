package com.example.androidlabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DetailsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        TextView nameTextView = view.findViewById(R.id.nameTextView);
        TextView heightTextView = view.findViewById(R.id.heightTextView);
        TextView massTextView = view.findViewById(R.id.massTextView);

        if (getArguments() != null) {
            nameTextView.setText(getArguments().getString("name"));
            heightTextView.setText(getArguments().getString("height"));
            massTextView.setText(getArguments().getString("mass"));
        }

        return view;
    }
}
