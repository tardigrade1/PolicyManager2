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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import in.gen2.policymanager.Helpers.SRSqliteData;

public class AddNewSrActivity extends AppCompatActivity {
    Unbinder unbinder;
    @BindView(R.id.tvNewSrName)
    TextView tvName;
    @BindView(R.id.tvNewSrSrNo)
    TextView tvSrNo;
    @BindView(R.id.tvNewSrDoj)
    TextView tvDoj;
    @BindView(R.id.tvNewSrBranch)
    TextView tvBranch;
    @BindView(R.id.tvNewSrEmail)
    TextView tvEmail;
    @BindView(R.id.tvNewSrMobile)
    TextView tvMobile;
    @BindView(R.id.tvNewSrResidence)
    TextView tvResidence;
    private FirebaseFirestore firestore;
    private String name;
    private String srNo;
    private String doj;
    private String branch;
    private String email;
    private String mobile;
    private String residence;
    private NetworkInfo activeNetwork;
    SRSqliteData srSqliteDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_sr);
        unbinder = ButterKnife.bind(this);
        firestore = FirebaseFirestore.getInstance();
        ConnectivityManager conn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetwork = conn.getActiveNetworkInfo();
        srSqliteDb = new SRSqliteData(this);
    }

    private boolean CheckText() {
        name = tvName.getText().toString().trim();
        srNo = tvSrNo.getText().toString().trim();
        doj = tvDoj.getText().toString().trim();
        branch = tvBranch.getText().toString().trim();
        email = tvEmail.getText().toString().trim();
        mobile = tvMobile.getText().toString().trim();
        residence = tvResidence.getText().toString().trim();

        if (name.isEmpty()) {
            tvName.requestFocus();
            tvName.setError("enter name first");
            return false;
        } else if (srNo.isEmpty()) {
            tvSrNo.requestFocus();
            tvSrNo.setError("Sr No is mandatory");
            return false;
        } else if (doj.isEmpty()) {
            tvDoj.requestFocus();
            tvDoj.setError("Doj is mandatory");
            return false;
        } else if (mobile.isEmpty()) {
            tvMobile.requestFocus();
            tvMobile.setError("Mobile no is mandatory");
            return false;
        }
        return true;
    }

    @OnClick(R.id.btnContinueNewSr)
    public void onAddNewSr() {
        if (CheckText()) {
            new uploadSRData().execute();
        }
    }

    class uploadSRData extends AsyncTask<Void, Void, Integer> {
        ProgressDialog Dialog = new ProgressDialog(AddNewSrActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Dialog.setTitle("Please Wait");
            Dialog.setMessage("entering data..");
            Dialog.show();
            isInternetOn();
        }

        protected void isInternetOn() {

            if (activeNetwork != null && activeNetwork.isConnected()) {
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
            final HashMap<String, Object> hm = new HashMap<>();
            hm.put("name", name);
            hm.put("srNo", srNo);
            hm.put("doj", doj);
            hm.put("branch", branch);
            hm.put("residence", residence);
            hm.put("mobileNo", mobile);
            hm.put("email", email);
            DocumentReference docRef = firestore
                    .collection("SalesRepresentatives")
                    .document(srNo);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            tvSrNo.requestFocus();
                            tvSrNo.setError("Sr No is already Existing");
                            Dialog.dismiss();
                        } else {
                            srSqliteDb.insertSr(name, srNo);
                            docRef

                                    .set(hm,SetOptions.merge())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            toastMessage("Successfully added new Sr");
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

                        }
                    }

                    else {

                        Dialog.dismiss();
                        Log.d("TAG", "Failed with: ", task.getException());
                    }
                }
            });
            return 0;
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
