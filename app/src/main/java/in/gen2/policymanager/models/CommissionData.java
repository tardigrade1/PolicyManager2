package in.gen2.policymanager.models;

public class CommissionData {
    private String applicationNo=null;
    private String srNo=null;
    private String commission=null;
    private String decisionDate=null;


    public CommissionData(){

    }

    public String getApplicationNo() {
        return applicationNo;
    }

    public void setApplicationNo(String applicationNo) {
        this.applicationNo = applicationNo;
    }

    public String getSrNo() {
        return srNo;
    }

    public void setSrNo(String srNo) {
        this.srNo = srNo;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public String getDecisionDate() {
        return decisionDate;
    }

    public void setDecisionDate(String decisionDate) {
        this.decisionDate = decisionDate;
    }

    public CommissionData(String applicationNo, String srNo, String commission, String decisionDate) {
        this.applicationNo = applicationNo;
        this.srNo = srNo;
        this.commission = commission;
        this.decisionDate = decisionDate;
    }
}
