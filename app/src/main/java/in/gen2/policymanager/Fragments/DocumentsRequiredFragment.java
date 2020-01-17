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

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import in.gen2.policymanager.R;

public class DocumentsRequiredFragment extends Fragment {


    private View view;
    private Unbinder unbinder;
    @BindView(R.id.docRequiredList)
    TextView docRequired;
    @BindView(R.id.salariedRequiredText)
    TextView salariedRequired;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("Documents Required");
        view = inflater.inflate(R.layout.fragment_documents_required, container, false);
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
        String colorPhotoColor="<span style=color:#D18400>Color photo</span>";
        String panColor="<span style=color:#D18400>PAN</span>";
        String addressProofColor="<span style=color:#D18400>Address proof</span>";
        String eAdarNotAcceptableColor="<span style=color:#D18400> E-AADHAR IS NOT ACCEPTABLE.</span>";
        String licPremColor="<span style=color:#D18400>LIC premium</span>";
        String employeeIdColor="<span style=color:#D18400>Employee ID</span>";
        String incomeProofColor="<span style=color:#D18400>Income proof- </span>";
        String noIncomeProofColor="<span style=color:#D18400> NO INCOME PROOF </span>";

        docRequired.setText(Html.fromHtml("<ul>" +
                "<li>"+colorPhotoColor+" ( Passport size with plane background, Ticket Size, Stylish photo not acceptable)</li>" +
                "<li>"+panColor+" ( Clear Copy of Sign, Photo, Name, Number etc.</li>" +
                "<li>"+addressProofColor+" (Clear copy of any one Aadhar Card/ DL / Passport/Voter ID  showing clear address & Photo)"+eAdarNotAcceptableColor+"</li>" +
                "<li>"+licPremColor+" Copy of Latest Prem receipt/Premium Paid Certificate /Policy Status from Branch or Premium Point</li>" +
                "<li>"+employeeIdColor+" in case of Salaried persons</li>" +
                "<li>"+incomeProofColor+"If Own life Yly Premium (Non Single)  is more than 30000 for last 3 Years or 5 lacs Single Prem"+noIncomeProofColor+"required otherwise Income Proof required.</li>" +
                "<ul>"));
        String itrColor="<span style=color:#000>ITR + Computation Sheet</span>";
        String salarySlipColor="<span style=color:#000>Last two months Salary Slip + 3 Months   Bank Statement</span>";
        String f16Color="<span style=color:#000>F16 (Part A & B) duly verified from issuing authority)</span>";
        salariedRequired.setText(Html.fromHtml("<ul>" +
                "<li>"+itrColor+"<br>Or</li>" +
                "<li>"+salarySlipColor+" (If Bank name and A/C no given on Salary Slip, Bank Statement not required)<br>Or</li>" +
                "<li>"+f16Color+"</li>" +
                "</ul>"));

    }
}
