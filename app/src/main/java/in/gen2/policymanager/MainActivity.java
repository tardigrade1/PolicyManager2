package in.gen2.policymanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import in.gen2.policymanager.Helpers.PolicyListSqliteData;
import in.gen2.policymanager.Helpers.SRSqliteData;
import in.gen2.policymanager.adminActivities.SrsCommissionActivity;
import in.gen2.policymanager.authActivities.PhoneAuthActivity;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.SEND_SMS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {
    public static final int RequestPermissionCode = 7;

    private SharedPreferences prefs = null;
    private Unbinder unbinder;
    @BindView(R.id.tvAdminName)
    TextView tvAdminName;
    @BindView(R.id.tvAdminId)
    TextView tvAdminId;
    @BindView(R.id.tvAdminBranch)
    TextView tvAdminBranch;
    @BindView(R.id.tvAdminEmail)
    TextView tvAdminEmail;
    @BindView(R.id.tvAdminContact)
    TextView tvAdminContact;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    @BindView(R.id.linearLayout2)
    LinearLayout linearLayout2;
    @BindView(R.id.linearLayout4)
    LinearLayout linearLayout4;
    @BindView(R.id.viewApplication)
    View viewApplication;
    @BindView(R.id.viewcommission)
    View viewCommission;
    @BindView(R.id.viewManageData)
    View viewManageData;
    @BindView(R.id.constActive)
    ConstraintLayout activeScreen;
    @BindView(R.id.constSuspend)
    ConstraintLayout suspendScreen;
    private FirebaseFirestore fireRef;


//    @BindView(R.id.policyCount)
//    TextView policyCount;
    @BindView(R.id.tvAdminDoj)
    TextView tvAdminDoj;
    @BindView(R.id.tvAdminResidence)
    TextView tvAdminResidence;
    private String srNoText;
PolicyListSqliteData policySQLiteDb;
SRSqliteData salesSQLdb;

    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        Boolean supervisor = prefs.getBoolean("supervisor", false);
//        if(active){
//            suspendScreen.setVisibility(View.GONE);
//            activeScreen.setVisibility(View.VISIBLE);
//        }
//        else{
//            activeScreen.setVisibility(View.GONE);
//            suspendScreen.setVisibility(View.VISIBLE);
//        }
        if(supervisor){
            viewApplication.setVisibility(View.GONE);
            viewCommission.setVisibility(View.GONE);
            viewManageData.setVisibility(View.GONE);
            linearLayout.setVisibility(View.GONE);
            linearLayout2.setVisibility(View.GONE);
            linearLayout4.setVisibility(View.GONE);
        }
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        fireRef = FirebaseFirestore.getInstance();
        fireRef.setFirestoreSettings(settings);
//        activeStatus();
        tvAdminName.setText(nameText);
        tvAdminId.setText(srNoText);
        tvAdminBranch.setText(branchText);
        tvAdminEmail.setText(emailText);
        tvAdminResidence.setText(residenceText);
        tvAdminDoj.setText(dojText);
        tvAdminContact.setText("+91-"+contactText);
        policySQLiteDb=new PolicyListSqliteData(this);
        salesSQLdb=new SRSqliteData(this);
        if (!CheckingPermissionIsEnabledOrNot()) {
            RequestMultiplePermission();
        }
    }
    private void logoutUser() {
        policySQLiteDb.deleteTable();
        salesSQLdb.deleteSrTable();
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(MainActivity.this, WelcomeInformationActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    public void myPolicies(View view) {
        Intent i = new Intent(MainActivity.this, MyCreatedPolicyActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }
    //Permission function starts from here
    private void RequestMultiplePermission() {
        // Creating String Array with Permissions.
        ActivityCompat.requestPermissions(MainActivity.this, new String[]
                {

                        WRITE_EXTERNAL_STORAGE,
                        READ_EXTERNAL_STORAGE
                }, RequestPermissionCode);

    }

    // Calling override method.
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case RequestPermissionCode:

                if (grantResults.length > 0) {


                    boolean WriteExternalStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadExternalStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (WriteExternalStorage && ReadExternalStorage) {

                        Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();

                    }
                }

                break;
        }
    }

    // Checking permission is enabled or not using function starts from here.
    public boolean CheckingPermissionIsEnabledOrNot() {
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ThirdPermissionResult == PackageManager.PERMISSION_GRANTED;
    }

    public void manageData(View view) {
        Intent i = new Intent(MainActivity.this, NewByodFormActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    public void salesRepsData(View view) {
        Intent i = new Intent(MainActivity.this, SalesRepsActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
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

    public void MyCommission(View view) {
        Intent i = new Intent(MainActivity.this, SrsCommissionActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);

    }

    public void knowMore(View view) {
        Intent i = new Intent(MainActivity.this, MoreActivity.class);
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
                            activeScreen.setVisibility(View.VISIBLE);
                            suspendScreen.setVisibility(View.GONE);
                        }
                        else{
                            editor.putBoolean("active", false);
                            editor.commit();
                            activeScreen.setVisibility(View.GONE);
                            suspendScreen.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });
    }
}
