package in.gen2.policymanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import in.gen2.policymanager.R;
import in.gen2.policymanager.models.FaqsDataModel;

public class FaqsAdapter  extends BaseAdapter {

    ArrayList<FaqsDataModel> faqList = new ArrayList<FaqsDataModel>();
    LayoutInflater minflate;

    public FaqsAdapter(ArrayList<FaqsDataModel> faqList, Context context){
        this.faqList=faqList;
        this.minflate=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return faqList.size();
    }

    @Override
    public Object getItem(int position) {
        return faqList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        FaqsDataModel objDataModel = (FaqsDataModel) getItem(position);
        if (convertView == null) {
            convertView = minflate.inflate(R.layout.faq_list, null);
            vh = new ViewHolder();
            vh.ques = (TextView) convertView.findViewById(R.id.tvFaqQues);
            vh.ans = (TextView) convertView.findViewById(R.id.tvFaqAns);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.ques.setText(objDataModel.getQues().toString());
        vh.ans.setText(objDataModel.getAns().toString());

        return convertView;
    }
    public class ViewHolder{
        TextView ques;
        TextView ans;
    }
}
