package in.gen2.policymanager.models;

import androidx.annotation.Keep;

@Keep
public class FaqsDataModel {
    private String ques=null;
    private String ans=null;

    public String getQues() {
        return ques;
    }

    public void setQues(String ques) {
        this.ques = ques;
    }

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }
}
