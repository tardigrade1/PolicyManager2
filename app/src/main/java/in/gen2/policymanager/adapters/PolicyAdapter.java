package in.gen2.policymanager.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.ObservableSnapshotArray;

import java.util.ArrayList;

import in.gen2.policymanager.PolicyDetailsActivity;
import in.gen2.policymanager.R;
import in.gen2.policymanager.models.PoliciesFormData;

public class PolicyAdapter extends FirestoreRecyclerAdapter<PoliciesFormData, PolicyAdapter.ViewHolder> {
    private ArrayList<PoliciesFormData> originalList;
    private Context mContext;
//    private PolicyDataFilter filter;
    private FirestoreRecyclerOptions<PoliciesFormData> mOptions;
    private ObservableSnapshotArray<PoliciesFormData> dataSet;

    public PolicyAdapter(FirestoreRecyclerOptions<PoliciesFormData> options) {
        super(options);
        mOptions=options;
        this.originalList = new ArrayList();
        dataSet=mOptions.getSnapshots();
        this.originalList.addAll(mOptions.getSnapshots());
        if (mOptions.getOwner() != null) {

            mOptions.getOwner().getLifecycle().addObserver(this);
        }
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, int i, PoliciesFormData policiesFormData) {


        Log.d("TAG", "policy data is"+policiesFormData.getApplicationNo()+","+policiesFormData.getApplicantName());
        holder.applicaitonId.setText(policiesFormData.getApplicationNo());
        holder.applicantName.setText(policiesFormData.getApplicantName());
        holder.applyDate.setText(policiesFormData.getPurchaseDate());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPolicyDetail = new Intent(mContext, PolicyDetailsActivity.class);
                intentPolicyDetail.putExtra("srNo", policiesFormData.getSrNo());
                intentPolicyDetail.putExtra("applicationId", policiesFormData.getApplicationNo());
                intentPolicyDetail.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(intentPolicyDetail);
            }
        });
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.policies_list,parent, false);
        mContext = parent.getContext();


        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView applicaitonId;
        private TextView applicantName;
        private  TextView applyDate;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            applicaitonId=itemView.findViewById(R.id.tvPolicyApplicationId);
            applicantName=itemView.findViewById(R.id.tvPolicyApplicantName);
            applyDate=itemView.findViewById(R.id.tvPolicyPurchaseDate);
        }
    }

}
