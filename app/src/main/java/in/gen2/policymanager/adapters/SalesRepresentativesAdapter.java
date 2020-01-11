package in.gen2.policymanager.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import in.gen2.policymanager.PoliciesListActivity;
import in.gen2.policymanager.R;
import in.gen2.policymanager.models.SalesRepsDatamodel;

public class SalesRepresentativesAdapter extends ArrayAdapter<SalesRepsDatamodel> {
    private static final String TAG = "ProfileAdapter";
    private Context mContext;
    private ArrayList<SalesRepsDatamodel> dataSet;
    private ArrayList<SalesRepsDatamodel> originalList;
    UserDataFilter filter;

    public SalesRepresentativesAdapter(ArrayList<SalesRepsDatamodel> items, Context context) {
        super(context, R.layout.sr_list, items);
        this.dataSet = items;
        this.mContext = context;
        this.originalList = new ArrayList();
        this.originalList.addAll(items);

    }
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sr_list, parent, false);
//        mContext = parent.getContext();
//        return new ViewHolder(v);
//    }

//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//
//        SalesRepsDatamodel item=items.get(position);
//        holder.srNo.setBackground(null);
//        holder.srName.setBackground(null);
//        holder.srNo.setText(item.getSrNo());
//        holder.srName.setText(item.getName());
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intentPolicyDetail = new Intent(mContext, PoliciesListActivity.class);
//                intentPolicyDetail.putExtra("srNo", item.getSrNo());
//                intentPolicyDetail.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                mContext.startActivity(intentPolicyDetail);
//            }
//        });
//    }
//
//
//    @Override
//    public int getItemCount() {
//        return items.size();
//    }

    public class ViewHolder {
        private TextView srNo;
        private TextView srName;


//        public ViewHolder(@NonNull View itemView) {
//
//
//            srNo = itemView.findViewById(R.id.tvRepNo);
//            srName = itemView.findViewById(R.id.tvRepName);
//
//        }
    }


//    public Filter getFilter() {
//        return new Filter() {
//            @Override
//            protected FilterResults performFiltering(CharSequence charSequence) {
//                charSequence = charSequence.toString().toLowerCase();
//
//                if (charSequence.toString().length() > 0)  {
//                    List<SalesRepsDatamodel> filteredList = new ArrayList<>();
//
//                    for (SalesRepsDatamodel row : serchList) {
//
//                        // name match condition. this might differ depending on your requirement
//                        // here we are looking for name or phone number match
//                        if (row.toString().toLowerCase().contains(charSequence)) {
//                            filteredList.add(row);
//                        }
//                    }
//
//                    items = filteredList;
//                }
//                else {
//                    synchronized (this) {
//                        items=serchList;
//                    }
//                }
//
//                FilterResults filterResults = new FilterResults();
//                filterResults.values = items;
//                return filterResults;
//            }
//
//            @Override
//            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
//                items = (ArrayList) filterResults.values;
//
//                // refresh the list with filtered data
//                notifyDataSetChanged();
//            }
//        };
//    }


    @NonNull
    public Filter getSRFilter() {
        if (this.filter == null) {
            this.filter = new UserDataFilter(this, null);
        }
        return this.filter;
    }

    private class UserDataFilter extends Filter {

        public UserDataFilter(SalesRepresentativesAdapter customAdapter, Object o) {

        }

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            charSequence = charSequence.toString().toLowerCase();
            FilterResults filterResults = new FilterResults();
            if (charSequence.toString().length() > 0) {
                ArrayList arrayList = new ArrayList();
                int size = originalList.size();
                for (int i = 0; i < size; i++) {
                    SalesRepsDatamodel UserData =  originalList.get(i);
                    if (UserData.getName().toLowerCase().contains(charSequence)||UserData.getSrNo().toLowerCase().contains(charSequence)) {
                        arrayList.add(UserData);
                    }
                }
                filterResults.count = arrayList.size();
                filterResults.values = arrayList;
            } else {
                synchronized (this) {
                    filterResults.values = originalList;
                    filterResults.count = originalList.size();
                }
            }
            return filterResults;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            dataSet = (ArrayList) results.values;
           notifyDataSetChanged();
           clear();
            int size = dataSet.size();
            for (int i = 0; i < size; i++) {
                SalesRepresentativesAdapter CustomAdapter = SalesRepresentativesAdapter.this;
                CustomAdapter.add(CustomAdapter.dataSet.get(i));
            }
            notifyDataSetInvalidated();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.sr_list, null);
            viewHolder.srName = convertView.findViewById(R.id.tvRepName);
            viewHolder.srNo = convertView.findViewById(R.id.tvRepNo);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final  SalesRepsDatamodel rowItem = dataSet.get(position);
        viewHolder.srName.setBackground(null);
        viewHolder.srNo.setBackground(null);
        viewHolder.srName.setText(rowItem.getName());
        viewHolder.srNo.setText(rowItem.getSrNo());
        // Return the completed view to render on screen
        return convertView;
    }
}
