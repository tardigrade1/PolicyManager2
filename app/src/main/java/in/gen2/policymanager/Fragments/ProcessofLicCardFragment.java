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


public class ProcessofLicCardFragment extends Fragment {


    private View view;
    private Unbinder unbinder;
    @BindView(R.id.tvProcessOfCardIssue)
    TextView processOfIssue;
    @BindView(R.id.tvProcessOfCardApproval)
    TextView processOfApproval;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("LIC card process");
        view = inflater.inflate(R.layout.fragment_processof_lic_card, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view=view;
        unbinder = ButterKnife.bind(this,view);
        init();
    }
    private void init(){
        String dipOkColor="<span style=color:#961301>DIP OK</span>";
        String verifyCallNoColor="<span style=color:#961301>022-71372314/15/16/17 or 022-62172314/15/16/17</span>";
        String tollFreeNoColor="<span style=color:#961301>18004190064.</span>";
        processOfIssue.setText(Html.fromHtml("<ul>" +
                "<li>Submit form with documents at CSM office, Bvli W.</li>" +
                "<li>Forms sent to Axis Bank Airoli for Card issue.</li>" +
                "<li>Axis Bank check the Documents and Scanned them if OK (that is "+dipOkColor+") or if Query returned papers.</li>" +
                "<li>Then Underwriting Deptt thoroughly check the Eligibility & Cibil Score etc.</li>" +
                "<li>If OK then they sent to Verification Deptt and a verification call goes to applicant from contact numbers "+verifyCallNoColor+" asking Name, Mob no, Mothers Name, Address, Profession etc, (Some time they also sent for physical verification).</li>" +
                "<li>Then Credit limit Deptt decides the Limit.</li>"+
                "<li>They sent a message to Applicant and SR giving information of Card approval or Declined.</li>"+
                "<li>Then proceed for Approved Card preparation.</li>"+
                "</ul>"));
        processOfApproval.setText(Html.fromHtml("<ul>" +
                "<li>Card sent through Blue Dart Courier and PIN no separately, PIN can b generated at ATM or Toll free.</li>" +
                "<li>Ask Applicant to activate card immediately so that his Accident Risk cover start and Rs 100/- to to you if activated within 30 days.</li>" +
                "<li>The whole process takes about One Months time approximately.</li>" +
                "<li>After issue of Card for any query Toll Free no is "+tollFreeNoColor+"</li>" +
                "</ul>"));

    }
}
