package in.gen2.policymanager.models;



import androidx.annotation.Keep;

@Keep
public class PoliciesFormData {

    private String id = null;
    private String applicantName = null;
    private String panNo = null;
    private String srNo = null;
    private String applicationNo = null;
    private String purchaseDate = null;
    private String PolicyStatus = null;

    public PoliciesFormData() {

    }

    public String getPolicyStatus() {
        return PolicyStatus;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getPanNo() {
        return panNo;
    }

    public void setPanNo(String panNo) {
        this.panNo = panNo;
    }

    public String getSrNo() {
        return srNo;
    }

    public void setSrNo(String srNo) {
        this.srNo = srNo;
    }

    public String getApplicationNo() {
        return applicationNo;
    }

    public void setApplicationNo(String applicationNo) {
        this.applicationNo = applicationNo;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }
}
