package in.gen2.policymanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
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
//    @BindView(R.id.search_policies)
//    EditText searchPolicies;
    @BindView(R.id.lvMyEmptyPolicyList)
    LinearLayout lvMyEmptyPolicyList;
    @BindView(R.id.pbLoadMyPolicy)
    ProgressBar pbLoadMyPolicy;
    private FirebaseFirestore fireRef;
    private PolicyAdapter adapter;
    private String srNo;
    private SharedPreferences prefs = null;
    private FirestoreRecyclerOptions<PoliciesFormData> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_created_policy);
        unbinder = ButterKnife.bind(this);
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        fireRef = FirebaseFirestore.getInstance();
        fireRef.setFirestoreSettings(settings);
        prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        srNo = prefs.getString("srNo", "");
        if (srNo != null && !srNo.equalsIgnoreCase("Admin")) {
            listenForUsers();

        }
//        searchPolicies.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
//                searchUsers(s.toString());
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    public void listenForUsers() {

        Query query = fireRef.collection("SalesRepresentatives").document(srNo).collection("PolicyForms");
        options = new FirestoreRecyclerOptions.Builder<PoliciesFormData>().setQuery(query, PoliciesFormData.class).build();
        adapter = new PolicyAdapter(options){
            @Override
            public void onDataChanged() {
                if(getItemCount()!=0){
                    pbLoadMyPolicy.setVisibility(View.GONE);
                }
                else
                {
                    pbLoadMyPolicy.setVisibility(View.GONE);
                    lvMyEmptyPolicyList.setVisibility(View.VISIBLE);
                }
                super.onDataChanged();
            }
        };
        rvPolicies.setHasFixedSize(true);
        rvPolicies.setLayoutManager(new LinearLayoutManager(MyCreatedPolicyActivity.this));
        adapter.startListening();
        rvPolicies.setAdapter(adapter);
    }
    private void searchUsers(String s) {
        Query  querySearch =fireRef.collection("SalesRepresentatives").document(srNo).collection("PolicyForms").orderBy("applicationNo").startAt(s).endAt(s+"\uf8ff");
        options = new FirestoreRecyclerOptions.Builder<PoliciesFormData>().setQuery(querySearch, PoliciesFormData.class).build();
        adapter = new PolicyAdapter(options);
        rvPolicies.setHasFixedSize(true);
        rvPolicies.setLayoutManager(new LinearLayoutManager(MyCreatedPolicyActivity.this));
        adapter.startListening();
        rvPolicies.setAdapter(adapter);

    }

}
