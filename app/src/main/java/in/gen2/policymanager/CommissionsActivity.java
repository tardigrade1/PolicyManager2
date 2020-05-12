package in.gen2.policymanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import in.gen2.policymanager.adapters.CommissionAdapter;
import in.gen2.policymanager.models.CommissionData;

public class CommissionsActivity extends AppCompatActivity {
    Unbinder unbinder;
    @BindView(R.id.rvCommission)
    RecyclerView rvCommission;
    @BindView(R.id.lvMyEmptyCommissionList)
    LinearLayout lvMyEmptyCommissionList;
    @BindView(R.id.pbLoadMyCommission)
    ProgressBar pbLoadMyCommission;
    private FirebaseFirestore fireRef;
    private CommissionAdapter adapter;
    private String srNo;
    private SharedPreferences prefs = null;
    private FirestoreRecyclerOptions<CommissionData> options;
    int count = 0;
    private String rupeeIcon;
    private Menu menu;
    private String monthId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commissions);
        unbinder = ButterKnife.bind(this);
        monthId = getIntent().getStringExtra("monthId");
        srNo = getIntent().getStringExtra("srNo");
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        fireRef = FirebaseFirestore.getInstance();
        fireRef.setFirestoreSettings(settings);
        rupeeIcon = getResources().getString(R.string.rupee_icon);
        if (srNo != null) {
            listenForUsers();

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    public void listenForUsers() {

        Query query = fireRef.collection("Commissions").document(srNo).collection("months").document(monthId).collection("ApplicationForms");
        options = new FirestoreRecyclerOptions.Builder<CommissionData>().setQuery(query, CommissionData.class).build();
        adapter = new CommissionAdapter(options) {
            @Override
            public void onDataChanged() {

                if (getItemCount() != 0) {
                    for (CommissionData document : options.getSnapshots()) {
                        String commissionAmmount = document.getCommission();
                        if (commissionAmmount != null) {
                            int amount = Integer.parseInt(document.getCommission());
                            count += amount;
                        }

                    }
                    invalidateOptionsMenu();
                    pbLoadMyCommission.setVisibility(View.GONE);
                } else {
                    pbLoadMyCommission.setVisibility(View.GONE);
                    lvMyEmptyCommissionList.setVisibility(View.VISIBLE);
                }
                super.onDataChanged();
            }
        };
        rvCommission.setHasFixedSize(true);
        rvCommission.setLayoutManager(new LinearLayoutManager(CommissionsActivity.this));
        adapter.startListening();
        rvCommission.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.commission_menu, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_commission_count);
        item.setTitle(rupeeIcon + " " + count);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
}
