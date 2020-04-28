package in.gen2.policymanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class PolicyDetailsActivity extends AppCompatActivity {
    Unbinder unbinder;
    @BindView(R.id.tvApplicationId)
    TextView tvApplicationId;
    @BindView(R.id.tvHolderName)
    TextView tvHolderName;
    @BindView(R.id.tvHolderPanNo)
    TextView tvPan;
    @BindView(R.id.tvHolderContact)
    TextView tvContact;
    @BindView(R.id.tvPolicyPurchase)
    TextView tvPolicyPurchase;
    @BindView(R.id.tvPolicyStatus)
    TextView tvPolicyStatus;
    @BindView(R.id.tvApproveDate)
    TextView tvApprovalDate;
    @BindView(R.id.tvRejectRequirement)
    TextView tvRejectReqiure;
    @BindView(R.id.tvRejectComments)
    TextView tvRejectComments;
    @BindView(R.id.etCurrentStatus)
    TextView etCurrentStatus;
    @BindView(R.id.etUpdateDate)
    EditText etUpdateDate;
    @BindView(R.id.etUpdateComment)
    EditText etUpdateComment;
    @BindView(R.id.etUpdateRequirement)
    EditText etUpdateRequirement;
    @BindView(R.id.applicationInfoLayout)
    LinearLayout appInfoLayout;
    @BindView(R.id.updateInfoLayout)
    LinearLayout updateInfoLayout;
    private FirebaseFirestore fireRef;
    private String applicationId, srNo;
    private SharedPreferences prefs = null;
    private Boolean admin;
    private Menu menu;
    private String requirementUpdate;
    private String commentUpdate;
    private String dateUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy_details);
        unbinder = ButterKnife.bind(this);
        prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        admin = prefs.getBoolean("admin", false);
        applicationId = getIntent().getStringExtra("applicationId");
        srNo = getIntent().getStringExtra("srNo");

        tvApplicationId.setText(applicationId);
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();

        fireRef = FirebaseFirestore.getInstance();
        fireRef.setFirestoreSettings(settings);
        new LoadFirebaseData().execute();
    }

    public void updateFormData(View view) {
        if(EditText()){
           new  updateFormData().execute();
        }
    }

    @SuppressLint("StaticFieldLeak")
    class LoadFirebaseData extends AsyncTask<Void, Void, Integer> {
        ProgressDialog Dialog = new ProgressDialog(PolicyDetailsActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Dialog.setTitle("Please Wait");
            Dialog.setMessage("loading..");
            Dialog.setIndeterminate(false);
            Dialog.setCancelable(false);
            Dialog.show();
            isInternetOn();

        }

        protected void isInternetOn() {
            ConnectivityManager conn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = conn.getActiveNetworkInfo();
            if (activeNetwork != null && activeNetwork.isConnected() == true) {
            } else {
                Dialog.setMessage("please check your internet connection...");
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
        }

        @Override
        protected Integer doInBackground(Void... params) {
            if (applicationId != null && srNo != null) {


                fireRef.collection("SalesRepresentatives").document(srNo)
                        .collection("ApplicationForms")
                        .document(applicationId)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                DocumentSnapshot appFormDocument = task.getResult();
                                if (appFormDocument != null && appFormDocument.exists()) {
                                    String ContactNo = appFormDocument.getString("contactNo");
                                    String name = appFormDocument.getString("applicantName");
                                    String panNo = appFormDocument.getString("panNo");
                                    String purchaseDate = appFormDocument.getString("purchaseDate");
                                    String policyStatus = appFormDocument.getString("PolicyStatus");
                                    String approveDate = appFormDocument.getString("decisionDate");
                                    String comment = appFormDocument.getString("comment");
                                    String requirement = appFormDocument.getString("requirements");

                                    tvHolderName.setText(name);
                                    tvContact.setText(ContactNo);
                                    tvPan.setText(panNo);
                                    tvPolicyPurchase.setText(purchaseDate);
                                    tvPolicyStatus.setText(policyStatus);
                                    tvApprovalDate.setText(approveDate);
                                    tvRejectReqiure.setText(requirement);
                                    tvRejectComments.setText(comment);
                                    etCurrentStatus.setText(policyStatus);
                                    etUpdateDate.setText(approveDate);
                                    etUpdateComment.setText(comment);
                                    etUpdateRequirement.setText(requirement);
                                    Dialog.dismiss();
                                }

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
            }
            return 0;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        if (admin) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.edit_menu, menu);
            MenuItem item = menu.findItem(R.id.action_close);
            item.setVisible(false);
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_edit:
                appInfoLayout.setVisibility(View.GONE);
                updateInfoLayout.setVisibility(View.VISIBLE);
                menu.findItem(R.id.action_close).setVisible(true);
                menu.findItem(R.id.action_edit).setVisible(false);
                return true;
            case R.id.action_close:
                appInfoLayout.setVisibility(View.VISIBLE);
                updateInfoLayout.setVisibility(View.GONE);
                menu.findItem(R.id.action_edit).setVisible(true);
                menu.findItem(R.id.action_close).setVisible(false);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private boolean EditText(){
        requirementUpdate = etUpdateRequirement.getText().toString().trim();
        commentUpdate = etUpdateComment.getText().toString().trim();
        dateUpdate = etUpdateDate.getText().toString().trim();
        return true;
    }

    @SuppressLint("StaticFieldLeak")
    class updateFormData extends AsyncTask<Void, Void, Integer> {
        ProgressDialog Dialog = new ProgressDialog(PolicyDetailsActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Dialog.setTitle("Please Wait");
            Dialog.setMessage("entering data..");
            Dialog.show();
//            isInternetOn();
        }

        protected void isInternetOn() {

        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
        }

        @Override
        protected Integer doInBackground(Void... params) {
            final HashMap<String, Object> hm = new HashMap<>();

            hm.put("decisionDate", dateUpdate);
            hm.put("comment", commentUpdate);
            hm.put("requirements", requirementUpdate);
            DocumentReference docRef = fireRef.collection("SalesRepresentatives").document(srNo)
                    .collection("ApplicationForms")
                    .document(applicationId);
            docRef.update(hm)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            toastMessage("data Updated successfully");
                            finish();
                            startActivity(getIntent());
                            Dialog.dismiss();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            toastMessage("Error in adding document");
                            Dialog.dismiss();
                        }
                    });

            return 0;
        }
    }
    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
