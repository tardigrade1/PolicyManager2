package in.gen2.policymanager.models;

import androidx.annotation.Keep;

@Keep
public class ApproveDeclineData {
    private String applicationNo=null;
    private String bankName=null;
    private String srNo=null;
    private String status=null;
    private String DecisionDate=null;
    private String comment=null;

    public String getApplicationNo() {
        return applicationNo;
    }

    public String getBankName() {
        return bankName;
    }

    public String getSrNo() {
        return srNo;
    }

    public String getStatus() {
        return status;
    }

    public String getDecisionDate() {
        return DecisionDate;
    }

    public String getComment() {
        return comment;
    }

    public ApproveDeclineData(String applicationNo, String bankName, String srNo, String status, String decisionDate, String comment) {
        this.applicationNo = applicationNo;
        this.bankName = bankName;
        this.srNo = srNo;
        this.status = status;
        DecisionDate = decisionDate;
        this.comment = comment;
    }
}
