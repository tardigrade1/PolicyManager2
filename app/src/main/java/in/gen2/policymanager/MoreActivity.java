package in.gen2.policymanager;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import in.gen2.policymanager.Fragments.AxisBankAppFragment;
import in.gen2.policymanager.Fragments.BenefitsOfLicCardFragment;
import in.gen2.policymanager.Fragments.CommsionStructureFragment;
import in.gen2.policymanager.Fragments.DocumentsRequiredFragment;
import in.gen2.policymanager.Fragments.EligibilityConditionFragment;
import in.gen2.policymanager.Fragments.HowToFragment;
import in.gen2.policymanager.Fragments.LicFaqFragment;
import in.gen2.policymanager.Fragments.LicPremiumOnlinePaymentFragment;
import in.gen2.policymanager.Fragments.ProcessofLicCardFragment;
import in.gen2.policymanager.Fragments.WhyLICCardFragment;

public class MoreActivity extends AppCompatActivity {

    Unbinder unbinder;
    @BindView(R.id.lvQueries)
    LinearLayout lvQueries;
    @BindView(R.id.frame_container)
    FrameLayout frame_container;
    WhyLICCardFragment WhyLic = new WhyLICCardFragment();
    BenefitsOfLicCardFragment BenefitsLic = new BenefitsOfLicCardFragment();
    EligibilityConditionFragment eligibleConditionFrag = new EligibilityConditionFragment();
    DocumentsRequiredFragment docFrag = new DocumentsRequiredFragment();
    CommsionStructureFragment commissionFrag=new CommsionStructureFragment();
    LicFaqFragment faqFrag=new LicFaqFragment();
    AxisBankAppFragment axisBankAppFragment=new AxisBankAppFragment();
    ProcessofLicCardFragment licCardFragment=new ProcessofLicCardFragment();
    LicPremiumOnlinePaymentFragment licPremiumOnlinePaymentFragment=new LicPremiumOnlinePaymentFragment();
    HowToFragment howToFragment=new HowToFragment();
    final FragmentManager fm = getSupportFragmentManager();
    private ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        unbinder = ButterKnife.bind(this);
        actionBar = getSupportActionBar();

    }

    @OnClick(R.id.whyLIC)
    public void onClickWhyLic() {
        lvQueries.setVisibility(View.GONE);
        fm.beginTransaction()
                .setCustomAnimations(R.animator.slide_left, 0, 0, 0)
                .replace(R.id.frame_container, WhyLic)
                .addToBackStack(null)
                .commit();


    }

    @OnClick(R.id.BenefitsOfLICCards)
    public void onClickBenefitsLicCards() {
        lvQueries.setVisibility(View.GONE);
        fm.beginTransaction()
                .setCustomAnimations(R.animator.slide_left, 0, 0, 0)
                .replace(R.id.frame_container, BenefitsLic)
                .addToBackStack(null)
                .commit();

    }

    @OnClick(R.id.EligibilityCondition)
    public void onClickEligibleCondition() {
        lvQueries.setVisibility(View.GONE);
        fm.beginTransaction()
                .setCustomAnimations(R.animator.slide_left, 0, 0, 0)
                .replace(R.id.frame_container, eligibleConditionFrag)
                .addToBackStack(null)
                .commit();

    }

    @OnClick(R.id.DocumentsRequired)
    public void onDocumentRequired() {
        lvQueries.setVisibility(View.GONE);
        fm.beginTransaction()
                .setCustomAnimations(R.animator.slide_left, 0, 0, 0)
                .replace(R.id.frame_container, docFrag)
                .addToBackStack(null)
                .commit();
    }
    @OnClick(R.id.commissionStructure)
    public void onClickCommissionStructure(){
        lvQueries.setVisibility(View.GONE);
        fm.beginTransaction()
                .setCustomAnimations(R.animator.slide_left, 0, 0, 0)
                .replace(R.id.frame_container, commissionFrag)
                .addToBackStack(null)
                .commit();
    }
    @OnClick(R.id.Faqs)
    public void onFaqsClick(){
        lvQueries.setVisibility(View.GONE);
        fm.beginTransaction()
                .setCustomAnimations(R.animator.slide_left, 0, 0, 0)
                .replace(R.id.frame_container, faqFrag)
                .addToBackStack(null)
                .commit();
    }@OnClick(R.id.OnlinePaymentClick)
    public void onOnlinePaymentClick(){
        lvQueries.setVisibility(View.GONE);
        fm.beginTransaction()
                .setCustomAnimations(R.animator.slide_left, 0, 0, 0)
                .replace(R.id.frame_container, licPremiumOnlinePaymentFragment)
                .addToBackStack(null)
                .commit();
    }

    @OnClick(R.id.ProcessOfLicCard)
    public void onProcessLicCardClick(){
        lvQueries.setVisibility(View.GONE);
        fm.beginTransaction()
                .setCustomAnimations(R.animator.slide_left, 0, 0, 0)
                .replace(R.id.frame_container, licCardFragment)
                .addToBackStack(null)
                .commit();
    }
    @OnClick(R.id.appOfAxis)
    public void onAxisClick(){
        lvQueries.setVisibility(View.GONE);
        fm.beginTransaction()
                .setCustomAnimations(R.animator.slide_left, 0, 0, 0)
                .replace(R.id.frame_container, axisBankAppFragment)
                .addToBackStack(null)
                .commit();
    }
    @OnClick(R.id.HowToClick)
    public void onHowToClick(){
        lvQueries.setVisibility(View.GONE);
        fm.beginTransaction()
                .setCustomAnimations(R.animator.slide_left, 0, 0, 0)
                .replace(R.id.frame_container, howToFragment)
                .addToBackStack(null)
                .commit();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onBackPressed() {
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStackImmediate();
            actionBar.setTitle("Know More");
            lvQueries.setVisibility(View.VISIBLE);

        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        actionBar.show();
    }
}
