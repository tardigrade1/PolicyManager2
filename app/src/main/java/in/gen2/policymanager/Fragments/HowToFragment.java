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

public class HowToFragment extends Fragment {


    private View view;
@BindView(R.id.tvIncreaseBusinessStepText)
    TextView increaseSteps;
@BindView(R.id.tvRejectionStepText)
    TextView rejectionSteps;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("How to");
        view = inflater.inflate(R.layout.fragment_how_to, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        increaseSteps.setText(Html.fromHtml("<ul>" +
                "<li>Huge Potential- we are having 30 Cr Pol holders.</li>" +
                "<li>It is a Entry Point and Talking Point.</li>" +
                "<li>Increase Return to Pol Holders by 1.5 to 2% by way of Reward points on Premium payment.</li>" +
                "<li>Our card is giving more facilities as compared to other cards in the Market.</li>" +
                "<li>Field activity with Standee & Canopy at Campus of Societies and Offices will give new customers.</li>" +
                "<li>We recommend for issue of card to each eligible applicant.</li>" +
                "<li>Auto Premium Payment will save your time for New Business.</li>" +
                "<li>Retain our Customers.</li>" +
                "<li>Strengthen LIC Brand.</li>" +
                "</ul>"));
        String scoreRejectColor="<span style=color:#000>Score Reject-</span>";
        String dipRejectColor="<span style=color:#000>DIP Reject-</span>";
        String verfiyFailColor="<span style=color:#000>Verification Failure-</span>";
        String rcuRejectColor="<span style=color:#000>RCU Reject-</span>";
        String otherRejectColor="<span style=color:#000>Other Reasons-</span>";
        rejectionSteps.setText(Html.fromHtml("<ul>" +
                "<li>"+dipRejectColor+"</br>" +

                " Not fulfill basic conditions</br>" +
                " Documents not clear</br>" +
                " Sign not tally with DOC</br>" +
                " Multiple Reasons</br>" +

                "<li>Cibil Score less than 720.</li>" +
                "<li>"+scoreRejectColor+" App Score Reject</li>" +
                "<li>"+verfiyFailColor+" Information mismatch</li>" +
                "<li>"+rcuRejectColor+" Bank’s Risk Dept feel Risk for Credit</li>" +
                "<li>"+otherRejectColor+" Queries not compliance etc.</li>" +
                "</ul>"));
    }
}
