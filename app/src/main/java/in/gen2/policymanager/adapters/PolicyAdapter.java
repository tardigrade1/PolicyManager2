package in.gen2.policymanager.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.gen2.policymanager.PolicyDetailsActivity;
import in.gen2.policymanager.R;
import in.gen2.policymanager.authActivities.PhoneAuthActivity;
import in.gen2.policymanager.authActivities.VerifyPhoneActivity;
import in.gen2.policymanager.models.PoliciesFormData;

public class PolicyAdapter extends RecyclerView.Adapter<PolicyAdapter.ViewHolder> {

    private Context mContext;
    private List<PoliciesFormData> mPolicies;



    public PolicyAdapter(Context context, List<PoliciesFormData> mPolicies) {
        this.mContext = context;
        this.mPolicies = mPolicies;

    }


    @NonNull
    @Override
    public PolicyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.policies_list, parent, false);
        return new PolicyAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PolicyAdapter.ViewHolder holder, int position) {

        PoliciesFormData policiesFormData=mPolicies.get(position);
        holder.applicaitonId.setText(policiesFormData.getApplicationNo());
        holder.applicantName.setText(policiesFormData.getApplicantName());
        holder.applyDate.setText(policiesFormData.getPolicyIssueDate());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPolicyDetail = new Intent(mContext, PolicyDetailsActivity.class);
                intentPolicyDetail.putExtra("srNo", policiesFormData.getApplicantSrNo());
                intentPolicyDetail.putExtra("applicationId", policiesFormData.getApplicationNo());
                intentPolicyDetail.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(intentPolicyDetail);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPolicies.size();
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
