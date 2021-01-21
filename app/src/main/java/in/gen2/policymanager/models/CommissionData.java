package in.gen2.policymanager.models;


import androidx.annotation.Keep;

import com.google.firebase.firestore.Exclude;

@Keep
public class CommissionData {
    private String applicationNo = null;
    private String srNo = null;
    private String name = null;
    private String ApplicantName = null;
    private String decisionDate = null;
    private String ApplicationId = null;
    private String Commission = null;
    private String decisionMonth = null;
    public CommissionData() {

    }

    public String getApplicantName() {
        return ApplicantName;
    }
    public String getDecisionMonth() {
        return decisionMonth;
    }
    public String getCommission() {
        return Commission;
    }
    public void setCommission(String commission) {
        Commission = commission;
    }
    public String getApplicationId() {
        return ApplicationId;
    }
    public String getApplicationNo() {
        return applicationNo;
    }
    public String getSrNo() {
        return srNo;
    }
    public void setSrNo(String srNo) {
        this.srNo = srNo;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDecisionDate() {
        return decisionDate;
    }
    public CommissionData(String srNo, String applicationNo, String name, String decisionDate, String commission, String decisionMonth) {
        this.applicationNo = applicationNo;
        this.srNo = srNo;
        this.name = name;
        this.Commission = commission;
        this.decisionDate = decisionDate;
        this.decisionMonth= decisionMonth;
    }
}
