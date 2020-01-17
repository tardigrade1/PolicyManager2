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

public class BenefitsOfLicCardFragment extends Fragment {
    private Unbinder unbinder;
    private View view;
@BindView(R.id.tvBenefitsListText)
    TextView tvBenefitsListText;
    private ActionBar actionBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();

        view = inflater.inflate(R.layout.fragment_benefits_of_lic_card, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view=view;
        unbinder = ButterKnife.bind(this, view);
        init();
    }

    @Override
    public void onResume() {
        super.onResume();
        actionBar.setTitle("Benefits");
    }


    private void init() {
        String LifetimeFreeColor="<span style=color:#D18400>Lifetime Free- </span>";
        String FreeAccidentalColor="<span style=color:#D18400>Free accidental death risk cover</span>";
        String CardMustActiveColor="<span style=color:#D18400>Card must active atleast once in six months</span>";
        String FreeColor="<span style=color:#D18400>Free</span>";
        String InterestFreeColor="<span style=color:#D18400>Interest Free</span>";
        String ConvenienceFreeColor="<span style=color:#D18400>Free Convenience fees</span>";
        String AirportVIPColor="<span style=color:#D18400>Airport VIP Lounge Access</span>";
        String OneRewardColor="<span style=color:#D18400>One reward point</span>";
        String Each100Color="<span style=color:#D18400>each ₹ 100/-</span>";
        String DoubleRewardColor="<span style=color:#D18400>double reward points</span>";
        String DinningDelightColor="<span style=color:#D18400>Dinning delight program</span>";
        String ZerolostcardColor="<span style=color:#D18400>Zero lost card liability</span>";
        tvBenefitsListText.setText(Html.fromHtml("<ul>" +
                "<li>"+LifetimeFreeColor+"No joing, Annual, Renewal or Convenience fees in payment of LIC Prem.</li>" +
                "<li>"+FreeAccidentalColor+" from 3 lacs/5lacs in Platinum /Signature Card("+CardMustActiveColor+")</li>" +
                "<li>"+FreeColor+" Petrol surcharge upto 1%(Min 400/-)</li>" +
                "<li>"+InterestFreeColor+" Credit limit for 20 to 50 days.</li>" +
                "<li>"+ConvenienceFreeColor+" for paying LIC prem giving longer grace period upto 75 days.</li>" +
                "<li>"+AirportVIPColor+" in Signature card at selected airport of 21 Cities of India.</li>" +
                "<li>"+OneRewardColor+" on payment of "+Each100Color+". Value of One point is ₹1.00 in Signature card & ₹0.75 for Platinum card.</li>" +
                "<li>"+DoubleRewardColor+" on LIC Premium and international purchase.</li>" +
                "<li>"+DinningDelightColor+"- 15% Discount ay over 4000 restaurant across India</li>" +
                "<li>"+ZerolostcardColor+"</li>" +
                "</ul>"));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // unbind the view to free some memory
        unbinder.unbind();
    }
}
