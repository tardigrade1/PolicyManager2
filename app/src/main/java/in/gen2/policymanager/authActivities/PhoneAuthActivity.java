package in.gen2.policymanager.authActivities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import in.gen2.policymanager.MainActivity;
import in.gen2.policymanager.MyCreatedPolicyActivity;
import in.gen2.policymanager.R;
import in.gen2.policymanager.SrDashboardActivity;

public class PhoneAuthActivity extends AppCompatActivity {
    //    private FirebaseAuth mAuth;
//    private FirebaseAuth.AuthStateListener mAuthListener;
    //    firestore
    FirebaseFirestore docRef;
    private EditText etSrNo;
    private TextView tvWelcomeText, tvWelcomeName;
    private SharedPreferences prefs = null;
    private Boolean admin, supervisor;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);
//        mAuth = FirebaseAuth.getInstance();
        prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        editor = prefs.edit();
        admin = prefs.getBoolean("admin", false);
        supervisor = prefs.getBoolean("supervisor", false);
        String name = prefs.getString("name", "");
        String srNumber = prefs.getString("srNo", "");
        etSrNo = findViewById(R.id.etSrNo);
        tvWelcomeText = findViewById(R.id.welcomeText);
        tvWelcomeName = findViewById(R.id.welcomeName);

        if (!name.equals("")) {
            tvWelcomeText.setText("Welcome back,");
            tvWelcomeName.setText(name);
        }
        if (srNumber != null) {
            etSrNo.setText(srNumber);
        }
        LinearLayout linearProcessBtn = findViewById(R.id.linearProcessBtn);
        docRef = FirebaseFirestore.getInstance();
        etSrNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 1) {
                    linearProcessBtn.setEnabled(true);
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter Sr Number", Toast.LENGTH_SHORT).show();
                    linearProcessBtn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public void BtnToContinue(View view) {
        new LoadFirebaseData().execute();
    }

    public void clickTologinWithAnotherAccount(View view) {
        editor.clear();
        editor.commit();
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

    //    progress dialog before laoding data
    private class LoadFirebaseData extends AsyncTask<Void, Void, Integer> {
        ProgressDialog Dialog = new ProgressDialog(PhoneAuthActivity.this);
        String srNo = etSrNo.getText().toString().trim();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Dialog.setMessage("checking your data...");
            Dialog.setIndeterminate(false);
            Dialog.setCancelable(false);
            Dialog.show();
            isInternetOn();

//            isInternetAvailable(PhoneAuthActivity.this);
        }


//        boolean isInternetAvailable(Context context) {
//            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
//            if (activeNetwork == null) return false;
//
//            switch (activeNetwork.getType()) {
//                case ConnectivityManager.TYPE_WIFI:
//                    if ((activeNetwork.getState() == NetworkInfo.State.CONNECTED ||
//                            activeNetwork.getState() == NetworkInfo.State.CONNECTING) &&
//                            isInternetWorking()){
//                        return true;
//                    }
//
//                    break;
//                case ConnectivityManager.TYPE_MOBILE:
//                    if ((activeNetwork.getState() == NetworkInfo.State.CONNECTED ||
//                            activeNetwork.getState() == NetworkInfo.State.CONNECTING) &&
//                            isInternetWorking()){
//                        return true;
//                    }
//
//                    break;
//                default:
//                    return false;
//            }
//            return false;
//        }


        protected void isInternetOn() {
            ConnectivityManager conn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            assert conn != null;
            NetworkInfo activeNetwork = conn.getActiveNetworkInfo();
            if (activeNetwork != null && activeNetwork.isConnected()) {
//                if(!isInternetWorking()){
//                    Dialog.setMessage("your internet connection is slow");
//                }
            } else {
                Dialog.setMessage("please check your internet connection...");
            }
        }

        // ping the google server to check if internet is really working or not
        boolean isInternetWorking() {
            boolean success = false;
            try {
                URL url = new URL("https://google.com");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(10000);
                connection.connect();
                success = connection.getResponseCode() == 200;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return success;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
        }

        @Override
        protected Integer doInBackground(Void... params) {

            docRef.collection("SalesRepresentatives").document(srNo).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            Boolean activeStatus = document.getBoolean("active");
                            if (activeStatus) {
                                String ContactNo = document.getString("mobileNo");
                                String name = document.getString("name");
                                String email = document.getString("email");
                                String branch = document.getString("branch");
                                String doj = document.getString("doj");
                                String residence = document.getString("residence");
                                if (document.get("admin") != null) {
                                    editor.putString("srNo", srNo);
                                    editor.putString("contactNo", ContactNo);
                                    editor.putString("branch", branch);
                                    editor.putString("email", email);
                                    editor.putString("name", name);
                                    editor.putString("doj", doj);
                                    editor.putString("residence", residence);
                                    editor.putBoolean("admin", true);
                                    editor.putBoolean("supervisor", false);
                                    editor.putBoolean("active", true);
                                    editor.commit();
                                } else if (document.get("supervisor") != null) {
                                    editor.putString("srNo", srNo);
                                    editor.putString("contactNo", ContactNo);
                                    editor.putString("branch", branch);
                                    editor.putString("email", email);
                                    editor.putString("name", name);
                                    editor.putString("doj", doj);
                                    editor.putString("residence", residence);
                                    editor.putBoolean("supervisor", true);
                                    editor.putBoolean("admin", false);
                                    editor.putBoolean("active", true);
                                    editor.commit();
                                } else {
                                    editor.putString("srNo", srNo);
                                    editor.putString("contactNo", ContactNo);
                                    editor.putString("branch", branch);
                                    editor.putString("email", email);
                                    editor.putString("name", name);
                                    editor.putString("doj", doj);
                                    editor.putString("residence", residence);
                                    editor.putBoolean("supervisor", false);
                                    editor.putBoolean("admin", false);
                                    editor.putBoolean("active", true);
                                    editor.commit();
                                }
                                Intent intentSignUp = new Intent(PhoneAuthActivity.this, VerifyPhoneActivity.class);
                                intentSignUp.putExtra("mobile", ContactNo);
                                intentSignUp.putExtra("srNo", srNo);
                                intentSignUp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intentSignUp);
                            } else {
                                etSrNo.setError("This account is suspended!");
                                etSrNo.requestFocus();
                            }
                            Dialog.dismiss();
                        } else {
                            superAdmin(srNo);
                            Dialog.dismiss();
                        }
                    } else {
                        superAdmin(srNo);
                        Dialog.dismiss();
                        Log.d("TAG", "get failed with ", task.getException());
                    }
                }
            });
            return 0;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
