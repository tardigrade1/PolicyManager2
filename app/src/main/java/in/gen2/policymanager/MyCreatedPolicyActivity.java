package in.gen2.policymanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
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
import in.gen2.policymanager.Helpers.PolicyListSqliteData;
import in.gen2.policymanager.Helpers.SRSqliteData;
import in.gen2.policymanager.adapters.MyCreatedPolicyAdapter;
import in.gen2.policymanager.adapters.PolicyAdapter;
import in.gen2.policymanager.models.PoliciesFormData;

import static java.security.AccessController.getContext;

public class MyCreatedPolicyActivity extends AppCompatActivity {

    Unbinder unbinder;
    @BindView(R.id.rvPolicies)
    ListView rvPolicies;
    @BindView(R.id.search_policies)
    EditText searchPolicies;
    @BindView(R.id.lvMyEmptyPolicyList)
    LinearLayout lvMyEmptyPolicyList;
    private FirebaseFirestore fireRef;
    private MyCreatedPolicyAdapter adapter;
    private String srNo;
    private SharedPreferences prefs = null;
    private SQLiteDatabase database = null;
    PolicyListSqliteData policySQLiteDb;
    private ArrayList<PoliciesFormData> policyList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_created_policy);
        unbinder = ButterKnife.bind(this);
        policySQLiteDb = new PolicyListSqliteData(this);
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        fireRef = FirebaseFirestore.getInstance();
        fireRef.setFirestoreSettings(settings);
        prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        srNo = prefs.getString("srNo", "");
        if (!policySQLiteDb.doesDatabaseExist(this)) {
            new LoadFireStoreData().execute();
        }
        else if(!policySQLiteDb.isTableExists()) {
            new LoadFireStoreData().execute();
        }
        else {
            showPolicyList();
        }
        searchPolicies.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                adapter.getSRFilter().filter(s.toString());

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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.srlist_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_refresh:
                policySQLiteDb.deleteAll();
                new LoadFireStoreData().execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    class LoadFireStoreData extends AsyncTask<Void, Void, Integer> {
        ProgressDialog Dialog = new ProgressDialog(MyCreatedPolicyActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Dialog.setTitle("Please Wait");
            Dialog.setMessage("loading...");
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
        protected Integer doInBackground(Void... voids) {
            Query query=fireRef.collection("SalesRepresentatives").document(srNo).collection("ApplicationForms")
                    .orderBy("applicantName");

            query.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    String nameText = (String) document.get("applicantName");
                                    String applicationNoText = (String) document.get("applicationNo");
                                    String policyStatusText = (String) document.get("PolicyStatus");

                                    assert applicationNoText != null;
                                    if(applicationNoText.length()>0){
                                        policySQLiteDb.insertPolicies(nameText,srNo,applicationNoText,policyStatusText);
                                    }


                                }
                                Dialog.dismiss();
//
                                showPolicyList();
                            } else {
                                Log.d("TAG", "Error getting documents: ", task.getException());
                                rvPolicies.setVisibility(View.GONE);
                                lvMyEmptyPolicyList.setVisibility(View.VISIBLE);
                                Dialog.dismiss();
                            }
                        }
                    });
            return 0;
        }
    }
    private void showPolicyList() {

        policyList = new ArrayList<>(policySQLiteDb.getApplicantNames());

        for (int i = 0; i < policyList.size(); i++) {

            try {

                PoliciesFormData loc = policyList.get(i);
                Log.d("TAG", "searchUsers: " + loc.getId());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        adapter = new MyCreatedPolicyAdapter(policyList,getApplicationContext());
        rvPolicies.setAdapter(adapter);
        rvPolicies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                PoliciesFormData dataModel= policyList.get(position);
                Intent intentPolicyDetail = new Intent(MyCreatedPolicyActivity.this, PolicyDetailsActivity.class);
                intentPolicyDetail.putExtra("srNo", dataModel.getSrNo());
                intentPolicyDetail.putExtra("applicationId", dataModel.getApplicationNo());
                intentPolicyDetail.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentPolicyDetail);
            }

        });

    }
}
