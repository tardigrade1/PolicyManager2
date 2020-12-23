package in.gen2.policymanager;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewByodFormActivity extends AppCompatActivity {

    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etMobileNo)
    EditText etMobileNo;
    @BindView(R.id.etLandlineNo)
    EditText etLandlineNo;
    @BindView(R.id.etMotherName)
    EditText etMotherName;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etPANNo)
    EditText etPANNo;
    @BindView(R.id.etAxisBankAccNo)
    EditText etAxisBankAccNo;
    @BindView(R.id.etNomineeName)
    EditText etNomineeName;
    @BindView(R.id.etNomineeRelation)
    EditText etNomineeRelation;
    @BindView(R.id.lvBasicDetails)
    LinearLayout lvBasicDetails;


    @BindView(R.id.etYrsOfLiving)
    EditText etYrsOfLiving;
    @BindView(R.id.etResidanceAddress)
    EditText etResidanceAddress;
    @BindView(R.id.etResidanceLandmark)
    EditText etResidanceLandmark;
    @BindView(R.id.etResidancePin)
    EditText etResidancePin;
    @BindView(R.id.lvResidetialDetails)
    LinearLayout lvResidetialDetails;

    @BindView(R.id.etSelfEmployedCompanyName)
    EditText etSelfEmployedCompanyName;
    @BindView(R.id.etEmployeesNo)
    EditText etEmployeesNo;
    @BindView(R.id.etYrsOfBusiness)
    EditText etYrsOfBusiness;
    @BindView(R.id.etTotalExp)
    EditText etTotalExp;
    @BindView(R.id.lvEmploymentDetails)
    LinearLayout lvEmploymentDetails;
    @BindView(R.id.lvSelfEmployed)
    LinearLayout lvSelfEmployed;
    @BindView(R.id.lvSalaried)
    LinearLayout lvSalaried;

    @BindView(R.id.etSalariedCompanyName)
    EditText etSalariedCompanyName;
    @BindView(R.id.etDesignation)
    EditText etDesignation;
    @BindView(R.id.etYrsOfService)
    EditText etYrsOfService;
    @BindView(R.id.etDepartment)
    EditText etDepartment;
    @BindView(R.id.etSalariedTotalExp)
    EditText etSalariedTotalExp;
    @BindView(R.id.etEmploymentID)
    EditText etEmploymentID;
    @BindView(R.id.etSalaryPM)
    EditText etSalaryPM;

    @BindView(R.id.rgRelationshipStatus)
    RadioGroup rgRelationshipStatus;
    @BindView(R.id.rgResidenceType)
    RadioGroup rgResidenceType;
    @BindView(R.id.rgEmploymentType)
    RadioGroup rgEmploymentType;
    @BindView(R.id.rgOwnerShipType)
    RadioGroup rgOwnerShipType;
    @BindView(R.id.rgBusinessNature)
    RadioGroup rgBusinessNature;
    @BindView(R.id.rgSalariedLevelType)
    RadioGroup rgSalariedLevelType;
    @BindView(R.id.rgSector)
    RadioGroup rgSector;
    @BindView(R.id.rgIndustry)
    RadioGroup rgIndustry;

    @BindView(R.id.etOfficeAddress)
    EditText etOfficeAddress;
    @BindView(R.id.etOfficeEmail)
    EditText etOfficeEmail;
    @BindView(R.id.etOfficePhone)
    EditText etOfficePhone;
    @BindView(R.id.lvOfficeDetails)
    LinearLayout lvOfficeDetails;

    Boolean isBasicDetailsExpanded = false;
    Boolean isEmploymentDetailExpanded = false;
    Boolean isResidenceDetailExpanded = false;
    Boolean isOfficeDetailExpanded = false;

    String name,mobileNo,landLineNo,motherName,relationshipStatus,email,panNo,axisBankACNo,nominee,nomineeRelation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_byod_form);
        ButterKnife.bind(this);
        rgRelationshipStatus.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton rbRelationshipStatus = group.findViewById(checkedId);
            if (null != rbRelationshipStatus && checkedId > (-1)) {
                Toast.makeText(NewByodFormActivity.this, rbRelationshipStatus.getText(), Toast.LENGTH_SHORT).show();
            }
        });
        rgResidenceType.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton rbResidenceType = group.findViewById(checkedId);
            if (null != rbResidenceType && checkedId > (-1)) {
                Toast.makeText(NewByodFormActivity.this, rbResidenceType.getText(), Toast.LENGTH_SHORT).show();
            }
        });
        rgEmploymentType.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton rbEmploymentType = group.findViewById(checkedId);
            if (null != rbEmploymentType && checkedId > (-1)) {
                int position = group.indexOfChild(rbEmploymentType);
                switch (position)
                {
                    case 0:
                        lvSelfEmployed.setVisibility(View.VISIBLE);
                        lvSalaried.setVisibility(View.GONE);
                        break;
                    case 1:
                        lvSelfEmployed.setVisibility(View.GONE);
                        lvSalaried.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        rgBusinessNature.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton rbBusinessNature = group.findViewById(checkedId);
            if (null != rbBusinessNature && checkedId > (-1)) {
                Toast.makeText(NewByodFormActivity.this, rbBusinessNature.getText(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.constCustomerBasicDetail)
    public void onClickCustomerBasicDetail() {
        if (isBasicDetailsExpanded) {
            isBasicDetailsExpanded = false;
            lvBasicDetails.setVisibility(View.GONE);
            closeKeyboard();
        } else {
            isBasicDetailsExpanded = true;
            isEmploymentDetailExpanded = false;
            isResidenceDetailExpanded = false;
            isOfficeDetailExpanded = false;
            lvBasicDetails.setVisibility(View.VISIBLE);
            lvEmploymentDetails.setVisibility(View.GONE);
            lvResidetialDetails.setVisibility(View.GONE);
            lvOfficeDetails.setVisibility(View.GONE);
        }

    }

    @OnClick(R.id.constResidentialDetail)
    public void onClickResidenceDetail() {
        if (isResidenceDetailExpanded) {
            isResidenceDetailExpanded = false;
            lvResidetialDetails.setVisibility(View.GONE);
            closeKeyboard();
        } else {
            isBasicDetailsExpanded = false;
            isEmploymentDetailExpanded = false;
            isResidenceDetailExpanded = true;
            isOfficeDetailExpanded = false;
            lvBasicDetails.setVisibility(View.GONE);
            lvEmploymentDetails.setVisibility(View.GONE);
            lvResidetialDetails.setVisibility(View.VISIBLE);
            lvOfficeDetails.setVisibility(View.GONE);
        }

    }

    @OnClick(R.id.constEmploymentDetail)
    public void onClickEmploymentDetail() {
        if (isEmploymentDetailExpanded) {
            isEmploymentDetailExpanded = false;
            lvEmploymentDetails.setVisibility(View.GONE);
            closeKeyboard();
        } else {
            isBasicDetailsExpanded = false;
            isEmploymentDetailExpanded = true;
            isResidenceDetailExpanded = false;
            isOfficeDetailExpanded = false;
            lvBasicDetails.setVisibility(View.GONE);
            lvEmploymentDetails.setVisibility(View.VISIBLE);
            lvResidetialDetails.setVisibility(View.GONE);
            lvOfficeDetails.setVisibility(View.GONE);
        }

    }

    @OnClick(R.id.constOfficeDetail)
    public void onClickOfficeDetail() {
        if (isOfficeDetailExpanded) {
            isOfficeDetailExpanded = false;
            lvOfficeDetails.setVisibility(View.GONE);
            closeKeyboard();
        } else {
            isBasicDetailsExpanded = false;
            isEmploymentDetailExpanded = false;
            isResidenceDetailExpanded = false;
            isOfficeDetailExpanded = true;
            lvBasicDetails.setVisibility(View.GONE);
            lvEmploymentDetails.setVisibility(View.GONE);
            lvResidetialDetails.setVisibility(View.GONE);
            lvOfficeDetails.setVisibility(View.VISIBLE);
        }

    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}