//        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
//        if (mAuthListener != null) {
//            mAuth.removeAuthStateListener(mAuthListener);
//        }
    }

    private static boolean isInternet() {

        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
//            Debug.i(exitValue + "");
            return (exitValue == 0);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }

    private void superAdmin(String srNo) {
        docRef.collection("SuperAdmin").document(srNo).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        String ContactNo = document.getString("mobileNo");
                        String name = document.getString("name");
                        String email = document.getString("email");
                        String branch = document.getString("branch");
                        String doj = document.getString("doj");
                        String residence = document.getString("residence");
                        editor.putString("srNo", srNo);
                        editor.putString("contactNo", ContactNo);
                        editor.putString("branch", branch);
                        editor.putString("email", email);
                        editor.putString("name", name);
                        editor.putString("doj", doj);
                        editor.putString("residence", residence);
                        editor.putBoolean("admin", true);
                        editor.putBoolean("supervisor", false);
                        editor.putBoolean("active", true);
                        editor.commit();
                        Intent intentSignUp = new Intent(PhoneAuthActivity.this, VerifyPhoneActivity.class);
                        intentSignUp.putExtra("mobile", ContactNo);
                        intentSignUp.putExtra("srNo", srNo);
                        intentSignUp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intentSignUp);
                    } else {
                        etSrNo.setError("Please enter valid Sr No");
                        etSrNo.requestFocus();
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });
    }
}
