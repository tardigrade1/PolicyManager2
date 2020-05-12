package in.gen2.policymanager.adminActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.ListView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import in.gen2.policymanager.AddNewSrActivity;
import in.gen2.policymanager.CommissionsActivity;
import in.gen2.policymanager.Helpers.SRSqliteData;
import in.gen2.policymanager.R;
import in.gen2.policymanager.SalesRepsActivity;
import in.gen2.policymanager.ViewSrDetailsActivity;
import in.gen2.policymanager.adapters.SalesRepresentativesAdapter;
import in.gen2.policymanager.commissionMonthActivity;
import in.gen2.policymanager.models.SalesRepsDatamodel;

public class SrsCommissionActivity extends AppCompatActivity {
    Unbinder unbinder;

    @BindView(R.id.rvRepresentativesCommissionList)
    ListView rvRepresentativesCommissionList;
    @BindView(R.id.edtFilterEmployee)
    EditText edtFilter;
    private ShimmerFrameLayout mShimmerViewContainer;
    private SQLiteDatabase database = null;
    SRSqliteData srSqliteDb;
    private FirebaseFirestore fireRef;
    private SalesRepresentativesAdapter adapter;
    private ArrayList<SalesRepsDatamodel> srList;
    private Boolean supervisor;
    private String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_srs_commission);
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        unbinder = ButterKnife.bind(this);
        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        userID = prefs.getString("srNo", "");
        Log.d("TAG", "onCreate: "+userID);
        supervisor = prefs.getBoolean("supervisor", false);

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        fireRef = FirebaseFirestore.getInstance();
        fireRef.setFirestoreSettings(settings);
        srSqliteDb = new SRSqliteData(this);

            if (!srSqliteDb.doesDatabaseExist(this)) {
                new LoadFireStoreFullData().execute();
            } else if (!srSqliteDb.isTableExists()) {
                new LoadFireStoreFullData().execute();
            } else {
                searchUsers();
            }

        edtFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getSRFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

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
                srSqliteDb.deleteAll();
                if (supervisor) {
                    new LoadFireStoreLimitedData().execute();
                }
                else {
                    new LoadFireStoreFullData().execute();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void searchUsers() {

        srList = new ArrayList<>(srSqliteDb.getSrNames());
        adapter = new SalesRepresentativesAdapter(srList, getApplicationContext());
        rvRepresentativesCommissionList.setAdapter(adapter);
        rvRepresentativesCommissionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                SalesRepsDatamodel dataModel = srList.get(position);
                Intent intentPolicyDetail = new Intent(SrsCommissionActivity.this, commissionMonthActivity.class);
                intentPolicyDetail.putExtra("srNo", dataModel.getSrNo());
                intentPolicyDetail.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentPolicyDetail);
            }

        });
        mShimmerViewContainer.stopShimmer();
        mShimmerViewContainer.setVisibility(View.GONE);
        rvRepresentativesCommissionList.setVisibility(View.VISIBLE);
    }
//

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    class LoadFireStoreFullData extends AsyncTask<Void, Void, Integer> {
        ProgressDialog Dialog = new ProgressDialog(SrsCommissionActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mShimmerViewContainer.startShimmer();
            mShimmerViewContainer.setVisibility(View.VISIBLE);
            rvRepresentativesCommissionList.setVisibility(View.GONE);
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
            Query query = fireRef.collection("SalesRepresentatives")
                    .orderBy("name");

            query.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    String nameText = (String) document.get("name");
                                    String srNoText = (String) document.get("srNo");
                                    Log.d("TAG", document.getId() + " => " + nameText + " => " + srNoText);
                                    srSqliteDb.insertSr(nameText, srNoText);


                                }
                                Dialog.dismiss();
                                searchUsers();
                            } else {
                                Log.d("TAG", "Error getting documents: ", task.getException());
                                Dialog.dismiss();
                            }
                        }
                    });
            return 0;
        }
    }

    class LoadFireStoreLimitedData extends AsyncTask<Void, Void, Integer> {
        ProgressDialog Dialog = new ProgressDialog(SrsCommissionActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mShimmerViewContainer.startShimmer();
            mShimmerViewContainer.setVisibility(View.VISIBLE);
            rvRepresentativesCommissionList.setVisibility(View.GONE);
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
            Query query = fireRef
                    .collection("SalesRepresentatives")
                    .whereEqualTo("supervisorCode",userID);
//                    .orderBy("name");
            Log.d("TAG", "doInBackground: "+query.toString());
            query.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {


                                    String nameText = (String) document.get("name");
                                    String srNoText = (String) document.get("srNo");
                                    Log.d("TAG", document.getId() + " => " + nameText + " => " + srNoText);
                                    srSqliteDb.insertSr(nameText, srNoText);

                                }
                                Dialog.dismiss();
                                searchUsers();
                            } else {
                                Log.d("TAG", "Error getting documents: ", task.getException());
                                Dialog.dismiss();
                            }
                        }
                    });
            return 0;
        }
    }
}
