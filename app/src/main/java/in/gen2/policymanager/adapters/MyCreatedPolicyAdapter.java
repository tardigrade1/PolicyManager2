package in.gen2.policymanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import in.gen2.policymanager.R;
import in.gen2.policymanager.models.PoliciesFormData;

public class MyCreatedPolicyAdapter extends ArrayAdapter<PoliciesFormData> {
    private static final String TAG = "ProfileAdapter";
    private Context mContext;
    private ArrayList<PoliciesFormData> dataSet;
    private ArrayList<PoliciesFormData> originalList;
    UserDataFilter filter;

    public MyCreatedPolicyAdapter(ArrayList<PoliciesFormData> items, Context context) {
        super(context, R.layout.my_create_policies_list, items);
        this.dataSet = items;
        this.mContext = context;
        this.originalList = new ArrayList();
        this.originalList.addAll(items);
    }

    public class ViewHolder {

        private TextView applicaitonId;
        private TextView applicantName;
        private  TextView applyDate;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.my_create_policies_list, null);
            viewHolder.applicaitonId = convertView.findViewById(R.id.tvPolicyApplicationId);
            viewHolder.applicantName = convertView.findViewById(R.id.tvPolicyApplicantName);
            viewHolder.applyDate = convertView.findViewById(R.id.tvPolicyPurchaseDate);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final  PoliciesFormData rowItem = dataSet.get(position);

        viewHolder.applicaitonId.setText(rowItem.getApplicationNo());
        viewHolder.applicantName.setText(rowItem.getApplicantName());
        viewHolder.applyDate.setText(rowItem.getPurchaseDate());
        // Return the completed view to render on screen
        return convertView;
    }
    @NonNull
    public Filter getSRFilter() {
        if (this.filter == null) {
            this.filter = new UserDataFilter(this, null);
        }
        return this.filter;
    }

    private class UserDataFilter extends Filter {

        public UserDataFilter(MyCreatedPolicyAdapter customAdapter, Object o) {

        }

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            charSequence = charSequence.toString().toLowerCase();
            FilterResults filterResults = new FilterResults();
            if (charSequence.toString().length() > 0) {
                ArrayList arrayList = new ArrayList();
                int size = originalList.size();
                for (int i = 0; i < size; i++) {
                    PoliciesFormData UserData =  originalList.get(i);
                    if (UserData.getApplicantName().toLowerCase().contains(charSequence)||UserData.getApplicationNo().toLowerCase().contains(charSequence)) {
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
                MyCreatedPolicyAdapter CustomAdapter = MyCreatedPolicyAdapter.this;
                CustomAdapter.add(CustomAdapter.dataSet.get(i));
            }
            notifyDataSetInvalidated();
        }
    }



}
