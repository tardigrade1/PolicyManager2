package in.gen2.policymanager.authActivities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import in.gen2.policymanager.R;

public class PhoneAuthActivity extends AppCompatActivity {

    //    firestore
    FirebaseFirestore docRef;
    private EditText etSrNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);
        etSrNo = findViewById(R.id.etSrNo);
        final Button btnContinue = findViewById(R.id.btnContinue);
        docRef = FirebaseFirestore.getInstance();
        etSrNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 1) {
                    btnContinue.setEnabled(true);
                    btnContinue.setTextColor(getResources().getColor(R.color.colorAccent));
                } else {
                    btnContinue.setEnabled(false);
                    btnContinue.setTextColor(getResources().getColor(R.color.btn_disable));
                }
            }
        });
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LoadFirebaseData().execute();

            }
        });
    }

    //    progress dialog before laoding data
    private class LoadFirebaseData extends AsyncTask<Void, Void, Integer> {
        ProgressDialog Dialog = new ProgressDialog(PhoneAuthActivity.this);
        String srNo = etSrNo.getText().toString().trim();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Dialog.setTitle("Please Wait");
            Dialog.setMessage("data is loading..");
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
            docRef.collection("SalesRepresentatives").document(srNo).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            String ContactNo = document.getString("mobileNo");
                            Intent intentSignUp = new Intent(PhoneAuthActivity.this, VerifyPhoneActivity.class);
                            intentSignUp.putExtra("mobile", ContactNo);
                            intentSignUp.putExtra("srNo", srNo);
                            intentSignUp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intentSignUp);
                            Toast.makeText(PhoneAuthActivity.this, "OTP sent", Toast.LENGTH_SHORT).show();
                            Log.d("TAG", ContactNo); //Print the name
                            Dialog.dismiss();
                        } else {
                            Log.d("TAG", "No such Employee Exist");
                            Dialog.dismiss();
                        }
                    } else {
                        Log.d("TAG", "get failed with ", task.getException());
                    }
                }
            });
//            myDataRef.orderByChild("mobile").equalTo(phonenumber).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    if (dataSnapshot.exists()) {
//                        Intent intentSignUp = new Intent(PhoneNumberAuthActivity.this, VerifyPhoneActivity.class);
//                        intentSignUp.putExtra("mobile", phonenumber);
//                        intentSignUp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intentSignUp);
//                        Toast.makeText(PhoneNumberAuthActivity.this, "OTP sent", Toast.LENGTH_SHORT).show();
//
//                    }
//                    else {
//                        Intent intentSignUp = new Intent(PhoneNumberAuthActivity.this, SignUpActivity.class);
//                        intentSignUp.putExtra("mobile", phonenumber);
//                        intentSignUp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intentSignUp);
//                        Dialog.dismiss();
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
            return 0;
        }
    }
}
