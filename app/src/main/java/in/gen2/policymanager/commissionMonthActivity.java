package in.gen2.policymanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import in.gen2.policymanager.adapters.CommissionMonthsAdapter;
import in.gen2.policymanager.models.CommissionMonthData;

public class commissionMonthActivity extends AppCompatActivity {
    Unbinder unbinder;
    @BindView(R.id.rvCommissionMonth)
    RecyclerView rvCommission;
    @BindView(R.id.lvMyEmptyCommissionList)
    LinearLayout lvMyEmptyCommissionList;
    @BindView(R.id.pbLoadMyCommission)
    ProgressBar pbLoadMyCommission;
    private FirebaseFirestore fireRef;
    private CommissionMonthsAdapter adapter;
    private String srNo;
    private SharedPreferences prefs = null;
    private FirestoreRecyclerOptions<CommissionMonthData> options;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commission_month);
        unbinder = ButterKnife.bind(this);
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        fireRef = FirebaseFirestore.getInstance();
        fireRef.setFirestoreSettings(settings);
        prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        srNo = prefs.getString("srNo", "");

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
        Query query=  fireRef.collection("Commissions")
                .document(srNo)
                .collection("months").orderBy("monthId", Query.Direction.DESCENDING);

        options = new FirestoreRecyclerOptions.Builder<CommissionMonthData>().setQuery(query, CommissionMonthData.class).build();
        adapter = new CommissionMonthsAdapter(options)
        {
            @Override
            public void onDataChanged() {

                if(getItemCount()!=0){

                    pbLoadMyCommission.setVisibility(View.GONE);
                }
                else
                {
                    pbLoadMyCommission.setVisibility(View.GONE);
                    lvMyEmptyCommissionList.setVisibility(View.VISIBLE);
                }
                super.onDataChanged();
            }
        };
        rvCommission.setHasFixedSize(true);
        rvCommission.setLayoutManager(new LinearLayoutManager(commissionMonthActivity.this));
        adapter.startListening();
        rvCommission.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return  true;
        }
        return super.onOptionsItemSelected(item);

    }
}
