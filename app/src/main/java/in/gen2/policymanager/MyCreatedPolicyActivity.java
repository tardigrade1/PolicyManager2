package in.gen2.policymanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import in.gen2.policymanager.adapters.PolicyAdapter;
import in.gen2.policymanager.models.PoliciesFormData;

import static java.security.AccessController.getContext;

public class MyCreatedPolicyActivity extends AppCompatActivity {

    Unbinder unbinder;
    @BindView(R.id.rvPolicies)
    RecyclerView rvPolicies;
    @BindView(R.id.search_policies)
    EditText searchPolicies;
    private PolicyAdapter policyAdapter;
    private List<PoliciesFormData> mPolicies;
    private FirebaseFirestore fireRef;
    private FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_created_policy);
        unbinder = ButterKnife.bind(this);

        mPolicies = new ArrayList<>();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();

        fireRef = FirebaseFirestore.getInstance();
        fireRef.setFirestoreSettings(settings);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        listenForUsers();
        new LoadFirebaseData().execute();
        searchPolicies.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                searchUsers(charSequence.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }


    //    progress dialog before laoding data
    private class LoadFirebaseData extends AsyncTask<Void, Void, Integer> {
        ProgressDialog Dialog = new ProgressDialog(MyCreatedPolicyActivity.this);

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
            mPolicies.clear();
            fireRef.collection("usersFirebaseId")
                    .document(firebaseUser.getUid())
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        final DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            String srNo = document.getString("srNo");
                            Log.d("Tag", "firebaseId is " + srNo);
                            fireRef.collection("SalesRepresentatives").document(srNo)
                                    .collection("PolicyForms")
                                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    for (QueryDocumentSnapshot documents : task.getResult()) {
                                        rvPolicies.setHasFixedSize(true);
                                        rvPolicies.setLayoutManager(new LinearLayoutManager(MyCreatedPolicyActivity.this));
                                        fireRef.collection("SalesRepresentatives")
                                                .document(srNo)
                                                .collection("PolicyForms")
                                                .document(documents.getId())
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        DocumentSnapshot appFormDocument = task.getResult();
                                                        if (appFormDocument != null && appFormDocument.exists()) {
                                                            PoliciesFormData formData = appFormDocument.toObject(PoliciesFormData.class);
                                                            mPolicies.add(formData);
                                                            Log.d("TAG", "onComplete: " + formData.getApplicantName());
                                                        }
                                                        policyAdapter = new PolicyAdapter(MyCreatedPolicyActivity.this, mPolicies);
                                                        rvPolicies.setAdapter(policyAdapter);
                                                        Dialog.dismiss();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {

                                                    }
                                                });
                                    }


                                }
                            });
//
                        }
                    }

                }
            });
            return 0;
        }

        public void listenForUsers() {

            fireRef.collection("SalesRepresentatives")
                    .whereEqualTo("srNo", "L0003689E")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override

                        public void onEvent(@Nullable QuerySnapshot snapshots,
                                            @Nullable FirebaseFirestoreException e) {
                            List<String> cities = new ArrayList<>();
                            for (QueryDocumentSnapshot doc : snapshots) {
                                if (doc.get("name") != null) {
                                    cities.add(doc.getString("name"));
                                }
                            }
                            Log.d("Snapshots", "Current user name: " + cities);
                        }
                    });

            // [END listen_for_users]

        }
    }
}
