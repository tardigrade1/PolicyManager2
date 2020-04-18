package in.gen2.policymanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
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

    private FirebaseFirestore fireRef;
    private String srNo, srName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_sr_details);
        unbinder = ButterKnife.bind(this);
        srNo = getIntent().getStringExtra("srNo");
        srName = getIntent().getStringExtra("srName");
        empName.setText(srName);
        empSrId.setText("Sr Id: "+srNo);
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

                                empdoj.setText(doj);
                                empContact.setText(contact);
                                empResidence.setText(residence);
                                empEmail.setText(email);
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
}
