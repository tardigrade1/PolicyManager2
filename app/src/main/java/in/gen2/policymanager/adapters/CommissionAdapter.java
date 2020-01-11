package in.gen2.policymanager.adapters;

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

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull CommissionData commissionData) {
        holder.policyId.setText(commissionData.getApplicationNo());
        holder.commission.setText("₹ "+commissionData.getCommission());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.commission_list,parent, false);
        mContext = parent.getContext();
        return new ViewHolder(v);
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView policyId;
        private TextView commission;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            policyId=itemView.findViewById(R.id.tvPolicyId);
            commission=itemView.findViewById(R.id.tvPolicyCommission);

        }
    }
}
