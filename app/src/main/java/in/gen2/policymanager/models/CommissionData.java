package in.gen2.policymanager.models;

public class CommissionData {
    private String applicationNo = null;
    private String srNo = null;
    private String name = null;
    private String decisionDate = null;
    private String ApplicationId = null;
    private String Commission = null;
    public CommissionData() {

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

    public void setApplicationNo(String applicationNo) {
        this.applicationNo = applicationNo;
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

    public void setDecisionDate(String decisionDate) {
        this.decisionDate = decisionDate;
    }

    public CommissionData(String srNo, String applicationNo, String name, String decisionDate, String commission) {
        this.applicationNo = applicationNo;
        this.srNo = srNo;
        this.name = name;
        this.Commission = commission;
        this.decisionDate = decisionDate;
    }
}
