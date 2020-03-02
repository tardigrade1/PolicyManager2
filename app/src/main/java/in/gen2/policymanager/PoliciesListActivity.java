package in.gen2.policymanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

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
    private FirebaseFirestore fireRef;
    private PolicyAdapter adapter;
    private String srNo;
    private FirestoreRecyclerOptions<PoliciesFormData> options;
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
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
    public void listenForUsers() {

        Query query =fireRef.collection("SalesRepresentatives").document(srNo).collection("ApplicationForms");

        options = new FirestoreRecyclerOptions.Builder<PoliciesFormData>().setQuery(query, PoliciesFormData.class).build();
        adapter = new PolicyAdapter(options){
            @Override
            public void onDataChanged() {
                if(getItemCount()!=0){
                    pbPolicyLoad.setVisibility(View.GONE);
                }
                else
                {
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
