package in.gen2.policymanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
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
    private FirebaseFirestore fireRef;
    private String applicationId,srNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy_details);
        unbinder = ButterKnife.bind(this);
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
            if(applicationId!=null&&srNo!=null){


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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return  true;
        }
        return super.onOptionsItemSelected(item);

    }
}
