package in.gen2.policymanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.SearchView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import in.gen2.policymanager.Helpers.SRSqliteData;
import in.gen2.policymanager.adapters.PolicyAdapter;
import in.gen2.policymanager.adapters.SalesRepresentativesAdapter;
import in.gen2.policymanager.models.EmpData;
import in.gen2.policymanager.models.PoliciesFormData;
import in.gen2.policymanager.models.SalesRepsDatamodel;


public class SalesRepsActivity extends AppCompatActivity {
    Unbinder unbinder;

    @BindView(R.id.rvRepresentativesList)
    ListView rvRepresentativesList;
    //    @BindView(R.id.searchSR)
//    SearchView searchSr;
    @BindView(R.id.edtFilterEmployee)
    EditText edtFilter;
    @BindView(R.id.addNewSr)
    FloatingActionButton addNewSr;
    private ShimmerFrameLayout mShimmerViewContainer;
    private SQLiteDatabase database = null;
    SRSqliteData srSqliteDb;
    private FirebaseFirestore fireRef;
    private SalesRepresentativesAdapter adapter;
    private ArrayList<SalesRepsDatamodel> srList;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_reps);
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        unbinder = ButterKnife.bind(this);
        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        Boolean supervisor = prefs.getBoolean("supervisor", false);
        if(supervisor){
            addNewSr.setVisibility(View.GONE);
        }
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        srSqliteDb = new SRSqliteData(this);

        fireRef = FirebaseFirestore.getInstance();
        fireRef.setFirestoreSettings(settings);
//        new LoadFireStoreData().execute();
        if (!srSqliteDb.doesDatabaseExist(this)) {
            new LoadFireStoreData().execute();
        }else if(!srSqliteDb.isTableExists()){
            new LoadFireStoreData().execute();
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
                new LoadFireStoreData().execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void searchUsers() {

        srList = new ArrayList<>(srSqliteDb.getSrNames());
        adapter = new SalesRepresentativesAdapter(srList,getApplicationContext());
        rvRepresentativesList.setAdapter(adapter);
        rvRepresentativesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                SalesRepsDatamodel dataModel= srList.get(position);
                Intent intentPolicyDetail = new Intent(SalesRepsActivity.this, ViewSrDetailsActivity.class);
                intentPolicyDetail.putExtra("srNo", dataModel.getSrNo());
                intentPolicyDetail.putExtra("srName", dataModel.getName());
                intentPolicyDetail.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentPolicyDetail);
            }

        });
        mShimmerViewContainer.stopShimmer();
        mShimmerViewContainer.setVisibility(View.GONE);
        rvRepresentativesList.setVisibility(View.VISIBLE);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                // stop animating Shimmer and hide the layout
//                mShimmerViewContainer.stopShimmer();
//                mShimmerViewContainer.setVisibility(View.GONE);
//                rvRepresentativesList.setVisibility(View.VISIBLE);
//            }
//        }, 2000);
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

    public void addNewSr(View view) {
        Intent i = new Intent(SalesRepsActivity.this, AddNewSrActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }


    class LoadFireStoreData extends AsyncTask<Void, Void, Integer> {
        ProgressDialog Dialog = new ProgressDialog(SalesRepsActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mShimmerViewContainer.startShimmer();
            mShimmerViewContainer.setVisibility(View.VISIBLE);
            rvRepresentativesList.setVisibility(View.GONE);
            Dialog.setTitle("Please Wait");
            Dialog.setMessage("data is on update..");
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
            Query query=fireRef.collection("SalesRepresentatives")
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

}
