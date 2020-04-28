package in.gen2.policymanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import in.gen2.policymanager.models.EmpData;

public class ViewSrDetailsActivity extends AppCompatActivity {
    Unbinder unbinder;
    @BindView(R.id.tvEmployeeName)
    TextView empName;
    @BindView(R.id.tvEmployeeSRId)
    TextView empSrId;
    @BindView(R.id.tvEmpEmail)
    TextView empEmail;
    @BindView(R.id.tvEmpDoj)
    TextView empdoj;
    @BindView(R.id.tvEmpResidence)
    TextView empResidence;
    @BindView(R.id.tvEmpContact)
    TextView empContact;
    @BindView(R.id.etEditEmpContact)
    EditText etEditContact;
    @BindView(R.id.tvEmpSupervisorCode)
    TextView empSupervisorCode;
    @BindView(R.id.tvActiveStatus)
    TextView tvActiveStatus;
    @BindView(R.id.etEditSuperVisorCode)
    EditText etEditSuperVisorCode;
    @BindView(R.id.rvEditDetails)
    RelativeLayout rvEdit;
    @BindView(R.id.tvEditEmployeeDetails)
    TextView tvEditEmployeeDetails;
    @BindView(R.id.imgEditEmployeeClose)
    ImageView imgClose;
    @BindView(R.id.etEditEmpEmail)
    EditText etEditEmpEmail;
    @BindView(R.id.etEditEmpName)
    EditText etEditEmpName;
    @BindView(R.id.etEditEmpDoj)
    EditText etEditEmpDoj;
    @BindView(R.id.etEditEmpResidence)
    EditText etEditEmpResidence;
    @BindView(R.id.lvAboutEmployee)
    LinearLayout lvAboutEmployee;
    @BindView(R.id.lvEditEmployee)
    LinearLayout lvEditEmployee;
    @BindView(R.id.lvViewApplicationList)
    LinearLayout lvViewApplicationList;
    @BindView(R.id.lvUpdateEmployeeDetail)
    LinearLayout lvUpdateEmployeeDetail;
    @BindView(R.id.cbActive)
    CheckBox cbActive;
    private FirebaseFirestore fireRef;
    private String srNo, srName;
    private String updatedName;
    private String updatedEmail;
    private String updatedDoj;
    private String updatedContact;
    private String updatedResidence;
    private String updatedSupervisorCode;
    private SharedPreferences prefs = null;
    private Boolean admin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_sr_details);
        unbinder = ButterKnife.bind(this);
        prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        admin = prefs.getBoolean("admin", false);
        srNo = getIntent().getStringExtra("srNo");
        srName = getIntent().getStringExtra("srName");
        empName.setText(srName);
        etEditEmpName.setText(srName);
        empSrId.setText("Sr Id: " + srNo);
        if(admin){
            rvEdit.setVisibility(View.VISIBLE);
        }
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();

        fireRef = FirebaseFirestore.getInstance();
        fireRef.setFirestoreSettings(settings);
        new LoadEmployeeData().execute();
    }

    public void viewApplication(View view) {
        Intent intentPolicyDetail = new Intent(this, PoliciesListActivity.class);
        intentPolicyDetail.putExtra("srNo", srNo);
        startActivity(intentPolicyDetail);
    }

    public void pressBack(View view) {
        finish();
    }

    public void updateEmployeeDetails(View view) {

        if(CheckEditText()){
            new updateEmployeeData().execute();
        }
    }

    public void editDetailsClick(View view) {
        lvAboutEmployee.setVisibility(View.GONE);
        lvViewApplicationList.setVisibility(View.GONE);
        lvEditEmployee.setVisibility(View.VISIBLE);
        lvUpdateEmployeeDetail.setVisibility(View.VISIBLE);
        tvEditEmployeeDetails.setVisibility(View.GONE);
        imgClose.setVisibility(View.VISIBLE);
    }

    public void closeDetailClick(View view) {
        lvEditEmployee.setVisibility(View.GONE);
        lvUpdateEmployeeDetail.setVisibility(View.GONE);
        lvAboutEmployee.setVisibility(View.VISIBLE);
        lvViewApplicationList.setVisibility(View.VISIBLE);
        tvEditEmployeeDetails.setVisibility(View.VISIBLE);
        imgClose.setVisibility(View.GONE);
    }


    class LoadEmployeeData extends AsyncTask<Void, Void, Integer> {
        ProgressDialog dialog = new ProgressDialog(ViewSrDetailsActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setTitle("Please Wait");
            dialog.setMessage("loading..");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
            isInternetOn();

        }

        protected void isInternetOn() {
            ConnectivityManager conn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = conn.getActiveNetworkInfo();
            if (activeNetwork != null && activeNetwork.isConnected() == true) {
            } else {
                dialog.setTitle("Network Alert");
                dialog.setMessage("no internet connection!!");
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
        }

        @Override
        protected Integer doInBackground(Void... params) {
            fireRef.collection("SalesRepresentatives")
                    .document(srNo)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot appFormDocument = task.getResult();
                            if (appFormDocument != null && appFormDocument.exists()) {
                                EmpData values = appFormDocument.toObject(EmpData.class);
                                String doj = values.getDoj();
                                String residence = values.getResidence();
                                String contact = values.getMobileNo();
                                String email = values.getEmail();
                                String supervisor = values.getSupervisorCode();
                                Boolean activeStatus = values.isActive();
                                if(activeStatus){
                                    cbActive.setChecked(true);
                                    tvActiveStatus.setText("Active");
                                }
                                else {
                                    cbActive.setChecked(false);
                                    tvActiveStatus.setText("Suspend");
                                }
                                empdoj.setText(doj);
                                empContact.setText(contact);
                                empResidence.setText(residence);
                                empEmail.setText(email);
                                empSupervisorCode.setText(supervisor);
                                etEditContact.setText(contact);
                                etEditEmpDoj.setText(doj);
                                etEditEmpEmail.setText(email);
                                etEditEmpResidence.setText(residence);
                                etEditSuperVisorCode.setText(supervisor);
                                dialog.dismiss();
                            }

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

            return 0;
        }
    }

    private boolean CheckEditText() {
        updatedName = etEditEmpName.getText().toString().trim();
        updatedEmail = etEditEmpEmail.getText().toString().trim();
        updatedDoj = etEditEmpDoj.getText().toString().trim();
        updatedContact = etEditContact.getText().toString().trim();
        updatedResidence = etEditEmpResidence.getText().toString().trim();
        updatedSupervisorCode = etEditSuperVisorCode.getText().toString().trim();
        if (updatedName.isEmpty()) {
            etEditEmpName.requestFocus();
            etEditEmpName.setError("enter name please");
            return false;
        } else if (updatedContact.isEmpty()) {
            etEditContact.requestFocus();
            etEditContact.setError("Contact is mandatory");
            return false;
        } else if (updatedSupervisorCode.isEmpty()) {
            etEditSuperVisorCode.requestFocus();
            etEditSuperVisorCode.setError("Supervisor code is mandatory");
            return false;
        }
        return true;
    }

    @SuppressLint("StaticFieldLeak")
    class updateEmployeeData extends AsyncTask<Void, Void, Integer> {
        ProgressDialog dialog = new ProgressDialog(ViewSrDetailsActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setTitle("Please Wait");
            dialog.setMessage("entering data..");
            dialog.show();
            isInternetOn();
        }

        protected void isInternetOn() {
            ConnectivityManager conn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = conn.getActiveNetworkInfo();
            if (activeNetwork != null && activeNetwork.isConnected() == true) {
            } else {
                dialog.setTitle("Network Alert");
                dialog.setMessage("no internet connection!!");
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
        }

        @Override
        protected Integer doInBackground(Void... params) {
            final HashMap<String, Object> hm = new HashMap<>();

            hm.put("name", updatedContact);
            hm.put("doj", updatedDoj);
            hm.put("residence", updatedResidence);
            hm.put("mobileNo", updatedContact);
            hm.put("email", updatedEmail);
            if (cbActive.isChecked()) {
                hm.put("active", true);
            } else {
                hm.put("active", false);
            }
            hm.put("supervisorCode", updatedSupervisorCode);

            DocumentReference docRef = fireRef.collection("SalesRepresentatives").document(srNo);
            docRef.update(hm)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            toastMessage("data Updated successfully");
                            finish();
                            startActivity(getIntent());
                            dialog.dismiss();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            toastMessage("Error in adding document");
                            dialog.dismiss();
                        }
                    });

            return 0;
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
