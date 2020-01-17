package in.gen2.policymanager.Fragments;


import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.gen2.policymanager.R;

public class CommsionStructureFragment extends Fragment {

    private ActionBar actionBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        return inflater.inflate(R.layout.fragment_commsion_structure, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        actionBar.setTitle("Commission structure");
    }
}
