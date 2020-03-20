package in.gen2.policymanager.Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import in.gen2.policymanager.R;
import in.gen2.policymanager.models.EmpData;

public class EligibilityConditionFragment extends Fragment {

    private Unbinder unbinder;
    @BindView(R.id.tabEligibility)
    TabLayout tabEligible;
    @BindView(R.id.lvPlatinumCard)
    LinearLayout lvPlatinumCard;
    @BindView(R.id.lvSignatureCard)
    LinearLayout lvSignatureCard;
    @BindView(R.id.tvPlatinumLicPremium)
    TextView tvPlatinumLicPremium;
    @BindView(R.id.tvPlatinumLicPremiumSingle)
    TextView tvPlatinumLicPremiumSingle;
    @BindView(R.id.tvPlatinumLicSalaried)
    TextView tvPlatinumLicSalaried;
    @BindView(R.id.tvPlatinumLicSelfEmployed)
    TextView tvPlatinumLicSelfEmployed;
    @BindView(R.id.tvSignatureLicPremium)
    TextView tvSignatureLicPremium;
    @BindView(R.id.tvSignatureLicPremiumSingle)
    TextView tvSignatureLicPremiumSingle;
    @BindView(R.id.tvSignatureLicSalaried)
    TextView tvSignatureLicSalaried;
    @BindView(R.id.tvSignatureLicSelfEmployed)
    TextView tvSignatureLicSelfEmployed;
    private FirebaseFirestore fireRef;
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
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        fireRef = FirebaseFirestore.getInstance();
        fireRef.setFirestoreSettings(settings);
        init();
getdata();
    }

    private void getdata(){
        fireRef.collection("know_more")
                .document("EligibilityCondition")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot appFormDocument = task.getResult();
                        if (appFormDocument != null && appFormDocument.exists()) {
                            String platinumLicPremiumText = appFormDocument.getString("platinumLicPremium");
                            String platinumLicPremiumSingleText = appFormDocument.getString("platinumLicPremiumSingle");
                            String platinumLicSalariedText = appFormDocument.getString("platinumLicSalaried");
                            String platinumLicSelfEmployedText = appFormDocument.getString("platinumLicSelfEmployed");
                            String signatureLicPremiumText = appFormDocument.getString("signatureLicPremium");
                            String signatureLicPremiumSingleText = appFormDocument.getString("signatureLicPremiumSingle");
                            String signatureLicSalariedText = appFormDocument.getString("signatureLicSalaried");
                            String signatureLicSelfEmployedText = appFormDocument.getString("signatureLicSelfEmployed");


                            tvPlatinumLicPremium.setText(platinumLicPremiumText);
                            tvPlatinumLicPremiumSingle.setText(platinumLicPremiumSingleText);
                            tvPlatinumLicSalaried.setText(platinumLicSalariedText);
                            tvPlatinumLicSelfEmployed.setText(platinumLicSelfEmployedText);
                            tvSignatureLicPremium.setText(signatureLicPremiumText);
                            tvSignatureLicPremiumSingle.setText(signatureLicPremiumSingleText);
                            tvSignatureLicSalaried.setText(signatureLicSalariedText);
                            tvSignatureLicSelfEmployed.setText(signatureLicSelfEmployedText);
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
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
