package in.gen2.policymanager.Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import in.gen2.policymanager.R;
import in.gen2.policymanager.adapters.FaqsAdapter;
import in.gen2.policymanager.models.FaqsDataModel;

public class LicFaqFragment extends Fragment {


    private View view;
    private Unbinder unbinder;
    @BindView(R.id.listViewLicFaqs)
    ListView listViewLicFaq;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("FAQs");
        view = inflater.inflate(R.layout.fragment_lic_faq, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view=view;
        unbinder = ButterKnife.bind(this,view);
        init();
    }

    private void init() {
        ArrayList<FaqsDataModel> faqList = listArray();
        listViewLicFaq.setAdapter(new FaqsAdapter(faqList, getContext()));
    }

    public ArrayList<FaqsDataModel> listArray() {

        ArrayList<FaqsDataModel> objList = new ArrayList<FaqsDataModel>();
        FaqsDataModel dm;

        dm = new FaqsDataModel();
        dm.setQues("How Bank decide limit?");
        dm.setAns("Mly limit is given on the Card on the basis of Mly income and Status of the Applicant");
        objList.add(dm);

        dm = new FaqsDataModel();
        dm.setQues("If LIC premium is less and ITR is more , what will be Credit?");
        dm.setAns("Then limit will be taken as per ITR return.");
        objList.add(dm);

        dm = new FaqsDataModel();
        String licCardLinkColor="<span style=color:#D18400>www.liccards.co.in</span>";
        dm.setQues("How can I know Status of my applications?");
        dm.setAns("Online tracking system on "+Html.fromHtml(licCardLinkColor)+", message of approval/decline also being sent directly to applicant and a copy to SR. Client May ask. ");
        objList.add(dm);

        dm = new FaqsDataModel();
        dm.setQues("How to active the Card ?");
        dm.setAns("If PIN no not received with Credit Card then applicant may generate PIN by visiting nearest Axis Bank ATM with his registered mobile or call to Toll free no.");
        objList.add(dm);

        dm = new FaqsDataModel();
        dm.setQues("How much time to card issue ?");
        dm.setAns("About one month after login in LIC ");
        objList.add(dm);

        dm = new FaqsDataModel();
        dm.setQues("What procedure to represent if wrong card is issued (e.g. Platinum Card in place of Signature Card)?");
        dm.setAns("Send email to liccards@axisbank.com from his registered email with full facts.");
        objList.add(dm);

        dm = new FaqsDataModel();
        dm.setQues("what if card lost or miss utilized ?");
        dm.setAns("Then Immediate card should be blocked on Toll free no and do as per their directions.");
        objList.add(dm);

        dm = new FaqsDataModel();
        dm.setQues("How to get signature card instead of platinum as at the time of application income was less or premium payment was less?");
        dm.setAns("Since he is not eligible, he will not get card.");
        objList.add(dm);

        dm = new FaqsDataModel();
        dm.setQues("How to pursue to convert Platinum into signature card ?");
        dm.setAns("Then sent request on email liccards@axisbank.co with income proof.");
        objList.add(dm);

        dm = new FaqsDataModel();
        dm.setQues("Card was there but since long back party got it cancelled?");
        dm.setAns("Better ask Toll free no that the cancellation action been taken in record, if yes, he may apply as a fresh.");
        objList.add(dm);

        dm = new FaqsDataModel();
        dm.setQues("How to activise unused credit card?");
        dm.setAns("Ask on Toll free no to cancel the previous card and after receiving cancellation u may apply fresh card.");
        objList.add(dm);

        dm = new FaqsDataModel();
        dm.setQues("How to change mob no or Address?");
        dm.setAns("Give form in Axis Bank.");
        objList.add(dm);

        dm = new FaqsDataModel();
        dm.setQues("Some of the customer received card but address written is not perfect or wrong.");
        dm.setAns("Call on Toll free no.");
        objList.add(dm);

        dm = new FaqsDataModel();
        dm.setQues("After issue of Card for any query.");
        dm.setAns("Call on toll free no is 18004190064, IRVS 18604195555.");
        objList.add(dm);

        dm = new FaqsDataModel();
        dm.setQues("How to get cancel ECS mandate and to start auto debit mandate?");
        dm.setAns("Auto mandate is not allowed in ECS/Mly/SSS/Term Assurance  pols.");
        objList.add(dm);

        dm = new FaqsDataModel();
        dm.setQues("Can Card bill payment made by different accounts?");
        dm.setAns("Yes.");
        objList.add(dm);

        dm = new FaqsDataModel();
        dm.setQues("There is a provision of payment through EMI for Single purchase of more than Rs 2500/-, whether it is also allowed if he pays LIC premium in a pol more than 2500?");
        dm.setAns("Yes.");
        objList.add(dm);

        dm = new FaqsDataModel();
        dm.setQues("Can HUF APPLY?");
        dm.setAns("Karta can apply in his own name.");
        objList.add(dm);
        return objList;
    }
}
