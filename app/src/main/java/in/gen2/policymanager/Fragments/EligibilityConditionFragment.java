package in.gen2.policymanager.Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import in.gen2.policymanager.R;

public class EligibilityConditionFragment extends Fragment {


    @BindView(R.id.tabEligibility)
    TabLayout tabEligible;
    @BindView(R.id.lvPlatinumCard)
    LinearLayout lvPlatinumCard;
    @BindView(R.id.lvSignatureCard)
    LinearLayout lvSignatureCard;
    private Unbinder unbinder;
    private View view;
    private ActionBar actionBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        view = inflater.inflate(R.layout.fragment_eligibility_condition, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view=view;
        unbinder=ButterKnife.bind(this,view);
        init();
    }

    private void init() {
        tabEligible.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition()==0){
                    lvSignatureCard.setVisibility(View.GONE);
                    lvPlatinumCard.setVisibility(View.VISIBLE);
                }
                else{
                    lvPlatinumCard.setVisibility(View.GONE);
                    lvSignatureCard.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        actionBar.setTitle("Eligibility Condition");
    }
}
