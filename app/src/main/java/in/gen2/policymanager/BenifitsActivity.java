package in.gen2.policymanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BenifitsActivity extends AppCompatActivity {
    private Unbinder unbinder;
    @BindView(R.id.tvBenefitsListText)
    TextView tvBenefitsListText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_benifits);
        unbinder = ButterKnife.bind(this);
        init();
    }

    private void init() {
        String LifetimeFreeColor="<span style=color:#961301>Lifetime Free- </span>";
        String FreeAccidentalColor="<span style=color:#961301>Free accidental death risk cover</span>";
        String CardMustActiveColor="<span style=color:#961301>Card must active atleast once in 90 days</span>";
        String FreeColor="<span style=color:#961301>Reimbursement</span>";
        String InterestFreeColor="<span style=color:#961301>Interest Free</span>";
        String ConvenienceFreeColor="<span style=color:#961301>Free Convenience fees</span>";
        String AirportVIPColor="<span style=color:#961301>Airport VIP Lounge Access</span>";
        String OneRewardColor="<span style=color:#961301>One Reward Point</span>";
        String Each100Color="<span style=color:#961301>each ₹ 100/-</span>";
        String DoubleRewardColor="<span style=color:#961301>Double Reward Points</span>";
        String DinningDelightColor="<span style=color:#961301>Dinning delight program</span>";
        String ZerolostcardColor="<span style=color:#961301>Zero lost card liability</span>";
        tvBenefitsListText.setText(Html.fromHtml("<ul>" +
                "<li>"+LifetimeFreeColor+"No joing, Annual, Renewal or Convenience fees in payment of LIC Prem.</li>" +
                "<li>"+FreeAccidentalColor+" 3 lacs/5lacs in Platinum /Signature Card("+CardMustActiveColor+")</li>" +
                "<li>"+FreeColor+" of Petrol surcharge upto 1%(Min 400/-)</li>" +
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
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
