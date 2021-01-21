package in.gen2.policymanager.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;


import in.gen2.policymanager.R;
import in.gen2.policymanager.models.CommissionData;

public class CommissionAdapter extends FirestoreRecyclerAdapter<CommissionData,CommissionAdapter.ViewHolder> {
    private Context mContext;

    public CommissionAdapter(@NonNull FirestoreRecyclerOptions<CommissionData> options) {
        super(options);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull CommissionData commissionData) {
        String applicationId=commissionData.getApplicationId();
        String commissionAmount=commissionData.getCommission();
        Log.d("TAG", "onBindViewHolder: "+applicationId+", "+commissionAmount);
        if(applicationId != null){
            holder.policyId.setText("("+applicationId+")");
        }
        else{
            holder.policyId.setText("");
        }
        holder.applicantName.setText(commissionData.getApplicantName());
        if(commissionAmount!=null){
            holder.commission.setText("₹ "+commissionAmount);
        }
        else {
            holder.commission.setText("");
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.commission_list,parent, false);
        mContext = parent.getContext();
        return new ViewHolder(v);
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView policyId,applicantName;
        private TextView commission;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            policyId=itemView.findViewById(R.id.tvApplicationId);
            applicantName=itemView.findViewById(R.id.tvApplicantName);
            commission=itemView.findViewById(R.id.tvPolicyCommission);
        }
    }
}
