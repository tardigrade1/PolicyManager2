package in.gen2.policymanager.Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import in.gen2.policymanager.R;

public class WhyLICCardFragment extends Fragment {

    private Unbinder unbinder;
    private View view;
    @BindView(R.id.tvLicCardPoints)
    TextView licCardPoints;
    private ActionBar actionBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        view = inflater.inflate(R.layout.fragment_why_liccard, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        unbinder = ButterKnife.bind(this, view);
        init();
    }

    private void init() {
        licCardPoints.setText(Html.fromHtml("<ul>" +
                "<li>To participate in campaign towards Cashless & Digital India</li>" +
                "<li>To ensures LIC's presence in every wallet</li>" +
                "<li>Branding of LIC and Retain our customers.</li>" +
                "<li>To increase LIC marketing activities to procure new leads for LIC business.</li>" +
                "</ul>"));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // unbind the view to free some memory
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        actionBar.setTitle("LIC card");
    }
}
