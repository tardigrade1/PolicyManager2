package in.gen2.policymanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import in.gen2.policymanager.Helpers.DetectConnection;
import in.gen2.policymanager.Helpers.PolicyListSqliteData;
import in.gen2.policymanager.authActivities.PhoneAuthActivity;

public class SrDashboardActivity extends AppCompatActivity {

    private SharedPreferences prefs = null;
    private Unbinder unbinder;
    @BindView(R.id.tvSrName)
    TextView srName;
    @BindView(R.id.tvSrId)
    TextView srId;
    @BindView(R.id.tvSrBranch)
    TextView srBranch;
    @BindView(R.id.tvSrEmail)
    TextView srEmail;
    @BindView(R.id.tvSrContact)
    TextView srContact;
    @BindView(R.id.policyCount)
    TextView policyCount;
    @BindView(R.id.tvdoj)
    TextView srDoj;
    @BindView(R.id.tvResidence)
    TextView srResidence;
    @BindView(R.id.srActiveScreen)
    ConstraintLayout srActiveScreen;
    @BindView(R.id.srSuspendScreen)
    ConstraintLayout srSuspendScreen;
    private FirebaseFirestore fireRef;
    private String srNoText;
    private PolicyListSqliteData policySQLiteDb;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sr_dashboard);
        unbinder = ButterKnife.bind(this);
        prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        editor = prefs.edit();

        String nameText = prefs.getString("name", "");
        srNoText = prefs.getString("srNo", "");
        String branchText = prefs.getString("branch", "");
        String emailText = prefs.getString("email", "");
        String contactText = prefs.getString("contactNo", "");
        String dojText = prefs.getString("doj", "");
        String residenceText = prefs.getString("residence", "");
        srName.setText(nameText);
        srId.setText(srNoText);
        srBranch.setText(branchText);
        srEmail.setText(emailText);
        srResidence.setText(residenceText);
        srDoj.setText(dojText);
        srContact.setText("+91-" + contactText);
        policySQLiteDb = new PolicyListSqliteData(this);
//        if(active){
//            srSuspendScreen.setVisibility(View.GONE);
//            srActiveScreen.setVisibility(View.VISIBLE);
//        }
//        else{
//            srActiveScreen.setVisibility(View.GONE);
//            srSuspendScreen.setVisibility(View.VISIBLE);
//        }
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        fireRef = FirebaseFirestore.getInstance();
        fireRef.setFirestoreSettings(settings);
//        activeStatus();
        totalPolicyCount();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_logout:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Logout Alert!");
                alertDialogBuilder.setMessage("Are you sure, You wanted to logout!");
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                                logoutUser();
                            }
                        });

                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void myPolicies(View view) {
        Intent i = new Intent(SrDashboardActivity.this, MyCreatedPolicyActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    public void manageCommission(View view) {

        Intent i = new Intent(SrDashboardActivity.this, commissionMonthActivity.class);
        i.putExtra("srNo",srNoText);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);

    }

    private void logoutUser() {

        policySQLiteDb.deleteTable();
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(SrDashboardActivity.this, WelcomeInformationActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();

    }

    private void totalPolicyCount() {
        Query query = fireRef.collection("SalesRepresentatives")
                .document(srNoText)
                .collection("ApplicationForms");

        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count = task.getResult().size();
                            policyCount.setText(String.valueOf(count));

                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());

                        }
                    }
                });
    }

    public void knowMore(View view) {
        Intent i = new Intent(SrDashboardActivity.this, MoreActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }
    private void activeStatus(){

        DocumentReference docRef = fireRef.collection("SalesRepresentatives")
                .document(srNoText);

        docRef.collection("SalesRepresentatives").document(srNoText).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        Boolean activeStatus = document.getBoolean("active");
                        if (activeStatus) {
                            editor.putBoolean("active", true);
                            editor.commit();
                            srActiveScreen.setVisibility(View.VISIBLE);
                            srSuspendScreen.setVisibility(View.GONE);
                        }
                        else{
                            editor.putBoolean("active", false);
                            editor.commit();
                            srActiveScreen.setVisibility(View.GONE);
                            srSuspendScreen.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });
    }

    public void openByodForm(View view) {

        if (!DetectConnection.checkInternetConnection(this)) {
            Toast.makeText(getApplicationContext(), "No Internet!", Toast.LENGTH_SHORT).show();
        } else {
            Intent i = new Intent(SrDashboardActivity.this, WebActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra("url", "https://docs.google.com/forms/d/e/1FAIpQLSdeR9hVT_6591HAeJxdUrRrTJKkDHdrq0PE8NPIl3nfBxHEcg/viewform");
            startActivity(i);
        }
    }
}
