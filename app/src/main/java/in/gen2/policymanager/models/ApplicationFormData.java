package in.gen2.policymanager.models;

import androidx.annotation.Keep;

@Keep
public class ApplicationFormData {


    private String applicationNo=null;
    private String applicantName=null;
    private String contactNo=null;
    private String panNo=null;
    private String srNo=null;
    private String dateOfPolicy=null;

    public String getApplicationNo() {
        return applicationNo;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public String getContactNo() {
        return contactNo;
    }

    public String getPanNo() {
        return panNo;
    }

    public String getSrNo() {
        return srNo;
    }

    public String getDateOfPolicy() {
        return dateOfPolicy;
    }

    public ApplicationFormData(String applicationNo, String applicantName, String contactNo, String panNo, String srNo, String dateOfPolicy) {
        this.applicationNo = applicationNo;
        this.applicantName = applicantName;
        this.contactNo = contactNo;
        this.panNo = panNo;
        this.srNo = srNo;
        this.dateOfPolicy = dateOfPolicy;
    }
}
