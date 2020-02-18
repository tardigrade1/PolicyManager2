package in.gen2.policymanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import in.gen2.policymanager.R;
import in.gen2.policymanager.models.CommissionMonthData;

public class CommissionMonthsAdapter extends FirestoreRecyclerAdapter<CommissionMonthData, CommissionMonthsAdapter.ViewHolder> {
    private Context mContext;

    public CommissionMonthsAdapter(@NonNull FirestoreRecyclerOptions<CommissionMonthData> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull CommissionMonthData commissionData) {
        holder.month.setText(commissionData.getMonth());
        holder.commission.setText("₹ "+commissionData.getCommission());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.commission_month_list,parent, false);
        mContext = parent.getContext();
        return new ViewHolder(v);
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView month;
        private TextView commission;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            month=itemView.findViewById(R.id.tvMonth);
            commission=itemView.findViewById(R.id.tvTotalCommission);

        }
    }
}
