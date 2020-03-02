package in.gen2.policymanager.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import in.gen2.policymanager.CommissionsActivity;
import in.gen2.policymanager.R;
import in.gen2.policymanager.models.CommissionMonthData;

public class CommissionMonthsAdapter extends FirestoreRecyclerAdapter<CommissionMonthData, CommissionMonthsAdapter.ViewHolder> {
    private Context mContext;

    public CommissionMonthsAdapter(@NonNull FirestoreRecyclerOptions<CommissionMonthData> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull CommissionMonthData commissionData) {
        holder.month.setText(commissionData.getMonthName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPolicyDetail = new Intent(mContext, CommissionsActivity.class);
                intentPolicyDetail.putExtra("monthId", commissionData.getMonthId());
                intentPolicyDetail.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(intentPolicyDetail);
            }
        });
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

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            month=itemView.findViewById(R.id.tvMonth);


        }
    }
}
