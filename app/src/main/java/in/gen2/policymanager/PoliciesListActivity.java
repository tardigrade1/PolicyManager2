package in.gen2.policymanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import in.gen2.policymanager.adapters.PolicyAdapter;
import in.gen2.policymanager.models.PoliciesFormData;

public class PoliciesListActivity extends AppCompatActivity {
    Unbinder unbinder;
    @BindView(R.id.rvPoliciesList)
    RecyclerView rvPoliciesList;
    @BindView(R.id.lvEmptyList)
    LinearLayout lvEmptyList;
    @BindView(R.id.pbPolicyLoad)
    ProgressBar pbPolicyLoad;
    @BindView(R.id.search_Emp_policies)
    EditText search_Emp_policies;
    @BindView(R.id.rbOrderByName)
    RadioButton rbOrderByName;
    @BindView(R.id.rbOrderByApplicationNo)
    RadioButton rbOrderByApplicationNo;
    private FirebaseFirestore fireRef;
    private PolicyAdapter adapter;
    private String srNo;
    private FirestoreRecyclerOptions<PoliciesFormData> options;
    private String orderByText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policies_list);
        unbinder = ButterKnife.bind(this);
        srNo = getIntent().getStringExtra("srNo");
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();

        fireRef = FirebaseFirestore.getInstance();
        fireRef.setFirestoreSettings(settings);

        listenForUsers();
        search_Emp_policies.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
//                adapter.getSRFilter().filter(s.toString());
                if (search_Emp_policies.getText().toString().equals("")) {
                    listenForUsers();
                }
                else {
                    searchForUsers(s.toString().toUpperCase());
                }
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

    public void listenForUsers() {
        if (search_Emp_policies.getText().toString().equals("")) {
            Query query = fireRef.collection("SalesRepresentatives").document(srNo).collection("ApplicationForms");

            options = new FirestoreRecyclerOptions.Builder<PoliciesFormData>().setQuery(query, PoliciesFormData.class).build();
            adapter = new PolicyAdapter(options) {
                @Override
                public void onDataChanged() {
                    if (getItemCount() != 0) {
                        pbPolicyLoad.setVisibility(View.GONE);
                        lvEmptyList.setVisibility(View.GONE);
                    } else {
                        pbPolicyLoad.setVisibility(View.GONE);
                        lvEmptyList.setVisibility(View.VISIBLE);
                    }
                    super.onDataChanged();
                }
            };
            rvPoliciesList.setHasFixedSize(true);
            rvPoliciesList.setLayoutManager(new LinearLayoutManager(PoliciesListActivity.this));
            adapter.startListening();
            rvPoliciesList.setAdapter(adapter);

            // [END listen_for_users]
        }
    }

    public void searchForUsers(String s) {
        Query query = null;
        if(rbOrderByName.isChecked()){
            query = fireRef.collection("SalesRepresentatives").document(srNo).collection("ApplicationForms").orderBy("applicantName").startAt(s).endAt(s+"\uf8ff");

        }
        else if(rbOrderByApplicationNo.isChecked()){
          query = fireRef.collection("SalesRepresentatives").document(srNo).collection("ApplicationForms").orderBy("applicationNo").startAt(s).endAt(s+"\uf8ff");

        }
        if(query != null){
        options = new FirestoreRecyclerOptions.Builder<PoliciesFormData>().setQuery(query, PoliciesFormData.class).build();
        adapter = new PolicyAdapter(options) {
            @Override
            public void onDataChanged() {
                if (getItemCount() != 0) {
                    pbPolicyLoad.setVisibility(View.GONE);
                    lvEmptyList.setVisibility(View.GONE);
                } else {
                    pbPolicyLoad.setVisibility(View.GONE);
                    lvEmptyList.setVisibility(View.VISIBLE);
                }
                super.onDataChanged();
            }
        };
        rvPoliciesList.setHasFixedSize(true);
        rvPoliciesList.setLayoutManager(new LinearLayoutManager(PoliciesListActivity.this));
        adapter.startListening();
        rvPoliciesList.setAdapter(adapter);

        // [END listen_for_users]
        }
    }
}
