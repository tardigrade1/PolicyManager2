package in.gen2.policymanager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

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
    @BindView(R.id.rgDeliveryPlace)
    RadioGroup rgDeliveryPlace;
    @BindView(R.id.rgAddressProof)
    RadioGroup rgAddressProof;
    @BindView(R.id.rgIncomeProof)
    RadioGroup rgIncomeProof;

    @BindView(R.id.etOfficeAddress)
    EditText etOfficeAddress;
    @BindView(R.id.etOfficeEmail)
    EditText etOfficeEmail;
    @BindView(R.id.etOfficePhone)
    EditText etOfficePhone;
    @BindView(R.id.lvOfficeDetails)
    LinearLayout lvOfficeDetails;

    @BindView(R.id.lvDeliveryDetails)
    LinearLayout lvDeliveryDetails;
    @BindView(R.id.lvDocumentsList)
    LinearLayout lvDocumentsList;

    @BindView(R.id.cbPANCArd)
    CheckBox cbPANCArd;
    @BindView(R.id.cbAadharCard)
    CheckBox cbAadharCard;
    @BindView(R.id.cbEmpCard)
    CheckBox cbEmpCard;
    private String srNo;

    Boolean isBasicDetailsExpanded = false;
    Boolean isEmploymentDetailExpanded = false;
    Boolean isResidenceDetailExpanded = false;
    Boolean isOfficeDetailExpanded = false;
    Boolean isDeliveryDetailExpanded = false;
    Boolean isDocumentsDetailExpanded = false;

    String name, mobileNo, landLineNo, motherName, relationshipStatus, email, panNo, axisBankACNo, nominee, nomineeRelation;
    String yrsOfLiving, residenceAddress, residenceLandmark, residencePin, residenceType;
    String employmentType = null;
    String companyName, employeesNo, yrsOfBusiness, totalExp, ownership, businessNature;
    String designation, employmentID, yrsOfService, department, salaryPM, level, sector, industry;
    String officeAddress, officeEmail, officeContact;
    String deliverAt, addressProof, incomeProof;
    private RadioButton rbRelationshipStatus;
    private SharedPreferences prefs = null;
    private FirebaseUser user;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_byod_form);
        ButterKnife.bind(this);
        firestore = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        srNo = prefs.getString("srNo", "");
        user = firebaseAuth.getCurrentUser();
        assert user != null;
        rgRelationshipStatus.setOnCheckedChangeListener((group, checkedId) -> {
            rbRelationshipStatus = group.findViewById(checkedId);
            if (null != rbRelationshipStatus && checkedId > (-1)) {
                Toast.makeText(NewByodFormActivity.this, rbRelationshipStatus.getText(), Toast.LENGTH_SHORT).show();
                relationshipStatus = rbRelationshipStatus.getText().toString().trim();
            }
        });
        rgResidenceType.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton rbResidenceType = group.findViewById(checkedId);
            if (null != rbResidenceType && checkedId > (-1)) {
                Toast.makeText(NewByodFormActivity.this, rbResidenceType.getText(), Toast.LENGTH_SHORT).show();
                residenceType = rbResidenceType.getText().toString().trim();
            }
        });
        rgEmploymentType.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton rbEmploymentType = group.findViewById(checkedId);
            if (null != rbEmploymentType && checkedId > (-1)) {
                int position = group.indexOfChild(rbEmploymentType);
                switch (position) {
                    case 0:
                        employmentType = "Self-employed";
                        lvSelfEmployed.setVisibility(View.VISIBLE);
                        lvSalaried.setVisibility(View.GONE);
                        break;

                    case 1:
                        employmentType = "salaried";
                        lvSelfEmployed.setVisibility(View.GONE);
                        lvSalaried.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        rgBusinessNature.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton rbBusinessNature = group.findViewById(checkedId);
            if (null != rbBusinessNature && checkedId > (-1)) {
                businessNature = rbBusinessNature.getText().toString().trim();
            }
        });
        rgOwnerShipType.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton rbBOwnerShipType = group.findViewById(checkedId);
            if (null != rbBOwnerShipType && checkedId > (-1)) {
                ownership = rbBOwnerShipType.getText().toString().trim();
            }
        });
        rgSector.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton rbSector = group.findViewById(checkedId);
            if (null != rbSector && checkedId > (-1)) {
                sector = rbSector.getText().toString().trim();
            }
        });
        rgIndustry.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton rbIndustry = group.findViewById(checkedId);
            if (null != rbIndustry && checkedId > (-1)) {
                industry = rbIndustry.getText().toString().trim();
            }
        });
        rgSalariedLevelType.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton rbSalariedLevel = group.findViewById(checkedId);
            if (null != rbSalariedLevel && checkedId > (-1)) {
                level = rbSalariedLevel.getText().toString().trim();
            }
        });
        rgDeliveryPlace.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton rbDeliveryPlace = group.findViewById(checkedId);
            if (null != rbDeliveryPlace && checkedId > (-1)) {
                deliverAt = rbDeliveryPlace.getText().toString().trim();
            }
        });
        rgAddressProof.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton rbAddressProof = group.findViewById(checkedId);
            if (null != rbAddressProof && checkedId > (-1)) {
                addressProof = rbAddressProof.getText().toString().trim();
            }
        });
        rgIncomeProof.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton rbIncomeProof = group.findViewById(checkedId);
            if (null != rbIncomeProof && checkedId > (-1)) {
                incomeProof = rbIncomeProof.getText().toString().trim();
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
            isDeliveryDetailExpanded = false;
            isDocumentsDetailExpanded = false;
            lvBasicDetails.setVisibility(View.VISIBLE);
            lvEmploymentDetails.setVisibility(View.GONE);
            lvResidetialDetails.setVisibility(View.GONE);
            lvOfficeDetails.setVisibility(View.GONE);
            lvDeliveryDetails.setVisibility(View.GONE);
            lvDocumentsList.setVisibility(View.GONE);
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
            isDeliveryDetailExpanded = false;
            isDocumentsDetailExpanded = false;
            lvBasicDetails.setVisibility(View.GONE);
            lvEmploymentDetails.setVisibility(View.GONE);
            lvResidetialDetails.setVisibility(View.VISIBLE);
            lvOfficeDetails.setVisibility(View.GONE);
            lvDeliveryDetails.setVisibility(View.GONE);
            lvDocumentsList.setVisibility(View.GONE);
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
            isDeliveryDetailExpanded = false;
            isDocumentsDetailExpanded = false;
            lvBasicDetails.setVisibility(View.GONE);
            lvEmploymentDetails.setVisibility(View.VISIBLE);
            lvResidetialDetails.setVisibility(View.GONE);
            lvOfficeDetails.setVisibility(View.GONE);
            lvDeliveryDetails.setVisibility(View.GONE);
            lvDocumentsList.setVisibility(View.GONE);
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
            isDeliveryDetailExpanded = false;
            isDocumentsDetailExpanded = false;
            lvBasicDetails.setVisibility(View.GONE);
            lvEmploymentDetails.setVisibility(View.GONE);
            lvResidetialDetails.setVisibility(View.GONE);
            lvOfficeDetails.setVisibility(View.VISIBLE);
            lvDeliveryDetails.setVisibility(View.GONE);
            lvDocumentsList.setVisibility(View.GONE);
        }


    }

    @OnClick(R.id.constDeliveryDetail)
    public void onClickDeliveryDetail() {
        if (isDeliveryDetailExpanded) {
            isDeliveryDetailExpanded = false;
            lvDeliveryDetails.setVisibility(View.GONE);
            closeKeyboard();
        } else {
            isBasicDetailsExpanded = false;
            isEmploymentDetailExpanded = false;
            isResidenceDetailExpanded = false;
            isOfficeDetailExpanded = false;
            isDeliveryDetailExpanded = true;
            isDocumentsDetailExpanded = false;
            lvBasicDetails.setVisibility(View.GONE);
            lvEmploymentDetails.setVisibility(View.GONE);
            lvResidetialDetails.setVisibility(View.GONE);
            lvOfficeDetails.setVisibility(View.GONE);
            lvDeliveryDetails.setVisibility(View.VISIBLE);
            lvDocumentsList.setVisibility(View.GONE);
        }

    }

    @OnClick(R.id.constDocumentsDetail)
    public void onClickDocumentsDetail() {
        if (isDocumentsDetailExpanded) {
            isDocumentsDetailExpanded = false;
            lvDocumentsList.setVisibility(View.GONE);
            closeKeyboard();
        } else {
            isBasicDetailsExpanded = false;
            isEmploymentDetailExpanded = false;
            isResidenceDetailExpanded = false;
            isOfficeDetailExpanded = false;
            isDeliveryDetailExpanded = false;
            isDocumentsDetailExpanded = true;
            lvBasicDetails.setVisibility(View.GONE);
            lvEmploymentDetails.setVisibility(View.GONE);
            lvResidetialDetails.setVisibility(View.GONE);
            lvOfficeDetails.setVisibility(View.GONE);
            lvDeliveryDetails.setVisibility(View.GONE);
            lvDocumentsList.setVisibility(View.VISIBLE);
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

    private boolean checkEditText() {
        name = etName.getText().toString().trim();
        mobileNo = etMobileNo.getText().toString().trim();
        landLineNo = etLandlineNo.getText().toString().trim();
        motherName = etMotherName.getText().toString().trim();
        email = etEmail.getText().toString();
        panNo = etPANNo.getText().toString();
        axisBankACNo = etAxisBankAccNo.getText().toString();
        nominee = etNomineeName.getText().toString();
        nomineeRelation = etNomineeRelation.getText().toString();
        yrsOfLiving = etYrsOfLiving.getText().toString();
        residenceAddress = etResidanceAddress.getText().toString();
        residenceLandmark = etResidanceLandmark.getText().toString();
        residencePin = etResidancePin.getText().toString();
        if (employmentType.equals("Self-employed")) {
            companyName = etSelfEmployedCompanyName.getText().toString();
            employeesNo = etEmployeesNo.getText().toString().trim();
            yrsOfBusiness = etYrsOfBusiness.getText().toString().trim();
            totalExp = etTotalExp.getText().toString().trim();
        } else if (employmentType.equals("salaried")) {
            companyName = etSalariedCompanyName.getText().toString();
            designation = etDesignation.getText().toString().trim();
            employmentID = etEmploymentID.getText().toString().trim();
            yrsOfService = etYrsOfService.getText().toString().trim();
            department = etDepartment.getText().toString().trim();
            totalExp = etSalariedTotalExp.getText().toString().trim();
            salaryPM = etSalaryPM.getText().toString().trim();
        }
        officeAddress = etOfficeAddress.getText().toString().trim();
        officeEmail = etOfficeEmail.getText().toString().trim();
        officeContact = etOfficePhone.getText().toString().trim();
        if(name.isEmpty()){
            return false;
        }
        return true;
    }

    private void addNewFormToDatabase() {
        ProgressDialog dialog = new ProgressDialog(NewByodFormActivity.this);
        dialog.setTitle("Please Wait");
        dialog.setMessage("Creating form..");
        dialog.setIndeterminate(false);
        dialog.setCancelable(false);
        dialog.show();
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("name", name);
        hm.put("mobile", mobileNo);
        hm.put("landLine", landLineNo);
        hm.put("email", email);
        hm.put("motherName", motherName);
        hm.put("relationshipStatus", relationshipStatus);
        hm.put("panNo", panNo);
        hm.put("axisBankNo", axisBankACNo);
        hm.put("nominee", nominee);
        hm.put("nomineeRelation", nomineeRelation);
        HashMap<String, Object> hmResidenceAddress = new HashMap<>();
        hmResidenceAddress.put("address", residenceAddress);
        hmResidenceAddress.put("yrs", yrsOfLiving);
        hmResidenceAddress.put("landmark", residenceLandmark);
        hmResidenceAddress.put("pin", residencePin);
        hmResidenceAddress.put("type", residenceType);
        hm.put("residenceAddress", hmResidenceAddress);
        HashMap<String, Object> hmOfficeDetails = new HashMap<>();
        if (employmentType.equals("Self-employed")) {
            hmOfficeDetails.put("name", companyName);
            hmOfficeDetails.put("type", employmentType);
            hmOfficeDetails.put("employeesNo", employeesNo);
            hmOfficeDetails.put("yrsOfBusiness", yrsOfBusiness);
            hmOfficeDetails.put("totalExp", totalExp);
            hmOfficeDetails.put("ownership", ownership);
            hmOfficeDetails.put("nature", businessNature);
        } else if (employmentType.equals("salaried")) {
            hmOfficeDetails.put("name", companyName);
            hmOfficeDetails.put("type", employmentType);
            hmOfficeDetails.put("employmentId", employmentID);
            hmOfficeDetails.put("yrsOfService", yrsOfService);
            hmOfficeDetails.put("totalExp", totalExp);
            hmOfficeDetails.put("designation", designation);
            hmOfficeDetails.put("department", department);
            hmOfficeDetails.put("salary", salaryPM);
            hmOfficeDetails.put("level", level);
            hmOfficeDetails.put("sector", sector);
            hmOfficeDetails.put("industry", industry);
        }
        hmOfficeDetails.put("address", officeAddress);
        hmOfficeDetails.put("phone", officeContact);
        hmOfficeDetails.put("email", officeEmail);
        hm.put("officeDetails", hmOfficeDetails);
        hm.put("deliverAt", deliverAt);
        hm.put("createdBy", srNo);
        hm.put("createdAt", FieldValue.serverTimestamp());
        HashMap<String, Object> hmDocuments = new HashMap<>();
        hmDocuments.put("pan", cbEmpCard.isChecked());
        hmDocuments.put("aadhar", cbAadharCard.isChecked());
        hmDocuments.put("employeeId", cbEmpCard.isChecked());
        hmDocuments.put("addressProof", addressProof);
        hmDocuments.put("incomeProof", incomeProof);
        hm.put("documents", hmDocuments);
        final DocumentReference docRef = firestore.collection("ByodForms").document();
        String ReferenceId = docRef.getId();
        docRef.set(hm)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .addOnFailureListener(e -> dialog.dismiss());
    }

    private void uploadDocuments(String id) {
        Intent i = new Intent(this, UploadDocumentsActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra("refId", id);
        startActivity(i);
    }
    @OnClick(R.id.btnContinueForm)
    public void onContinueForm(){
        if(checkEditText()){
            addNewFormToDatabase();
        }
    }
}