package in.gen2.policymanager.models;

public class ApproveDeclineData {
    private String applicationNo=null;
    private String bankName=null;
    private String status=null;
    private String comment=null;

    public String getApplicationNo() {
        return applicationNo;
    }

    public String getBankName() {
        return bankName;
    }

    public String getStatus() {
        return status;
    }

    public String getComment() {
        return comment;
    }

    public ApproveDeclineData(String applicationNo, String bankName, String status, String comment) {
        this.applicationNo = applicationNo;
        this.bankName = bankName;
        this.status = status;
        this.comment = comment;
    }
}
